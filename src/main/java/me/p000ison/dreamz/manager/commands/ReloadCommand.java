package me.p000ison.dreamz.manager.commands;

import me.p000ison.dreamz.DreamZ;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author p000ison
 */
public class ReloadCommand extends BasicCommand {

    private DreamZ plugin = null;

    public ReloadCommand(DreamZ plugin) {
        super("Reload");
        this.plugin = plugin;
        setDescription("Reloads the Config.");
        setUsage("/dreamz reload");
        setArgumentRange(0, 0);
        setIdentifiers("reload");
        setPermission("dreamz.command.reload");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        plugin.getSettingsManager().load();
        sender.sendMessage(ChatColor.GREEN + "Config Reloaded!");
        return true;
    }
}