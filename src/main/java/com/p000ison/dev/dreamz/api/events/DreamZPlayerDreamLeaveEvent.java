package com.p000ison.dev.dreamz.api.events;

import com.p000ison.dev.dreamz.api.DreamLeaveType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author p000ison
 */
public class DreamZPlayerDreamLeaveEvent extends Event implements Cancellable {

    private Player player;
    private boolean Cancelled = false;
    private DreamLeaveType leavetype;
    private String dream;
    private static final HandlerList handlers = new HandlerList();

    public DreamZPlayerDreamLeaveEvent(Player player, DreamLeaveType leavetype) {
        this.player = player;
        this.leavetype = leavetype;
        this.dream = player.getWorld().getName();
    }

    @Override
    public boolean isCancelled() {
        return Cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.Cancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public DreamLeaveType getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(DreamLeaveType leavetype) {
        this.leavetype = leavetype;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }
}