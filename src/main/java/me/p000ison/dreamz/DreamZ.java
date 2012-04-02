package me.p000ison.dreamz;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.p000ison.dreamz.listener.DZEntityListener;
import me.p000ison.dreamz.listener.DZPlayerListener;
import me.p000ison.dreamz.listener.DZWeatherListener;
import me.p000ison.dreamz.manager.CommandManager;
import me.p000ison.dreamz.manager.DreamManager;
import me.p000ison.dreamz.manager.SettingsManager;
import me.p000ison.dreamz.manager.WorldManager;
import me.p000ison.dreamz.manager.commands.*;
import me.p000ison.dreamz.util.Inventory;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DreamZ extends JavaPlugin {

    private static DreamZ instance;
    private static final Logger logger = Logger.getLogger("Minecraft");
    private static Permission perms = null;
    private static SettingsManager settingsManager;
    private static DreamManager dreamManager;
    private static WorldManager worldManager;
    private static CommandManager commandManager = new CommandManager();
    private static Inventory inventory;
    public HashMap<Player, Location> returnLocation = new HashMap<Player, Location>();
    public HashMap<Player, Integer> schedulers = new HashMap<Player, Integer>();
    public HashMap<Player, Location> deathLocation = new HashMap<Player, Location>();
    public HashMap<Player, Boolean> afterTeleport = new HashMap<Player, Boolean>();

    @Override
    public void onDisable() {
        schedulers.clear();
        returnLocation.clear();
        deathLocation.clear();
        afterTeleport.clear();
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        setupManagers();
        setupMetrics();
        registerCommands();
        registerEvents();
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
        }

    }

    public void setupManagers() {
        settingsManager = new SettingsManager();
        worldManager = new WorldManager();

        if (getSettingsManager().isUsingThisWorldCreator()) {
            getWorldManager().createWorld(true);
            getWorldManager().createWorld(false);
        }

        dreamManager = new DreamManager();
        worldManager = new WorldManager();
        inventory = new Inventory();
    }

    public void setupMetrics() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            logger.log(Level.WARNING, String.format("Could not send Plugin-Stats: %s", e.getMessage()));
        }
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new DZPlayerListener(this), this);
        pm.registerEvents(new DZWeatherListener(this), this);
        pm.registerEvents(new DZEntityListener(this), this);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private void registerCommands() {
        commandManager = new CommandManager();

        commandManager.addCommand(new HelpCommand(this));
        commandManager.addCommand(new SetCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
        commandManager.addCommand(new EnterCommand(this));
        commandManager.addCommand(new LeaveCommand(this));
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

    public CommandManager getCommandManager() {
        return commandManager;
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

    /**
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        return commandManager.dispatch(sender, cmd, commandLabel, args);
    }

    public static boolean hasPermission(Player player, String permission) {
        if (perms != null) {
            return perms.has(player, permission);
        }
        return player.hasPermission(permission);
    }
}