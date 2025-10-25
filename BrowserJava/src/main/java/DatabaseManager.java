import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
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
    public void addBookmark(String url) {
        Document doc = new Document("url", url)
                      .append("added_at", new java.util.Date());
        bookmarks.insertOne(doc);
    }

    public List<String> getBookmarks() {
        List<String> bookmarkList = new ArrayList<>();
        bookmarks.find()
                .sort(Sorts.descending("added_at"))
                .forEach(doc -> bookmarkList.add(doc.getString("url")));
        return bookmarkList;
    }

    public void deleteBookmark(String url) {
        bookmarks.deleteOne(new Document("url", url));
    }

    // History
    public void addHistory(String url) {
        Document doc = new Document("url", url)
                      .append("visited_at", new java.util.Date());
        history.insertOne(doc);
    }

    public List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        history.find()
              .sort(Sorts.descending("visited_at"))
              .forEach(doc -> historyList.add(doc.getString("url")));
        return historyList;
    }

    public void deleteHistory(String url) {
        history.deleteOne(new Document("url", url));
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
