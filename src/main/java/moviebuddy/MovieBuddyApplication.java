package moviebuddy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieBuddyApplication {

    protected void run() throws Exception {
        try (
            // use `System.in` to read user input and `System.out` to display output
            final var input = new BufferedReader(new InputStreamReader(System.in));
            final var output = new PrintWriter(System.out, true)
        ) {
            output.println("\napplication is ready.");

            while (true) {
                try {
                    output.print("❯ ");
                    output.flush();

                    // read user input
                    final var command = input.readLine();

                    // handle commands
                    switch (command) {
                        case "quit":
                            output.println("quit application.");
                            throw new InterruptedException();
                        default:
                            throw new IllegalArgumentException("unknown command, please try again!");
                    }
                } catch (InterruptedException error) {
                    // exit loop
                    return;
                } catch (Exception error) {
                    // display errors that during command processing
                    output.println(error.getMessage() != null ? error.getMessage() : "unexpected error occurred.");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new MovieBuddyApplication().run();
    }
}
