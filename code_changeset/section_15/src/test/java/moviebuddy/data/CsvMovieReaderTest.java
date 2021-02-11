package moviebuddy.data;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.core.io.DefaultResourceLoader;

class CsvMovieReaderTest {

	@Test
	void Valid_Metadata() throws Exception {
		CsvMovieReader csvMovieReader = new CsvMovieReader(new NoOpCacheManager());
		csvMovieReader.setMetadata("movie_metadata.csv");
		csvMovieReader.setResourceLoader(new DefaultResourceLoader());

		csvMovieReader.afterPropertiesSet();
	}

	@Test
	void Invalid_Metadata() throws Exception {
		CsvMovieReader csvMovieReader = new CsvMovieReader(new NoOpCacheManager());
		csvMovieReader.setResourceLoader(new DefaultResourceLoader());

		Assertions.assertThrows(FileNotFoundException.class, () -> {
			csvMovieReader.setMetadata("invalid");
			csvMovieReader.afterPropertiesSet();
		});
	}

}
