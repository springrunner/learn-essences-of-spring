package moviebuddy.data;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.AopTestUtils;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

@ActiveProfiles(MovieBuddyProfile.XML_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
@TestPropertySource(properties = "movie.metadata=movie_metadata.xml")
public class XmlMovieReaderTest {
	
	@Autowired MovieReader movieReader;
	
	@Test
	void NotEmpty_LoadedMovies() {
		List<Movie> movies = movieReader.loadMovies();
		Assertions.assertEquals(1375, movies.size());
	}
	
	@Test
	void Check_MovieReaderType() {
		Assertions.assertTrue(AopUtils.isAopProxy(movieReader));
		
		MovieReader targetObject = AopTestUtils.getTargetObject(movieReader);
		Assertions.assertTrue(XmlMovieReader.class.isAssignableFrom(targetObject.getClass()));		
	}

}
