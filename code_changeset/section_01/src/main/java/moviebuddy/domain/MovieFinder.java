package moviebuddy.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import moviebuddy.ApplicationException;
import moviebuddy.util.FileSystemUtils;

public class MovieFinder {
	
	/**
	 * 저장된 영화 목록에서 감독으로 영화를 검색한다.   
	 * 
	 * @param directedBy 감독
	 * @return 검색된 영화 목록
	 */
	public List<Movie> directedBy(String directedBy) {
		return loadMovies().stream()
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
		return loadMovies().stream()
						   .filter(it -> Objects.equals(it.getReleaseYear(), releasedYearBy))
						   .collect(Collectors.toList());		
    }

	/**
	 * 영화 메타데이터를 읽어 저장된 영화 목록을 불러온다.
	 * 
	 * @return 불러온 영화 목록
	 */
	public List<Movie> loadMovies() {
		try {
			final URI resourceUri = ClassLoader.getSystemResource("movie_metadata.csv").toURI();
			final Path data = Path.of(FileSystemUtils.checkFileSystem(resourceUri));
			final Function<String, Movie> mapCsv = csv -> {
				try {
					// split with comma
					String[] values = csv.split(",");
		
					String title = values[0];
                    List<String> genres = Arrays.asList(values[1].split("\\|"));
                    String language = values[2].trim();
                    String country = values[3].trim();
                    int releaseYear = Integer.valueOf(values[4].trim());
                    String director = values[5].trim();
                    List<String> actors = Arrays.asList(values[6].split("\\|"));
                    URL imdbLink = new URL(values[7].trim());
                    String watchedDate = values[8]; 
                    
                    return Movie.of(title, genres, language, country, releaseYear, director, actors, imdbLink, watchedDate);
				} catch(IOException error) {
					throw new ApplicationException("mapping csv to object failed.", error);
				}
			};

			return Files.readAllLines(data, StandardCharsets.UTF_8)
						.stream()
						.skip(1)
						.map(mapCsv)
						.collect(Collectors.toList());
		} catch(IOException|URISyntaxException error) {
			throw new ApplicationException("failed to load movies data.", error);
		}
	}	

}
