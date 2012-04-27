package com.p000ison.dev.dreamz.manager.commands;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.DreamLeaveType;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import com.p000ison.dev.dreamz.util.dUtil;
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
            if (dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(player.getWorld().getName())) {
                plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.COMMAND));
            }
        } else {
            sender.sendMessage("Console cant leave a dream!");
        }
        return true;
    }
}
