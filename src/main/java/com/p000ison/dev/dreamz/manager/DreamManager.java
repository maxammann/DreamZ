package com.p000ison.dev.dreamz.manager;

import com.p000ison.dev.dreamz.Dream;
import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.DreamLeaveType;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import com.p000ison.dev.dreamz.util.Util;
import com.p000ison.dev.dreamz.util.dUtil;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 *
 * @author p000ison
 */
public class DreamManager {

    private DreamZ plugin;

    public DreamManager(DreamZ plugin) {
        this.plugin = plugin;
    }

    /**
     * teleports the player to the dream from a normal world
     *
     * @param player the player to teleport
     */
    public void enterDreamInNormal(final Player player, final Dream dream) {


        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, dream));
                if (plugin.getSettingsManager().isDebugging() == true) {
                    plugin.getLogger().log(Level.INFO, String.format("player teleported enterded %s", dream));
                }
            }
        }, (plugin.getSettingsManager().getDelay() * 20));

        if (dream == null) {
            new DreamZPlayerDreamEnterEvent(player, null).setCancelled(true);
        }

    }

    /**
     * teleports the player to the dream from a dream
     *
     * @param player the player to teleport
     * @param event event to cancel
     */
    public void enterDreamInDream(Player player, Dream dream, PlayerBedEnterEvent event) {
        if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + dream.getDream() + ".EscapeThrowBed")) {
            Location loc = event.getBed().getLocation();
            if (dUtil.isBed(loc, dream.getDream(), plugin.getSettingsManager().getConfig())) {
                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED));
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(Util.color(plugin.getSettingsManager().getCantEscapeMessage()));
            if (plugin.getSettingsManager().isDebugging() == true) {
                plugin.getLogger().log(Level.INFO, "player tried to escape");
            }
        }
    }

    public boolean isInDreamWorld(Player player) {
        return dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(player.getWorld().getName());
    }

    public boolean isInDream(Player player) {
        return plugin.SCHEDULERS.containsKey(player);
    }
}