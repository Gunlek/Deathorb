package com.simpleduino.deathorb.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player p;

    public GameStartEvent(Player player)
    {
        this.p = player;
    }

    public Player getStarter()
    {
        return this.p;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
