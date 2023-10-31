package moviebuddy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import moviebuddy.util.UrlUtils;

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
    
    /**
     * find movies directed by specified director. case-insensitive and checks if director's name contains provided characters.
     *
     * @param director director name (or part of name)
     * @return list of found movies
     */
    public List<Movie> directedBy(String director) {
        Objects.requireNonNull(director, "director must be not null");

        return loadMovies().stream().filter(it ->
            it.director().toLowerCase().contains(director.toLowerCase())
        ).toList();
    }

    /**
     * find movies released in specified year.
     *
     * @param releaseYear release year
     * @return list of found movies
     */
    public List<Movie> releasedIn(int releaseYear) {
        return loadMovies().stream().filter(it ->
            it.releaseYear() == releaseYear
        ).toList();
    }

    /**
     * reads metadata files to load list of movies.
     *
     * @return list of loaded movies
     */
    public List<Movie> loadMovies() {
        try {
            final URI resourceUri = ClassLoader.getSystemResource("movie_metadata.csv").toURI();
            final Path data = Path.of(resourceUri);
            final Function<String, Movie> csvToMovie = csv -> {
                // split csv by commas: title,genres,language,country,releaseYear,director,actors,imdbLink,watchedDate
                var values = csv.split(",");

                var title = values[0];
                var genres = Arrays.asList(values[1].split("\\|"));
                var language = values[2].trim();
                var country = values[3].trim();
                var releaseYear = Integer.parseInt(values[4].trim());
                var director = values[5].trim();
                var actors = Arrays.asList(values[6].split("\\|"));
                var imdbLink = UrlUtils.create(values[7]);
                var watchedDate = values[8];

                return Movie.of(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
            };

            return Files.lines(data, StandardCharsets.UTF_8).skip(1).map(csvToMovie).toList();
        } catch (Exception error) {
            throw new IllegalStateException("failed to load movies.", error);
        }
    }

    public static void main(String[] args) throws Exception {
        new MovieBuddyApplication().run();
    }
}
