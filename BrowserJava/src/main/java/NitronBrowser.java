import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;

import javafx.scene.layout.Priority;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;


public class NitronBrowser extends Application {
    private final String defaultHomepage = "https://search.brave.com";
    private boolean darkModeEnabled = true;
    private TabPane tabPane;
    private BorderPane root;
    private ToolBar navToolbar;
    private DatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dbManager = new DatabaseManager();
        root = new BorderPane();
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

        addNewTab(defaultHomepage, "Home");

        navToolbar = createToolbar();

        root.setTop(navToolbar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Nitron Browser");
        primaryStage.setScene(scene);
        
        applyStyles();
        
        primaryStage.show();
    }

    private ToolBar createToolbar() {
        Button backBtn = new Button("Back");
        Button forwardBtn = new Button("Forward");
        Button reloadBtn = new Button("Reload");
        Button homeBtn = new Button("Home");
        Button newTabBtn = new Button("Newtab");
        Button bookmarkBtn = new Button("Add Bookmark");
        Button viewBookmarksBtn = new Button("Bookmarks");
        Button viewHistoryBtn = new Button("History");
        Button darkModeBtn = new Button("Toggle");

        backBtn.setOnAction(e -> getCurrentWebView().getEngine().executeScript("history.back()"));
        forwardBtn.setOnAction(e -> getCurrentWebView().getEngine().executeScript("history.forward()"));
        reloadBtn.setOnAction(e -> getCurrentWebView().getEngine().reload());
        homeBtn.setOnAction(e -> getCurrentWebView().getEngine().load(defaultHomepage));
        newTabBtn.setOnAction(e -> addNewTab(defaultHomepage, "New Tab"));
        bookmarkBtn.setOnAction(e -> addBookmark());
        viewBookmarksBtn.setOnAction(e -> showBookmarksDialog());
        viewHistoryBtn.setOnAction(e -> showHistoryDialog());
        darkModeBtn.setOnAction(e -> toggleDarkMode());

        ToolBar toolbar = new ToolBar(
                backBtn, forwardBtn, reloadBtn, homeBtn,
                newTabBtn, bookmarkBtn, viewBookmarksBtn, viewHistoryBtn, darkModeBtn
        );
        toolbar.setPadding(new Insets(6));
        return toolbar;
    }

    private void addNewTab(String url, String title) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load(url);

