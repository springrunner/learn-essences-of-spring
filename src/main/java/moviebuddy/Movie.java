package moviebuddy;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public record Movie(
    String title,
    List<String> genres,
    String language,
    String country,
    int releaseYear,
    String director,
    List<String> actors,
    URL imdbLink,
    LocalDate watchedDate
) {

    public static final DateTimeFormatter DEFAULT_WATCHED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Movie {
        Objects.requireNonNull(title, "title is required value");
        genres = Objects.nonNull(genres) ? genres : List.of();
        Objects.requireNonNull(language, "language is required value");
        Objects.requireNonNull(country, "country is required value");
        Objects.requireNonNull(director, "director is required value");
        actors = Objects.nonNull(actors) ? actors : List.of();
        Objects.requireNonNull(watchedDate, "watchedDate is required value");
    }

    @Override
    public String toString() {
        return "Movie [title=" + title() + ", releaseYear=" + releaseYear() + ", director=" + director() + ", watchedDate=" + watchedDate().format(
            DEFAULT_WATCHED_DATE_FORMATTER) + "]";
    }

    public static Movie of(String title, List<String> genres, String language, String country, int releaseYear, String director, List<String> actors,
        URL imdbLink, String watchedDate) {
        return new Movie(title, genres, language, country, releaseYear, director, actors, imdbLink,
            LocalDate.parse(watchedDate, Movie.DEFAULT_WATCHED_DATE_FORMATTER));
    }
}
