package xyz.lotho.me.skyblock;

import com.mongodb.client.model.Sorts;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.skyblock.command.CommandAPI;
import xyz.lotho.me.skyblock.command.impl.IslandCommand;
import xyz.lotho.me.skyblock.database.MongoManager;
import xyz.lotho.me.skyblock.database.utils.MongoUtils;
import xyz.lotho.me.skyblock.listeners.InventoryListener;
import xyz.lotho.me.skyblock.listeners.IslandProtectionListener;
import xyz.lotho.me.skyblock.listeners.MemberListener;
import xyz.lotho.me.skyblock.managers.island.Island;
import xyz.lotho.me.skyblock.managers.island.IslandManager;
import xyz.lotho.me.skyblock.managers.member.MemberManager;
import xyz.lotho.me.skyblock.utils.world.VoidWorldGenerator;

import java.util.Arrays;


public final class Skyblock extends JavaPlugin {

    @Getter
    private static Skyblock instance;

    @Getter
    private World islandWorld;

    @Getter @Setter
    private Island lastIsland;

    @Getter
    private MongoManager mongoManager;
    @Getter
    private MemberManager memberManager;
    @Getter
    private IslandManager islandManager;
    @Getter
    private MongoUtils mongoUtils;

    @Override
    public void onEnable() {
        instance = this;

        mongoManager = new MongoManager(this);
        memberManager = new MemberManager(this);
        islandManager = new IslandManager(this);
        mongoUtils = new MongoUtils(this);

        this.islandWorld = this.getServer().getWorld("islands");

        if (this.islandWorld == null) {
            System.out.println("Creating 'islands' world...");
            WorldCreator worldCreator = new WorldCreator("islands");
            worldCreator.generator(new VoidWorldGenerator());
            worldCreator.createWorld();
            System.out.println("Successfully created 'islands' world.");

            this.islandWorld = this.getServer().getWorld("islands");
        }

        CommandAPI.registerCommands(
                new IslandCommand()
        );

        // load all island data into IslandManager cache
        this.getMongoManager().getIslandsCollection().find().sort(Sorts.ascending("createdAt")).forEach(document -> {
            System.out.println(document.toJson());
            this.getIslandManager().addIsland(document);
        }, (result, t) -> {
            System.out.println("Finished loading " + this.getIslandManager().getIslandsArray().size() + " islands!");
        });

        this.loadListeners();

        // save the last island in the database so new islands don't overlap with each other
        this.getMongoManager().getIslandsCollection().find().sort(Sorts.descending("createdAt")).first((document, throwable) -> {
            if (throwable != null) this.getIslandManager().setLastIsland(document);
        });

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            System.out.println("Running save island task..");
            for (Island island : this.getIslandManager().getIslandsArray()) {
                island.save();
            }
        }, 20 * 60 * 60, 20 * 60 * 60); // task will run every hour to async save every island's updated data to the database
    }

    @Override
    public void onDisable() {
        this.mongoManager.destroy();
    }

    public void loadListeners() {
        Arrays.asList(
                new MemberListener(this),
                new IslandProtectionListener(this),
                new InventoryListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }
}
