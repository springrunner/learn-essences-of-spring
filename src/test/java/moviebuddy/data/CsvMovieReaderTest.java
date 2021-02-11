package moviebuddy.data;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CsvMovieReaderTest {

	@Test
	void Valid_Metadata() throws Exception {
		CsvMovieReader csvMovieReader = new CsvMovieReader();
		csvMovieReader.setMetadata("movie_metadata.csv");

		csvMovieReader.afterPropertiesSet();
	}

	@Test
	void Invalid_Metadata() throws Exception {
		CsvMovieReader csvMovieReader = new CsvMovieReader();

		Assertions.assertThrows(FileNotFoundException.class, () -> {
			csvMovieReader.setMetadata("invalid");
			csvMovieReader.afterPropertiesSet();
		});
	}

}
