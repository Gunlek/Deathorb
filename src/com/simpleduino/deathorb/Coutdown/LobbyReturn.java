package com.simpleduino.deathorb.Coutdown;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.simpleduino.deathorb.DeathorbPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

/**
 * Created by Simple-Duino on 06/06/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class LobbyReturn extends BukkitRunnable {

    File f = new File("plugins/Deathorb/config.yml");
    YamlConfiguration cf = YamlConfiguration.loadConfiguration(f);

    @Override
    public void run() {
        String[] lobby = cf.get("returnLobby").toString().replace(" ", "").split(",");
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.getInventory().clear();
            p.teleport(p.getLocation().getWorld().getSpawnLocation());
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(lobby[0]);

            p.sendPluginMessage(DeathorbPlugin.getPlugin(DeathorbPlugin.class), "BungeeCord", out.toByteArray());
        }

        Bukkit.unloadWorld(cf.get("main.main-worldName").toString(), false);
        File saveMap = new File(cf.get("main.save-worldName").toString());
        File restoreMap = new File(cf.get("main.main-worldName").toString());
        try {
            FileUtils.deleteDirectory(restoreMap);
            restoreMap.mkdir();
            FileUtils.copyDirectory(saveMap, restoreMap);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }
}
