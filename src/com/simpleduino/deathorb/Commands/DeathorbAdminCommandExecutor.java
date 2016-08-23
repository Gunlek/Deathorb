package com.simpleduino.deathorb.Commands;

import com.simpleduino.deathorb.Commands.Admin.SetSpawnCommand;
import com.simpleduino.deathorb.Commands.Admin.setWinPointCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class DeathorbAdminCommandExecutor {

    public DeathorbAdminCommandExecutor(CommandSender sender, Command command, String[] args)
    {
        if(args.length >= 2)
        {
            if(args[1].equalsIgnoreCase("setspawn"))
            {
                if(sender.hasPermission("deathorb.admin.setspawn"))
                {
                    new SetSpawnCommand(sender, args);
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande");
                }
            }
            else if(args[1].equalsIgnoreCase("setwinpoint"))
            {
                if(sender.hasPermission("deathorb.admin.setwinpoint"))
                {
                    new setWinPointCommand(sender, args);
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Erreur: Syntaxe incorrecte, utilisez /do admin <setspawn|setwinpoint>");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Commande inconnue");
        }
    }

}