        TextField urlBar = new TextField(url);
        urlBar.setPromptText("Enter URL and press Enter");
        urlBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String inputUrl = urlBar.getText().trim();
                if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://")) {
                    inputUrl = "https://" + inputUrl;
                }
                engine.load(inputUrl);
            }
        });

        engine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
            urlBar.setText(newLoc);
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            if (tab != null) {
                tab.setText(getDomain(newLoc));
            }
            // Add to history
            if (dbManager != null && newLoc != null && !newLoc.isEmpty()) {
                dbManager.addHistory(newLoc);
            }
            // Apply theme to the loaded webpage
            applyWebpageTheme(engine);
        });
        
        // Also apply theme when page finishes loading
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                applyWebpageTheme(engine);
            }
        });

        VBox tabContent = new VBox(urlBar, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);

        Tab tab = new Tab(title, tabContent);
        tab.setOnClosed(e -> {
            if (tabPane.getTabs().size() == 1) {
                ((Stage) root.getScene().getWindow()).close();
            }
        });

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private WebView getCurrentWebView() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            VBox content = (VBox) currentTab.getContent();
            return (WebView) content.getChildren().get(1);
        }
        return null;
    }

    private void addBookmark() {
        WebView webView = getCurrentWebView();
        if (webView != null) {
            String url = webView.getEngine().getLocation();
            dbManager.addBookmark(url);
            showAlert("Bookmark Added", "Bookmarked: " + url);
        }
    }

    private void showBookmarksDialog() {
        List<String> bookmarks = dbManager.getBookmarks();
        ObservableList<String> items = FXCollections.observableArrayList(bookmarks);
        ListView<String> listView = new ListView<>(items);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                dbManager.deleteBookmark(selected);
                items.remove(selected);
            }
        });
        contextMenu.getItems().add(deleteItem);
        listView.setContextMenu(contextMenu);

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    getCurrentWebView().getEngine().load(selected);
                }
            }
        });

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Bookmarks");
        VBox vbox = new VBox(new Label("Double-click to open. Right-click to delete."), listView);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        Scene scene = new Scene(vbox, 400, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showHistoryDialog() {
        List<String> history = dbManager.getHistory();
        ObservableList<String> items = FXCollections.observableArrayList(history);
        ListView<String> listView = new ListView<>(items);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                dbManager.deleteHistory(selected);
                items.remove(selected);
            }
        });
        contextMenu.getItems().add(deleteItem);
        listView.setContextMenu(contextMenu);

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    getCurrentWebView().getEngine().load(selected);
                }
            }
        });

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("History");
        VBox vbox = new VBox(new Label("Double-click to open. Right-click to delete."), listView);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        Scene scene = new Scene(vbox, 400, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void toggleDarkMode() {
        darkModeEnabled = !darkModeEnabled;
        applyStyles();
        
        // Apply theme to all open webpages
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getContent() instanceof VBox) {
                VBox vbox = (VBox) tab.getContent();
                for (var child : vbox.getChildren()) {
                    if (child instanceof WebView) {
                        WebView webView = (WebView) child;
                        applyWebpageTheme(webView.getEngine());
                    }
                }
            }
        }
    }

    private void applyStyles() {
        String darkStyle = "-fx-background-color: #282828; -fx-text-fill: white;";
        String lightStyle = "-fx-background-color: #f2f2f2; -fx-text-fill: black;";
        String toolbarDark = "-fx-background-color: #333333;";
        String toolbarLight = "-fx-background-color: #e0e0e0;";
        
        String darkButtonStyle = "-fx-background-color: #444444; -fx-text-fill: white; -fx-border-color: #555555;";
        String lightButtonStyle = "-fx-background-color: #d0d0d0; -fx-text-fill: black; -fx-border-color: #999999;";
        
        String darkTextFieldStyle = "-fx-control-inner-background: #3c3c3c; -fx-text-fill: white;";
        String lightTextFieldStyle = "-fx-control-inner-background: #ffffff; -fx-text-fill: black;";

        if (darkModeEnabled) {
            root.setStyle(darkStyle);
            navToolbar.setStyle(toolbarDark);
            tabPane.setStyle("-fx-background-color: #333333;");
            
            // Apply button styles
            applyStyleToAllButtons(darkButtonStyle);
            
            // Apply styles to all tabs
            applyStylesToAllTabs(darkStyle, darkTextFieldStyle);
            
            // Apply Scene stylesheet
            root.getScene().getStylesheets().clear();
            root.getScene().getStylesheets().add(getDarkModeCSS());
        } else {
            root.setStyle(lightStyle);
            navToolbar.setStyle(toolbarLight);
            tabPane.setStyle("-fx-background-color: #e0e0e0;");
            
            // Apply button styles
            applyStyleToAllButtons(lightButtonStyle);
            
            // Apply styles to all tabs
            applyStylesToAllTabs(lightStyle, lightTextFieldStyle);
            
            // Apply Scene stylesheet
            root.getScene().getStylesheets().clear();
            root.getScene().getStylesheets().add(getLightModeCSS());
        }
    }
    
    private void applyStyleToAllButtons(String style) {
        if (navToolbar != null) {
            for (var item : navToolbar.getItems()) {
                if (item instanceof Button btn) {
                    btn.setStyle(style);
                }
            }
        }
    }
    
    private void applyStylesToAllTabs(String baseStyle, String textFieldStyle) {
        if (tabPane != null) {
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getContent() instanceof VBox vbox) {
                    for (var child : vbox.getChildren()) {
                        if (child instanceof TextField textField) {
                            textField.setStyle(textFieldStyle);
                        } else if (child instanceof WebView webView) {
                            // WebView styling through CSS
                            webView.setStyle(baseStyle);
                        }
                    }
                }
            }
        }
    }
    
    private String getDarkModeCSS() {
        return "data:text/css," +
                ".root { -fx-base: #282828; -fx-control-inner-background: #3c3c3c; -fx-text-fill: white; } " +
                ".button { -fx-text-fill: white; -fx-background-color: #444444; } " +
                ".button:hover { -fx-background-color: #555555; } " +
                ".text-field { -fx-control-inner-background: #3c3c3c; -fx-text-fill: white; } " +
                ".tab-pane { -fx-background-color: #333333; } " +
                ".tab { -fx-background-color: #404040; -fx-text-fill: white; } " +
                ".menu { -fx-background-color: #333333; -fx-text-fill: white; } " +
                ".menu-item { -fx-background-color: #404040; -fx-text-fill: white; } " +
                ".context-menu { -fx-background-color: #333333; } " +
                ".list-view { -fx-control-inner-background: #3c3c3c; } " +
                ".list-cell { -fx-text-fill: white; -fx-background-color: #404040; } " +
                ".dialog-pane { -fx-background-color: #282828; -fx-text-fill: white; } " +
                ".label { -fx-text-fill: white; }";
    }
    
    private String getLightModeCSS() {
        return "data:text/css," +
                ".root { -fx-base: #f2f2f2; -fx-control-inner-background: #ffffff; -fx-text-fill: black; } " +
                ".button { -fx-text-fill: black; -fx-background-color: #d0d0d0; } " +
                ".button:hover { -fx-background-color: #b0b0b0; } " +
                ".text-field { -fx-control-inner-background: #ffffff; -fx-text-fill: black; } " +
                ".tab-pane { -fx-background-color: #e0e0e0; } " +
                ".tab { -fx-background-color: #f0f0f0; -fx-text-fill: black; } " +
                ".menu { -fx-background-color: #e0e0e0; -fx-text-fill: black; } " +
                ".menu-item { -fx-background-color: #f0f0f0; -fx-text-fill: black; } " +
                ".context-menu { -fx-background-color: #e0e0e0; } " +
                ".list-view { -fx-control-inner-background: #ffffff; } " +
                ".list-cell { -fx-text-fill: black; -fx-background-color: #f0f0f0; } " +
                ".dialog-pane { -fx-background-color: #f2f2f2; -fx-text-fill: black; } " +
                ".label { -fx-text-fill: black; }";
    }

    private void applyWebpageTheme(WebEngine engine) {
        // Remove Brave download prompts and ads
        String removeAds = "var elements = document.querySelectorAll('[class*=\"brave\"], [class*=\"download\"], .adsbox, .adbox, .banner-ad'); " +
                "elements.forEach(function(el) { el.style.display = 'none'; }); " +
                "var iframes = document.querySelectorAll('iframe[src*=\"brave\"], iframe[src*=\"download\"]'); " +
                "iframes.forEach(function(el) { el.style.display = 'none'; }); " +
                "var scripts = document.querySelectorAll('script[src*=\"brave\"], script[src*=\"ad\"], script[src*=\"analytics\"]'); " +
                "scripts.forEach(function(el) { el.remove(); }); ";
        
        if (darkModeEnabled) {
            // Inject dark mode CSS into the webpage
            String darkCss = removeAds +
                    "var style = document.createElement('style'); " +
                    "style.innerHTML = 'body, html { background-color: #1e1e1e !important; color: #e0e0e0 !important; } " +
                    "a { color: #64b5f6 !important; } " +
                    "button { background-color: #333333 !important; color: #e0e0e0 !important; border: 1px solid #555555 !important; } " +
                    "input, textarea, select { background-color: #2d2d2d !important; color: #e0e0e0 !important; border: 1px solid #555555 !important; } " +
                    "pre, code { background-color: #2d2d2d !important; color: #e0e0e0 !important; } " +
                    ".header, .navbar, nav { background-color: #252525 !important; } " +
                    "table { border-color: #555555 !important; } " +
                    "td, th { border-color: #555555 !important; background-color: #2d2d2d !important; color: #e0e0e0 !important; } " +
                    "[class*=\"banner\"], [class*=\"ad\"], [class*=\"promo\"] { display: none !important; }'; " +
                    "document.head.appendChild(style);";
            engine.executeScript(darkCss);
        } else {
            // Inject light mode CSS into the webpage
            String lightCss = removeAds +
                    "var style = document.createElement('style'); " +
                    "style.innerHTML = 'body, html { background-color: #ffffff !important; color: #000000 !important; } " +
                    "a { color: #0066cc !important; } " +
                    "button { background-color: #e0e0e0 !important; color: #000000 !important; border: 1px solid #999999 !important; } " +
                    "input, textarea, select { background-color: #ffffff !important; color: #000000 !important; border: 1px solid #cccccc !important; } " +
                    "pre, code { background-color: #f5f5f5 !important; color: #000000 !important; } " +
                    ".header, .navbar, nav { background-color: #f0f0f0 !important; } " +
                    "table { border-color: #cccccc !important; } " +
                    "td, th { border-color: #cccccc !important; background-color: #f5f5f5 !important; color: #000000 !important; } " +
                    "[class*=\"banner\"], [class*=\"ad\"], [class*=\"promo\"] { display: none !important; }'; " +
                    "document.head.appendChild(style);";
            engine.executeScript(lightCss);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private String getDomain(String url) {
        try {
            String domain = url.replaceFirst("https?://", "");
            int slash = domain.indexOf('/');
            return (slash > 0) ? domain.substring(0, slash) : domain;
        } catch (Exception e) {
            return url;
        }
    }
}