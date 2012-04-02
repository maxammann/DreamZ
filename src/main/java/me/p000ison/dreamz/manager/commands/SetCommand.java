package me.p000ison.dreamz.manager.commands;

import me.p000ison.dreamz.DreamZ;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
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
        setUsage("/dreamz setbed §9<dreamworld|nightmare>");
        setArgumentRange(1, 1);
        setIdentifiers("setbed");
        setPermission("dreamz.command.set");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Block targetblock = player.getTargetBlock(null, 101);
            if (args.length == 1) {

                if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getDreamWorldName())) {
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.X", targetblock.getX());
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.Y", targetblock.getY());
                    plugin.getSettingsManager().getPlugin().getConfig().set("DreamWorld.Bed.Z", targetblock.getZ());
                    plugin.getSettingsManager().save();
                    player.sendMessage(ChatColor.GREEN + "Bed set!");
                } else if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getNightMareName())) {
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.X", targetblock.getX());
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.Y", targetblock.getY());
                    plugin.getSettingsManager().getPlugin().getConfig().set("NightMare.Bed.Z", targetblock.getZ());
                    plugin.getSettingsManager().save();
                    player.sendMessage(ChatColor.GREEN + "Bed set!");
                }
            }
        } else {
            sender.sendMessage("Console cant set the bed!");
        }

        return true;
    }
}
