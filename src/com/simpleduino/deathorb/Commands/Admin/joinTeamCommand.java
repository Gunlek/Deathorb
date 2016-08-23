package com.simpleduino.deathorb.Commands.Admin;

import com.simpleduino.deathorb.Listeners.GameListener;
import com.simpleduino.deathorb.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * Created by Simple-Duino on 02/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class joinTeamCommand {

    public joinTeamCommand(CommandSender sender, String[] args)
    {
        if(sender instanceof Player)
        {
            Player p = (Player)sender;
            if(args.length >= 3)
            {
                if(args.length >= 4)
                {
                    Player pChange = Bukkit.getPlayer(args[3]);
                    String teamName = args[2];
                    if(PlayerListener.playerLink.get(pChange).hasTeam()) {
                        PlayerListener.playerLink.get(pChange).getTeam().delMember(pChange);
                    }
                    if(teamName.equalsIgnoreCase("def"))
                    {
                        PlayerListener.playerLink.get(pChange).setTeam(GameListener.teamBlue);
                        GameListener.teamBlue.addMember(pChange);
                        p.sendMessage(ChatColor.GREEN + pChange.getName() + " a rejoint l'equipe des defenseurs");
                    }
                    else
                    {
                        PlayerListener.playerLink.get(pChange).setTeam(GameListener.teamRed);
                        GameListener.teamRed.addMember(pChange);
                        p.sendMessage(ChatColor.GREEN + pChange.getName() + " a rejoint l'equipe des attaquants");
                    }
                }
                else
                {
                    String teamName = args[2];
                    if(PlayerListener.playerLink.get(p).hasTeam()) {
                        PlayerListener.playerLink.get(p).getTeam().delMember(p);
                    }
                    if(teamName.equalsIgnoreCase("def"))
                    {
                        PlayerListener.playerLink.get(p).setTeam(GameListener.teamBlue);
                        GameListener.teamBlue.addMember(p);
                        p.sendMessage(ChatColor.GREEN + "Vous avez rejoint l'equipe des defenseurs");
                    }
                    else
                    {
                        PlayerListener.playerLink.get(p).setTeam(GameListener.teamRed);
                        GameListener.teamRed.addMember(p);
                        p.sendMessage(ChatColor.GREEN + "Vous avez rejoint l'equipe des attaquants");
                    }
                }

                Scoreboard right_sb = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective players_list = right_sb.registerNewObjective("Joueurs", "dummy");
                players_list.setDisplaySlot(DisplaySlot.SIDEBAR);
                Team blueTeam = right_sb.registerNewTeam("Defenseurs");
                blueTeam.setAllowFriendlyFire(false);
                Team redTeam = right_sb.registerNewTeam("Attaquants");
                redTeam.setAllowFriendlyFire(false);
                int sb_counter=1;
                for(Player p1 : GameListener.teamBlue.getMembers())
                {
                    Score score = players_list.getScore(ChatColor.BLUE + p1.getName());
                    score.setScore(sb_counter);
                    sb_counter++;
                    blueTeam.addEntry(p1.getName());
                }
                Score separator = players_list.getScore(ChatColor.GREEN + "-----------");
                separator.setScore(sb_counter);
                sb_counter++;
                for(Player p1 : GameListener.teamRed.getMembers())
                {
                    Score score = players_list.getScore(ChatColor.RED + p1.getName());
                    score.setScore(sb_counter);
                    sb_counter++;
                    redTeam.addEntry(p1.getName());
                }
                for(Player p1 : Bukkit.getOnlinePlayers())
                {
                    p1.setScoreboard(right_sb);
                }

            }
            else
            {
                p.sendMessage(ChatColor.RED + "Erreur: Pas assez d'arguments, syntaxe: /hd admin jointeam <team_name> ou /hd admin jointeam <team_name> <player_name>");
            }
        }
    }

}
