package com.p000ison.dev.dreamz;

import com.p000ison.dev.dreamz.manager.WorldManager;
import com.p000ison.dev.dreamz.util.dUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author p000ison
 */
public class Dream {

    private String dream;
    private World dreamworld;
    private DreamZ plugin;
    private String dreamType;
    private boolean OneHundredPercent;

    public Dream(String dream, boolean onehundret) {
        this.plugin = DreamZ.getPlugin();
        this.OneHundredPercent = onehundret;
        for (String dreams : dUtil.getDreams(plugin.getSettingsManager().getConfig())) {
            if (dream.equalsIgnoreCase(dreams)) {
                this.dream = dreams;
            }
        }
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equalsIgnoreCase(dream)) {
                this.dreamworld = world;
            }
        }
        this.dreamType = plugin.getSettingsManager().getConfig().getString("dreams." + dream + ".DreamType", "DEFAULT");
    }

    public void applyWeather() {
        WorldManager.setWeather(plugin.getSettingsManager().getConfig().getBoolean("dreams." + getDream() + ".Thundering"), plugin.getSettingsManager().getConfig().getBoolean("dreams." + getDream() + ".Storming"), getDreamWorld());
    }

    /**
     * @return the dream
     */
    public String getDream() {
        return dream;
    }

    /**
     * @param dream the dream to set
     */
    public void setDream(String dream) {
        this.dream = dream;
    }

    /**
     * @return the dreamworld
     */
    public World getDreamWorld() {
        return dreamworld;
    }

    /**
     * @param dreamworld the dreamworld to set
     */
    public void setDreamWorld(World dreamworld) {
        this.dreamworld = dreamworld;
    }

    /**
     * @return the NottOneHundredPercent
     */
    public boolean isOneHundredPercent() {
        return OneHundredPercent;
    }

    /**
     * @param NottOneHundredPercent the NottOneHundredPercent to set
     */
    public void setOneHundredPercent(boolean OneHundredPercent) {
        this.OneHundredPercent = OneHundredPercent;
    }

    /**
     * @return the dreamType
     */
    public String getDreamType() {
        return dreamType;
    }

    /**
     * @param dreamType the dreamType to set
     */
    public void setDreamType(String dreamType) {
        this.dreamType = dreamType;
    }
}
