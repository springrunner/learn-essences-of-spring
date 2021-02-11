package moviebuddy;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;

public class MovieBuddyFactory {

	public MovieFinder movieFinder() {
		return new MovieFinder(new CsvMovieReader());
	}

}
