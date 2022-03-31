package xyz.lotho.me.skyblock.managers.island;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.member.IslandMember;
import xyz.lotho.me.skyblock.managers.island.member.IslandMemberManager;
import xyz.lotho.me.skyblock.managers.island.member.IslandRole;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Island {

    private final Skyblock instance;

    @Getter @Setter
    private int islandID;

    @Getter @Setter
    private UUID islandOwner;

    @Getter @Setter
    private int radius;

    @Getter @Setter
    private Location center, cornerOne, cornerTwo;

    @Getter @Setter
    private IslandMemberManager islandMemberManager;

    @Getter @Setter
    private long createdAt;


    public Island(Skyblock instance) {
        this.instance = instance;

        this.islandMemberManager = new IslandMemberManager(this.instance);
    }

    public boolean isBlockWithinBounds(Block block) {
        Location blockLocation = block.getLocation();

        BlockVector3 one = BlockVector3.at(this.getCornerOne().getBlockX(), this.getCornerOne().getBlockY(), this.getCornerOne().getBlockZ());
        BlockVector3 two = BlockVector3.at(this.getCornerTwo().getBlockX(), this.getCornerTwo().getBlockY(), this.getCornerTwo().getBlockZ());

        CuboidRegion region = new CuboidRegion(one, two);
        return region.contains(BlockVector3.at(blockLocation.getX(), blockLocation.getBlockY(), blockLocation.getBlockZ()));
    }

    public void addMember(UUID uuid, IslandRole islandRole) {
        this.getIslandMemberManager().addMember(uuid, islandRole);
    }

    public boolean isMember(UUID uuid) {
        return this.getIslandMemberManager().isMember(uuid);
    }

    public void loadTheme() throws IOException {
        File defaultSchematic = new File(this.instance.getDataFolder().getAbsolutePath() + "/schematics/" + "endIsland" + ".schem");

        BlockVector3 pasteLocation = BlockVector3.at(this.getCenter().getBlockX(), this.getCenter().getBlockY(), this.getCenter().getBlockZ());
        ClipboardFormat format = ClipboardFormats.findByFile(defaultSchematic);

        if (format == null) return;
        Clipboard clipboard = format.getReader(new FileInputStream(defaultSchematic)).read();

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(FaweAPI.getWorld(Objects.requireNonNull(this.getCenter().getWorld()).getName()))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(pasteLocation)
                    .ignoreAirBlocks(true)
                    .copyEntities(false)
                    .copyBiomes(true)
                    .build();

            Operations.complete(operation);
            editSession.flushQueue();
        }
    }

    public void resetTheme() throws IOException {
        BlockVector3 one = BlockVector3.at(this.getCornerOne().getBlockX(), this.getCornerOne().getBlockY(), this.getCornerOne().getBlockZ());
        BlockVector3 two = BlockVector3.at(this.getCornerTwo().getBlockX(), this.getCornerTwo().getBlockY(), this.getCornerTwo().getBlockZ());

        new CuboidRegion(one, two).forEach(blockVector3 -> {
            Location location = new Location(this.instance.getIslandWorld(), blockVector3.getBlockX(), blockVector3.getBlockY(), blockVector3.getBlockZ());
            if (location.getBlock().getType() != Material.AIR) this.instance.getIslandWorld().getBlockAt(location).setType(Material.AIR);
        });

        this.loadTheme();
    }

    public void save() {
        Document islandMembers = new Document();

        this.getIslandMemberManager().getIslandMembers().forEach(((uuid, islandMember) -> {
            islandMembers.append(uuid.toString(), islandMember.getIslandRole().toString());
        }));

        Document document = new Document()
                .append("_id", this.getIslandID())
                .append("islandOwner", this.getIslandOwner().toString())
                .append("islandRadius", this.getRadius())
                .append("members", islandMembers)
                .append("center", new Document()
                        .append("x", this.getCenter().getBlockX())
                        .append("y", this.getCenter().getBlockY())
                        .append("z", this.getCenter().getBlockZ())
                )
                .append("createdAt", this.getCreatedAt());

        this.instance.getMongoUtils().replaceOneIsland(this, document);
    }
}
