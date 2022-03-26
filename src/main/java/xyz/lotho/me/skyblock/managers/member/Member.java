package xyz.lotho.me.skyblock.managers.member;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;

import java.util.UUID;

public class Member {

    private final Skyblock instance;

    @Getter @Setter
    private int id;
    @Getter @Setter
    private UUID uuid;
    @Getter @Setter
    private Island island;
    @Getter @Setter
    private long createdAt;
    @Getter @Setter
    private long lastLogin;

    @Getter @Setter
    private boolean loaded = false;

    public Member(Skyblock instance, UUID uuid) {
        this.instance = instance;

        this.uuid = uuid;
    }

    public boolean hasIsland() {
        return this.island != null;
    }

    public void load() {
        this.instance.getMongoManager().getPlayersCollection().find(Filters.eq("uuid", this.getUuid().toString())).first((document, throwable) -> {
            if (document == null || document.isEmpty()) {
                this.instance.getMongoManager().getPlayersCollection().countDocuments(((documents, t) -> {
                    Document insertDocument = new Document()
                            .append("_id", documents.intValue() + 1)
                            .append("uuid", this.getUuid().toString())
                            .append("island", 0)
                            .append("lastLogin", System.currentTimeMillis())
                            .append("createdAt", System.currentTimeMillis());

                    this.instance.getMongoUtils().replaceOneMember(this.getUuid().toString(), insertDocument);
                    this.instance.getServer().getScheduler().runTaskLaterAsynchronously(this.instance, this::load, 20L);
                }));
            }
            else {
                System.out.println(document.toJson());

                this.setId(document.getInteger("_id"));
                this.setUuid(UUID.fromString(document.getString("uuid")));
                this.setIsland(this.instance.getIslandManager().findIslandByID(document.getInteger("island")));
                this.setLastLogin(document.getLong("lastLogin"));
                this.setCreatedAt(document.getLong("createdAt"));

                this.setLoaded(true);
            }
        });
}

    public void save() {
        Document saveDocument = new Document()
                .append("_id", this.getId())
                .append("uuid", this.getUuid().toString())
                .append("island", this.getIsland() == null ? 0 : this.getIsland().getIslandID())
                .append("lastLogin", System.currentTimeMillis())
                .append("createdAt", this.getCreatedAt());


        this.instance.getMongoUtils().replaceOneMember(this.getUuid().toString(), saveDocument);
    }
}
