package com.p000ison.dev.dreamz.manager;

import com.p000ison.dev.dreamz.DreamZ;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author p000ison
 */
public final class SettingsManager {

    private DreamZ plugin;
    private File main;
    private FileConfiguration config;
    private int DefaultRescueHealth;
    private boolean Debugging;
    private List<String> preventSpawnOn;
    private boolean canEscapeThrowBed;
    private int Delay;
    private boolean PlaceBeds;
    private boolean FakeNetherBeds;
    private boolean DisableHunger;
    private boolean UsingThisWorldCreator;
    private boolean UsingDamageExit;
    private String CantEscapeMessage;
    private String EnterDreamMessage;
    private String CantPlaceBedsMessage;
    private String LeaveDreamMessage;
    private String DamageEscapeMessage;
    private boolean FakeBedsDuringDay;

    /**
     *
     */
    public SettingsManager(DreamZ plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        main = new File(getPlugin().getDataFolder() + File.separator + "config.yml");
        load();
    }

    /**
     * Load the configuration
     */
    public void load() {
        boolean exists = (main).exists();

        if (exists) {
            try {
                getConfig().options().copyDefaults(true);
                getConfig().load(main);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.WARNING, "Loading the config failed!:{0}", ex.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);

        }


        DefaultRescueHealth = getConfig().getInt("Global.DefaultRescueHealth");
        canEscapeThrowBed = getConfig().getBoolean("Global.canEscapeThrowBed");
        Delay = getConfig().getInt("Global.Delay");
        Debugging = getConfig().getBoolean("Global.Debugging");
        preventSpawnOn = getConfig().getStringList("Global.preventSpawnOn");
        UsingDamageExit = getConfig().getBoolean("Global.UsingDamageExit");
        CantEscapeMessage = getConfig().getString("Messages.CantEscapeMessage");
        CantPlaceBedsMessage = getConfig().getString("Messages.CantPlaceBedsMessage");
        EnterDreamMessage = getConfig().getString("Messages.EnterDreamMessage");
        LeaveDreamMessage = getConfig().getString("Messages.LeaveDreamMessage");
        DamageEscapeMessage = getConfig().getString("Messages.DamageEscapeMessage");
        UsingThisWorldCreator = getConfig().getBoolean("Global.UsingThisWorldCreator");
        DisableHunger = getConfig().getBoolean("Global.DisableHunger");
        PlaceBeds = getConfig().getBoolean("Global.PlaceBedsInDream");
        FakeNetherBeds = getConfig().getBoolean("Global.FakeNetherBeds");
        FakeBedsDuringDay = getConfig().getBoolean("Global.FakeBedsDuringDay");

        save();
    }

    public void save() {
        try {
            getConfig().save(main);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Saving the config failed!:{0}", ex.getMessage());
        }
    }

    /**
     * @return the plugin
     */
    public DreamZ getPlugin() {
        return plugin;
    }

    /**
     * @return the config
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * @return the DefaultRescueHealth
     */
    public int getDefaultRescueHealth() {
        return DefaultRescueHealth;
    }

    /**
     * @return the Debugging
     */
    public boolean isDebugging() {
        return Debugging;
    }

    /**
     * @return the preventSpawnOn
     */
    public List<String> getPreventSpawnOn() {
        return preventSpawnOn;
    }

    /**
     * @return the canEscapeThrowBed
     */
    public boolean isCanEscapeThrowBed() {
        return canEscapeThrowBed;
    }

    /**
     * @return the Delay
     */
    public int getDelay() {
        return Delay;
    }

    /**
     * @return the PlaceBeds
     */
    public boolean isPlaceBeds() {
        return PlaceBeds;
    }

    /**
     * @return the FakeNetherBeds
     */
    public boolean isFakeNetherBeds() {
        return FakeNetherBeds;
    }

    /**
     * @return the DisableHunger
     */
    public boolean isDisableHunger() {
        return DisableHunger;
    }

    /**
     * @return the UsingThisWorldCreator
     */
    public boolean isUsingThisWorldCreator() {
        return UsingThisWorldCreator;
    }

    /**
     * @return the UsingDamageExit
     */
    public boolean isUsingDamageExit() {
        return UsingDamageExit;
    }

    /**
     * @return the CantEscapeMessage
     */
    public String getCantEscapeMessage() {
        return CantEscapeMessage;
    }

    /**
     * @return the CantPlaceBedsMessage
     */
    public String getCantPlaceBedsMessage() {
        return CantPlaceBedsMessage;
    }

    /**
     * @return the DamageEscapeMessage
     */
    public String getDamageEscapeMessage() {
        return DamageEscapeMessage;
    }

    /**
     * @return the FakeBedsDuringDay
     */
    public boolean isFakeBedsDuringDay() {
        return FakeBedsDuringDay;
    }

    /**
     * @return the EnterDreamMessage
     */
    public String getEnterDreamMessage() {
        return EnterDreamMessage;
    }

    /**
     * @return the LeaveDreamMessage
     */
    public String getLeaveDreamMessage() {
        return LeaveDreamMessage;
    }
}