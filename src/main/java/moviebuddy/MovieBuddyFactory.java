package moviebuddy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;

@Configuration
public class MovieBuddyFactory {

	@Bean
	public MovieFinder movieFinder() {
		return new MovieFinder(new CsvMovieReader());
	}

}
