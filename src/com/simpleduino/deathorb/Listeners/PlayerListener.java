package com.simpleduino.deathorb.Listeners;

import com.simpleduino.deathorb.Coutdown.ArrowGiver;
import com.simpleduino.deathorb.Coutdown.ExplodeCooldown;
import com.simpleduino.deathorb.Coutdown.LightningCooldown;
import com.simpleduino.deathorb.DeathorbPlugin;
import com.simpleduino.deathorb.Events.GameStartEvent;
import com.simpleduino.deathorb.Events.TeamWinEvent;
import com.simpleduino.deathorb.Players.DeathorbPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Simple-Duino on 04/08/2016.
 * Copyrights Simple-Duino, all rights reserved
 */
public class PlayerListener implements Listener {

    public static boolean gameStarted = false;
    public static HashMap<Player, DeathorbPlayer> playerLink = new HashMap<>();
    private ArrayList<Player> diedPlayers = new ArrayList<>();
    public HashMap<Player, ItemStack[]> diedPlayerInventories = new HashMap<>();
    private HashMap<Player, Integer> diedPlayerLevels = new HashMap<>();
    public HashMap<Player, Float> diedPlayerExps = new HashMap<>();
    public static ArrayList<Player> lightning_cooldown = new ArrayList<>();
    public static ArrayList<Player> explode_cooldown = new ArrayList<>();
    private File f = new File("plugins/Deathorb/winpoints.yml");
    private YamlConfiguration cf = YamlConfiguration.loadConfiguration(f);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        playerLink.put(e.getPlayer(), new DeathorbPlayer(e.getPlayer()));

