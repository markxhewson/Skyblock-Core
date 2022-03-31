package xyz.lotho.me.skyblock.managers.island.member;

import lombok.Getter;
import lombok.Setter;
import xyz.lotho.me.skyblock.Skyblock;

import java.util.UUID;

public class IslandMember {

    private final Skyblock instance;

    @Getter @Setter
    private UUID uuid;
    @Getter @Setter
    private IslandRole islandRole;

    public IslandMember(Skyblock instance, UUID uuid) {
        this.instance = instance;
        this.islandRole = IslandRole.ISLAND_MEMBER;
    }
}
