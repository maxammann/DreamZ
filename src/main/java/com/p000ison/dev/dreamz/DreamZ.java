package com.p000ison.dev.dreamz;

import com.p000ison.dev.dreamz.manager.commands.EnterCommand;
import com.p000ison.dev.dreamz.manager.commands.ReloadCommand;
import com.p000ison.dev.dreamz.manager.commands.HelpCommand;
import com.p000ison.dev.dreamz.manager.commands.SetCommand;
import com.p000ison.dev.dreamz.manager.commands.LeaveCommand;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.p000ison.dev.dreamz.listener.DZCustomEventListener;
import com.p000ison.dev.dreamz.listener.DZEntityListener;
import com.p000ison.dev.dreamz.listener.DZPlayerListener;
import com.p000ison.dev.dreamz.listener.DZWeatherListener;
import com.p000ison.dev.dreamz.manager.CommandManager;
import com.p000ison.dev.dreamz.manager.DreamManager;
import com.p000ison.dev.dreamz.manager.SettingsManager;
import com.p000ison.dev.dreamz.manager.WorldManager;
import com.p000ison.dev.dreamz.manager.commands.*;
import com.p000ison.dev.dreamz.util.dUtil;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DreamZ extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private static Permission perms = null;
    private SettingsManager settingsManager;
    private DreamManager dreamManager;
    private WorldManager worldManager;
    private static DreamZ instance;
    private CommandManager commandManager = new CommandManager();
    public HashMap<Player, Location> RETURN_LOCATIONS = new HashMap<Player, Location>();
    public HashMap<Player, Integer> SCHEDULERS = new HashMap<Player, Integer>();
    public HashMap<Player, Location> DEATH_LOCATIONS = new HashMap<Player, Location>();
    public HashMap<Player, Boolean> RETURN_TELEPORT_TIMER = new HashMap<Player, Boolean>();

    @Override
    public void onDisable() {
        SCHEDULERS.clear();
        RETURN_LOCATIONS.clear();
        DEATH_LOCATIONS.clear();
        RETURN_TELEPORT_TIMER.clear();
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        setupMetrics();
        registerCommands();
        registerEvents();
        setupManagers();
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
        }

    }

    public void setupManagers() {
        settingsManager = new SettingsManager(this);
        worldManager = new WorldManager(this);
        System.out.println(dUtil.getDreams(getSettingsManager().getConfig()));
        if (getSettingsManager().isUsingThisWorldCreator()) {
            getWorldManager().createDreams();

        }
        worldManager = new WorldManager(this);
        dreamManager = new DreamManager(this);
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
        pm.registerEvents(new DZCustomEventListener(this), this);

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
        commandManager.addCommand(new InfoCommand(this));
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * @return the commandManager
     */
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

    public static DreamZ getPlugin() {
        return instance;
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