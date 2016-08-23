package com.simpleduino.deathorb.Teams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class DeathorbTeam {

    private String teamName;
    private ChatColor teamColor;
    private ArrayList<Player> members = new ArrayList<>();
    File f = new File("plugins/Deathorb/spawnpoints.yml");
    YamlConfiguration cf = YamlConfiguration.loadConfiguration(f);

    public DeathorbTeam(String tn, ChatColor c)
    {
        this.teamName = tn;
        this.teamColor = c;
    }

    public String getName()
    {
        return this.teamName;
    }

    public ChatColor getColor()
    {
        return this.teamColor;
    }

    public ArrayList<Player> getMembers()
    {
        return members;
    }

    public void addMember(Player p)
    {
        this.members.add(p);
    }

    public void delMember(Player p)
    {
        this.members.remove(p);
    }

    public int getTeamSize()
    {
        return this.members.size();
    }

    public boolean isMember(Player p)
    {
        if(members.contains(p))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Location getSpawnPoint()
    {
        Location l = new Location(Bukkit.getWorld(cf.get(this.getName().toLowerCase()+".world").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+".x").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+".y").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+".z").toString()), Float.parseFloat(cf.get(this.getName().toLowerCase()+".yaw").toString()), Float.parseFloat(cf.get(this.getName().toLowerCase()+".pitch").toString()));
        return l;
    }

    public Location getGameSpawn()
    {
        Location l = new Location(Bukkit.getWorld(cf.get(this.getName().toLowerCase()+"_game"+".world").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+"_game"+".x").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+"_game"+".y").toString()), Double.parseDouble(cf.get(this.getName().toLowerCase()+"_game"+".z").toString()), Float.parseFloat(cf.get(this.getName().toLowerCase()+"_game"+".yaw").toString()), Float.parseFloat(cf.get(this.getName().toLowerCase()+"_game"+".pitch").toString()));
        return l;
    }

}
