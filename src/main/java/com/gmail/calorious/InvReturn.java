package com.gmail.calorious;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.calorious.util.FormatUtil;

public class InvReturn extends JavaPlugin implements Listener {
    private Map<UUID, List<ItemStack>> itemmap = new HashMap<UUID, List<ItemStack>>();
    @Override
    public void onEnable() {
	getLogger().info("InvReturn v" + getDescription().getVersion() + " has been enabled.");
	saveDefaultConfig();
	getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
	getLogger().info("InvReturn v" + getDescription().getVersion() + " has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if(cmd.getName().equalsIgnoreCase("invreturn")) {
	    if(sender instanceof Player) {
		Player p = (Player) sender;
		if(sender.hasPermission("invreturn.use")) {
		    if(itemmap.containsKey(p.getUniqueId())) {
			returnItems(p, itemmap.get(p.getUniqueId()));
			return true;
		    }
		    if(!(itemmap.containsKey(p.getUniqueId()))) {
			sender.sendMessage(FormatUtil.format(getConfig().getString("message-noinvfound"))
				.replace("{player}", p.getName()));
		    }
		}
		if(!(sender.hasPermission("invreturn.use"))) {
		    sender.sendMessage(FormatUtil.format(getConfig().getString("message-nopermission")
			    .replace("{permission}", "invreturn.use").replace("{player}", sender.getName())));
		    return true;
		}
	    }
	    if(!(sender instanceof Player)) {
		sender.sendMessage(FormatUtil.format(getConfig().getString("message-notplayer")));
		return true;
	    }
	}
	return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
	if(e.getEntity() instanceof Player) {
	    Player p = e.getEntity();
	    if(p.hasPermission("invreturn.use")) {
		itemmap.put(p.getUniqueId(), e.getDrops());
	    }
	}
    }

    private void returnItems(Player p, List<ItemStack> items) {
	if(getConfig().getBoolean("clearinventory")) {
	    p.getInventory().clear();
	}
	for(ItemStack item : items) {
	    if(p.getInventory().firstEmpty() == -1) {
		p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
	    }
	    p.getInventory().addItem(item);
	}
	p.sendMessage(FormatUtil.format(getConfig().getString("message-success")));
    }
}
