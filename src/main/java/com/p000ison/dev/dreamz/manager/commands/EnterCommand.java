package com.p000ison.dev.dreamz.manager.commands;

import com.p000ison.dev.dreamz.Dream;
import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamEnterEvent;
import com.p000ison.dev.dreamz.util.dUtil;
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
        setUsage("/dreamz enter §9<dream>");
        setArgumentRange(1, 1);
        setIdentifiers("enter", "sleep");
        setPermission("dreamz.command.enter");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (dUtil.isDreamAviable(args[0], plugin.getSettingsManager().getConfig())) {
                    plugin.getServer().getPluginManager().callEvent(new DreamZPlayerDreamEnterEvent(player, new Dream(args[0], true)));
                }
            }
        } else {
            sender.sendMessage("Console cant sleep!");
        }

        return true;
    }
}
