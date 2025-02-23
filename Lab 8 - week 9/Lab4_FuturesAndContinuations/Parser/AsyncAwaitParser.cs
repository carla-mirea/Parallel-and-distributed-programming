using Lab4_FuturesAndContinuations.Socket;

namespace Lab4_FuturesAndContinuations.Parser
{
    internal class AsyncAwaitParser: BaseParser
    {
        protected override string ParserType => "Async/Await";

        public AsyncAwaitParser(List<string> urls) : base(urls)
        {
        }

        protected override void Execute()
        {
            var tasks = Map((index, url) => Task.Run(() =>
                Start(AsyncSocketClient.Initialize(url, index))));

            Task.WhenAll(tasks).Wait();
        }

        private async Task Start(AsyncSocketClient socket)
        {
            await socket.BeginConnectAsync();
            LogConnected(socket);

            var numberOfSentBytes = await socket.BeginSendAsync();
            LogSent(socket, numberOfSentBytes);

            await socket.BeginReceiveAsync();
            LogReceived(socket);

            socket.ShutdownAndClose();
        }
    }
}
