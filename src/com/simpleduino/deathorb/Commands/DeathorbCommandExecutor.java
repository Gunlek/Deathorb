package com.simpleduino.deathorb.Commands;

import com.simpleduino.deathorb.Events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class DeathorbCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length >= 1)
        {
            if(args[0].equalsIgnoreCase("start"))
            {
                if(sender instanceof Player) {
                    if((sender).hasPermission("deathorb.start")) {
                        Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent((Player) sender));
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande");
                    }
                }
                else
                    sender.sendMessage(ChatColor.RED + "Vous devez être joueur pour exécuter cette commande");
            }
            else if(args[0].equalsIgnoreCase("admin"))
            {
                new DeathorbAdminCommandExecutor(sender, command, args);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Commande inconnue");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Utilisez /do <start>");
        }
        return false;
    }
}
