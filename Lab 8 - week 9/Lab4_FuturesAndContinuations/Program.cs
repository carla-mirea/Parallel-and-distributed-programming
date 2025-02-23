using Lab4_FuturesAndContinuations.Parser;

namespace Lab4_FuturesAndContinuations
{
    class Program
    {
        static void Main(string[] args)
        {
            var urls = new List<string>
            {
                "http://www.google.com/",
                "http://www.example.com/",
                "http://www.bing.com/"
            };

            while (true)
            {
                Console.WriteLine("\nChoose a parser type:");
                Console.WriteLine("1. Async/Await");
                Console.WriteLine("2. Callback");
                Console.WriteLine("3. Task");
                Console.WriteLine("4. Exit");
                Console.Write("Your choice: ");

                var choice = Console.ReadLine();

                switch (choice)
                {
                    case "1":
                        Console.WriteLine("Running Async/Await Solution...");
                        new AsyncAwaitParser(urls);
                        break;
                    case "2":
                        Console.WriteLine("Running Callback Solution...");
                        new CallbackParser(urls);
                        break;
                    case "3":
                        Console.WriteLine("Running Task Solution...");
                        new TaskParser(urls);
                        break;
                    case "4":
                        Console.WriteLine("Exiting program.");
                        return;
                    default:
                        Console.WriteLine("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }
}