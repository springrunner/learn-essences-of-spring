package moviebuddy.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MovieFinder {
	
	private final MovieReader movieReader;

	public MovieFinder(@Qualifier("csvMovieReader") MovieReader movieReader) {
		this.movieReader = Objects.requireNonNull(movieReader);
	}
	
	/**
	 * 저장된 영화 목록에서 감독으로 영화를 검색한다.   
	 * 
	 * @param directedBy 감독
	 * @return 검색된 영화 목록
	 */
	public List<Movie> directedBy(String directedBy) {
		return movieReader.loadMovies()
						  .stream()
						  .filter(it -> it.getDirector().toLowerCase().contains(directedBy.toLowerCase()))
						  .collect(Collectors.toList());		
	}
	
	/**
	 * 저장된 영화 목록에서 개봉년도로 영화를 검색한다.
	 * 
	 * @param releasedYearBy
	 * @return 검색된 영화 목록
	 */
	public List<Movie> releasedYearBy(int releasedYearBy) {
		return movieReader.loadMovies()
						  .stream()
						  .filter(it -> Objects.equals(it.getReleaseYear(), releasedYearBy))
						  .collect(Collectors.toList());		
    }	

}
