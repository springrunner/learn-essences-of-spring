package moviebuddy;

/**
 * @author springrunner.kr@gmail.com
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class UnknownCommandException extends ApplicationException {

        public UnknownCommandException() {
            super("unknown command, please try again!");
        }
    }

    public static class InvalidCommandArgumentsException extends ApplicationException {

        public InvalidCommandArgumentsException() {
            super("input error, please try again!");
        }

        public InvalidCommandArgumentsException(Throwable cause) {
            super("input error, please try again!", cause);
        }
    }

    public static class TerminateException extends ApplicationException {

        public TerminateException() {
            super("terminate application.");
        }
    }
}
