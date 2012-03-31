package me.p000ison.dreamz.manager;

import java.util.logging.Level;
import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import me.p000ison.dreamz.util.Inventory;
import me.p000ison.dreamz.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 *
 * @author p000ison
 */
public class DreamManager {

    private DreamZ plugin;
    private DreamZPlayerDreamLeaveEvent DPDLE;
    private DreamZPlayerDreamEnterEvent DPDEE;
    private SettingsManager settings;
    private Util util = new Util();
    private Inventory inv = new Inventory();

    public DreamManager() {
        plugin = DreamZ.getInstance();
        settings = new SettingsManager();
    }

    /**
     * teleports the player to the dream from a normal world
     *
     * @param player the player to teleport
     */
    public void enterDreamInNormal(final Player player) {
        DreamType randomDream = util.randomDream();
        switch (randomDream) {
            case DREAMWORLD:
                if (!settings.isDreamWorldEnabled()) {
                    return;
                }
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        DPDEE = new DreamZPlayerDreamEnterEvent(player, DreamType.DREAMWORLD);
                        plugin.getServer().getPluginManager().callEvent(DPDEE);
                        enter(player, DreamType.DREAMWORLD);
                        if (settings.isDebugging() == true) {
                            plugin.getLogger().log(Level.INFO, "player teleported enterded DreamWorld");
                        }
                    }
                }, (settings.getDelay() * 20));
                break;
            case NIGHTMARE:
                if (!settings.isNightMareEnabled()) {
                    return;
                }

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        DPDEE = new DreamZPlayerDreamEnterEvent(player, DreamType.NIGHTMARE);
                        plugin.getServer().getPluginManager().callEvent(DPDEE);
                        enter(player, DreamType.NIGHTMARE);
                        if (settings.isDebugging() == true) {
                            plugin.getLogger().log(Level.INFO, "player entered NightMare");
                        }
                    }
                }, (settings.getDelay() * 20));
                break;
            case NOTHING:
                DPDEE = new DreamZPlayerDreamEnterEvent(player, null);
                DPDEE.setCancelled(true);
//                DPDEE = new DreamZPlayerDreamEnterEvent(player, DreamType.DREAM);
//                DPDEE.setCancelled(true);
                break;
        }
    }

    /**
     * teleports the player to the dream from a dream
     *
     * @param player the player to teleport
     * @param event event to cancel
     */
    public void enterDreamInDream(Player player, PlayerBedEnterEvent event) {
        if (settings.isCanEscapeThrowBed()) {
            Location loc = event.getBed().getLocation();
            if (loc.getX() == settings.getDreamWorldBedX() && loc.getY() == settings.getDreamWorldBedY() && loc.getZ() == settings.getDreamWorldBedZ()
                    || loc.getX() == settings.getNightMareBedX() && loc.getY() == settings.getNightMareBedY() && loc.getZ() == settings.getNightMareBedZ()) {
                DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED);
                plugin.getServer().getPluginManager().callEvent(DPDLE);
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(util.color(settings.getCantEscapeMessage()));
            if (settings.isDebugging() == true) {
                plugin.getLogger().log(Level.INFO, "player tried to escape");
            }
        }
    }

    /**
     * starts the timer to return
     *
     * @param player the player to teleport
     * @param event event to cancel
     */
    public void startRetrunTimer(final Player player, DreamType dtype) {
        if (dtype == DreamType.DREAMWORLD) {
            plugin.schedulers.put(player, plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.NORMAL);
                    plugin.getServer().getPluginManager().callEvent(DPDLE);
                    leave(player, DreamType.DREAMWORLD, DreamLeaveType.NORMAL);
                    if (settings.isDebugging() == true) {
                        plugin.getLogger().log(Level.INFO, "player teleported back");
                    }
                }
            }, (settings.getDreamWorldDuration() * 20)));
        }

        if (dtype == DreamType.NIGHTMARE) {
            plugin.schedulers.put(player, plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.NORMAL);
                    plugin.getServer().getPluginManager().callEvent(DPDLE);
                    leave(player, DreamType.NIGHTMARE, DreamLeaveType.NORMAL);
                    if (settings.isDebugging() == true) {
                        plugin.getLogger().log(Level.INFO, "player teleported back");
                    }
                }
            }, (settings.getNightMareDuration() * 20)));
        }
    }

    /**
     * @return weather or not player is in dream world
     */
    public boolean isInDreamWorld(Player player) {
        if (player.getWorld() == plugin.getWorldManager().getDreamWorld() || player.getWorld() == plugin.getWorldManager().getNightMare()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return weather or not player is in dream
     */
    public boolean isInDream(Player player) {
        if (plugin.schedulers.containsKey(player)) {
            return true;
        } else {
            return false;
        }
    }

    public void enter(Player player, DreamType dtype) {
        System.out.println(settings.isDreamWorldRandomSpawn());
        switch (dtype) {
            case DREAMWORLD:
                if (settings.isDreamWorldRandomSpawn()) {
                    player.teleport(util.randomLoc(plugin.getWorldManager().getDreamWorld()));
                    player.setNoDamageTicks(40);
                } else {
                    player.teleport(plugin.getWorldManager().getDreamWorld().getSpawnLocation());
                    player.setNoDamageTicks(40);
                }
                player.sendMessage(util.color(settings.getEnterDreamWorldMessage()));
                plugin.getWorldManager().setWeather(settings.isDreamWorldThundering(), settings.isDreamWorldStorm(), plugin.getWorldManager().getNightMare());
                if (settings.isDreamWorldUsingDuration()) {
                    startRetrunTimer(player, dtype);
                }
                break;
            case NIGHTMARE:
                if (settings.isNightMareRandomSpawn()) {
                    player.teleport(util.randomLoc(plugin.getWorldManager().getNightMare()));
                    player.setNoDamageTicks(40);
                } else {
                    player.teleport(plugin.getWorldManager().getNightMare().getSpawnLocation());
                    player.setNoDamageTicks(40);
                }
                player.sendMessage(util.color(settings.getEnterNightMareMessage()));
                plugin.getWorldManager().setWeather(settings.isNightMareThundering(), settings.isNightMareStorm(), plugin.getWorldManager().getNightMare());
                if (settings.isNightMareUsingDuration()) {
                    startRetrunTimer(player, dtype);
                }

                break;
        }
        inv.clearInventory(player, dtype);
        inv.save(dtype, player);
    }

    public void leave(Player player, DreamType dtype, DreamLeaveType leavetype) {

        if (player.isOnline()) {
            if (plugin.returnLocation.containsKey(player)) {
                player.teleport(plugin.returnLocation.get(player));
            } else {
                Location defaultspawn = plugin.getWorldManager().getDefaultWorld().getSpawnLocation();
                defaultspawn.setY(plugin.getWorldManager().getDefaultWorld().getHighestBlockYAt(plugin.getWorldManager().getDefaultWorld().getSpawnLocation()));
                player.teleport(defaultspawn);
            }
            player.setHealth(player.getMaxHealth());
            player.setNoDamageTicks(100);
            player.setFallDistance(0);
            player.sendMessage(util.color(settings.getLeaveMessage()));
        }

        if (plugin.schedulers.containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(plugin.schedulers.get(player));
            plugin.schedulers.remove(player);
        }

        if (settings.isDebugging() == true) {
            plugin.getLogger().log(Level.INFO, "player teleported left the dream");
        }
        inv.load(dtype, player);
        plugin.afterTeleport.put(null, Boolean.TRUE);
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                plugin.afterTeleport.put(null, Boolean.FALSE);
            }
        }, (200));
    }
}