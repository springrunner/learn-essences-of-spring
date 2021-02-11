package moviebuddy.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.annotation.Profile;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import moviebuddy.ApplicationException;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

@Profile(MovieBuddyProfile.XML_MODE)
@Repository
public class XmlMovieReader extends AbstractMetadataResourceMovieReader implements MovieReader {
	
	private final Unmarshaller unmarshaller;

	public XmlMovieReader(Unmarshaller unmarshaller) {
		this.unmarshaller = Objects.requireNonNull(unmarshaller);
	}
	
	@Override
	public List<Movie> loadMovies() {
		try {
			final InputStream content = getMetadataResource().getInputStream();
	        final Source source = new StreamSource(content);
	        final MovieMetadata metadata = (MovieMetadata) unmarshaller.unmarshal(source);

			return metadata.toMovies();
		} catch (IOException error) {
			throw new ApplicationException("failed to load movies data.", error);
		}
	}

	@XmlRootElement(name = "moviemetadata")
	static class MovieMetadata {

		private List<MovieData> movies;

		public List<MovieData> getMovies() {
			return movies;
		}

		public void setMovies(List<MovieData> movies) {
			this.movies = movies;
		}
		
		public List<Movie> toMovies() {
			return movies.stream().map(MovieData::toMovie).collect(Collectors.toList());
		}

	}

	static class MovieData {

		private String title;
		private List<String> genres;
		private String language;
		private String country;
		private int releaseYear;
		private String director;
		private List<String> actors;
		private URL imdbLink;
		private String watchedDate;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<String> getGenres() {
			return genres;
		}

		public void setGenres(List<String> genres) {
			this.genres = genres;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public int getReleaseYear() {
			return releaseYear;
		}

		public void setReleaseYear(int releaseYear) {
			this.releaseYear = releaseYear;
		}

		public String getDirector() {
			return director;
		}

		public void setDirector(String director) {
			this.director = director;
		}

		public List<String> getActors() {
			return actors;
		}

		public void setActors(List<String> actors) {
			this.actors = actors;
		}

		public URL getImdbLink() {
			return imdbLink;
		}

		public void setImdbLink(URL imdbLink) {
			this.imdbLink = imdbLink;
		}

		public String getWatchedDate() {
			return watchedDate;
		}

		public void setWatchedDate(String watchedDate) {
			this.watchedDate = watchedDate;
		}
		
		public Movie toMovie() {
			String title = getTitle();
			List<String> genres = getGenres();
			String language = getLanguage();
			String country = getCountry();
			int releaseYear = getReleaseYear();
			String director = getDirector();
			List<String> actors = getActors();
			URL imdbLink = getImdbLink();
			String watchedDate = getWatchedDate();

			return Movie.of(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
		}

	}

}
