package moviebuddy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
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

                    // parse user input, e.g., "directedBy Michael Bay"
                    final var inputs = Stream.of(input.readLine().split(" "))
                        .map(String::trim)
                        .filter(data -> !data.isBlank())
                        .toList();

                    // extract command and subsequent arguments 
                    final var command = inputs.isEmpty() ? "" : inputs.get(0);
                    final var arguments = inputs.size() > 1 ? inputs.subList(1, inputs.size()) : Collections.<String>emptyList();

                    // handle commands
                    switch (command) {
                        case "directedBy":
                            var director = String.join(" ", arguments);
                            if (director.isBlank()) {
                                throw new IllegalArgumentException("input error, please try again!");
                            }

                            var moviesDirectedBy = directedBy(director);

                            output.println("find for movies by `%s`.".formatted(director));
                            for (var idx = 0; idx < moviesDirectedBy.size(); idx++) {
                                output.println(formatted(idx + 1, moviesDirectedBy.get(idx)));
                            }
                            output.println(String.format("%d movies found.", moviesDirectedBy.size()));
                            break;
                        case "releasedIn":
                            int releaseYear;
                            try {
                                releaseYear = Integer.parseInt(arguments.getFirst());
                            } catch (NoSuchElementException | NumberFormatException error) {
                                throw new IllegalArgumentException("input error, please try again!", error);
                            }

                            var moviesReleasedIn = releasedIn(releaseYear);

                            output.println("find for movies from %s year.".formatted(releaseYear));
                            for (var idx = 0; idx < moviesReleasedIn.size(); idx++) {
                                output.println(formatted(idx + 1, moviesReleasedIn.get(idx)));
                            }
                            output.println(String.format("%d movies found.", moviesReleasedIn.size()));
                            break;
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

    private String formatted(int lineNumber, Movie movie) {
        return "%d. title: %-50s\treleaseYear: %d\tdirector: %-25s\twatchedDate: %s".formatted(
            lineNumber, movie.title(), movie.releaseYear(), movie.director(),
            movie.watchedDate().format(Movie.DEFAULT_WATCHED_DATE_FORMATTER));
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
