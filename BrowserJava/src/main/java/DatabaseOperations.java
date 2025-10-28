import java.util.List;
import java.util.Map;

/**
 * Interface for database operations.
 * Defines the contract for all database-related operations in the browser.
 * Implemented by DatabaseManager.
 */
public interface DatabaseOperations {
    /**
     * Add a bookmark to the database
     * @param url The URL to bookmark
     */
    void addBookmark(String url);

    /**
     * Get all bookmarks from the database
     * @return List of bookmark URLs
     */
    List<String> getBookmarks();

    /**
     * Delete a bookmark from the database
     * @param url The URL to delete
     */
    void deleteBookmark(String url);

    /**
     * Add a browsing history entry to the database
     * @param url The URL to add to history
     */
    void addHistory(String url);

    /**
     * Get all history entries from the database
     * @return List of history URLs
     */
    List<String> getHistory();

    /**
     * Get history entries grouped by day
     * @return Map with date as key and list of URLs as value
     */
    Map<String, List<String>> getHistoryByDay();

    /**
     * Delete a history entry from the database
     * @param url The URL to delete from history
     */
    void deleteHistory(String url);

    /**
     * Close the database connection
     */
    void close();
}
