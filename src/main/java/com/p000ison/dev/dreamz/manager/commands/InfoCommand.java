package com.p000ison.dev.dreamz.manager.commands;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.util.dUtil;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author p000ison
 */
public class InfoCommand extends BasicCommand {

    private DreamZ plugin = null;

    public InfoCommand(DreamZ plugin) {
        super("Info");
        this.plugin = plugin;
        setDescription("Gives you infos about DreamZ.");
        setUsage("/dreamz info");
        setArgumentRange(0, 0);
        setIdentifiers("info");
        setPermission("dreamz.command.info");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        sender.sendMessage("--------------------------------------");
        
        sender.sendMessage("All Active Tasks:");
        for (BukkitTask task : plugin.getServer().getScheduler().getPendingTasks()) {
            sender.sendMessage("     " + String.valueOf(task.getTaskId()) + " - Sync: " + task.isSync());
        }
        
        sender.sendMessage("--------------------------------------");
        sender.sendMessage("DreamZ Tasks:");
        for (Map.Entry<Player, Integer> entry : plugin.SCHEDULERS.entrySet()) {
            sender.sendMessage("     " + entry.getKey().getName() + " - " + entry.getValue());
        }
        
        sender.sendMessage("--------------------------------------");
        return true;
    }
}