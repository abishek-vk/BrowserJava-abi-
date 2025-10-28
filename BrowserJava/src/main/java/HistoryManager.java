import java.util.List;
import java.util.Map;

/**
 * HistoryManager is a derived class from BrowserFeature.
 * Manages all browsing history-related operations in the browser.
 * Demonstrates inheritance from the abstract BrowserFeature class.
 */
public class HistoryManager extends BrowserFeature {
    private final DatabaseOperations dbOperations;

    /**
     * Constructor for HistoryManager
     * @param dbOperations The database operations interface
     */
    public HistoryManager(DatabaseOperations dbOperations) {
        super("History Manager");
        this.dbOperations = dbOperations;
    }

    /**
     * Initialize the history manager
     */
    @Override
    public void initialize() {
        System.out.println("Initializing " + featureName);
        enable();
    }

    /**
     * Execute history operation
     */
    @Override
    public void execute() {
        System.out.println("Executing " + featureName);
    }

    /**
     * Add a URL to browsing history
     * @param url The URL to add to history
     */
    public void addToHistory(String url) {
        if (!isEnabled) {
            System.out.println("History Manager is disabled");
            return;
        }
        
        if (url == null || url.trim().isEmpty()) {
            System.out.println("Cannot add empty URL to history");
            return;
        }
        
        dbOperations.addHistory(url);
        System.out.println("Added to history: " + url);
    }

    /**
     * Get all browsing history
     * @return List of history URLs
     */
    public List<String> getHistory() {
        if (!isEnabled) {
            return java.util.Collections.emptyList();
        }
        return dbOperations.getHistory();
    }

    /**
     * Get browsing history grouped by day
     * @return Map with date as key and list of URLs as value
     */
    public Map<String, List<String>> getHistoryByDay() {
        if (!isEnabled) {
            return java.util.Collections.emptyMap();
        }
        return dbOperations.getHistoryByDay();
    }

    /**
     * Delete a specific history entry
     * @param url The URL to delete from history
     */
    public void deleteHistoryEntry(String url) {
        if (!isEnabled) {
            System.out.println("History Manager is disabled");
            return;
        }
        dbOperations.deleteHistory(url);
        System.out.println("Deleted from history: " + url);
    }

    /**
     * Clear all browsing history
     */
    public void clearAllHistory() {
        if (!isEnabled) {
            System.out.println("History Manager is disabled");
            return;
        }
        List<String> allHistory = getHistory();
        for (String url : allHistory) {
            dbOperations.deleteHistory(url);
        }
        System.out.println("All history cleared");
    }

    /**
     * Get the count of history entries
     * @return Number of history entries
     */
    public int getHistoryCount() {
        return getHistory().size();
    }

    /**
     * Get the most recently visited URL
     * @return The most recent URL or null if no history
     */
    public String getMostRecentUrl() {
        List<String> history = getHistory();
        return history.isEmpty() ? null : history.get(0);
    }
}
