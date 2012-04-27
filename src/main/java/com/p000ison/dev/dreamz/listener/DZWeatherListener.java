package com.p000ison.dev.dreamz.listener;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.util.dUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 *
 * @author p000ison
 */
public class DZWeatherListener implements Listener {

    private DreamZ plugin;

    public DZWeatherListener(DreamZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (!event.toWeatherState()) {
            if (dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(event.getWorld().getName()) && plugin.getSettingsManager().getConfig().getBoolean("dreams." + event.getWorld().getName() + ".Thundering")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onThunderChange(ThunderChangeEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (!event.toThunderState()) {
            if (dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(event.getWorld().getName()) && plugin.getSettingsManager().getConfig().getBoolean("dreams." + event.getWorld().getName() + ".Thundering")) {
                event.setCancelled(true);
            }
        }
    }
}
