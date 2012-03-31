package me.p000ison.dreamz.listener;

import me.p000ison.dreamz.DreamZ;
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
            if (plugin.getSettingsManager().isNightMareStorm() && event.getWorld() == plugin.getWorldManager().getNightMare()
                    || plugin.getSettingsManager().isDreamWorldStorm() && event.getWorld() == plugin.getWorldManager().getDreamWorld()) {
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
            if (plugin.getSettingsManager().isNightMareThundering() && event.getWorld() == plugin.getWorldManager().getNightMare()
                    || plugin.getSettingsManager().isDreamWorldThundering() && event.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                event.setCancelled(true);
            }
        }
    }
}
