package moviebuddy;

import moviebuddy.ApplicationException.InvalidCommandArgumentsException;
import moviebuddy.ApplicationException.TerminateException;
import moviebuddy.ApplicationException.UnknownCommandException;
import moviebuddy.util.FileSystemUtils;
import moviebuddy.util.UrlUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieBuddyApplication {

    public static void main(String[] args) throws Exception {
        new MovieBuddyApplication().run();
    }

    /*
     * 애플리케이션 추가 요구사항:
     *
     * TODO 1. XML 문서로 작성된 영화 메타데이터도 다룰 수 있게 기능을 확장하라
     * TODO 2. 영화 메타데이터 위치를 변경할 수 있도록 하라
     * TODO 3. 영화 메타데이터 읽기 속도를 빠르게 하라
     * TODO 4. 시스템 언어설정에 따라 애플리케이션 메시지가 영어 또는 한글로 출력되게 하라
     */

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
                                throw new InvalidCommandArgumentsException();
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
                                throw new InvalidCommandArgumentsException(error);
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
                            throw new TerminateException();
                        default:
                            throw new UnknownCommandException();
                    }
                } catch (TerminateException error) {
                    // exit loop
                    return;
                } catch (ApplicationException error) {
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
            final Path data = Path.of(FileSystemUtils.initializeJarFileSystem(resourceUri));
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
            throw new ApplicationException("failed to load movies.", error);
        }
    }
}
