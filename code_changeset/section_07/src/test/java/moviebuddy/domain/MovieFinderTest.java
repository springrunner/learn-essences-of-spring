package moviebuddy.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import moviebuddy.MovieBuddyFactory;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieFinderTest {

	final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MovieBuddyFactory.class);
	final MovieFinder movieFinder = applicationContext.getBean(MovieFinder.class);
	
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
