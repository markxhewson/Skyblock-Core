package xyz.lotho.me.skyblock.managers.island.invite;

import lombok.Getter;
import lombok.Setter;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;

import java.util.UUID;

public class Invite {

    private final Skyblock instance;

    @Getter @Setter
    private UUID inviter;
    @Getter @Setter
    private Island islandToJoin;
    @Getter @Setter
    private long inviteTime;

    public Invite(Skyblock instance) {
        this.instance = instance;
    }

    public long getExpireTime() { // 5 mins
        return this.inviteTime + 300000;
    }
}
