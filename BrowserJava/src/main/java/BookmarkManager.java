/**
 * BookmarkManager is a derived class from BrowserFeature.
 * Manages all bookmark-related operations in the browser.
 * Demonstrates inheritance from the abstract BrowserFeature class.
 */
public class BookmarkManager extends BrowserFeature {
    private DatabaseOperations dbOperations;

    /**
     * Constructor for BookmarkManager
     * @param dbOperations The database operations interface
     */
    public BookmarkManager(DatabaseOperations dbOperations) {
        super("Bookmark Manager");
        this.dbOperations = dbOperations;
    }

    /**
     * Initialize the bookmark manager
     */
    @Override
    public void initialize() {
        System.out.println("Initializing " + featureName);
        enable();
    }

    /**
     * Execute bookmark operation
     */
    @Override
    public void execute() {
        System.out.println("Executing " + featureName);
    }

    /**
     * Add a bookmark with validation
     * @param url The URL to bookmark
     * @throws InvalidURLException if the URL is invalid
     */
    public void addBookmark(String url) throws InvalidURLException {
        if (!isEnabled) {
            throw new InvalidURLException("Bookmark Manager is disabled", url);
        }
        
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidURLException("URL cannot be empty", url);
        }
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new InvalidURLException("URL must start with http:// or https://", url);
        }
        
        dbOperations.addBookmark(url);
        System.out.println("Bookmark added: " + url);
    }

    /**
     * Get all bookmarks
     * @return List of bookmarks
     */
    public java.util.List<String> getBookmarks() {
        if (!isEnabled) {
            return java.util.Collections.emptyList();
        }
        return dbOperations.getBookmarks();
    }

    /**
     * Delete a bookmark
     * @param url The URL to delete
     */
    public void deleteBookmark(String url) {
        if (!isEnabled) {
            System.out.println("Bookmark Manager is disabled");
            return;
        }
        dbOperations.deleteBookmark(url);
        System.out.println("Bookmark deleted: " + url);
    }

    /**
     * Get the count of bookmarks
     * @return Number of bookmarks
     */
    public int getBookmarkCount() {
        return getBookmarks().size();
    }
}
