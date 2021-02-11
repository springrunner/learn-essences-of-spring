package moviebuddy.domain;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class Movie {

    public static final DateTimeFormatter DEFAULT_WATCHED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String title;
    private List<String> genres;
    private String language;
    private String country;
    private int releaseYear;
    private String director;
    private List<String> actors;
    private URL imdbLink;
    private LocalDate watchedDate;

    private Movie(String title, List<String> genres, String language, String country, int releaseYear, String director, List<String> actors, URL imdbLink, LocalDate watchedDate)  {
        this.title = Objects.requireNonNull(title, "title is required value");
        this.genres = Objects.nonNull(genres) ? genres : Collections.emptyList();
        this.language = Objects.requireNonNull(language, "language is required value");
        this.country = Objects.requireNonNull(country, "country is required value");
        this.releaseYear = releaseYear;
        this.director = Objects.requireNonNull(director, "director is required value");
        this.actors = Objects.nonNull(actors) ? actors : Collections.emptyList();
        this.imdbLink = imdbLink;
        this.watchedDate = Objects.requireNonNull(watchedDate, "watchedDate is required value");
    }    

    public String getTitle() {
        return title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public List<String> getActors() {
        return actors;
    }

    public URL getImdbLink() {
        return imdbLink;
    }

    public LocalDate getWatchedDate() {
        return watchedDate;
    }

    @Override
    public String toString() {
        return "Movie [title=" + title + ", releaseYear=" + releaseYear + ", director=" + director + ", watchedDate=" + watchedDate.format(DEFAULT_WATCHED_DATE_FORMATTER) + "]";
    }

    
    public static Movie of(String title, List<String> genres, String language, String country, int releaseYear, String director, List<String> actors, URL imdbLink, String watchedDate) {
        return new Movie(title, genres, language, country, releaseYear, director, actors, imdbLink, LocalDate.parse(watchedDate, Movie.DEFAULT_WATCHED_DATE_FORMATTER));
    }
    
    public static Movie of(String title, List<String> genres, String language, String country, int releaseYear, String director, List<String> actors, URL imdbLink, LocalDate watchedDate) {
        return new Movie(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
    }

}