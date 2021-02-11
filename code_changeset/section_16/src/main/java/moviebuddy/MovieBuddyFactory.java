package moviebuddy;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.github.benmanes.caffeine.cache.Caffeine;

import moviebuddy.data.CachingMovieReader;
import moviebuddy.domain.MovieReader;

@Configuration
@PropertySource("/application.properties")
@ComponentScan(basePackages = "moviebuddy")
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class })
public class MovieBuddyFactory {
	
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("moviebuddy");

		return marshaller;
	}
	
	@Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS));

        return cacheManager;
    }

	@Configuration
	static class DomainModuleConfig {

	}

	@Configuration
	static class DataSourceModuleConfig {
		
		@Bean
	    @Primary
	    public MovieReader cachingMovieReader(CacheManager cacheManager, MovieReader movieReader) {
	      return new CachingMovieReader(cacheManager, movieReader);
	    }
		
	}

}
