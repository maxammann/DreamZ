/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.p000ison.dreamz;

import java.io.File;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import me.p000ison.dreamz.manager.DreamManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Max
 */
public class DreamZCommandExecutor implements CommandExecutor {

    private DreamZ plugin;

    public DreamZCommandExecutor(DreamZ plugin) {
        this.plugin = plugin;
    }

    /**
     * @param executes commands
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        Block targetblock = null;
        DreamManager dream = new DreamManager();
        if (sender instanceof Player) {
            player = (Player) sender;
            targetblock = player.getTargetBlock(null, 5);
        }


        if (cmd.getName().equalsIgnoreCase("dreamz")) {
            if (args.length == 1) {
                if (args[0].equals("reload")) {
                    if (plugin.hasPermission("dreamz.command.reload", sender)) {
                        plugin.sendCommandMessage("Config reloaded.", player, ChatColor.GREEN);
                        plugin.getSettingsManager().load();
                    }
                } else if (args[0].equals("nmbed")) {
                    if (player != null) {
                        if (plugin.hasPermission("dreamz.command.nmbed", sender)) {
                            plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.X", targetblock.getX());
                            plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.Y", targetblock.getY());
                            plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.Z", targetblock.getZ());
                            plugin.getSettingsManager().save();
                            player.sendMessage(ChatColor.GREEN + plugin.getSettingsManager().getNightMareName() + " bed set!");
                        }
                    }
                } else if (args[0].equals("dwbed")) {
                    if (player != null) {
                        if (plugin.hasPermission("dreamz.command.dwbed", sender)) {
                            plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.X", targetblock.getX());
                            plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.Y", targetblock.getY());
                            plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.Z", targetblock.getZ());
                            plugin.getSettingsManager().save();
                            player.sendMessage(ChatColor.GREEN + plugin.getSettingsManager().getDreamWorldName() + " bed set!");
                        }
                    }
                } else if (args[0].equals("wakeup")) {
                    if (player != null) {
                        if (plugin.hasPermission("dreamz.command.wakeup", sender)) {
                            plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.NORMAL));
                            if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                                dream.leave(player, DreamType.DREAMWORLD, DreamLeaveType.COMMAND);
                            } else if (player.getWorld() == plugin.getWorldManager().getNightMare()) {
                                dream.leave(player, DreamType.NIGHTMARE, DreamLeaveType.COMMAND);
                            }
                        }
                    }
                } else if (args[0].equals("help")) {
                    plugin.sendCommandMessage("Commands:", player, ChatColor.RED);
                    plugin.sendCommandMessage("/dreamz reload", player, ChatColor.RED);
                    plugin.sendCommandMessage("/dreamz nmbed:", player, ChatColor.RED);
                    plugin.sendCommandMessage("/dreamz dwbed", player, ChatColor.RED);
                    plugin.sendCommandMessage("/dreamz wakeup", player, ChatColor.RED);
                    plugin.sendCommandMessage("/dreamz sleep", player, ChatColor.RED);
                } else {
                    plugin.sendCommandMessage("Help: - /dreamz help", player, ChatColor.RED);
                }
            }
            if (args.length == 2) {
                if (args[0].equals("sleep")) {
                    if (player != null) {
                        if (plugin.hasPermission("dreamz.command.sleep", sender)) {
                            if (args[1].equalsIgnoreCase(plugin.getSettingsManager().getDreamWorldName())) {
                                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, DreamType.DREAMWORLD));
                                dream.enter(player, DreamType.DREAMWORLD);
                            }
                            if (args[1].equalsIgnoreCase(plugin.getSettingsManager().getNightMareName())) {
                                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, DreamType.NIGHTMARE));
                                dream.enter(player, DreamType.NIGHTMARE);
                            }
                        }
                    }
                }
            }
            return true;
        }
        if (args.length == 0) {
            plugin.sendCommandMessage("Help: - /dreamz help", player, ChatColor.RED);
            return true;
        }

        return false;
    }
}
