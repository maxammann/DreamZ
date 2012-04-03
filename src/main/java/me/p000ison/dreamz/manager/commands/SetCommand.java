package me.p000ison.dreamz.manager.commands;

import me.p000ison.dreamz.DreamZ;
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
        setUsage("/dreamz setbed §9<dreamworld|nightmare> <bedname>");
        setArgumentRange(2, 2);
        setIdentifiers("setbed");
        setPermission("dreamz.command.set");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getDreamWorldName())) {
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Beds." + args[1] + ".X", plugin.getUtil().getTarget(player).getX());
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Beds." + args[1] + ".Y", plugin.getUtil().getTarget(player).getY());
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Beds." + args[1] + ".Z", plugin.getUtil().getTarget(player).getZ());
                    plugin.getSettingsManager().save();
                    player.sendMessage(ChatColor.GREEN + "Bed " + args[1] + "set! Please target always the pillow of the bed!");
                } else if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getNightMareName())) {
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Beds." + args[1] + ".X", plugin.getUtil().getTarget(player).getX());
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Beds." + args[1] + ".Y", plugin.getUtil().getTarget(player).getY());
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Beds." + args[1] + ".Z", plugin.getUtil().getTarget(player).getZ());
                    plugin.getSettingsManager().save();
                    player.sendMessage(ChatColor.GREEN + "Bed " + args[1] + "set! Please target always the pillow of the bed!");
                }
            }
        } else {
            sender.sendMessage("Console cant set the bed!");
        }

        return true;
    }
}
