package me.p000ison.dreamz.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import me.p000ison.dreamz.DreamZ;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author p000ison
 */
public final class SettingsManager {

    private DreamZ plugin;
    private File main;
    private FileConfiguration config;
    private String DefaultReturnWorld;
    private String DreamWorldName;
    private String NightMareName;
    private String DreamWorldType;
    private String NightMareType;
    private int DefaultRescueHealth;
    private int NightMareDuration;
    private int DreamWorldDuration;
    private boolean Debugging;
    private boolean canEscapeThrowBed;
    private boolean NightMareEnabled;
    private boolean DreamWorldEnabled;
    private double DreamWorldBedX;
    private double DreamWorldBedY;
    private double DreamWorldBedZ;
    private double NightMareBedX;
    private double NightMareBedY;
    private double NightMareBedZ;
    private int Delay;
    private String DreamWorldGenerator;
    private String NightMareGenerator;
    private boolean NightMareUsingDuration;
    private boolean DreamWorldUsingDuration;
    private double NightMareChance;
    private double DreamWorldChance;
    private long DreamWorldSeed;
    private long NightMareSeed;
    private double NightMareMaxZ;
    private double NightMareMinZ;
    private double DreamWorldMaxZ;
    private double DreamWorldMaxX;
    private boolean DreamWorldRandomSpawn;
    private double DreamWorldMinX;
    private boolean NightMareRandomSpawn;
    private double NightMareMinX;
    private double NightMareMaxX;
    private double DreamWorldMinZ;
    private List<String> preventSpawnOn;
    private boolean UsingDamageExit;
    private boolean DreamWorldThundering;
    private boolean DreamWorldStorm;
    private boolean NightMareThundering;
    private boolean NightMareStorm;
    private boolean DreamWorldPassiveMobs;
    private boolean NighMarePassiveMobs;
    private String CantEscapeMessage;
    private String EnterDreamWorldMessage;
    private String EnterNightMareMessage;
    private String LeaveMessage;
    private String DamageEscapeMessage;
    private int NightMareFlyMultiplier;
    private int DreamWorldFlyMultiplier;
    private boolean UsingThisWorldCreator;
    private boolean DreamWorldRespawnAtDeathPoint;
    private boolean NightMareRespawnAtDeathPoint;
    private boolean NightMareRespawn;
    private boolean DreamWorldRespawn;
    private boolean SaveInventory;
    private boolean ClearInventory;

