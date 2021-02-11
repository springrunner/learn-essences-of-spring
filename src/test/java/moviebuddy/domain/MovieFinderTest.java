package moviebuddy.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;

/**
 * @author springrunner.kr@gmail.com
 */
@ActiveProfiles(MovieBuddyProfile.CSV_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
public class MovieFinderTest {

	@Autowired MovieFinder movieFinder;
	@Autowired ApplicationContext applicationContext;
	
	@Test
	void NotEmpty_directedBy() {
		List<Movie> result = movieFinder.directedBy("Michael Bay");
		Assertions.assertEquals(3, result.size());
	}
	
	@Test
	void NotEmpty_ReleasedYearBy() {
		List<Movie> result = movieFinder.releasedYearBy(2015);
		Assertions.assertEquals(225, result.size());
	}
	
	@Test
	void Equals_MovieFinderBean() {
		Assertions.assertEquals(movieFinder, applicationContext.getBean(MovieFinder.class));
	}
	
}
