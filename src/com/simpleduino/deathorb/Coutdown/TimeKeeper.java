package com.simpleduino.deathorb.Coutdown;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Simple-Duino on 11/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class TimeKeeper extends BukkitRunnable {
    @Override
    public void run() {
        for(World w : Bukkit.getWorlds())
        {
            w.setTime(30000);
        }
    }
}
