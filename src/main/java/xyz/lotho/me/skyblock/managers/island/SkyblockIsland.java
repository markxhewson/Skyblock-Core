package xyz.lotho.me.skyblock.managers.island;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import xyz.lotho.me.skyblock.Skyblock;

public class SkyblockIsland {

    private final Skyblock instance;

    @Getter
    private final int radius;

    @Getter
    private Location center, cornerOne, cornerTwo;

    public SkyblockIsland(Skyblock instance, int radius) {
        this.instance = instance;
        this.radius = radius;
    }

    public void generate() {
        Island lastIsland = this.instance.getLastIsland();

        int x, y, z;

        if (lastIsland == null) {
            x = 0;
            y = 85;
            z = 0;
        }
        else {
            x = lastIsland.getCenter().getBlockX();
            y = lastIsland.getCenter().getBlockY();
            z = lastIsland.getCenter().getBlockZ();
        }

        int index = instance.getIslandManager().getIslandsArray().size() / 100;
        int lastIndex = (instance.getIslandManager().getIslandsArray().size() - 1) / 100;

        if (index % 2 == 0) { // even rows
            if (index != lastIndex) {
                x -= 250;
            } else {
                z += 250;
            }
        } else { // odd rows
            if (index != lastIndex) {
                x -= 250;
            } else {
                z -= 250;
            }
        }

        this.center = new Location(this.instance.getIslandWorld(), x, y, z);

        this.cornerOne = new Location(this.center.getWorld(), this.center.getX() - radius, this.center.getY() + radius, this.center.getZ() + radius);
        this.cornerTwo = new Location(this.center.getWorld(), this.center.getX() + radius, this.center.getY() - radius, this.center.getZ() - radius);

        this.instance.getIslandWorld().getBlockAt(center).setType(Material.BEDROCK);
        this.instance.getIslandWorld().getBlockAt(cornerOne).setType(Material.END_STONE);
        this.instance.getIslandWorld().getBlockAt(cornerTwo).setType(Material.END_STONE);
    }

}
