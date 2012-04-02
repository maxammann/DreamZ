package me.p000ison.dreamz.manager.commands;

import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class EnterCommand extends BasicCommand {

    private DreamZ plugin = null;

    public EnterCommand(DreamZ plugin) {
        super("Enter");
        this.plugin = plugin;
        setDescription("Forces you to enter the dream.");
        setUsage("/dreamz enter §9<dreamworld|nightmare>");
        setArgumentRange(1, 1);
        setIdentifiers("enter", "sleep");
        setPermission("dreamz.command.enter");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getDreamWorldName())) {
                    plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, DreamType.DREAMWORLD));
                    plugin.getDreamManager().enter(player, DreamType.DREAMWORLD);
                }
                if (args[0].equalsIgnoreCase(plugin.getSettingsManager().getNightMareName())) {
                    plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, DreamType.NIGHTMARE));
                    plugin.getDreamManager().enter(player, DreamType.NIGHTMARE);
                }
            }
        } else {
            sender.sendMessage("Console cant sleep!");
        }

        return true;
    }
}
