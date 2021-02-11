package moviebuddy.data;

import java.io.FileNotFoundException;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import moviebuddy.ApplicationException;
import moviebuddy.domain.MovieReader;

public abstract class AbstractMetadataResourceMovieReader implements MovieReader, ResourceLoaderAware {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private String metadata;
	private ResourceLoader resourceLoader;

	public String getMetadata() {
		return metadata;
	}

	@Value("${movie.metadata}")
	public void setMetadata(String metadata) {
		this.metadata = Objects.requireNonNull(metadata, "metadata is a required value.");
	}

	public Resource getMetadataResource() {
		return resourceLoader.getResource(getMetadata());
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = Objects.requireNonNull(resourceLoader, "resourceLoader is must not be null");
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		Resource resource = getMetadataResource();
		if (resource.exists() == false) {
			throw new FileNotFoundException(getMetadata());
		}

		if (resource.isReadable() == false) {
			throw new ApplicationException(String.format("cannot read to metadata. [%s]", getMetadata()));
		}

		log.info(resource + " is ready.");
	}

	@PreDestroy
	public void destroy() throws Exception {
		log.info("Destroyed bean");
	}

}