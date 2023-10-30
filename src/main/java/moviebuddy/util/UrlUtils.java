package moviebuddy.util;

import java.net.URI;
import java.net.URL;

/**
 * Utility class for URL-related operations.
 *
 * @author springrunner.kr@gmail.com
 */
public class UrlUtils {

    /**
     * Converts given string to URL. string is trimmed before conversion.
     *
     * @param value the string representation of the URL
     * @return URL object
     * @throws IllegalArgumentException if the provided string cannot be converted to URL
     */
    public static URL create(String value) {
        try {
            return URI.create(value.trim()).toURL();
        } catch (Exception error) {
            throw new IllegalArgumentException(error);
        }
    }

    private UrlUtils() {
    }
}