    /**
     *
     */
    public SettingsManager() {
        plugin = DreamZ.getInstance();
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

        DreamWorldName = getConfig().getString("DreamWorld.Generation.Name");
        DreamWorldType = getConfig().getString("DreamWorld.Generation.Type");
        NightMareName = getConfig().getString("NightMare.Generation.Name");
        NightMareType = getConfig().getString("NightMare.Generation.Type");
        DefaultReturnWorld = getConfig().getString("Global.DefaultReturnWorld");
        DefaultRescueHealth = getConfig().getInt("Global.DefaultRescueHealth");
        NightMareDuration = getConfig().getInt("NightMare.Duration");
        DreamWorldDuration = getConfig().getInt("DreamWorld.Duration");
        canEscapeThrowBed = getConfig().getBoolean("Global.canEscapeThrowBed");
        Delay = getConfig().getInt("Global.Delay");
        Debugging = getConfig().getBoolean("Global.Debugging");
        NightMareEnabled = getConfig().getBoolean("NightMare.Enabled");
        DreamWorldEnabled = getConfig().getBoolean("DreamWorld.Enabled");
        DreamWorldBedX = getConfig().getDouble("DreamWorld.Bed.X");
        DreamWorldBedY = getConfig().getDouble("DreamWorld.Bed.Y");
        DreamWorldBedZ = getConfig().getDouble("DreamWorld.Bed.Z");
        NightMareBedX = getConfig().getDouble("NightMare.Bed.X");
        NightMareBedY = getConfig().getDouble("NightMare.Bed.Y");
        NightMareBedZ = getConfig().getDouble("NightMare.Bed.Z");
        DreamWorldGenerator = getConfig().getString("DreamWorld.Generation.Generator");
        NightMareGenerator = getConfig().getString("NightMare.Generation.Generator");
        NightMareUsingDuration = getConfig().getBoolean("NightMare.UsingDuration");
        DreamWorldUsingDuration = getConfig().getBoolean("DreamWorld.UsingDuration");
        NightMareChance = getConfig().getDouble("NightMare.Chance");
        DreamWorldChance = getConfig().getDouble("DreamWorld.Chance");
        NightMareSeed = getConfig().getLong("NightMare.Generation.Seed");
        DreamWorldSeed = getConfig().getLong("DreamWorld.Generation.Seed");
        NightMareMaxZ = getConfig().getDouble("NightMare.Spawning.SpawnArea.MaxZ");
        NightMareMinZ = getConfig().getDouble("NightMare.Spawning.SpawnArea.MinZ");
        DreamWorldMaxZ = getConfig().getDouble("DreamWorld.Spawning.SpawnArea.MaxZ");
        DreamWorldMaxX = getConfig().getDouble("DreamWorld.Spawning.SpawnArea.MaxX");
        DreamWorldRandomSpawn = getConfig().getBoolean("DreamWorld.Spawning.UseRandomSpawn");
        DreamWorldMinX = getConfig().getDouble("DreamWorld.Spawning.SpawnArea.MinX");
        NightMareRandomSpawn = getConfig().getBoolean("NightMare.Spawning.UseRandomSpawn");
        NightMareMinX = getConfig().getDouble("NightMare.Spawning.SpawnArea.MinX");
        NightMareMaxX = getConfig().getDouble("NightMare.Spawning.SpawnArea.MaxX");
        DreamWorldMinZ = getConfig().getDouble("DreamWorld.Spawning.SpawnArea.MinZ");
        preventSpawnOn = getConfig().getStringList("Global.preventSpawnOn");
        UsingDamageExit = getConfig().getBoolean("Global.UsingDamageExit");
        DreamWorldThundering = getConfig().getBoolean("DreamWorld.Thundering");
        DreamWorldStorm = getConfig().getBoolean("DreamWorld.Storm");
        NightMareThundering = getConfig().getBoolean("NightMare.Thundering");
        NightMareStorm = getConfig().getBoolean("NightMare.Storm");
        DreamWorldPassiveMobs = getConfig().getBoolean("DreamWorld.PassiveMonsters");
        NighMarePassiveMobs = getConfig().getBoolean("NightMare.PassiveMonsters");
        CantEscapeMessage = (getConfig().getString("Messages.CantEscapeMessage"));
        EnterDreamWorldMessage = getConfig().getString("Messages.EnterDreamWorldMessage");
        EnterNightMareMessage = getConfig().getString("Messages.EnterNightMareMessage");
        LeaveMessage = getConfig().getString("Messages.LeaveMessage");
        DamageEscapeMessage = getConfig().getString("Messages.DamageEscapeMessage");
        NightMareFlyMultiplier = getConfig().getInt("NightMare.FlyMultiplier");
        DreamWorldFlyMultiplier = getConfig().getInt("DreamWorld.FlyMultiplier");
        UsingThisWorldCreator = getConfig().getBoolean("Global.UsingThisWorldCreator");
        DreamWorldRespawnAtDeathPoint = getConfig().getBoolean("DreamWorld.Spawning.RespawnAtDeathPoint");
        NightMareRespawnAtDeathPoint = getConfig().getBoolean("NightMare.Spawning.RespawnAtDeathPoint");
        NightMareRespawn = getConfig().getBoolean("NightMare.Spawning.RespawnInDream");
        DreamWorldRespawn = getConfig().getBoolean("DreamWorld.Spawning.RespawnInDream");
        ClearInventory = getConfig().getBoolean("Global.ClearInventory");
        SaveInventory = getConfig().getBoolean("Global.SaveInventory");

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
     * @return the DreamWorldName
     */
    public String getDreamWorldName() {
        return DreamWorldName;
    }

    /**
     * @return the NightMareName
     */
    public String getNightMareName() {
        return NightMareName;
    }

    /**
     * @return the DreamWorldType
     */
    public String getDreamWorldType() {
        return DreamWorldType;
    }

    /**
     * @return the NightMareType
     */
    public String getNightMareType() {
        return NightMareType;
    }

    /**
     * @return the DefaultRescueHealth
     */
    public int getDefaultRescueHealth() {
        return DefaultRescueHealth;
    }

    /**
     * @return the NightMareDuration
     */
    public int getNightMareDuration() {
        return NightMareDuration;
    }

    /**
     * @return the DreamWorldDuration
     */
    public int getDreamWorldDuration() {
        return DreamWorldDuration;
    }

    /**
     * @return the isDebugging
     */
    public boolean isDebugging() {
        return Debugging;
    }

    /**
     * @return the canEscapeThrowBed
     */
    public boolean isCanEscapeThrowBed() {
        return canEscapeThrowBed;
    }

    /**
     * @return the NightMareEnabled
     */
    public boolean isNightMareEnabled() {
        return NightMareEnabled;
    }

    /**
     * @return the DreamWorldEnabled
     */
    public boolean isDreamWorldEnabled() {
        return DreamWorldEnabled;
    }

    /**
     * @return the DreamWorldBedX
     */
    public double getDreamWorldBedX() {
        return DreamWorldBedX;
    }

    /**
     * @return the DreamWorldBedY
     */
    public double getDreamWorldBedY() {
        return DreamWorldBedY;
    }

    /**
     * @return the DreamWorldBedZ
     */
    public double getDreamWorldBedZ() {
        return DreamWorldBedZ;
    }

    /**
     * @return the NightMareBedX
     */
    public double getNightMareBedX() {
        return NightMareBedX;
    }

    /**
     * @return the NightMareBedY
     */
    public double getNightMareBedY() {
        return NightMareBedY;
    }

    /**
     * @return the NightMareBedZ
     */
    public double getNightMareBedZ() {
        return NightMareBedZ;
    }

    /**
     * @return the Delay
     */
    public int getDelay() {
        return Delay;
    }

    /**
     * @return the DreamWorldGenerator
     */
    public String getDreamWorldGenerator() {
        return DreamWorldGenerator;
    }

    /**
     * @return the NightMareGenerator
     */
    public String getNightMareGenerator() {
        return NightMareGenerator;
    }

    /**
     * @return the NightMareUsingDuration
     */
    public boolean isNightMareUsingDuration() {
        return NightMareUsingDuration;
    }

    /**
     * @return the DreamWorldUsingDuration
     */
    public boolean isDreamWorldUsingDuration() {
        return DreamWorldUsingDuration;
    }

    /**
     * @return the NightMareChance
     */
    public double getNightMareChance() {
        return NightMareChance;
    }

    /**
     * @return the DreamWorldChance
     */
    public double getDreamWorldChance() {
        return DreamWorldChance;
    }

    /**
     * @return the DreamWorldSeed
     */
    public long getDreamWorldSeed() {
        return DreamWorldSeed;
    }

    /**
     * @return the NightMareSeed
     */
    public long getNightMareSeed() {
        return NightMareSeed;
    }

    /**
     * @return the NightMareMaxZ
     */
    public double getNightMareMaxZ() {
        return NightMareMaxZ;
    }

    /**
     * @return the NightMareMinZ
     */
    public double getNightMareMinZ() {
        return NightMareMinZ;
    }

    /**
     * @return the DreamWorldMaxZ
     */
    public double getDreamWorldMaxZ() {
        return DreamWorldMaxZ;
    }

    /**
     * @return the DreamWorldMaxX
     */
    public double getDreamWorldMaxX() {
        return DreamWorldMaxX;
    }

    /**
     * @return the DreamWorldRandomSpawn
     */
    public boolean isDreamWorldRandomSpawn() {
        return DreamWorldRandomSpawn;
    }

    /**
     * @return the DreamWorldMinX
     */
    public double getDreamWorldMinX() {
        return DreamWorldMinX;
    }

    /**
     * @return the NightMareRandomSpawn
     */
    public boolean isNightMareRandomSpawn() {
        return NightMareRandomSpawn;
    }

    /**
     * @return the NightMareMinX
     */
    public double getNightMareMinX() {
        return NightMareMinX;
    }

    /**
     * @return the NightMareMaxX
     */
    public double getNightMareMaxX() {
        return NightMareMaxX;
    }

    /**
     * @return the DreamWorldMinZ
     */
    public double getDreamWorldMinZ() {
        return DreamWorldMinZ;
    }

    /**
     * @return the preventSpawnOn
     */
    public List<String> getPreventSpawnOn() {
        return preventSpawnOn;
    }

    /**
     * @return the UsingDamageExit
     */
    public boolean isUsingDamageExit() {
        return UsingDamageExit;
    }

    /**
     * @return the DreamWorldThundering
     */
    public boolean isDreamWorldThundering() {
        return DreamWorldThundering;
    }

    /**
     * @return the DreamWorldStorm
     */
    public boolean isDreamWorldStorm() {
        return DreamWorldStorm;
    }

    /**
     * @return the NightMareThundering
     */
    public boolean isNightMareThundering() {
        return NightMareThundering;
    }

    /**
     * @return the NightMareStorm
     */
    public boolean isNightMareStorm() {
        return NightMareStorm;
    }

    /**
     * @return the DreamWorldPassiveMobs
     */
    public boolean isDreamWorldPassiveMobs() {
        return DreamWorldPassiveMobs;
    }

    /**
     * @return the NighMarePassiveMobs
     */
    public boolean isNighMarePassiveMobs() {
        return NighMarePassiveMobs;
    }

    /**
     * @return the CantEscapeMessage
     */
    public String getCantEscapeMessage() {
        return CantEscapeMessage;
    }

    /**
     * @return the EnterDreamWorldMessage
     */
    public String getEnterDreamWorldMessage() {
        return EnterDreamWorldMessage;
    }

    /**
     * @return the EnterNightMareMessage
     */
    public String getEnterNightMareMessage() {
        return EnterNightMareMessage;
    }

    /**
     * @return the LeaveMessage
     */
    public String getLeaveMessage() {
        return LeaveMessage;
    }

    /**
     * @return the DamageEscapeMessage
     */
    public String getDamageEscapeMessage() {
        return DamageEscapeMessage;
    }

    /**
     * @return the NightMareFlyMultiplier
     */
    public int getNightMareFlyMultiplier() {
        return NightMareFlyMultiplier;
    }

    /**
     * @return the DreamWorldFlyMultiplier
     */
    public int getDreamWorldFlyMultiplier() {
        return DreamWorldFlyMultiplier;
    }

    /**
     * @return the UsingThisWorldCreator
     */
    public boolean isUsingThisWorldCreator() {
        return UsingThisWorldCreator;
    }

    /**
     * @return the DreamWorldRespawnAtDeathPoint
     */
    public boolean isDreamWorldRespawnAtDeathPoint() {
        return DreamWorldRespawnAtDeathPoint;
    }

    /**
     * @return the NightMareRespawnAtDeathPoint
     */
    public boolean isNightMareRespawnAtDeathPoint() {
        return NightMareRespawnAtDeathPoint;
    }

    /**
     * @return the NightMareRespawn
     */
    public boolean isNightMareRespawn() {
        return NightMareRespawn;
    }

    /**
     * @return the DreamWorldRespawn
     */
    public boolean isDreamWorldRespawn() {
        return DreamWorldRespawn;
    }

    /**
     * @return the SaveInventory
     */
    public boolean isSaveInventory() {
        return SaveInventory;
    }

    /**
     * @return the ClearInventory
     */
    public boolean isClearInventory() {
        return ClearInventory;
    }
}