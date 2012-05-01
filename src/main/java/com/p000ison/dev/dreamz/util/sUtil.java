package com.p000ison.dev.dreamz.util;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author p000ison
 */
public class sUtil {

    /**
     * @return the random location to teleport
     */
    public static Location randomSpawn(FileConfiguration config, World world, List<String> preventSpawnOnBlocks) {
        Location loc = null;
        do {

            double X = (config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MinX")
                    + (Math.random() * (config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MaxX")
                    - config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MinX"))));

            double Z = (config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MinZ")
                    + (Math.random() * (config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MaxZ")
                    - config.getDouble("dreams." + world.getName() + ".Spawning.SpawnArea.MinZ"))));

            double Y = getHeighestFreeBlockAt(config, (int) X, (int) Z, world);
            loc = new Location(world, X, Y, Z);

        } while (!checkDreamSpawnLocation(loc, world, preventSpawnOnBlocks) /*
                 * && maxtries >= tries
                 */);
        return loc;
    }

    /**
     * @return true if the player can spawn there
     */
    public static boolean checkEmtyXZ(double X, double Z, World world) {
        int a = 0;
        for (int i = 0; i <= 258; i++) {
            if (world.getBlockTypeIdAt((int) X, i, (int) Z) == 0) {
                a++;
            }
        }
        return a < 255;
    }

    /**
     * @return true if the Location is valid
     */
    public static boolean checkDreamSpawnLocation(Location loc, World world, List<String> preventedSpawnBlocks) {
        for (Iterator<String> it = preventedSpawnBlocks.iterator(); it.hasNext();) {
            Material mat = Material.valueOf(it.next().toUpperCase());
            for (int i = -1; i <= +1; i++) {
                Location blockloc = new Location(world, loc.getBlockX(), loc.getBlockY() + i, loc.getBlockZ());
                if (blockloc.getBlock().getType().equals(mat) || !checkEmtyXZ(loc.getX(), loc.getZ(), world)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getHeighestFreeBlockAt(FileConfiguration config, int posX, int posZ, World world) {
        int maxHeight = config.getInt("dreams." + world.getName() + ".Spawning.SpawnArea.WorldHeight");
        int searchedHeight = maxHeight - 1;
        int lastBlock = -1;
        while (searchedHeight > 0) {
            final int block = world.getBlockTypeIdAt(posX, searchedHeight, posZ);
            if (lastBlock != -1 && lastBlock == 0
                    && block != 0) {
                break;
            }
            lastBlock = block;
            searchedHeight--;
        }
        return ++searchedHeight;
    }
}
