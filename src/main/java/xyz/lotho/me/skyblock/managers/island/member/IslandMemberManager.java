package xyz.lotho.me.skyblock.managers.island.member;

import lombok.Getter;
import xyz.lotho.me.skyblock.Skyblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandMemberManager {

    private final Skyblock instance;

    @Getter
    private final Map<UUID, IslandMember> islandMembers = new HashMap<>();

    public IslandMemberManager(Skyblock instance) {
        this.instance = instance;
    }

    public void addMember(UUID uuid, IslandRole islandRole) {
        if (this.getIslandMembers().containsKey(uuid)) return;

        IslandMember islandMember = new IslandMember(this.instance, uuid);
        islandMember.setIslandRole(islandRole);

        this.getIslandMembers().put(uuid, islandMember);
    }

    public boolean isMember(UUID uuid) {
        return this.getIslandMembers().containsKey(uuid);
    }

    public void removeMember(UUID uuid) {
        this.getIslandMembers().remove(uuid);
    }
}
