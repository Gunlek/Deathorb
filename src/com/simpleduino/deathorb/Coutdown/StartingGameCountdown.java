package com.simpleduino.deathorb.Coutdown;

import com.simpleduino.deathorb.Listeners.PlayerListener;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class StartingGameCountdown extends BukkitRunnable {

    private int counter = 10;

    @Override
    public void run() {
        if(counter != 0) {
            IChatBaseComponent titleComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+ ChatColor.YELLOW+"La partie démarre dans\"}");
            IChatBaseComponent subtitleComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.DARK_AQUA + Integer.toString(counter) + " secondes\"}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComp);
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComp);
            PacketPlayOutTitle timePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, 5, 10, 5);

            for (Player p : Bukkit.getOnlinePlayers()) {
                CraftPlayer cp = (CraftPlayer) p;
                cp.getHandle().playerConnection.sendPacket(titlePacket);
                cp.getHandle().playerConnection.sendPacket(subtitlePacket);
                cp.getHandle().playerConnection.sendPacket(timePacket);
                p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                p.setExp(0);
                p.setLevel(counter);
                p.setHealthScale(40);
                p.setMaxHealth(40);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        else
        {
            IChatBaseComponent titleComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+ ChatColor.YELLOW+"C'est parti !\"}");
            IChatBaseComponent subtitleComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComp);
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComp);
            PacketPlayOutTitle timePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, 5, 10, 5);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().clear();
                ItemStack firecharge_lvl1 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack firecharge_lvl15 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack firecharge_lvl18 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack firecharge_lvl20 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack firecharge_lvl10 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack firecharge_lvl5 = new ItemStack(Material.FIREWORK_CHARGE, 1);
                ItemStack iron_sword = new ItemStack(Material.IRON_SWORD, 1);
                ItemMeta firecharge_lvl = firecharge_lvl1.getItemMeta();
                firecharge_lvl.setDisplayName("-Lvl 1-");
                firecharge_lvl1.setItemMeta(firecharge_lvl);
                firecharge_lvl.setDisplayName("-Lvl 15-");
                firecharge_lvl15.setItemMeta(firecharge_lvl);
                firecharge_lvl.setDisplayName("-Lvl 18-");
                firecharge_lvl18.setItemMeta(firecharge_lvl);
                firecharge_lvl.setDisplayName("-Lvl 20-");
                firecharge_lvl20.setItemMeta(firecharge_lvl);
                firecharge_lvl.setDisplayName("-Lvl 10-");
                firecharge_lvl10.setItemMeta(firecharge_lvl);
                firecharge_lvl.setDisplayName("-Lvl 5-");
                firecharge_lvl5.setItemMeta(firecharge_lvl);

                p.getInventory().setItem(0, firecharge_lvl1);
                p.getInventory().setItem(1, firecharge_lvl15);
                p.getInventory().setItem(2, firecharge_lvl18);

                p.getInventory().setItem(4, iron_sword);

                p.getInventory().setItem(6, firecharge_lvl20);
                p.getInventory().setItem(7, firecharge_lvl10);
                p.getInventory().setItem(8, firecharge_lvl5);

                CraftPlayer cp = (CraftPlayer) p;
                cp.getHandle().playerConnection.sendPacket(titlePacket);
                cp.getHandle().playerConnection.sendPacket(subtitlePacket);
                cp.getHandle().playerConnection.sendPacket(timePacket);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
                p.setExp(0);
                p.setLevel(counter);
                p.teleport(PlayerListener.playerLink.get(p).getTeam().getSpawnPoint());
                p.setHealthScale(40);
                p.setMaxHealth(40);
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setGameMode(GameMode.SURVIVAL);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 99999, 1, false, false));
            }
            PlayerListener.gameStarted = true;
            this.cancel();
        }
        counter--;
    }
}
