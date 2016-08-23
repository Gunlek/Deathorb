package com.simpleduino.deathorb;

import com.simpleduino.deathorb.Commands.DeathorbCommandExecutor;
import com.simpleduino.deathorb.Coutdown.TimeKeeper;
import com.simpleduino.deathorb.Listeners.GameListener;
import com.simpleduino.deathorb.Listeners.PlayerListener;
import com.simpleduino.deathorb.Listeners.PluginListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class DeathorbPlugin extends JavaPlugin {

    private File f = new File("plugins/Deathorb/spawnpoints.yml");
    private File f2 = new File("plugins/Deathorb/winpoints.yml");
    private File f3 = new File("plugins/Deathorb/config.yml");

    public void onEnable()
    {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new GameListener(), this);
        if(!f.exists())
        {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!f2.exists())
        {
            f2.getParentFile().mkdirs();
            try {
                f2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!f3.exists())
        {
            f3.getParentFile().mkdirs();
            try {
                f3.createNewFile();
                YamlConfiguration cf = YamlConfiguration.loadConfiguration(f3);
                cf.set("returnLobby", "lobby");
                cf.save(f3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeKeeper(), 20L, 20L);
        this.getServer().getPluginCommand("do").setExecutor(new DeathorbCommandExecutor());

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginListener());
    }

    public void onDisable()
    {

    }

}
