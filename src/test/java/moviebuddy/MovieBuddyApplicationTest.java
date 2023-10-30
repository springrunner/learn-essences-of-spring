package moviebuddy;

public class MovieBuddyApplicationTest {

    public static void main(String[] args) {
        final var application = new MovieBuddyApplication();

        var movies = application.loadMovies();
        assertEquals(1375, movies.size());
    }

    static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new RuntimeException(String.format("actual(%d) is different from the expected(%d)", actual, expected));
        }
    }
}
