package xyz.lotho.me.skyblock.database;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.*;
import org.bson.Document;
import xyz.lotho.me.skyblock.Skyblock;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoManager {

    private final Skyblock instance;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> players, islands;

    public MongoManager(Skyblock instance) {
        this.instance = instance;

        disableLogging();
        connect();
    }

    public void disableLogging() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }

    public void connect() {
        try {
            mongoClient = MongoClients.create(new ConnectionString("mongodb+srv://admin:admin@cluster0.cexgn.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"));

            mongoDatabase = mongoClient.getDatabase("skyblock");

            players = mongoDatabase.getCollection("players");
            islands = mongoDatabase.getCollection("islands");
        } catch (Exception exception) {
            this.instance.getServer().getConsoleSender().sendMessage("Unable to connect to database, disabling plugin.");
            this.instance.getServer().getPluginManager().disablePlugin(this.instance);
            exception.printStackTrace();
        }
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    public MongoCollection<Document> getPlayersCollection() {
        return this.players;
    }

    public MongoCollection<Document> getIslandsCollection() {
        return this.islands;
    }

    public void destroy() {
        this.mongoClient.close();
    }
}
