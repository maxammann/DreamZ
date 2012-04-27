package com.p000ison.dev.dreamz.manager;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.util.dUtil;
import java.util.logging.Level;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 *
 * @author p000ison
 */
public class WorldManager {

    private DreamZ plugin;
    private World DefaultWorld;
    private WorldCreator wc;

    public WorldManager(DreamZ plugin) {
        this.plugin = plugin;
        DefaultWorld = plugin.getServer().getWorlds().get(0);
    }

    /**
     * creates the DreamWorld
     */
    public void createDreams() {
        for (String dream : dUtil.getDreams(plugin.getSettingsManager().getConfig())) {
            try {
                plugin.getLogger().log(Level.INFO, String.format("Loading: %s", dream));
                wc = new WorldCreator(dream);
                if (plugin.getSettingsManager().getConfig().getString("dreams." + dream + ".Generation.Generator") != null
                        && !"DEFAULT".equalsIgnoreCase(plugin.getSettingsManager().getConfig().getString("dreams." + dream + ".Generation.Generator"))) {
                    wc.generator(plugin.getSettingsManager().getConfig().getString("dreams." + dream + ".Generation.Generator"));
                }
                wc.environment(World.Environment.valueOf(plugin.getSettingsManager().getConfig().getString("dreams." + dream + ".Generation.Environment")));
                wc.seed(plugin.getSettingsManager().getConfig().getLong("dreams." + dream + ".Generation.Seed"));
                wc.generateStructures(plugin.getSettingsManager().getConfig().getBoolean("dreams." + dream + ".Generation.GenerateStructures"));
                plugin.getServer().createWorld(wc);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.WARNING, String.format("Failed Loading (%s): %s", dream, ex.getMessage()));
            }
        }
    }

    /**
     * @param thunder weather of not thundering
     * @param storm weather of not raining
     * @param world world to apply
     */
    public static void setWeather(boolean thunder, boolean storm, World world) {
        if (!world.hasStorm()) {
            world.setThundering(thunder);
            world.setStorm(storm);
        }
    }

    /**
     * @return the DefaultWorld
     */
    public World getDefaultWorld() {
        return DefaultWorld;
    }
}
