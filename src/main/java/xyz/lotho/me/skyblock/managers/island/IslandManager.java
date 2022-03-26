package xyz.lotho.me.skyblock.managers.island;

import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class IslandManager {

    private final Skyblock instance;
    private final ArrayList<Island> islandsArray = new ArrayList<>();

    public IslandManager(Skyblock instance) {
        this.instance = instance;
    }

    public ArrayList<Island> getIslandsArray() {
        return this.islandsArray;
    }

    public void addIsland(Document document) {
        Island island = this.loadIsland(document);
        this.getIslandsArray().add(island);
    }

    public void setLastIsland(Document document) {
        Island island = this.loadIsland(document);
        this.instance.setLastIsland(island);
    }

    public Island loadIsland(Document document) {
        Document centerData = document.get("center", new Document());
        int radius = document.getInteger("islandRadius");

        Location center = new Location(this.instance.getIslandWorld(), centerData.getInteger("x"), centerData.getInteger("y"), centerData.getInteger("z"));
        Location cornerOne = new Location(center.getWorld(), center.getX() - radius, center.getY() + radius, center.getZ() + radius);
        Location cornerTwo = new Location(center.getWorld(), center.getX() + radius, center.getY() - radius, center.getZ() - radius);

        Island island = new Island(this.instance);
        island.setIslandID(document.getInteger("_id"));
        island.setIslandOwner(UUID.fromString(document.getString("islandOwner")));
        island.setMembersArray(document.get("members", new ArrayList<>()));
        island.setCenter(center);
        island.setCornerOne(cornerOne);
        island.setCornerTwo(cornerTwo);
        island.setRadius(radius);
        island.setCreatedAt(document.getLong("createdAt"));

        return island;
    }

    public void createIsland(Player player) {
        SkyblockIsland skyblockIsland = new SkyblockIsland(this.instance, 25);
        skyblockIsland.generate();

        this.instance.getMongoManager().getIslandsCollection().countDocuments((documents, throwable) -> {
            int islandID = documents.intValue() + 1;

            Island island = new Island(this.instance);
            island.setIslandID(islandID);
            island.setIslandOwner(player.getUniqueId());
            island.setMembersArray(new ArrayList<>());
            island.setCenter(skyblockIsland.getCenter());
            island.setCornerOne(skyblockIsland.getCornerOne());
            island.setCornerTwo(skyblockIsland.getCornerTwo());
            island.setRadius(skyblockIsland.getRadius());
            island.setCreatedAt(System.currentTimeMillis());

            this.getIslandsArray().add(island);

            Document document = new Document()
                    .append("_id", island.getIslandID())
                    .append("islandOwner", island.getIslandOwner().toString())
                    .append("islandRadius", island.getRadius())
                    .append("members", island.getMembersArray())
                    .append("center", new Document()
                            .append("x", island.getCenter().getBlockX())
                            .append("y", island.getCenter().getBlockY())
                            .append("z", island.getCenter().getBlockZ())
                    )
                    .append("createdAt", island.getCreatedAt());

            try {
                island.loadTheme();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.instance.getMongoUtils().replaceOneIsland(island, document);
            this.instance.setLastIsland(island);

            Member member = this.instance.getMemberManager().getMember(player.getUniqueId());
            member.setIsland(island);

            player.sendMessage(Chat.color("&a&l<!> &aYour island has been created! &7You can travel to it using /island home!"));
        });
    }

    public Island findIslandByID(int id) {
        Island island = null;

        for (Island islands : this.getIslandsArray()) {
            if (islands.getIslandID() == id) {
                island = islands;
            }
        }

        return island;
    }
}
