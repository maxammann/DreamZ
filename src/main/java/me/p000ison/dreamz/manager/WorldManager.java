package me.p000ison.dreamz.manager;

import java.util.logging.Level;
import me.p000ison.dreamz.DreamZ;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 *
 * @author p000ison
 */
public class WorldManager {
    private DreamZ plugin;
    private String DreamWorldName;
    private String NightMareName;
    private World DreamWorld;
    private World NightMare;
    private World DefaultWorld;
    WorldCreator wc;



    public WorldManager() {
        plugin = DreamZ.getInstance();
        DreamWorld = Bukkit.getWorld(plugin.getSettingsManager().getDreamWorldName());
        NightMare = Bukkit.getWorld(plugin.getSettingsManager().getNightMareName());
        DefaultWorld = plugin.getServer().getWorlds().get(0);
        DreamWorldName = plugin.getSettingsManager().getDreamWorldName();
        NightMareName = plugin.getSettingsManager().getNightMareName();
    }
    /**
     * creates the DreamWorld
     */
    public void createDreamWorld() {
        try {
            plugin.getLogger().log(Level.INFO, "Loading: " + getDreamWorldName());
            wc = new WorldCreator(getDreamWorldName());
            if (plugin.getSettingsManager().getDreamWorldGenerator() != null && !"DEFAULT".equalsIgnoreCase(plugin.getSettingsManager().getDreamWorldGenerator())) {
                wc.generator(plugin.getSettingsManager().getDreamWorldGenerator());
            }
            wc.environment(World.Environment.valueOf(plugin.getSettingsManager().getDreamWorldType().toUpperCase()));
            wc.seed(plugin.getSettingsManager().getDreamWorldSeed());
            wc.generateStructures(false);
            plugin.getServer().createWorld(wc);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, "World load issue: " + ex.getMessage());
        }
    }
    /**
     * creates the NightMare
     */
    public void createNightMare() {
        try {
            plugin.getLogger().log(Level.INFO, "Loading: " + getNightMareName());
            wc = new WorldCreator(getNightMareName());
            if (plugin.getSettingsManager().getDreamWorldGenerator() != null && !"DEFAULT".equalsIgnoreCase(plugin.getSettingsManager().getNightMareGenerator())) {
                wc.generator(plugin.getSettingsManager().getNightMareGenerator());
            }
            wc.environment(World.Environment.valueOf(plugin.getSettingsManager().getNightMareType().toUpperCase()));
            wc.seed(plugin.getSettingsManager().getNightMareSeed());
            wc.generateStructures(false);
            plugin.getServer().createWorld(wc);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, "World load issue: " + ex.getMessage());
        }
    }
    
    /**
     * @param thunder weather of not thundering 
     * @param storm weather of not raining 
     * @param world world to apply
     */
    public void setWeather(boolean thunder, boolean storm, World world) {
        if (!world.hasStorm()) {
            world.setThundering(thunder);
            world.setStorm(storm);
        } 
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
     * @return the DreamWorld
     */
    public World getDreamWorld() {
        return DreamWorld;
    }

    /**
     * @return the NightMare
     */
    public World getNightMare() {
        return NightMare;
    }

    /**
     * @return the DefaultWorld
     */
    public World getDefaultWorld() {
        return DefaultWorld;
    }
}
