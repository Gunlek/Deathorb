package com.simpleduino.deathorb.Commands.Admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by Simple-Duino on 30/05/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class SetSpawnCommand {

    File f = new File("plugins/Deathorb/spawnpoints.yml");
    YamlConfiguration cf = YamlConfiguration.loadConfiguration(f);

    public SetSpawnCommand(CommandSender sender, String[] args)
    {
        if(!f.exists())
        {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(args.length >= 3)
        {
            if(sender instanceof Player)
            {
                Player p = (Player)sender;
                cf.set(args[2]+".x", p.getLocation().getX());
                cf.set(args[2]+".y", p.getLocation().getY());
                cf.set(args[2]+".z", p.getLocation().getZ());
                cf.set(args[2]+".yaw", p.getLocation().getYaw());
                cf.set(args[2]+".pitch", p.getLocation().getPitch());
                cf.set(args[2]+".world", p.getLocation().getWorld().getName().toString());
                try {
                    cf.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                p.sendMessage(ChatColor.GREEN + "Vous avez défini le spawn "+args[2]);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Erreur: Cette commande doit être executée par un joueur");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Erreur: Pas assez d'arguments, syntaxe: /hd admin setspawn <spawn_name>");
        }
    }

}
