package com.p000ison.dev.dreamz.manager.commands;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class SetCommand extends BasicCommand {

    private DreamZ plugin = null;

    public SetCommand(DreamZ plugin) {
        super("SetBed");
        this.plugin = plugin;
        setDescription("Sets the rescue-bed to return.");
        setUsage("/dreamz set §9<bed/dream> <dream> <bedname>");
        setArgumentRange(2, 4);
        setIdentifiers("set");
        setPermission("dreamz.command.set");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("bed")) {
                    plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".beds." + args[2] + ".X", Util.getTarget(player).getX());
                    plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".beds." + args[2] + ".Y", Util.getTarget(player).getY());
                    plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".beds." + args[2] + ".Z", Util.getTarget(player).getZ());
                    player.sendMessage(ChatColor.GREEN + "Bed " + args[2] + " for dream " + args[1] + " has been set! Please target always the pillow of the bed!");
                    plugin.getSettingsManager().save();
                }
            }
        } else {
            sender.sendMessage("Console cant set the bed!");
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("dream")) {
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Generation.GenerateStructures", false);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Generation.Environment", "NORMAL");
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Generation.Generator", "SkylandsPlus");
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Generation.Seed", 123456789L);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Chance", 50);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".DisableHunger", true);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".UsingDuration", true);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Duration", 5L);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Thundering", false);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Storming", false);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".PassiveMonsters", false);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".FlyMultiplier", 1);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.RespawnInDream", true);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.RespawnAtDeathPoint", true);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.UseRandomSpawn", true);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.SpawnArea.MaxX", 100);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.SpawnArea.MinX", -100);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.SpawnArea.MaxZ", 100);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.SpawnArea.MinZ", -100);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".Spawning.SpawnArea.WorldHeight", 256);
                plugin.getSettingsManager().getConfig().set("dreams." + args[1] + ".DreamType", "DEFAULT");
                sender.sendMessage(ChatColor.GREEN + "Dream " + args[1] + " set! Please target always the pillow of the bed!");
                plugin.getSettingsManager().save();
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("value")) {
                if (args[3].equals("true") || args[3].equals("false")) {
                    if (plugin.getSettingsManager().getConfig().isSet("dreams." + args[1] + "." + args[2])) {
                        plugin.getSettingsManager().getConfig().set("dreams." + args[1] + "." + args[2], Boolean.parseBoolean(args[3]));
                    } else {
                        sender.sendMessage("There is no setting with this name!");
                    }
                } else if (args[3].matches("[0-9]+")) {
                    if (plugin.getSettingsManager().getConfig().isSet("dreams." + args[1] + "." + args[2])) {
                        plugin.getSettingsManager().getConfig().set("dreams." + args[1] + "." + args[2], Double.parseDouble(args[3]));
                    } else {
                        sender.sendMessage("There is no setting with this name!");
                    }
                } else {
                    if (plugin.getSettingsManager().getConfig().isSet("dreams." + args[1] + "." + args[2])) {
                        plugin.getSettingsManager().getConfig().set("dreams." + args[1] + "." + args[2], args[3]);
                    } else {
                        sender.sendMessage("There is no setting with this name!");
                    }
                }
                plugin.getSettingsManager().save();
            }
        }
        return true;
    }
}
