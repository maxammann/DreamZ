package me.p000ison.dreamz.manager;

import java.util.logging.Level;
import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
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

    public DreamManager() {
        this.plugin = DreamZ.getInstance();
    }

    /**
     * teleports the player to the dream from a normal world
     *
     * @param player the player to teleport
     */
    public void enterDreamInNormal(final Player player) {
        final DreamType randomDream = plugin.getUtil().randomDream(plugin.getSettingsManager().getDreamWorldChance(), plugin.getSettingsManager().getNightMareChance());

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                DPDEE = new DreamZPlayerDreamEnterEvent(player, randomDream);
                plugin.getServer().getPluginManager().callEvent(DPDEE);
                enter(player, randomDream);
                if (plugin.getSettingsManager().isDebugging() == true) {
                    plugin.getLogger().log(Level.INFO, String.format("player teleported enterded %s", randomDream.toString()));
                }
            }
        }, (plugin.getSettingsManager().getDelay() * 20));

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
        if (plugin.getSettingsManager().isCanEscapeThrowBed()) {
            Location loc = event.getBed().getLocation();
            if (plugin.getUtil().checkBed(loc)) {
                DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.BED);
                plugin.getServer().getPluginManager().callEvent(DPDLE);
                plugin.getDreamManager().leave(player, player.getWorld().equals(plugin.getWorldManager().getDreamWorld()) ? DreamType.DREAMWORLD : DreamType.NIGHTMARE, DreamLeaveType.BED);
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(plugin.getUtil().color(plugin.getSettingsManager().getCantEscapeMessage()));
            if (plugin.getSettingsManager().isDebugging() == true) {
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
                if (plugin.getSettingsManager().isDebugging() == true) {
                    plugin.getLogger().log(Level.INFO, "player teleported back");
                }
            }
        }, (DreamType.isNightMare() ? plugin.getSettingsManager().getNightMareDuration() * 20 : plugin.getSettingsManager().getDreamWorldDuration() * 20)));
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

        if (plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getSettingsManager().isDreamWorldRandomSpawn() : plugin.getSettingsManager().isNightMareRandomSpawn()) {
            player.teleport(plugin.getUtil().randomLoc(plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld() : plugin.getWorldManager().getNightMare(), plugin.getUtil().isDreamDreamWorld(dtype) ? DreamType.DREAMWORLD : DreamType.NIGHTMARE));
            player.setNoDamageTicks(40);
        } else {
            player.teleport(plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld().getSpawnLocation() : plugin.getWorldManager().getNightMare().getSpawnLocation());
            player.setNoDamageTicks(40);
        }
        player.sendMessage(plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getUtil().color(plugin.getSettingsManager().getEnterDreamWorldMessage()) : plugin.getUtil().color(plugin.getSettingsManager().getEnterNightMareMessage()));
        plugin.getWorldManager().setWeather(plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getSettingsManager().isDreamWorldThundering() : plugin.getSettingsManager().isNightMareThundering(), plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getSettingsManager().isDreamWorldStorm() : plugin.getSettingsManager().isNightMareStorm(), plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getWorldManager().getDreamWorld() : plugin.getWorldManager().getNightMare());
        if (plugin.getUtil().isDreamDreamWorld(dtype) ? plugin.getSettingsManager().isDreamWorldUsingDuration() : plugin.getSettingsManager().isNightMareUsingDuration()) {
            startRetrunTimer(player, dtype);
        }

        player.setHealth(player.getMaxHealth());
        plugin.getInventory().save(dtype, player);
        plugin.getInventory().clearInventory(player, dtype);
    }

    public void leave(final Player player, DreamType dtype, DreamLeaveType leavetype) {

        if (player.isOnline()) {
            plugin.getInventory().load(dtype, player);
            if (plugin.returnLocation.containsKey(player)) {
                player.teleport(plugin.returnLocation.get(player));
            } else {
                player.teleport(plugin.getWorldManager().getDefaultWorld().getSpawnLocation());
            }
            player.setHealth(player.getMaxHealth());
            player.setNoDamageTicks(40);
            player.setFallDistance(0);
            player.sendMessage(plugin.getUtil().color(plugin.getSettingsManager().getLeaveMessage()));

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

        if (plugin.getSettingsManager().isDebugging() == true) {
            plugin.getLogger().log(Level.INFO, String.format("player teleported left %s", dtype));
        }
    }
}