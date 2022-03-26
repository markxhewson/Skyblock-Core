package xyz.lotho.me.skyblock.managers.island;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.member.Member;

import java.util.ArrayList;
import java.util.UUID;

public class Island {

    private final Skyblock instance;

    @Getter @Setter
    private final int islandID;

    private final UUID islandOwner;

    @Getter @Setter
    private Location center, cornerOne, cornerTwo;

    @Getter
    private ArrayList<UUID> membersArray;


    public Island(Skyblock instance, int islandID, UUID islandOwner, ArrayList<UUID> membersArray, Location center, Location cornerOne, Location cornerTwo) {
        this.instance = instance;

        this.islandID = islandID;
        this.islandOwner = islandOwner;
        this.membersArray = membersArray;

        this.center = center;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
    }

    public void save() {

    }

    public int getIslandID() {
        return this.islandID;
    }

    public ArrayList<UUID> getMembers() {
        return this.membersArray;
    }
}
