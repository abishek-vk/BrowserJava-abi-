import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DatabaseManager implements the DatabaseOperations interface.
 * Manages all database operations for the Nitron Browser using MongoDB.
 */
public class DatabaseManager implements DatabaseOperations {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> bookmarks;
    private final MongoCollection<Document> history;

    public DatabaseManager() {
        // Connect to MongoDB (default localhost:27017)
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("nitronbrowser");
        bookmarks = database.getCollection("bookmarks");
        history = database.getCollection("history");

        // Create indexes if needed
        bookmarks.createIndex(new Document("url", 1));
        history.createIndex(new Document("url", 1));
    }

    // Bookmarks
    @Override
    public void addBookmark(String url) {
        Document doc = new Document("url", url)
                      .append("added_at", new java.util.Date());
        bookmarks.insertOne(doc);
    }

    @Override
    public List<String> getBookmarks() {
        List<String> bookmarkList = new ArrayList<>();
        bookmarks.find()
                .sort(Sorts.descending("added_at"))
                .forEach(doc -> bookmarkList.add(doc.getString("url")));
        return bookmarkList;
    }

    @Override
    public void deleteBookmark(String url) {
        bookmarks.deleteOne(new Document("url", url));
    }

    // History
    @Override
    public void addHistory(String url) {
        Document doc = new Document("url", url)
                      .append("visited_at", new java.util.Date());
        history.insertOne(doc);
    }

    @Override
    public List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        history.find()
              .sort(Sorts.descending("visited_at"))
              .forEach(doc -> historyList.add(doc.getString("url")));
        return historyList;
    }

    @Override
    public Map<String, List<String>> getHistoryByDay() {
        Map<String, List<String>> historyByDay = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        
        history.find()
              .sort(Sorts.descending("visited_at"))
              .forEach(doc -> {
                  String url = doc.getString("url");
                  Date visitedAt = doc.getDate("visited_at");
                  
                  if (url != null && visitedAt != null) {
                      String dateKey = dateFormat.format(visitedAt);
                      historyByDay.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(url);
                  }
              });
        
        return historyByDay;
    }

    @Override
    public void deleteHistory(String url) {
        history.deleteOne(new Document("url", url));
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
