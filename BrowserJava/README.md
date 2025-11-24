# Nitron Browser

A modern, feature-rich web browser built with JavaFX and MongoDB, demonstrating advanced object-oriented programming concepts including inheritance, exception handling, and database integration.

## 🚀 Features

### Core Browsing
- **Multi-Tab Browsing**: Open and manage multiple tabs simultaneously
- **Navigation Controls**: Back, forward, reload, and home buttons
- **Address Bar**: Direct URL entry with auto-search fallback
- **Web Rendering**: Full HTML5 support powered by JavaFX WebView

### Bookmark Management
- Add and organize bookmarks
- View all saved bookmarks in a dedicated dialog
- Delete bookmarks with context menu
- Persistent storage using MongoDB

### Browsing History
- Automatic history tracking of visited URLs
- View history organized by date
- Daily browsing statistics
- Clear individual entries or entire history
- Day summary showing browsing patterns

### Theme System
- **Dark Mode**: Eye-friendly dark theme (default)
- **Light Mode**: Traditional bright interface
- Toggle themes with a single click
- Persistent theme preferences

### Additional Features
- Keyboard shortcuts (Enter to navigate, Ctrl+T for new tab)
- Context menus for bookmarks and history
- Exception handling for invalid URLs
- Database-backed persistence

## 🛠️ Technology Stack

- **Language**: Java 17
- **UI Framework**: JavaFX 21.0.9
- **Database**: MongoDB 4.11.1
- **Build Tool**: Apache Maven
- **IDE**: Visual Studio Code

## 📋 Prerequisites

Before running Nitron Browser, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   ```powershell
   java -version
   ```

2. **Apache Maven**
   ```powershell
   mvn -version
   ```

3. **MongoDB Server** (running locally on default port 27017)
   - Download from [MongoDB Community Server](https://www.mongodb.com/try/download/community)
   - Start the MongoDB service:
     ```powershell
     net start MongoDB
     ```

4. **JavaFX SDK** (included in `lib/javafx-sdk-21.0.9/`)

## 🚀 Installation & Setup

### 1. Clone the Repository
```powershell
git clone https://github.com/abishek-vk/nitronbrowser.git
cd nitronbrowser/BrowserJava
```

### 2. Install Dependencies
```powershell
mvn clean install
```

### 3. Configure MongoDB
Ensure MongoDB is running on `localhost:27017`. The browser will automatically create the necessary database and collections on first run.

### 4. Run the Application

**Using Maven:**
```powershell
mvn javafx:run
```

**Using Java directly:**
```powershell
java --module-path "lib/javafx-sdk-21.0.9/lib" --add-modules javafx.controls,javafx.web -cp "target/classes" NitronBrowser
```

## 📁 Project Structure

```
BrowserJava/
├── src/
│   └── main/
│       └── java/
│           ├── NitronBrowser.java          # Main application class
│           ├── BrowserFeature.java         # Abstract base class
│           ├── BookmarkManager.java        # Bookmark functionality
│           ├── HistoryManager.java         # History tracking
│           ├── ThemeManager.java           # Theme management
│           ├── DatabaseManager.java        # MongoDB connection
│           ├── DatabaseOperations.java     # Database interface
│           ├── DaySummaryPage.java         # Daily statistics
│           └── InvalidURLException.java    # Custom exception
├── lib/
│   └── javafx-sdk-21.0.9/                 # JavaFX libraries
├── target/                                 # Compiled classes
├── pom.xml                                # Maven configuration
└── README.md                              # This file
```

## 🎯 Usage Guide

### Basic Navigation
1. **New Tab**: Click "Newtab" or press Ctrl+T
2. **Navigate**: Enter URL in address bar and press Enter
3. **Search**: Type search terms (auto-redirects to Brave Search)
4. **Go Back/Forward**: Use navigation buttons
5. **Reload**: Click reload button to refresh current page

### Managing Bookmarks
1. Navigate to your desired page
2. Click "Add Bookmark"
3. View bookmarks: Click "Bookmarks" button
4. Delete: Right-click bookmark → "Delete Bookmark"

### Viewing History
1. Click "History" button
2. Browse by date
3. Right-click entry to delete
4. Use "Clear All History" to remove everything

### Changing Themes
1. Click "Toggle Dark Mode" button
2. Theme persists across sessions

## 🏗️ Architecture Highlights

### Object-Oriented Design
- **Abstract Base Class**: `BrowserFeature` provides common functionality
- **Inheritance**: `BookmarkManager`, `HistoryManager`, and `ThemeManager` extend `BrowserFeature`
- **Polymorphism**: Interface-based database operations
- **Encapsulation**: Private fields with public methods

### Exception Handling
- Custom `InvalidURLException` for URL validation
- Try-catch blocks for database operations
- Graceful error handling throughout

### Database Integration
- MongoDB for persistent storage
- Separate collections for bookmarks and history
- Timestamp tracking for history entries

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**Abishek VK**
- GitHub: [@abishek-vk](https://github.com/abishek-vk)

## 🙏 Acknowledgments

- JavaFX for the UI framework
- MongoDB for database support
- Brave Search for default search engine

## 📞 Support

For issues, questions, or suggestions, please open an issue on the [GitHub repository](https://github.com/abishek-vk/nitronbrowser/issues).

---

**Happy Browsing with Nitron! 🌐**
