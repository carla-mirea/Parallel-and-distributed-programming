using Lab4_FuturesAndContinuations.Socket;

namespace Lab4_FuturesAndContinuations.Parser
{
    internal abstract class BaseParser
    {
        public List<string> Urls { get; }

        protected abstract string ParserType { get; }

        protected BaseParser(List<string> urls)
        {
            Urls = urls;
            Execute();
        }

        protected void ProcessEach(Action<int, string> action)
        {
            var index = 0;
            Urls.ForEach(url => action(index++, url));
        }

        protected List<T> Map<T>(Func<int, string, T> mapper)
        {
            var index = 0;
            return Urls.Select(url => mapper(index++, url)).ToList();
        }

        protected void LogConnected(AsyncSocketClient socket)
        {
            Console.WriteLine($"{ParserType}-{socket.ClientId}: Socket connected to {socket.HostName} ({socket.ResourcePath})");
        }

        protected void LogSent(AsyncSocketClient socket, int numberOfSentBytes)
        {
            Console.WriteLine($"{ParserType}-{socket.ClientId}: Sent {numberOfSentBytes} bytes to server.");
        }

        protected void LogReceived(AsyncSocketClient socket)
        {
            Console.WriteLine(socket.GetResponseContent);
        }

        protected abstract void Execute();
    }
}
