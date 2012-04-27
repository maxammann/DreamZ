package com.p000ison.dev.dreamz;

import com.p000ison.dev.dreamz.manager.WorldManager;
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
    private boolean OneHundredPercent;

    public Dream(String dream, boolean onehundret) {
        this.OneHundredPercent = onehundret;
        this.dream = dream;
        this.plugin = DreamZ.getPlugin();
        if (dream != null) {
            this.dreamworld = Bukkit.getWorld(dream);
        }
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
}
