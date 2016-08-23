package com.simpleduino.deathorb.Coutdown;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by Simple-Duino on 11/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class ArrowGiver extends BukkitRunnable {

    private String uuid;

    public ArrowGiver(String id)
    {
        this.uuid = id;
    }

    @Override
    public void run() {
        if(Bukkit.getPlayer(UUID.fromString(this.uuid))!=null)
        {
            Player p = Bukkit.getPlayer(UUID.fromString(this.uuid));
            if(p.getInventory().getItem(4).getAmount()<64)
            {
                p.getInventory().setItem(4, new ItemStack(Material.ARROW, p.getInventory().getItem(4).getAmount()+1));
            }
        }
    }
}
