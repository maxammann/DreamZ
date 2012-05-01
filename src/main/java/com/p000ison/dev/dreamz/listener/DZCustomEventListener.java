package com.p000ison.dev.dreamz.listener;

import com.p000ison.dev.dreamz.Dream;
import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.DreamLeaveType;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import com.p000ison.dev.dreamz.util.Util;
import com.p000ison.dev.dreamz.util.sUtil;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author p000ison
 */
public class DZCustomEventListener implements Listener {

    DreamZ plugin;

    public DZCustomEventListener(DreamZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDreamEnter(DreamZPlayerDreamEnterEvent event) {
        final Player player = event.getPlayer();
        final Dream dream = event.getDream();

        if (event.isCancelled()) {
            return;
        }

        if (dream != null && dream.getDream() != null && dream.getDreamWorld() != null && dream.isOneHundredPercent()) {
            if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + dream.getDream() + ".Spawning.UseRandomSpawn")) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        Location loc = sUtil.randomSpawn(plugin.getSettingsManager().getConfig(), dream.getDreamWorld(), plugin.getSettingsManager().getPreventSpawnOn());
                        if (loc != null) {
                            player.teleport(loc);
                        } else {
                            plugin.getLogger().log(Level.INFO, String.format("No Random-Spawn found for %s.", player.getName()));
                        }
                        player.setNoDamageTicks(40);
                    }
                });
            } else {
                player.teleport(dream.getDreamWorld().getSpawnLocation());
                player.setNoDamageTicks(40);
            }
            player.sendMessage(Util.color(String.format(plugin.getSettingsManager().getEnterDreamMessage(), dream.getDream())));
            
            if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + dream.getDream() + ".UsingDuration")) {
                plugin.SCHEDULERS.put(player, plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.NORMAL));
                        if (plugin.getSettingsManager().isDebugging() == true) {
                            plugin.getLogger().log(Level.INFO, String.format("%s teleported back after the duration expired.", player.getName()));
                        }
                    }
                }, (plugin.getSettingsManager().getConfig().getLong("dreams." + dream.getDream() + ".Duration") * 20)));
            }
            dream.applyWeather();
            player.setHealth(player.getMaxHealth());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDreamLeave(DreamZPlayerDreamLeaveEvent event) {
        final Player player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        if (player.isOnline() && plugin.getDreamManager().isInDream(player)) {
            player.sendMessage(Util.color(String.format(plugin.getSettingsManager().getLeaveDreamMessage(), /*plugin.getDreamManager().getActiveDream(player).getDream()*/ player.getWorld().getName())));

            //keep order: first remove the player out of the map, then teleport and then remove the player out of the scheduler
            if (plugin.DREAMS.containsKey(player)) {
                plugin.DREAMS.remove(player);
            }
            if (plugin.RETURN_LOCATIONS.containsKey(player)) {
                player.teleport(plugin.RETURN_LOCATIONS.get(player));
            } else {
                player.teleport(plugin.getWorldManager().getDefaultWorld().getSpawnLocation());
            }
            player.setHealth(player.getMaxHealth());
            player.setNoDamageTicks(40);
            player.setFallDistance(0);

            plugin.RETURN_TELEPORT_TIMER.put(player, true);
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    plugin.RETURN_TELEPORT_TIMER.put(player, false);
                }
            }, (200));
        }


        if (plugin.SCHEDULERS.containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(plugin.SCHEDULERS.get(player));
            plugin.SCHEDULERS.remove(player);
        }
    }
}
