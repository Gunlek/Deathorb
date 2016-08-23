package com.simpleduino.deathorb.Events;

import com.simpleduino.deathorb.Teams.DeathorbTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Simple-Duino on 27/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class TeamWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private DeathorbTeam team;

    public TeamWinEvent(DeathorbTeam t)
    {
        this.team = t;
    }

    public DeathorbTeam getTeam()
    {
        return this.team;
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
