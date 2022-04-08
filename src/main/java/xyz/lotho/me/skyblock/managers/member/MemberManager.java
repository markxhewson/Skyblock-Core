package xyz.lotho.me.skyblock.managers.member;

import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemberManager {

    private final Skyblock instance;
    private final Map<UUID, Member> membersMap = new HashMap<>();

    public MemberManager(Skyblock instance) {
        this.instance = instance;
    }

    public void removeMember(UUID uuid) {
        this.membersMap.remove(uuid);
    }

    public void addMember(UUID uuid) {
        this.membersMap.put(uuid, new Member(this.instance, uuid));
    }

    public Member getMember(Player player) { return getMember(player.getUniqueId()); }

    public Member getMember(UUID uuid) {
        return this.membersMap.get(uuid);
    }

    public boolean isPresent(UUID uuid) {
        return this.membersMap.containsKey(uuid);
    }

    public Map<UUID, Member> getMembersMap() {
        return this.membersMap;
    }

    public Island getMemberIsland(Member member) {
        if (member.getIsland() == null) return null;

        Island memberIsland = null;

        for (Island island : this.instance.getIslandManager().getIslandsArray()) {
            if (island.getIslandID() == member.getIsland().getIslandID()) {
                memberIsland = island;
            }
        }

        return memberIsland;
    }
}
