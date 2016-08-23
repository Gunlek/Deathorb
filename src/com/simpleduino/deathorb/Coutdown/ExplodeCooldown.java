package com.simpleduino.deathorb.Coutdown;

import com.simpleduino.deathorb.Listeners.PlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Simple-Duino on 13/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class ExplodeCooldown extends BukkitRunnable {

    private Player p;
    private int c;
    private int counter = 0;

    public ExplodeCooldown(Player p, int cooldown)
    {
        this.p = p;
        this.c = cooldown;
    }

    @Override
    public void run() {
        if(this.c - this.counter > 0)
        {
            p.getInventory().getItem(8).setAmount(this.c - this.counter);
        }
        else
        {
            if(PlayerListener.explode_cooldown.contains(this.p))
                PlayerListener.explode_cooldown.remove(this.p);
            this.cancel();
        }

        this.counter++;
    }
}
