package me.p000ison.dreamz;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.p000ison.dreamz.listener.DZEntityListener;
import me.p000ison.dreamz.listener.DZPlayerListener;
import me.p000ison.dreamz.listener.DZWeatherListener;
import me.p000ison.dreamz.manager.DreamManager;
import me.p000ison.dreamz.manager.SettingsManager;
import me.p000ison.dreamz.manager.WorldManager;
import me.p000ison.dreamz.util.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DreamZ extends JavaPlugin {

    private SettingsManager settingsManager;
    private DreamManager dreamManager;
    private WorldManager worldManager;
    private DreamZCommandExecutor CommandExecutor;
    private Inventory inventory;
    private static DreamZ instance;
    private static final Logger logger = Logger.getLogger("Minecraft");
    public HashMap<Player, Location> returnLocation = new HashMap<Player, Location>();
    public HashMap<Player, Integer> schedulers = new HashMap<Player, Integer>();
    public HashMap<Player, Location> deathLocation = new HashMap<Player, Location>();
    public HashMap<Player, Boolean> afterTeleport = new HashMap<Player, Boolean>();
    private WorldCreator wc;

    @Override
    public void onDisable() {
        schedulers.clear();
        returnLocation.clear();
        deathLocation.clear();
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        settingsManager = new SettingsManager();
        worldManager = new WorldManager();

        if (getSettingsManager().isUsingThisWorldCreator()) {
            getWorldManager().createDreamWorld();
            getWorldManager().createNightMare();
        }

        dreamManager = new DreamManager();
        worldManager = new WorldManager();
        inventory = new Inventory();
        CommandExecutor = new DreamZCommandExecutor(this);
        
        getCommand("dreamz").setExecutor(CommandExecutor);

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not send Plugin-Stats: " + e.getMessage());
        }

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new DZPlayerListener(this), this);
        pm.registerEvents(new DZWeatherListener(this), this);
        pm.registerEvents(new DZEntityListener(this), this);
    }

    /**
     * @return the logger
     */
    public static Logger getLog() {
        return logger;
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * @return the instance
     */
    public static DreamZ getInstance() {
        return instance;
    }

    /**
     * @return the dreamManager
     */
    public DreamManager getDreamManager() {
        return dreamManager;
    }

    /**
     * @return the worldManager
     */
    public WorldManager getWorldManager() {
        return worldManager;
    }

    public void sendCommandMessage(String msg, CommandSender sender, ChatColor color) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(color + msg);
        } else {
            logger.log(Level.INFO, msg);
        }
    }

    public boolean hasPermission(String permission, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(permission)) {
                player.hasPermission(permission);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public String color(String text) {
        String colourised = text.replaceAll("&(?=[0-9a-fA-FkK])", "\u00a7");
        return colourised;
    }
}