package moviebuddy.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;

/**
 * Utility class for file-system operations.
 *
 * @author springrunner.kr@gmail.com
 */
public class FileSystemUtils {

    /**
     * Initializes the JAR-based FileSystem for provided URI.
     *
     * @param uri target URI
     * @throws IOException if there's error during initialization
     */
    public static URI initializeJarFileSystem(URI uri) throws IOException {
        if (!"jar".equalsIgnoreCase(uri.getScheme())) {
            return uri;
        }

        for (var provider : FileSystemProvider.installedProviders()) {
            if (!provider.getScheme().equalsIgnoreCase("jar")) {
                continue;
            }

            try {
                provider.getFileSystem(uri);
            } catch (FileSystemNotFoundException ignore) {
                provider.newFileSystem(uri, Collections.emptyMap());
            }
        }

        return uri;
    }

    private FileSystemUtils() {
    }
}
