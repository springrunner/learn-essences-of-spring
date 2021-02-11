package moviebuddy.data;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

public class CachingMovieReaderTest {

	@Test
	void caching() {
		CacheManager cacheManager = new ConcurrentMapCacheManager();
		MovieReader target = new DummyMovieReader();

		MovieReader movieReader = new CachingMovieReader(cacheManager, target);

		Cache cache = cacheManager.getCache(CachingMovieReader.CACHE_NAME);
		assertNull(cache.get(CachingMovieReader.CACHE_KEY_MOVIES));

		List<Movie> movies = movieReader.loadMovies();
		assertNotNull(cache.get(CachingMovieReader.CACHE_KEY_MOVIES));
		assertSame(movieReader.loadMovies(), movies);

	}

	class DummyMovieReader implements MovieReader {

		@Override
		public List<Movie> loadMovies() {
			return new ArrayList<>();
		}

	}

}
