package moviebuddy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;
import moviebuddy.domain.MovieReader;

@Configuration
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class })
public class MovieBuddyFactory {

	@Configuration
	static class DomainModuleConfig {

		@Bean
		public MovieFinder movieFinder(MovieReader movieReader) {
			return new MovieFinder(movieReader);
		}

	}

	@Configuration
	static class DataSourceModuleConfig {

		@Bean
		public CsvMovieReader csvMovieReader() {
			return new CsvMovieReader();
		}

	}

}
