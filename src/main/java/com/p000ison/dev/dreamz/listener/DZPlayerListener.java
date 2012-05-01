package com.p000ison.dev.dreamz.listener;

import com.p000ison.dev.dreamz.Dream;
import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.DreamLeaveType;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import com.p000ison.dev.dreamz.util.Util;
import com.p000ison.dev.dreamz.util.dUtil;
import com.p000ison.dev.dreamz.util.sUtil;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
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

    public DZPlayerListener(DreamZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (clickedBlock.getType().equals(Material.BED_BLOCK)) {
                if (plugin.getDreamManager().isInDream(player)) {
                    if (plugin.getSettingsManager().isFakeNetherBeds()) {
                        if (clickedBlock.getWorld().getEnvironment().equals(Environment.NETHER)) {
                            event.setCancelled(true);
                            if (dUtil.isBed(clickedBlock.getLocation(), player.getWorld().getName(), plugin.getSettingsManager().getConfig())) {
                                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED));
                            }
                        }
                    }
                } else {
                    if (plugin.getSettingsManager().isFakeBedsDuringDay()) {
                        if (player.getWorld().getTime() >= 0 && player.getWorld().getTime() <= 14000) {
                            Dream randomDream = dUtil.randomDream(plugin.getSettingsManager().getConfig());
                            plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, randomDream));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBedPlace(BlockPlaceEvent event) {
        if (plugin.getSettingsManager().isPlaceBeds() == false) {
            if (plugin.getDreamManager().isInDream(event.getPlayer())) {
                if (event.getBlockPlaced().getType().equals(Material.BED_BLOCK)) {
                    event.setCancelled(true);
                    for (BlockFace face : BlockFace.values()) {
                        event.getBlockPlaced().getRelative(face).getState().update();
                    }
                    event.getPlayer().sendMessage(Util.color(plugin.getSettingsManager().getCantPlaceBedsMessage()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            Dream randomDream = dUtil.randomDream(plugin.getSettingsManager().getConfig());
            if (randomDream != null) {
                if (randomDream.isOneHundredPercent()) {
                    if (plugin.getDreamManager().isInDream(player)) {
                        plugin.getDreamManager().enterDreamInDream(player, randomDream, event);
                    } else {
                        plugin.RETURN_LOCATIONS.put(player, event.getBed().getLocation());
                        plugin.getDreamManager().enterDreamInNormal(player, randomDream);
                    }
                } else {
                    player.sendMessage("Please set the chances that they have 100% together.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getDreamManager().isInDreamWorld(player)) {
            plugin.DEATH_LOCATIONS.put(player, player.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            if (plugin.getDreamManager().isInDream(player)) {
                final Dream activedream = plugin.getDreamManager().getActiveDream(player);
                if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + activedream.getDream() + ".RespawnInDream")) {
                    if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + activedream.getDream() + ".Spawning.UseRandomSpawn")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                            @Override
                            public void run() {
                                Location loc = sUtil.randomSpawn(plugin.getSettingsManager().getConfig(), activedream.getDreamWorld(), plugin.getSettingsManager().getPreventSpawnOn());
                                if (loc != null) {
                                    event.setRespawnLocation(loc);
                                } else {
                                    player.sendMessage("No randomspawn found!");
                                }
                            }
                        });
                    } else {
                        event.setRespawnLocation(activedream.getDreamWorld().getSpawnLocation());
                    }
                } else if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + activedream.getDream() + ".RespawnAtDeathPoint")) {
                    if (plugin.DEATH_LOCATIONS.containsKey(player)) {
                        event.setRespawnLocation(plugin.DEATH_LOCATIONS.get(player));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            for (String dream : dUtil.getDreams(plugin.getSettingsManager().getConfig())) {
                if (dream.equalsIgnoreCase(event.getFrom().getName())) {
                    if (plugin.getDreamManager().isInDream(player) && !plugin.DREAMS.containsKey(player)) {
                        plugin.getServer().getScheduler().cancelTask(plugin.SCHEDULERS.get(player));
                        plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.WORLD_CHANGE));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("dreamz.join.noteleportback")) {
            if (plugin.getDreamManager().isInDreamWorld(player)) {
                for (String dream : dUtil.getDreams(plugin.getSettingsManager().getConfig())) {
                    if (plugin.getSettingsManager().getConfig().getBoolean("dreams." + dream + ".UsingDuration")) {
                        if (!plugin.SCHEDULERS.containsKey(player)) {

                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.DISCONNECT));
                                        if (plugin.RETURN_LOCATIONS.containsKey(player)) {
                                            player.teleport(plugin.RETURN_LOCATIONS.get(player));
                                        } else {
                                            player.teleport(plugin.getWorldManager().getDefaultWorld().getSpawnLocation());
                                        }
                                    } catch (Exception ex) {
                                        plugin.getLogger().log(Level.SEVERE, String.format("Failed teleporting!(%s)", ex.getMessage()));
                                    }
                                }
                            }, 100L);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getDreamManager().isInDream(player) && plugin.getSettingsManager().getConfig().getBoolean("dreams." + plugin.getDreamManager().getActiveDream(player).getDream() + ".DisableHunger")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (plugin.SCHEDULERS.containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(plugin.SCHEDULERS.get(event.getPlayer()));
            plugin.SCHEDULERS.remove(player);
        }
        if (plugin.RETURN_LOCATIONS.containsKey(player)) {
            plugin.RETURN_LOCATIONS.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Vector vec;
        if (player.hasPermission("dreamz.fly")) {
            if (plugin.getDreamManager().isInDream(player) && plugin.getSettingsManager().getConfig().getDouble("dreams." + plugin.getDreamManager().getActiveDream(player).getDream() + ".FlyMultiplier") != -1) {
                vec = player.getLocation().getDirection().multiply(plugin.getSettingsManager().getConfig().getDouble("dreams." + plugin.getDreamManager().getActiveDream(player).getDream() + ".FlyMultiplier" + 5));
                player.setAllowFlight(true);
                vec.setY(vec.getY() + 0.75);
                player.setVelocity(vec);
                player.setFallDistance(0);
                player.setAllowFlight(false);
            }
        }
    }
}
