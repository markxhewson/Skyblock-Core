package xyz.lotho.me.skyblock.managers.island;

import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        Document centerData = document.get("center", new Document());
        int radius = document.getInteger("islandRadius");

        Location center = new Location(this.instance.getIslandWorld(), centerData.getInteger("x"), centerData.getInteger("y"), centerData.getInteger("z"));
        Location cornerOne = new Location(center.getWorld(), center.getX() - radius, center.getY() + radius, center.getZ() + radius);
        Location cornerTwo = new Location(center.getWorld(), center.getX() + radius, center.getY() - radius, center.getZ() - radius);

        this.islandsArray.add(
                new Island(
                        this.instance,
                        document.getInteger("_id"),
                        UUID.fromString(document.getString("islandOwner")),
                        document.get("members", new ArrayList<>()),
                        center,
                        cornerOne,
                        cornerTwo
                )
        );
    }

    public void createIsland(Player player) {
        SkyblockIsland skyblockIsland = new SkyblockIsland(this.instance, 25);
        skyblockIsland.generate();

        this.instance.getMongoManager().getIslandsCollection().countDocuments((documents, throwable) -> {
            int islandID = documents.intValue() + 1;

            this.islandsArray.add(
                    new Island(
                            this.instance,
                            documents.intValue() + 1,
                            player.getUniqueId(),
                            new ArrayList<>(),
                            skyblockIsland.getCenter(),
                            skyblockIsland.getCornerOne(),
                            skyblockIsland.getCornerTwo()
                    )
            );

            Document document = new Document()
                    .append("_id", islandID)
                    .append("islandOwner", player.getUniqueId().toString())
                    .append("islandRadius", skyblockIsland.getRadius())
                    .append("members", new ArrayList<>())
                    .append("center", new Document()
                            .append("x", skyblockIsland.getCenter().getBlockX())
                            .append("y", skyblockIsland.getCenter().getBlockY())
                            .append("z", skyblockIsland.getCenter().getBlockZ())
                    );

            this.instance.getMongoUtils().replaceOneIsland(this.findIslandByID(islandID), document);

            Member member = this.instance.getMemberManager().getMember(player.getUniqueId());
            member.setIsland(this.findIslandByID(islandID));

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
