package com.p000ison.dev.dreamz.api.events;

import com.p000ison.dev.dreamz.Dream;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author p000ison
 */
public class DreamZPlayerDreamEnterEvent extends Event implements Cancellable {

    private Player player;
    private Dream dream;
    private boolean Cancelled = false;
    private static final HandlerList handlers = new HandlerList();

    public DreamZPlayerDreamEnterEvent(Player player, Dream dream) {
        this.player = player;
        this.dream = dream;
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

    public Dream getDream() {
        return dream;
    }

    public void setDream(Dream dream) {
        this.dream = dream;
    }
}