package me.p000ison.dreamz.util;

import java.util.Iterator;
import java.util.TreeMap;
import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.manager.SettingsManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author p000ison
 */
public class Util {

    private static DreamZ plugin;
    private SettingsManager settings = new SettingsManager();

    public Util() {
        plugin = DreamZ.getInstance();
    }

    /**
     * @return the random location to teleport
     */
    public Location randomLoc(World world, DreamType dtype) {
        Location loc;
        do {
            double X = (settings.getDreamWorldMinX() + (Math.random() * (settings.getDreamWorldMaxX() - settings.getDreamWorldMinX())));
            double Z = (settings.getDreamWorldMinZ() + (Math.random() * (settings.getDreamWorldMaxZ() - settings.getDreamWorldMinZ())));
            double Y = getHeighestFreeBlockAt((int) X, (int) Z, world, dtype);
            loc = new Location(world, X, Y, Z);
        } while (checkDreamSpawnLocation(loc, loc.getWorld()) == false);

        return loc;
    }

    /**
     * checks if the spawn location isnt deadly
     */
    public boolean checkEmtyXZ(double X, double Z, World world) {
        int a = 0;
        for (int i = 0; i <= 258; i++) {
            if (world.getBlockAt((int) X, i, (int) Z).getType() == Material.AIR) {
                a++;
            }
        }
        if (a < 255) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks if the spawn location isnt deadly
     */
    public boolean checkDreamSpawnLocation(Location loc, World world) {
        boolean safe = false;

        Location block1 = new Location(world, loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        Location block2 = new Location(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Location block3 = new Location(world, loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());

        for (Iterator it = plugin.getSettingsManager().getPreventSpawnOn().iterator(); it.hasNext();) {
            String preventSpawnOn = (String) it.next();
            Material mat = Material.valueOf(preventSpawnOn.toUpperCase());

            if (block1.getBlock().getType() == mat || block2.getBlock().getType() == mat || block3.getBlock().getType() == mat || !checkEmtyXZ(loc.getX(), loc.getZ(), world)) {
                safe = false;
            } else {
                safe = true;
            }
        }
        return safe;
    }

    public static int getHeighestFreeBlockAt(final int posX, final int posZ, final World world, DreamType dtype) {
        int maxHeight = 258;
        switch (dtype) {
            case DREAMWORLD:
                maxHeight = plugin.getSettingsManager().getDreamWorldWorldHeight();
                break;
            case NIGHTMARE:
                maxHeight = plugin.getSettingsManager().getNightMareWorldHeight();
                break;
        }
        int searchedHeight = maxHeight - 1;
        Block lastBlock = null;
        while (searchedHeight > 0) {
            final Block block = world.getBlockAt(posX, searchedHeight, posZ);
            if (lastBlock != null && lastBlock.getType() == Material.AIR
                    && block.getType() != Material.AIR) {
                break;
            }
            lastBlock = block;
            searchedHeight--;
        }
        return ++searchedHeight;
    }

    /**
     * chooses a random dream
     */
    public DreamType randomDream() {

        TreeMap<Double, DreamType> map = new TreeMap<Double, DreamType>();

        map.put(plugin.getSettingsManager().getDreamWorldChance() / 100, DreamType.DREAMWORLD);
        map.put((plugin.getSettingsManager().getDreamWorldChance() / 100) + plugin.getSettingsManager().getNightMareChance() / 100, DreamType.NIGHTMARE);
        map.put(1 - (plugin.getSettingsManager().getDreamWorldChance() / 100) + plugin.getSettingsManager().getNightMareChance() / 100, DreamType.NOTHING);

        DreamType type = map.ceilingEntry(Math.random()).getValue();
        if (plugin.getSettingsManager().getDreamWorldChance() / 100 == 0.50 || plugin.getSettingsManager().getNightMareChance() / 100 == 0.50) {
            if (Math.random() > plugin.getSettingsManager().getDreamWorldChance() / 100) {
                type = DreamType.DREAMWORLD;
            } else {
                type = DreamType.NIGHTMARE;
            }
        }
        if (plugin.getSettingsManager().getDreamWorldChance() / 100 == 1.0) {
            type = DreamType.DREAMWORLD;
        }
        if (plugin.getSettingsManager().getNightMareChance() / 100 == 1.0) {
            type = DreamType.NIGHTMARE;
        }
        return type;
    }

    public String color(String text) {
        String colourised = text.replaceAll("&(?=[0-9a-fA-FkK])", "\u00a7");
        return colourised;
    }

    public boolean isDreamDreamWorld(DreamType dtype) {
        if (dtype == DreamType.DREAMWORLD) {
            return true;
        } else if (dtype == DreamType.NIGHTMARE) {
            return false;
        }
        return false;
    }
}
