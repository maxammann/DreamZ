package me.p000ison.dreamz.listener;

import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.manager.DreamManager;
import me.p000ison.dreamz.manager.SettingsManager;
import me.p000ison.dreamz.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

/**
 *
 * @author p000ison
 */
public class DZPlayerListener implements Listener {

    private DreamZ plugin;
    private DreamManager dream = new DreamManager();
    private Util util = new Util();
    private SettingsManager settings = new SettingsManager();

    public DZPlayerListener(DreamZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedEnter(final PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            if (dream.isInDreamWorld(player)) {
                dream.enterDreamInDream(player, event);
            } else {
                plugin.returnLocation.put(player, event.getBed().getLocation());
                dream.enterDreamInNormal(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.getWorld() == plugin.getWorldManager().getDreamWorld() || player.getWorld() == plugin.getWorldManager().getNightMare()) {
            plugin.deathLocation.put(player, player.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            if (player.getWorld() == plugin.getWorldManager().getDreamWorld() && settings.isDreamWorldRespawn()) {
                if (settings.isDreamWorldRespawnAtDeathPoint()) {
                    if (plugin.deathLocation.containsKey(player)) {
                        event.setRespawnLocation(plugin.deathLocation.get(player));
                    }
                } else {
                    if (settings.isDreamWorldRandomSpawn()) {
                        event.setRespawnLocation(util.randomLoc(plugin.getWorldManager().getDreamWorld(), DreamType.DREAMWORLD));
                    } else {
                        event.setRespawnLocation(plugin.getWorldManager().getDreamWorld().getSpawnLocation());
                    }
                }
            } else if (player.getWorld() == plugin.getWorldManager().getNightMare() && settings.isNightMareRespawn()) {
                if (settings.isNightMareRespawnAtDeathPoint()) {
                    if (plugin.deathLocation.containsKey(player)) {
                        event.setRespawnLocation(plugin.deathLocation.get(player));
                    }
                } else {
                    if (settings.isNightMareRandomSpawn()) {
                        event.setRespawnLocation(util.randomLoc(plugin.getWorldManager().getNightMare(), DreamType.NIGHTMARE));
                    } else {
                        event.setRespawnLocation(plugin.getWorldManager().getNightMare().getSpawnLocation());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            if (plugin.getWorldManager().getNightMare() == event.getFrom() || plugin.getWorldManager().getNightMare() == event.getFrom()) {
                if (plugin.schedulers.containsKey(player)) {
                    plugin.getServer().getScheduler().cancelTask(plugin.schedulers.get(player));
                    plugin.schedulers.remove(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("dreamz.join.noteleportback")) {
            if (dream.isInDreamWorld(player)) {
                if (plugin.getSettingsManager().isDreamWorldUsingDuration() || plugin.getSettingsManager().isNightMareUsingDuration()) {
                    if (plugin.schedulers.containsKey(player) == false) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                            @Override
                            public void run() {
                                if (plugin.returnLocation.containsKey(player)) {
                                    player.teleport(plugin.returnLocation.get(player));
                                } else {
                                    player.teleport(plugin.getWorldManager().getDefaultWorld().getSpawnLocation());
                                }
                            }
                        }, (5));
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerHungerChange(FoodLevelChangeEvent  event) {
        if (settings.isDisableHunger() && dream.isInDreamWorld((Player)event.getEntity())) {
            event.setCancelled(true);
        } else if (settings.isDisableHunger() && event.getEntity().getWorld() == plugin.getWorldManager().getNightMare()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Vector vec;
        if (player.hasPermission("dreamz.fly")) {
            if (player.getWorld() == plugin.getWorldManager().getDreamWorld() && plugin.getSettingsManager().getDreamWorldFlyMultiplier() != 0
                    || player.getWorld() == plugin.getWorldManager().getNightMare() && plugin.getSettingsManager().getNightMareFlyMultiplier() != 0) {
                if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                    vec = player.getLocation().getDirection().multiply(plugin.getSettingsManager().getDreamWorldFlyMultiplier() + 5);
                } else {
                    vec = player.getLocation().getDirection().multiply(plugin.getSettingsManager().getNightMareFlyMultiplier() + 5);
                }
                vec.setY(vec.getY() + 0.75);
                player.setVelocity(vec);
                player.setFallDistance(0);
            }
        }
    }
}
