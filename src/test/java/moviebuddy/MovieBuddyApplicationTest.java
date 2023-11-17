package moviebuddy;

public class MovieBuddyApplicationTest {

    public static void main(String[] args) {
        final var application = new MovieBuddyApplication();

        var movies = application.loadMovies();
        assertEquals(1375, movies.size());

        movies = application.directedBy("Michael Bay");
        assertEquals(3, movies.size());

        movies = application.releasedIn(2015);
        assertEquals(225, movies.size());
    }

    static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new RuntimeException(String.format("actual(%d) is different from the expected(%d)", actual, expected));
        }
    }
}
