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
        final DreamType randomDream = util.randomDream(plugin.getSettingsManager().getDreamWorldChance(), plugin.getSettingsManager().getNightMareChance());

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                DPDEE = new DreamZPlayerDreamEnterEvent(player, randomDream);
                plugin.getServer().getPluginManager().callEvent(DPDEE);
                enter(player, randomDream);
                if (settings.isDebugging() == true) {
                    plugin.getLogger().log(Level.INFO, String.format("player teleported enterded %s", randomDream.toString()));
                }
            }
        }, (settings.getDelay() * 20));

        if (randomDream.equals(DreamType.NOTHING)) {
            DPDEE = new DreamZPlayerDreamEnterEvent(player, randomDream);
            DPDEE.setCancelled(true);
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
            if ((player.getWorld() == plugin.getWorldManager().getDreamWorld()
                    && loc.getX() == settings.getDreamWorldBedX()
                    && loc.getY() == settings.getDreamWorldBedY()
                    && loc.getZ() == settings.getDreamWorldBedZ())
                    || (player.getWorld() == plugin.getWorldManager().getDreamWorld()
                    && loc.getX() == settings.getNightMareBedX()
                    && loc.getY() == settings.getNightMareBedY()
                    && loc.getZ() == settings.getNightMareBedZ())) {

                DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED);
                plugin.getServer().getPluginManager().callEvent(DPDLE);
                leave(player, DreamType.NOTHING, DreamLeaveType.BED);
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
    public void startRetrunTimer(final Player player, final DreamType dtype) {
        plugin.schedulers.put(player, plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.NORMAL);
                plugin.getServer().getPluginManager().callEvent(DPDLE);
                leave(player, dtype, DreamLeaveType.NORMAL);
                if (settings.isDebugging() == true) {
                    plugin.getLogger().log(Level.INFO, "player teleported back");
                }
            }
        }, (DreamType.isNightMare() ? settings.getNightMareDuration() * 20 : settings.getDreamWorldDuration() * 20)));
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

        if (util.isDreamDreamWorld(dtype) ? settings.isDreamWorldRandomSpawn() : settings.isNightMareRandomSpawn()) {
            player.teleport(util.randomLoc(util.isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld() : plugin.getWorldManager().getNightMare(), util.isDreamDreamWorld(dtype) ? DreamType.DREAMWORLD : DreamType.NIGHTMARE));
            player.setNoDamageTicks(40);
        } else {
            player.teleport(util.isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld().getSpawnLocation() : plugin.getWorldManager().getNightMare().getSpawnLocation());
            player.setNoDamageTicks(40);
        }
        player.sendMessage(util.isDreamDreamWorld(dtype) ? util.color(settings.getEnterDreamWorldMessage()) : util.color(settings.getEnterNightMareMessage()));
        plugin.getWorldManager().setWeather(util.isDreamDreamWorld(dtype) ? settings.isDreamWorldThundering() : settings.isNightMareThundering(), util.isDreamDreamWorld(dtype) ? settings.isDreamWorldStorm() : settings.isNightMareStorm(), util.isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld() : plugin.getWorldManager().getNightMare());
        if (util.isDreamDreamWorld(dtype) ? settings.isDreamWorldUsingDuration() : settings.isNightMareUsingDuration()) {
            startRetrunTimer(player, dtype);
        }

        player.setHealth(player.getMaxHealth());
        inv.save(dtype, player);
        inv.clearInventory(player, dtype);
    }

    public void leave(final Player player, DreamType dtype, DreamLeaveType leavetype) {

        if (player.isOnline()) {
            inv.load(dtype, player);
            if (plugin.returnLocation.containsKey(player)) {
                player.teleport(plugin.returnLocation.get(player));
            } else {
                Location defaultspawn = plugin.getWorldManager().getDefaultWorld().getSpawnLocation();
                defaultspawn.setY(plugin.getWorldManager().getDefaultWorld().getHighestBlockYAt(plugin.getWorldManager().getDefaultWorld().getSpawnLocation()));
                player.teleport(defaultspawn);
            }
            player.setHealth(player.getMaxHealth());
            player.setNoDamageTicks(40);
            player.setFallDistance(0);
            player.sendMessage(util.color(settings.getLeaveMessage()));

            plugin.afterTeleport.put(player, true);
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    plugin.afterTeleport.put(player, false);
                }
            }, (200));
        }

        if (plugin.schedulers.containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(plugin.schedulers.get(player));
            plugin.schedulers.remove(player);
        }

        if (settings.isDebugging() == true) {
            plugin.getLogger().log(Level.INFO, String.format("player teleported left %s", dtype));
        }
    }
}