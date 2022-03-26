package xyz.lotho.me.skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.member.Member;

import java.util.UUID;

public class MemberListener implements Listener {

    private final Skyblock instance;

    public MemberListener(Skyblock instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        if (!this.instance.getMemberManager().isPresent(uuid)) {
            this.instance.getMemberManager().addMember(uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Member member = this.instance.getMemberManager().getMember(player.getUniqueId());

        if (!member.isLoaded()) {
            this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, member::load);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Member member = this.instance.getMemberManager().getMember(player.getUniqueId());

        if (member != null) {
            this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, member::save);
            this.instance.getMemberManager().removeMember(player.getUniqueId());
        }
    }
}
