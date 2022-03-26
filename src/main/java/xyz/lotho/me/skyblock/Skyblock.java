package xyz.lotho.me.skyblock;

import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.status.PacketStatusOutServerInfo;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.skyblock.command.CommandAPI;
import xyz.lotho.me.skyblock.command.impl.IslandCommand;
import xyz.lotho.me.skyblock.database.MongoManager;
import xyz.lotho.me.skyblock.database.utils.MongoUtils;
import xyz.lotho.me.skyblock.listeners.MemberListener;
import xyz.lotho.me.skyblock.managers.island.Island;
import xyz.lotho.me.skyblock.managers.island.IslandManager;
import xyz.lotho.me.skyblock.managers.member.MemberManager;
import xyz.lotho.me.skyblock.world.VoidWorldGenerator;

import java.util.Arrays;
import java.util.Objects;


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

        this.getMongoManager().getIslandsCollection().find().forEach(document -> {
            System.out.println(document.toJson());
            this.getIslandManager().addIsland(document);
        }, (result, t) -> {
            System.out.println("Finished loading islands!");
        });

        this.loadListeners();

        this.getServer().getScheduler().runTaskLater(this, () -> {
            System.out.println(this.getIslandManager().getIslandsArray());
        }, 100L);
    }

    @Override
    public void onDisable() {
        this.mongoManager.destroy();
    }

    public void loadListeners() {
        Arrays.asList(
                new MemberListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }
}
