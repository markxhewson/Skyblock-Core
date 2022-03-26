package xyz.lotho.me.skyblock.database.utils;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;


public class MongoUtils {

    private final Skyblock instance;

    public MongoUtils(Skyblock instance) {
        this.instance = instance;
    }

    public void replaceOneMember(String uuid, Document document) {
        this.instance.getMongoManager().getPlayersCollection().replaceOne(Filters.eq("uuid", uuid), document, new UpdateOptions().upsert(true), (result, throwable) -> {});
    }

    public void replaceOneIsland(Island island, Document document) {
        this.instance.getMongoManager().getIslandsCollection().replaceOne(Filters.eq("_id", island.getIslandID()), document, new UpdateOptions().upsert(true), (result, throwable) -> {
            if (throwable != null) System.out.println(throwable);
        });
    }
}
