package xyz.lotho.me.skyblock.managers.island.invite;

import lombok.Getter;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;
import xyz.lotho.me.skyblock.utils.chat.Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class InviteManager {

    private Skyblock instance;

    @Getter
    private final Map<UUID, Invite> inviteMap = new HashMap<>();

    public InviteManager(Skyblock instance) {
        this.instance = instance;
    }

    public boolean createIslandInvite(UUID inviter, UUID player, Island island) {
        if (this.getInviteMap().containsKey(player)) return false;

        Player invitedPlayer = this.instance.getServer().getPlayer(player);

        Invite invite = new Invite(this.instance);
        invite.setInviter(inviter);
        invite.setInviteTime(System.currentTimeMillis());
        invite.setIslandToJoin(island);

        island.getIslandMemberManager().getIslandMembers().forEach(((uuid, islandMember) -> {
            Player islandPlayer = Skyblock.getInstance().getServer().getPlayer(uuid);
            if (islandPlayer == null) return;

            islandPlayer.sendMessage(Chat.color("&a&l<!> &f" + Objects.requireNonNull(this.instance.getServer().getPlayer(inviter)).getName() + " &ahas sent an invite to &f" + invitedPlayer.getName() + " &ato join your island."));
        }));

        this.getInviteMap().put(player, invite);
        return true;
    }

    public boolean isAlreadyInvited(Island island) {
        AtomicBoolean alreadyInvited = new AtomicBoolean(false);

        this.getInviteMap().forEach((uuid, invite) -> {
            if (invite.getIslandToJoin().getIslandID() == island.getIslandID()) {
                alreadyInvited.set(true);
            }
        });

        return alreadyInvited.get();
    }

    public void removeInvite(UUID uuid) {
        this.getInviteMap().remove(uuid);
    }

    public boolean hasInvite(UUID uuid) {
        return this.getInviteMap().containsKey(uuid);
    }

    public Invite getInvite(UUID uuid) {
        return this.getInviteMap().get(uuid);
    }
}
