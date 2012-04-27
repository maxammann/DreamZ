package com.p000ison.dev.dreamz.listener;

import com.p000ison.dev.dreamz.DreamZ;
import com.p000ison.dev.dreamz.api.DreamLeaveType;
import com.p000ison.dev.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import com.p000ison.dev.dreamz.util.Util;
import com.p000ison.dev.dreamz.util.dUtil;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 *
 * @author p000ison
 */
public class DZEntityListener implements Listener {

    private DreamZ plugin;
    private DreamZPlayerDreamLeaveEvent DPDLE;

    public DZEntityListener(DreamZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            DamageCause cause = event.getCause();
            if (plugin.getDreamManager().isInDream(player)) {
                if (cause == DamageCause.VOID) {
                    DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.VOID);
                    plugin.getServer().getPluginManager().callEvent(DPDLE);

                }
            }

            if (plugin.RETURN_TELEPORT_TIMER.containsKey(player)) {
                if (cause == DamageCause.FIRE_TICK && plugin.RETURN_TELEPORT_TIMER.get(player)) {
                    event.setDamage(0);
                }
            }

            if (player.hasPermission("dreamz.escape.damage")) {
                if (plugin.getDreamManager().isInDream(player) && plugin.getSettingsManager().isUsingDamageExit()) {
                    if (player.getHealth() <= plugin.getSettingsManager().getDefaultRescueHealth()) {
                        player.sendMessage(Util.color(plugin.getSettingsManager().getDamageEscapeMessage()));
                        event.setDamage(0);
                        DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.DEATH);
                        plugin.getServer().getPluginManager().callEvent(DPDLE);

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (event.getEntity() instanceof Player && entity instanceof Creature) {
            Player target = (Player) event.getEntity();

            if (event.isCancelled()) {
                return;
            }

            if (target != null && entity != null) {
                if (dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(event.getEntity().getWorld().getName()) && plugin.getSettingsManager().getConfig().getBoolean("dreams." + event.getEntity().getWorld().getName() + ".PassiveMonsters")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntitytarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player && event.getEntity() instanceof Creature) {
            Player target = (Player) event.getTarget();

            if (event.isCancelled()) {
                return;
            }

            if (target != null) {
                if (dUtil.getDreams(plugin.getSettingsManager().getConfig()).contains(event.getEntity().getWorld().getName()) && plugin.getSettingsManager().getConfig().getBoolean("dreams." + event.getEntity().getWorld().getName() + ".PassiveMonsters")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}