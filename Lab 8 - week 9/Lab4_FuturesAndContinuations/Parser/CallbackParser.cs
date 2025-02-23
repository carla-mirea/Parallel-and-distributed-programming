using Lab4_FuturesAndContinuations.Socket;

namespace Lab4_FuturesAndContinuations.Parser
{
    internal class CallbackParser: BaseParser
    {
        protected override string ParserType => "Callback";

        public CallbackParser(List<string> urls) : base(urls)
        {
        }

        protected override void Execute()
        {
            ProcessEach((index, url) => Start(AsyncSocketClient.Initialize(url, index)));
        }

        private void Start(AsyncSocketClient socket)
        {
            socket.BeginConnect(HandleConnected);
            do
            {
                Thread.Sleep(100);
            }
            while (socket.Connected);
        }

        private void HandleConnected(AsyncSocketClient socket)
        {
            LogConnected(socket);
            socket.BeginSend(HandleSent);
        }

        private void HandleSent(AsyncSocketClient socket, int numberOfSentBytes)
        {
            LogSent(socket, numberOfSentBytes);
            socket.BeginReceive(HandleReceived);
        }

        private void HandleReceived(AsyncSocketClient socket)
        {
            LogReceived(socket);
            socket.ShutdownAndClose();
        }
    }
}
