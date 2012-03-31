package me.p000ison.dreamz.util;

import java.io.File;
import java.util.logging.Level;
import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author p000ison
 */
public class Inventory {

    DreamZ plugin = DreamZ.getInstance();

    private void write(File save, ItemStack[] inventory) {
        try {
            FileConfiguration out = YamlConfiguration.loadConfiguration(save);

            for (int i = 0; i < inventory.length; i++) {
                ItemStack item = inventory[i];
                if (item != null) {
                    out.set(i + "", item);
                } else {
                    out.set(i + "", new ItemStack(Material.AIR));
                }
            }
            out.save(save);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to write the Inventory!");
        }
    }

    private void read(File save, ItemStack[] inventory) {
        try {
            FileConfiguration in = YamlConfiguration.loadConfiguration(save);

            for (int i = 0; i < inventory.length; i++) {
                inventory[i] = in.getItemStack(i + "");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load the Inventory!");
        }
    }

    private File getInventoryFile(DreamType dtype, Player player) {

        if (!getDic(player).exists()) {
            getDic(player).mkdir();
        }
        return new File(getDic(player) + File.separator + dtype.toString().toLowerCase() + ".inv");
    }

    private File getArmorFile(DreamType dtype, Player player) {

        if (!getDic(player).exists()) {
            getDic(player).mkdir();
        }
        return new File(getDic(player) + File.separator + dtype.toString().toLowerCase() + ".armor");
    }

    public void save(DreamType dtype, Player player) {
        if ((plugin.getSettingsManager().isDreamWorldSaveInventory()
                && player.getWorld() == plugin.getWorldManager().getDreamWorld())
                || plugin.getSettingsManager().isNightMareSaveInventory()
                && player.getWorld() == plugin.getWorldManager().getNightMare()) {
            write(getInventoryFile(dtype, player), player.getInventory().getContents());
            write(getArmorFile(dtype, player), player.getInventory().getArmorContents());
        }
    }

    public void load(DreamType dtype, Player player) {
        File save = getInventoryFile(dtype, player);
        if (save.exists()) {
            if ((plugin.getSettingsManager().isDreamWorldSaveInventory()
                    && player.getWorld() == plugin.getWorldManager().getDreamWorld())
                    || plugin.getSettingsManager().isNightMareSaveInventory()
                    && player.getWorld() == plugin.getWorldManager().getNightMare()) {
                player.getInventory().clear();

                ItemStack[] inventory = player.getInventory().getContents();
                read(save, inventory);
                player.getInventory().setContents(inventory);

                ItemStack[] armor = new ItemStack[4];
                read(getInventoryFile(dtype, player), armor);
                player.getInventory().setArmorContents(armor);
            }
        }
    }

    private File getDic(Player player) {
        File dic = new File(plugin.getDataFolder().getAbsolutePath()
                + File.separator + "players"
                + File.separator + player.getName());
        return dic;
    }

    public void clearInventory(Player player, DreamType dtype) {
        switch (dtype) {
            case DREAMWORLD:
                if (plugin.getSettingsManager().isDreamWorldClearInventory())
                    player.getInventory().clear();
                break;
            case NIGHTMARE:
                if (plugin.getSettingsManager().isNightMareClearInventory())
                    player.getInventory().clear();
                break;
        }
    }
}
