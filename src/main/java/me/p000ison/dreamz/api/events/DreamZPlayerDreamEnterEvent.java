package me.p000ison.dreamz.api.events;

import me.p000ison.dreamz.api.DreamType;
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
    private DreamType dreamtype;
    private boolean Cancelled = false;
    private static final HandlerList handlers = new HandlerList();

    public DreamZPlayerDreamEnterEvent(Player player, DreamType dreamtype) {
        this.player = player;
        this.dreamtype = dreamtype;
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

    public DreamType getDreamType() {
        return dreamtype;
    }

    public void setDreamType(DreamType dt) {
        this.dreamtype = dt;
    }
}
