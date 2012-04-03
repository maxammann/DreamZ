package me.p000ison.dreamz.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class Util {

    private static DreamZ plugin;
    private static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static {
        AIR_MATERIALS_TARGET.add((byte) Material.AIR.getId());
    }

    public Util() {
        this.plugin = DreamZ.getInstance();
    }

    /**
     * @return the random location to teleport
     */
    public Location randomLoc(World world, DreamType dtype) {
        Location loc;
        do {
            double X = (plugin.getSettingsManager().getDreamWorldMinX() + (Math.random() * (plugin.getSettingsManager().getDreamWorldMaxX() - plugin.getSettingsManager().getDreamWorldMinX())));
            double Z = (plugin.getSettingsManager().getDreamWorldMinZ() + (Math.random() * (plugin.getSettingsManager().getDreamWorldMaxZ() - plugin.getSettingsManager().getDreamWorldMinZ())));
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
    public DreamType randomDream(double dwchance, double nmchance) {

        TreeMap<Double, DreamType> map = new TreeMap<Double, DreamType>();

        map.put(dwchance / 100, DreamType.DREAMWORLD);
        map.put((dwchance / 100) + nmchance / 100, DreamType.NIGHTMARE);
        map.put(1 - (dwchance / 100) + nmchance / 100, DreamType.NOTHING);

        DreamType type = map.ceilingEntry(Math.random()).getValue();
        if (dwchance / 100 == 0.50 || nmchance / 100 == 0.50) {
            if (Math.random() > dwchance / 100) {
                type = DreamType.DREAMWORLD;
            } else {
                type = DreamType.NIGHTMARE;
            }
        }
        if (dwchance / 100 == 1.0) {
            type = DreamType.DREAMWORLD;
        }
        if (nmchance / 100 == 1.0) {
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

    public Location getTarget(final LivingEntity entity) {
        final Block block = entity.getTargetBlock(AIR_MATERIALS_TARGET, 300);
        if (block == null && block.getType() == Material.BED_BLOCK) {
            System.out.println("Please target a bed!");
        }
        return block.getLocation();
    }
    
    public boolean checkBed(Location loc) {
        String world = "";
        if (loc.getWorld().equals(plugin.getWorldManager().getDreamWorld())) {
            world = "DreamWorld";
        } else if (loc.getWorld().equals(plugin.getWorldManager().getNightMare())) {
            world = "NightMare";
        } 
        for (String str : plugin.getConfig().getConfigurationSection(world + ".Beds").getKeys(false)) {
            if ((loc.getX() == plugin.getSettingsManager().getConfig().getDouble(world + ".Beds." + str + ".X")
                    && loc.getY() == plugin.getSettingsManager().getConfig().getDouble(world + ".Beds." + str + ".Y")
                    && loc.getZ() == plugin.getSettingsManager().getConfig().getDouble(world + ".Beds." + str + ".Z"))) {
                return true;
            }
        }
        return false;
    }
    
    public DreamType getPlayerDreamType(Player player) {
        if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
            return DreamType.DREAMWORLD;
        } else if (player.getWorld() == plugin.getWorldManager().getNightMare()) {
            return DreamType.NIGHTMARE;
        } else {
            return DreamType.NOTHING;
        }
    }
}
