using System.Net.Sockets;
using System.Net;
using System.Text;

namespace Lab4_FuturesAndContinuations.Socket
{
    internal class AsyncSocketClient: System.Net.Sockets.Socket
    {
        public int ClientId { get; }

        public string HostName { get; }

        public string ResourcePath { get; }

        private IPEndPoint ServerEndPoint { get; }

        private StringBuilder ResponseContent { get; } = new();

        private const int HttpPort = 80;
        private const int BufferSize = 1024;

        public static AsyncSocketClient Initialize(string url, int id)
        {
            try
            {
                var uri = new Uri(url.StartsWith("http") ? url : "http://" + url);
                var baseUrl = uri.Host;
                var path = uri.PathAndQuery;

                var hostEntry = Dns.GetHostEntry(baseUrl);
                var ipAddress = hostEntry.AddressList[0];

                return new AsyncSocketClient(baseUrl, path, ipAddress, id);
            }
            catch (UriFormatException ex)
            {
                Console.WriteLine($"Invalid URL format: {url}. Error: {ex.Message}");
                throw;
            }
            catch (SocketException ex)
            {
                Console.WriteLine($"Could not resolve host: {url}. Error: {ex.Message}");
                throw;
            }
        }

        private AsyncSocketClient(string host, string path, IPAddress ipAddress, int id) :
            base(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp)
        {
            ClientId = id;
            HostName = host;
            ResourcePath = path;
            ServerEndPoint = new IPEndPoint(ipAddress, HttpPort);
        }

        public void BeginConnect(Action<AsyncSocketClient> onConnected)
        {
            try
            {
                BeginConnect(ServerEndPoint, asyncResult => {
                    EndConnect(asyncResult);
                    onConnected(this);
                }, null);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error connecting to {HostName}: {ex.Message}");
                ShutdownAndClose();
            }
        }

        public void BeginSend(Action<AsyncSocketClient, int> onRequestSent)
        {
            var request = $"GET {ResourcePath} HTTP/1.1\r\n" +
                        $"Host: {HostName}\r\n" +
                        "Content-Length: 0\r\n\r\n";

            var requestData = Encoding.ASCII.GetBytes(request);

            BeginSend(requestData, 0, requestData.Length, SocketFlags.None, asyncResult => {
                var numberOfSentBytes = EndSend(asyncResult);
                onRequestSent(this, numberOfSentBytes);
            }, null);
        }

        public void BeginReceive(Action<AsyncSocketClient> onResponseReceived)
        {
            var buffer = new byte[BufferSize];
            ResponseContent.Clear();

            BeginReceive(buffer, 0, BufferSize, SocketFlags.None, asyncResult =>
                HandleReceiveResult(asyncResult, buffer, onResponseReceived), null);
        }

        public Task BeginConnectAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource();

            BeginConnect(_ => { taskCompletion.TrySetResult(); });

            return taskCompletion.Task;
        });

        public Task<int> BeginSendAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource<int>();

            BeginSend((_, numberOfSentBytes) =>
                taskCompletion.TrySetResult(numberOfSentBytes));

            return taskCompletion.Task;
        });

        public Task BeginReceiveAsync() => Task.Run(() =>
        {
            var taskCompletion = new TaskCompletionSource();

            BeginReceive(_ => taskCompletion.TrySetResult());

            return taskCompletion.Task;
        });

        public void ShutdownAndClose()
        {
            Shutdown(SocketShutdown.Both);
            Close();
        }

        public string GetResponseContent => ResponseContent.ToString();

        private void HandleReceiveResult(
            IAsyncResult asyncResult,
            byte[] buffer,
            Action<AsyncSocketClient> onReceived)
        {
            var numberOfReadBytes = EndReceive(asyncResult);
            ResponseContent.Append(Encoding.ASCII.GetString(buffer, 0, numberOfReadBytes));
            if (!ResponseContent.ToString().Contains("</html>"))
            {
                BeginReceive(buffer, 0, BufferSize, SocketFlags.None, asyncResult2 =>
                    HandleReceiveResult(asyncResult2, buffer, onReceived), null);
                return;
            }

            onReceived(this);
        }
    }
}
