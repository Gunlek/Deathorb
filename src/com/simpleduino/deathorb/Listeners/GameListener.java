package com.simpleduino.deathorb.Listeners;

import com.simpleduino.deathorb.Coutdown.LobbyReturn;
import com.simpleduino.deathorb.Coutdown.StartingGameCountdown;
import com.simpleduino.deathorb.DeathorbPlugin;
import com.simpleduino.deathorb.Events.GameStartEvent;
import com.simpleduino.deathorb.Events.TeamWinEvent;
import com.simpleduino.deathorb.Teams.DeathorbTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class GameListener implements Listener {

    public static DeathorbTeam teamBlue, teamRed;

    @EventHandler
    public void onGameStarting(GameStartEvent e) {
        teamBlue = new DeathorbTeam("blue", ChatColor.BLUE);
        teamRed = new DeathorbTeam("red", ChatColor.RED);
        ArrayList<Player> onlinePlayers = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
            ItemMeta bootsMeta = boots.getItemMeta();
            bootsMeta.setDisplayName(ChatColor.DARK_PURPLE + "20 coeurs");
            boots.setItemMeta(bootsMeta);
            p.getInventory().setBoots(boots);
        }
        for (Player p : PlayerListener.playerLink.keySet()) {
            onlinePlayers.add(p);
        }
        randomizeTeams(onlinePlayers);
        new StartingGameCountdown().runTaskTimer(DeathorbPlugin.getPlugin(DeathorbPlugin.class), 0, 20L);
    }

    @EventHandler
    public void onTeamWin(TeamWinEvent e)
    {
        String team = "";
        PlayerListener.gameStarted = false;
        if(e.getTeam().getName().equalsIgnoreCase("red"))
            team = "ROUGES";
        else
            team = "BLEUS";
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.MAGIC + "|||||"+ChatColor.RESET.toString() + ChatColor.RED +"         LES "+team+" GAGNENT !          "+ChatColor.MAGIC+"|||||");
        }

        new LobbyReturn().runTaskLater(DeathorbPlugin.getPlugin(DeathorbPlugin.class), 20L*15);
    }

    private void randomizeTeams(ArrayList<Player> pList) {
        for (Player p : pList) {
            Integer i = Integer.valueOf(Long.toString(Math.round(Math.random())));
            if (i == 0) {
                if (teamBlue.getTeamSize() >= pList.size() / 2) {
                    teamRed.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamRed);
                } else {
                    teamBlue.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamBlue);
                }
            } else {
                if (teamRed.getTeamSize() >= pList.size() / 2) {
                    teamBlue.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamBlue);
                } else {
                    teamRed.addMember(p);
                    PlayerListener.playerLink.get(p).setTeam(teamRed);
                }
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e)
    {
        e.setCancelled(true);
        e.getWorld().setStorm(false);
    }
}
