package moviebuddy.data;

import java.util.List;
import java.util.Objects;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

public class CachingMovieReader implements MovieReader {

	static final String CACHE_NAME = CachingMovieReader.class.getName();
	static final String CACHE_KEY_MOVIES = "movies";

	private final CacheManager cacheManager;
	private final MovieReader target;

	public CachingMovieReader(CacheManager cacheManager, MovieReader target) {
		this.cacheManager = Objects.requireNonNull(cacheManager);
		this.target = Objects.requireNonNull(target);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Movie> loadMovies() {
		// 캐시된 데이터가 있으면 즉시 반환 처리
		Cache cache = cacheManager.getCache(CACHE_NAME);
		List<Movie> movies = cache.get(CACHE_KEY_MOVIES, List.class);
		if (Objects.nonNull(movies)) {
			return movies;
		}

		movies = target.loadMovies();

		// 읽어온 데이터를 캐시 처리하고 반환
		cache.put(CACHE_KEY_MOVIES, movies);
		return movies;
	}

}