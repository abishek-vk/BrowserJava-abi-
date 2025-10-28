/**
 * ThemeManager is a derived class from BrowserFeature.
 * Manages dark mode toggling and theme-related operations.
 * Demonstrates inheritance from the abstract BrowserFeature class.
 */
public class ThemeManager extends BrowserFeature {
    private boolean darkModeEnabled;
    private Runnable themeChangeCallback;

    /**
     * Constructor for ThemeManager
     */
    public ThemeManager() {
        super("Theme Manager");
        this.darkModeEnabled = true;
    }

    /**
     * Initialize the theme manager
     */
    @Override
    public void initialize() {
        System.out.println("Initializing " + featureName);
        enable();
    }

    /**
     * Execute theme operation
     */
    @Override
    public void execute() {
        System.out.println("Executing " + featureName);
        toggleTheme();
    }

    /**
     * Toggle between dark and light mode
     */
    public void toggleTheme() {
        if (!isEnabled) {
            System.out.println("Theme Manager is disabled");
            return;
        }
        
        darkModeEnabled = !darkModeEnabled;
        System.out.println("Theme toggled to: " + (darkModeEnabled ? "DARK" : "LIGHT"));
        
        // Execute callback if set
        if (themeChangeCallback != null) {
            themeChangeCallback.run();
        }
    }

    /**
     * Set a callback to be executed when theme changes
     * @param callback The callback to execute
     */
    public void setThemeChangeCallback(Runnable callback) {
        this.themeChangeCallback = callback;
    }

    /**
     * Check if dark mode is enabled
     * @return true if dark mode is enabled, false if light mode
     */
    public boolean isDarkModeEnabled() {
        return darkModeEnabled;
    }

    /**
     * Explicitly set the theme
     * @param isDark true for dark mode, false for light mode
     */
    public void setDarkMode(boolean isDark) {
        if (darkModeEnabled != isDark) {
            toggleTheme();
        }
    }

    /**
     * Get current theme name
     * @return Theme name as string
     */
    public String getCurrentTheme() {
        return darkModeEnabled ? "Dark Mode" : "Light Mode";
    }

    /**
     * Get theme colors based on current mode
     * @return Array with [backgroundColor, textColor]
     */
    public String[] getThemeColors() {
        if (darkModeEnabled) {
            return new String[]{"#1a1a1a", "#e0e0e0"};
        } else {
            return new String[]{"#ffffff", "#000000"};
        }
    }
}
