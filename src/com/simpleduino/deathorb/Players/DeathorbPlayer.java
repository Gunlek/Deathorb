package com.simpleduino.deathorb.Players;

import com.simpleduino.deathorb.Teams.DeathorbTeam;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 31/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class DeathorbPlayer {

    private String name;
    private String uuid;
    private Player playerObject;
    private DeathorbTeam team = null;
    private String className = null;

    public DeathorbPlayer(Player p)
    {
        this.name = p.getName();
        this.uuid = p.getUniqueId().toString();
        this.playerObject = p;
    }

    public Player getPlayerObject()
    {
        return this.playerObject;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUuid()
    {
        return this.uuid;
    }

    public boolean hasTeam()
    {
        if(this.team==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean hasClass()
    {
        if(this.className == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public DeathorbTeam getTeam()
    {
        return this.team;
    }

    public void setTeam(DeathorbTeam t)
    {
        this.team = t;
    }

    public void setClassName(String cm)
    {
        this.className = cm;
    }

    public String getClassName()
    {
        if(this.className!=null) {
            return this.className;
        }
        else {
            return "";
        }
    }

}
