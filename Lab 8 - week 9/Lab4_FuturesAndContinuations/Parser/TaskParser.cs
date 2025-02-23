using Lab4_FuturesAndContinuations.Socket;

namespace Lab4_FuturesAndContinuations.Parser
{
    internal class TaskParser: BaseParser
    {
        protected override string ParserType => "Task";

        public TaskParser(List<string> urls) : base(urls)
        {
        }

        protected override void Execute()
        {
            var tasks = Map((index, url) => Task.Run(() =>
                Start(AsyncSocketClient.Initialize(url, index))));

            Task.WhenAll(tasks).Wait();
        }

        private Task Start(AsyncSocketClient socket)
        {
            socket.BeginConnectAsync().Wait();
            LogConnected(socket);

            var sendTask = socket.BeginSendAsync();
            sendTask.Wait();

            var numberOfSentBytes = sendTask.Result;
            LogSent(socket, numberOfSentBytes);

            socket.BeginReceiveAsync().Wait();
            LogReceived(socket);

            socket.ShutdownAndClose();
            return Task.CompletedTask;
        }
    }
}
