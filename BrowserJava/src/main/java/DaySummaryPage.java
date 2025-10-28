import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DaySummaryPage displays a summary of the day's browsing activity.
 * Shows: number of sites visited, total browsing time, and top 3 most visited sites.
 * Displayed when the browser closes.
 */
public class DaySummaryPage {
    private final HistoryManager historyManager;
    private final LocalDate today;
    private final long sessionStartTime;

    /**
     * Constructor for DaySummaryPage
     * @param historyManager The history manager to get browsing data
     */
    public DaySummaryPage(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.today = LocalDate.now();
        this.sessionStartTime = System.currentTimeMillis();
    }

    /**
     * Display the day summary in a new window
     */
    public void show() {
        Stage summaryStage = new Stage();
        summaryStage.setTitle("Day Summary - " + today);
        summaryStage.setWidth(600);
        summaryStage.setHeight(500);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("📊 Browsing Activity Summary");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        root.getChildren().add(titleLabel);

        // Date
        Label dateLabel = new Label("Date: " + today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        dateLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");
        root.getChildren().add(dateLabel);

        // Sites visited
        List<String> history = historyManager.getHistory();
        Set<String> uniqueSites = extractUniqueSites(history);
        Label sitesLabel = new Label("🔗 Number of Sites Visited: " + uniqueSites.size());
        sitesLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        root.getChildren().add(sitesLabel);

        // Total browsing time
        long elapsedTime = (System.currentTimeMillis() - sessionStartTime) / 1000; // in seconds
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        Label timeLabel = new Label(String.format("⏱️  Total Browsing Time: %d hours %d minutes", hours, minutes));
        timeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        root.getChildren().add(timeLabel);

        // Top 3 sites
        Label topSitesLabel = new Label("🏆 Top 3 Most Visited Sites:");
        topSitesLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-margin-top: 20;");
        root.getChildren().add(topSitesLabel);

        List<Map.Entry<String, Integer>> topSites = getTopSites(history, 3);
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topSites) {
            String siteInfo = String.format("%d. %s (%d visits)", rank, entry.getKey(), entry.getValue());
            Label siteLabel = new Label(siteInfo);
            siteLabel.setStyle("-fx-font-size: 14; -fx-padding: 8;");
            root.getChildren().add(siteLabel);
            rank++;
        }

        if (topSites.isEmpty()) {
            Label noSitesLabel = new Label("No browsing history available");
            noSitesLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #999;");
            root.getChildren().add(noSitesLabel);
        }

        Scene scene = new Scene(root);
        summaryStage.setScene(scene);
        summaryStage.show();
    }

    /**
     * Extract unique sites from history URLs
     * @param history List of history URLs
     * @return Set of unique domain names
     */
    private Set<String> extractUniqueSites(List<String> history) {
        Set<String> sites = new HashSet<>();
        for (String url : history) {
            try {
                String domain = url.replaceFirst("https?://", "");
                int slash = domain.indexOf('/');
                if (slash > 0) {
                    domain = domain.substring(0, slash);
                }
                sites.add(domain);
            } catch (Exception e) {
                sites.add(url);
            }
        }
        return sites;
    }

    /**
     * Get the top N most visited sites
     * @param history List of history URLs
     * @param topN Number of top sites to return
     * @return List of top sites with visit counts
     */
    private List<Map.Entry<String, Integer>> getTopSites(List<String> history, int topN) {
        Map<String, Integer> siteVisits = new HashMap<>();
        
        for (String url : history) {
            try {
                String domain = url.replaceFirst("https?://", "");
                int slash = domain.indexOf('/');
                if (slash > 0) {
                    domain = domain.substring(0, slash);
                }
                siteVisits.put(domain, siteVisits.getOrDefault(domain, 0) + 1);
            } catch (Exception e) {
                siteVisits.put(url, siteVisits.getOrDefault(url, 0) + 1);
            }
        }

        return siteVisits.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(topN)
                .toList();
    }

    /**
     * Get browsing statistics
     * @return Statistics as formatted string
     */
    public String getStatisticsAsString() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== Day Summary ===\n");
        stats.append("Date: ").append(today).append("\n");
        
        List<String> history = historyManager.getHistory();
        Set<String> uniqueSites = extractUniqueSites(history);
        stats.append("Sites Visited: ").append(uniqueSites.size()).append("\n");
        
        long elapsedTime = (System.currentTimeMillis() - sessionStartTime) / 1000;
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        stats.append("Browsing Time: ").append(hours).append("h ").append(minutes).append("m\n");
        
        List<Map.Entry<String, Integer>> topSites = getTopSites(history, 3);
        stats.append("Top Sites:\n");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topSites) {
            stats.append(rank).append(". ").append(entry.getKey()).append(" (").append(entry.getValue()).append(" visits)\n");
            rank++;
        }
        
        return stats.toString();
    }
}
