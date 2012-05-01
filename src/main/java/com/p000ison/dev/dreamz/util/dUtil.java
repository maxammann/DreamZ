package com.p000ison.dev.dreamz.util;

import com.p000ison.dev.dreamz.Dream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author p000ison
 */
public class dUtil {

    public static Dream randomDream(FileConfiguration config) {
        List<String> list = new ArrayList<String>();
        for (String dream : getDreams(config)) {
            for (int i = 0; i < config.getInt("dreams." + dream + ".Chance"); i++) {
                list.add(dream.toLowerCase());
            }
        }
        for (int i = list.size(); i <= 100; i++) {
            if (list.size() < 100) {
                list.add(null);
            }
        }
        return list.size() > 100 ? new Dream(null, false) : new Dream(list.get(new Random().nextInt(100)), true);
    }

    public static List<String> getDreams(FileConfiguration config) {
        List<String> dreams = new ArrayList<String>();
        for (String dream : config.getConfigurationSection("dreams").getKeys(false)) {
            dreams.add(dream);
        }
        return dreams;
    }

    public static boolean isDreamAviable(String dream, FileConfiguration config) {
        for (Iterator<String> it = getDreams(config).iterator(); it.hasNext();) {
            if (dream.equalsIgnoreCase(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBed(Location loc, String dream, FileConfiguration config) {

        for (String str : config.getConfigurationSection(dream + ".beds").getKeys(false)) {
            if ((loc.getX() == config.getDouble(dream + ".beds." + str + ".X")
                    && loc.getY() == config.getDouble(dream + ".beds." + str + ".Y")
                    && loc.getZ() == config.getDouble(dream + ".beds." + str + ".Z"))) {
                return true;
            }
        }
        return false;
    }
}