        if(Bukkit.getServer().getMaxPlayers()==Bukkit.getServer().getOnlinePlayers().size())
        {
            Bukkit.getPluginManager().callEvent(new GameStartEvent(e.getPlayer()));
        }
        for(Player p1 : Bukkit.getOnlinePlayers())
        {
            p1.sendMessage(net.md_5.bungee.api.ChatColor.RED + e.getPlayer().getName()+ net.md_5.bungee.api.ChatColor.YELLOW+" a rejoint la partie");
            p1.sendMessage(net.md_5.bungee.api.ChatColor.DARK_AQUA+"("+Integer.toString(Bukkit.getOnlinePlayers().size())+"/"+Integer.toString(Bukkit.getServer().getMaxPlayers())+") joueur(s) avant le démarrage de la partie");
        }
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e)
    {
        playerLink.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRegen(EntityRegainHealthEvent e)
    {
        if(e.getEntityType().equals(EntityType.PLAYER) && e.getRegainReason()== EntityRegainHealthEvent.RegainReason.SATIATED)
        {
            Player p = (Player)e.getEntity();
            if(p.getHealth()>=20)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        if(gameStarted) {
            e.setKeepInventory(true);
            diedPlayers.add(e.getEntity());
            diedPlayerLevels.put(e.getEntity(), e.getEntity().getLevel());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e)
    {
        if(gameStarted)
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        final Player p = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(DeathorbPlugin.getPlugin(DeathorbPlugin.class), new Runnable()
        {

            @Override
            public void run() {
                if(PlayerListener.playerLink.containsKey(p) && playerLink.get(p).hasTeam()) {
                    p.teleport(playerLink.get(p).getTeam().getSpawnPoint());
                    if(diedPlayers.contains(p))
                    {
                        p.setLevel(diedPlayerLevels.get(p));
                        diedPlayerLevels.remove(p);
                        diedPlayers.remove(p);
                    }
                }
            }
        }, 1L);

    }

    @EventHandler
    public void onPlayerLevelUp(PlayerLevelChangeEvent e)
    {
        if(gameStarted) {
            ItemStack sword;
            net.minecraft.server.v1_8_R3.ItemStack sword_nms;
            NBTTagCompound compound;
            NBTTagList modifiers;
            NBTTagCompound damage;
            if (e.getNewLevel() >= 1) {
                switch (e.getNewLevel()) {
                    case 1:
                        e.getPlayer().getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
                        break;
                    case 2:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.1));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 3:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.2));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 4:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.3));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 5:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.4));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 6:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.5));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 7:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.6));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 8:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.7));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 9:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.8));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                    case 10:
                        sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        sword_nms = CraftItemStack.asNMSCopy(sword);
                        compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                        modifiers = new NBTTagList();
                        damage = new NBTTagCompound();
                        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                        damage.set("Name", new NBTTagString("generic.attackDamage"));
                        damage.set("Amount", new NBTTagDouble(7.9));
                        damage.set("Operation", new NBTTagInt(0));
                        damage.set("UUIDLeast", new NBTTagInt(894654));
                        damage.set("UUIDMost", new NBTTagInt(2872));
                        modifiers.add(damage);
                        compound.set("AttributeModifiers", modifiers);
                        sword_nms.setTag(compound);
                        sword = CraftItemStack.asBukkitCopy(sword_nms);
                        e.getPlayer().getInventory().setItem(0, sword);
                        break;
                }
            }
            if (e.getNewLevel() == 5) {
                e.getPlayer().getInventory().setItem(8, new ItemStack(Material.SULPHUR, 1));
            }
            if (e.getNewLevel() == 10) {
                e.getPlayer().getInventory().setItem(7, new ItemStack(Material.GOLD_AXE, 1));
            }
            if (e.getNewLevel() == 11) {
                sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                ItemMeta swordMeta = sword.getItemMeta();
                swordMeta.setDisplayName("Ultimate Sword");
                sword.setItemMeta(swordMeta);
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                sword_nms = CraftItemStack.asNMSCopy(sword);
                compound = (sword_nms.hasTag()) ? sword_nms.getTag() : new NBTTagCompound();
                modifiers = new NBTTagList();
                damage = new NBTTagCompound();
                damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
                damage.set("Name", new NBTTagString("generic.attackDamage"));
                damage.set("Amount", new NBTTagDouble(8));
                damage.set("Operation", new NBTTagInt(0));
                damage.set("UUIDLeast", new NBTTagInt(894654));
                damage.set("UUIDMost", new NBTTagInt(2872));
                modifiers.add(damage);
                compound.set("AttributeModifiers", modifiers);
                sword_nms.setTag(compound);
                sword = CraftItemStack.asBukkitCopy(sword_nms);
                e.getPlayer().getInventory().setItem(0, sword);
            }
            if (e.getNewLevel() == 15) {
                e.getPlayer().getInventory().setItem(1, new ItemStack(Material.BOW, 1));
                e.getPlayer().getInventory().setItem(4, new ItemStack(Material.ARROW, 1));
                Bukkit.getScheduler().scheduleSyncRepeatingTask(DeathorbPlugin.getPlugin(DeathorbPlugin.class), new ArrowGiver(e.getPlayer().getUniqueId().toString()), 20L, 20L * 6);
            }
            if (e.getNewLevel() == 18) {
                e.getPlayer().getInventory().setItem(2, new ItemStack(Material.DIAMOND_PICKAXE, 1));
            }
            if (e.getNewLevel() == 20) {
                ItemStack potion = new ItemStack(Material.POTION, 1, (short)16000);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10*20, 1, false, true), true);
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 2, false, true), true);
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10*20, 0, false, true), true);
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 5*20, 2, false, true), true);
                potion.setItemMeta(potionMeta);
                e.getPlayer().getInventory().setItem(6, potion);
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e)
    {
        if(gameStarted) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_PICKAXE)) {
                if (e.getBlock().getType().equals(Material.STAINED_GLASS)) {
                    if (e.getBlock().getData() != 0) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerKillEntity(EntityDamageByEntityEvent e)
    {
        if(gameStarted) {
            Bukkit.broadcastMessage("started");
            if (e.getEntity() instanceof Player) {
                Bukkit.broadcastMessage("player");
                if (e.getEntity().isDead() || ((Player) e.getEntity()).getHealth()-e.getDamage() <= 0) {
                    Bukkit.broadcastMessage("dead");
                    if (e.getDamager() instanceof Player) {
                        Bukkit.broadcastMessage("damager player");
                        Player damager = (Player) e.getDamager();
                        damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 2, false, false));
                        damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 0, false, false));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithGoldAxe(PlayerInteractEvent e)
    {
        final Player executor = e.getPlayer();
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(e.getPlayer().getInventory().getItemInHand().getType().equals(Material.GOLD_AXE))
            {
                if(!lightning_cooldown.contains(executor)) {
                    for (org.bukkit.entity.Entity entity : e.getPlayer().getNearbyEntities(5, 5, 5)) {
                        if (entity instanceof Player) {
                            Player p = (Player) entity;
                            if (playerLink.get(p).hasTeam() && playerLink.get(e.getPlayer()).hasTeam()) {
                                if (!playerLink.get(p).getTeam().getName().equalsIgnoreCase(playerLink.get(e.getPlayer()).getTeam().getName())) {
                                    p.getWorld().strikeLightning(p.getLocation());
                                }
                            }
                        }
                    }

                    lightning_cooldown.add(e.getPlayer());
                    new LightningCooldown(executor, 10).runTaskTimer(DeathorbPlugin.getPlugin(DeathorbPlugin.class), 1L, 20L);
                }
                else
                {
                    executor.sendMessage(ChatColor.RED + "Vous devez attendre 10 secondes avant de pouvoir réutiliser ce pouvoir");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithGunPowder(PlayerInteractEvent e)
    {
        final Player executor = e.getPlayer();
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(e.getPlayer().getInventory().getItemInHand().getType().equals(Material.SULPHUR))
            {
                if(!explode_cooldown.contains(executor))
                {
                    for(Entity entity : e.getPlayer().getNearbyEntities(5, 5, 5))
                    {
                        if(entity instanceof Player)
                        {
                            Player p = (Player)entity;
                            p.getWorld().createExplosion(p.getLocation(), 4F, false);
                        }
                    }

                    explode_cooldown.add(executor);
                    new ExplodeCooldown(executor, 10).runTaskTimer(DeathorbPlugin.getPlugin(DeathorbPlugin.class), 1L, 20L);

                }
                else
                {
                    executor.sendMessage(ChatColor.RED + "Vous devez attendre 10 secondes avant de pouvoir réutiliser ce pouvoir");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        if(gameStarted && playerLink.get(e.getPlayer()).hasTeam()) {
            Player p = e.getPlayer();
            Location playerWinLoc = new Location(Bukkit.getWorld(cf.get(playerLink.get(p).getTeam().getName() + ".world").toString()), Double.parseDouble(cf.get(playerLink.get(p).getTeam().getName() + ".x").toString()), Double.parseDouble(cf.get(playerLink.get(p).getTeam().getName() + ".y").toString()), Double.parseDouble(cf.get(playerLink.get(p).getTeam().getName() + ".z").toString()));
            if (Math.floor(p.getLocation().getX()) == Math.floor(playerWinLoc.getX()) && Math.floor(p.getLocation().getY()) == Math.floor(playerWinLoc.getY())) {
                Bukkit.getPluginManager().callEvent(new TeamWinEvent(playerLink.get(p).getTeam()));
            }
        }
    }

}
