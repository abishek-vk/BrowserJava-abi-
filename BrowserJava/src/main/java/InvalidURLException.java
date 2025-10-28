/**
 * Custom exception for handling invalid URLs.
 * Thrown when a user attempts to add an invalid URL as a bookmark or navigate to an invalid URL.
 * Provides graceful error handling and improves the robustness of the application.
 */
public class InvalidURLException extends Exception {
    private String invalidURL;

    /**
     * Constructor for InvalidURLException
     * @param message Error message
     */
    public InvalidURLException(String message) {
        super(message);
        this.invalidURL = null;
    }

    /**
     * Constructor for InvalidURLException with URL
     * @param message Error message
     * @param url The invalid URL
     */
    public InvalidURLException(String message, String url) {
        super(message);
        this.invalidURL = url;
    }

    /**
     * Constructor for InvalidURLException with cause
     * @param message Error message
     * @param cause The cause of the exception
     */
    public InvalidURLException(String message, Throwable cause) {
        super(message, cause);
        this.invalidURL = null;
    }

    /**
     * Get the invalid URL
     * @return The invalid URL that caused the exception
     */
    public String getInvalidURL() {
        return invalidURL;
    }

    /**
     * Get detailed error information
     * @return Detailed error message with URL information
     */
    @Override
    public String toString() {
        if (invalidURL != null) {
            return "InvalidURLException: " + getMessage() + " [URL: " + invalidURL + "]";
        }
        return "InvalidURLException: " + getMessage();
    }
}
