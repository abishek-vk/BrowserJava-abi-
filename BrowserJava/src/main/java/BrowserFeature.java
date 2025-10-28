/**
 * Abstract base class for browser-related features.
 * Serves as the foundation for browser features like bookmarks, history, and theme management.
 * Demonstrates inheritance and polymorphism in the browser application.
 */
public abstract class BrowserFeature {
    protected String featureName;
    protected boolean isEnabled;

    /**
     * Constructor for BrowserFeature
     * @param featureName The name of the feature
     */
    public BrowserFeature(String featureName) {
        this.featureName = featureName;
        this.isEnabled = true;
    }

    /**
     * Abstract method to initialize the feature
     */
    public abstract void initialize();

    /**
     * Abstract method to execute the feature
     */
    public abstract void execute();

    /**
     * Enable the feature
     */
    public void enable() {
        this.isEnabled = true;
        System.out.println(featureName + " enabled");
    }

    /**
     * Disable the feature
     */
    public void disable() {
        this.isEnabled = false;
        System.out.println(featureName + " disabled");
    }

    /**
     * Get the feature name
     * @return Feature name
     */
    public String getFeatureName() {
        return featureName;
    }

    /**
     * Check if feature is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Get feature status
     * @return Feature status as string
     */
    @Override
    public String toString() {
        return featureName + " [" + (isEnabled ? "ENABLED" : "DISABLED") + "]";
    }
}
