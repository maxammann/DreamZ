package me.p000ison.dreamz.manager.commands;

import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class LeaveCommand extends BasicCommand {

    private DreamZ plugin = null;

    public LeaveCommand(DreamZ plugin) {
        super("Leave");
        this.plugin = plugin;
        setDescription("Forces you to leave the dream.");
        setUsage("/dreamz leave");
        setArgumentRange(0, 0);
        setIdentifiers("leave", "wakeup");
        setPermission("dreamz.command.leave");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.COMMAND));
            if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                plugin.getDreamManager().leave(player, DreamType.DREAMWORLD, DreamLeaveType.COMMAND);
            } else if (player.getWorld() == plugin.getWorldManager().getNightMare()) {
                plugin.getDreamManager().leave(player, DreamType.NIGHTMARE, DreamLeaveType.COMMAND);

            }
        } else {
            sender.sendMessage("Console cant leave a dream!");
        }

        return true;
    }
}
