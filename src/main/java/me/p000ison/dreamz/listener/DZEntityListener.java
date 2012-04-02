package me.p000ison.dreamz.listener;

import me.p000ison.dreamz.DreamZ;
import me.p000ison.dreamz.api.DreamLeaveType;
import me.p000ison.dreamz.api.DreamType;
import me.p000ison.dreamz.api.events.DreamZPlayerDreamLeaveEvent;
import me.p000ison.dreamz.manager.DreamManager;
import me.p000ison.dreamz.util.Util;
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
    private DreamManager dream = new DreamManager();
    private Util util = new Util();
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
            if (dream.isInDream(player)) {
                if (cause == DamageCause.VOID) {
                    DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.VOID);
                    plugin.getServer().getPluginManager().callEvent(DPDLE);

                    if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                        dream.leave(player, DreamType.DREAMWORLD, DreamLeaveType.VOID);
                    } else if (player.getWorld() == plugin.getWorldManager().getNightMare()) {
                        dream.leave(player, DreamType.NIGHTMARE, DreamLeaveType.VOID);
                    }
                }
            }

            if (plugin.afterTeleport.containsKey(player)) {
                if (cause == DamageCause.FIRE_TICK && plugin.afterTeleport.get(player)) {
                    event.setDamage(0);
                }
            }

            if (player.hasPermission("dreamz.escape.damage")) {
                if (dream.isInDream(player) && plugin.getSettingsManager().isUsingDamageExit()) {
                    if (player.getHealth() <= plugin.getSettingsManager().getDefaultRescueHealth()) {
                        player.sendMessage(util.color(plugin.getSettingsManager().getDamageEscapeMessage()));
                        event.setDamage(0);

                        DPDLE = new DreamZPlayerDreamLeaveEvent(player, DreamLeaveType.DEATH);
                        plugin.getServer().getPluginManager().callEvent(DPDLE);

                        if (player.getWorld() == plugin.getWorldManager().getDreamWorld()) {
                            dream.leave(player, DreamType.DREAMWORLD, DreamLeaveType.DEATH);
                        } else if (player.getWorld() == plugin.getWorldManager().getNightMare()) {
                            dream.leave(player, DreamType.NIGHTMARE, DreamLeaveType.DEATH);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        Entity target = event.getEntity();

        if (event.isCancelled()) {
            return;
        }

        if ((target.getWorld() == plugin.getWorldManager().getDreamWorld()
                && plugin.getSettingsManager().isDreamWorldPassiveMobs())
                || (target.getWorld() == plugin.getWorldManager().getNightMare()
                && plugin.getSettingsManager().isNighMarePassiveMobs())) {
            if (target instanceof Player) {
                if (entity instanceof Creature) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntitytarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();

        if (event.isCancelled()) {
            return;
        }

        if (target != null) {
            if ((target.getWorld() == plugin.getWorldManager().getDreamWorld()
                    && plugin.getSettingsManager().isDreamWorldPassiveMobs())
                    || (target.getWorld() == plugin.getWorldManager().getNightMare()
                    && plugin.getSettingsManager().isNighMarePassiveMobs())) {
                if (target instanceof Player) {
                    if (entity instanceof Creature) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}