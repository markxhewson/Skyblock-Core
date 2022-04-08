package xyz.lotho.me.skyblock.managers.member;

import com.mongodb.client.model.Filters;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;

import java.util.UUID;

@Data
public class Member {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Skyblock instance;

    // Persisted //
    private int id;
    private UUID uuid;
    private Island island;
    private long createdAt;
    private long lastLogin;

    // Internal //
    private boolean loaded = false;

    // Not persisted //
    private boolean islandProtectionBypass = false;

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
