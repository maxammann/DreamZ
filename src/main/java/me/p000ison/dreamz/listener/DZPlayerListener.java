package me.p000ison.dreamz.listener;

import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
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
                            if (plugin.getUtil().checkBed(event.getClickedBlock().getLocation())) {
                                DreamZPlayerDreamLeaveEvent DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED);
                                plugin.getServer().getPluginManager().callEvent(DPDLE);
                                plugin.getDreamManager().leave(player, player.getWorld().equals(plugin.getWorldManager().getDreamWorld()) ? DreamType.DREAMWORLD : DreamType.NIGHTMARE, DreamLeaveType.BED);
                            }
                        }
                    }
                }
//                if (plugin.getSettingsManager().isFakeBedsDuringDay()) {
//                    plugin.getDreamManager().enterDreamInNormal(event.getPlayer());
//                }
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
                    event.getPlayer().sendMessage(plugin.getUtil().color(plugin.getSettingsManager().getCantPlaceBedsMessage()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("dreamz.enter")) {
            if (plugin.getDreamManager().isInDreamWorld(player)) {
                plugin.getDreamManager().enterDreamInDream(player, event);
            } else {
                plugin.returnLocation.put(player, event.getBed().getLocation());
                plugin.getDreamManager().enterDreamInNormal(player);
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
            if (player.getWorld() == plugin.getWorldManager().getDreamWorld() && plugin.getSettingsManager().isDreamWorldRespawn()) {
                if (plugin.getSettingsManager().isDreamWorldRespawnAtDeathPoint()) {
                    if (plugin.deathLocation.containsKey(player)) {
                        event.setRespawnLocation(plugin.deathLocation.get(player));
                    }
                } else {
                    if (plugin.getSettingsManager().isDreamWorldRandomSpawn()) {
                        event.setRespawnLocation(plugin.getUtil().randomLoc(plugin.getWorldManager().getDreamWorld(), DreamType.DREAMWORLD));
                    } else {
                        event.setRespawnLocation(plugin.getWorldManager().getDreamWorld().getSpawnLocation());
                    }
                }
            } else if (player.getWorld() == plugin.getWorldManager().getNightMare() && plugin.getSettingsManager().isNightMareRespawn()) {
                if (plugin.getSettingsManager().isNightMareRespawnAtDeathPoint()) {
                    if (plugin.deathLocation.containsKey(player)) {
                        event.setRespawnLocation(plugin.deathLocation.get(player));
                    }
                } else {
                    if (plugin.getSettingsManager().isNightMareRandomSpawn()) {
                        event.setRespawnLocation(plugin.getUtil().randomLoc(plugin.getWorldManager().getNightMare(), DreamType.NIGHTMARE));
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
            if (plugin.getDreamManager().isInDreamWorld(player)) {
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
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        if (plugin.getSettingsManager().isDisableHunger() && plugin.getDreamManager().isInDream((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Vector vec;
        if (player.hasPermission("dreamz.fly")) {
            if ((player.getWorld() == plugin.getWorldManager().getDreamWorld() && plugin.getSettingsManager().getDreamWorldFlyMultiplier() != 0)
                    || (player.getWorld() == plugin.getWorldManager().getNightMare() && plugin.getSettingsManager().getNightMareFlyMultiplier() != 0)) {
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
