package com.p000ison.dev.dreamz.util;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author p000ison
 */
public class Util {

    private static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static {
        AIR_MATERIALS_TARGET.add((byte) Material.AIR.getId());
    }

    public static String color(String text) {
        String colourised = text.replaceAll("&(?=[0-9a-fA-FkK])", "\u00a7");
        return colourised;
    }

    public static Location getTarget(final LivingEntity entity) {
        final Block block = entity.getTargetBlock(AIR_MATERIALS_TARGET, 300);
        if (block == null && block.getType() == Material.BED_BLOCK) {
            System.out.println("Please target a bed!");
        }
        return block.getLocation();
    }
}
