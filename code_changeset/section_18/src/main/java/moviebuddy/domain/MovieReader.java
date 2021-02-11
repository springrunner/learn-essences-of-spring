package moviebuddy.domain;

import java.util.List;

import javax.cache.annotation.CacheResult;

public interface MovieReader {
	
	@CacheResult(cacheName = "movies")
	List<Movie> loadMovies();

}
