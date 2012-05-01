package com.p000ison.dev.dreamz.manager.commands;

import com.p000ison.dev.dreamz.DreamZ;
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
        long start = System.nanoTime();
        plugin.getSettingsManager().load();
        long end = System.nanoTime();
        sender.sendMessage(String.format(ChatColor.GREEN + "Config Reloaded! (%s s)", (end - start) / 1000000000.0));
        return true;
    }
}