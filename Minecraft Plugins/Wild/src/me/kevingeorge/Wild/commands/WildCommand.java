package me.kevingeorge.Wild.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kevingeorge.Wild.Wild;

public class WildCommand implements CommandExecutor{
	private final Wild plugin;
	
	public WildCommand(Wild plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			UUID id = p.getUniqueId();
			plugin.addQue(id);
			p.sendMessage(ChatColor.RED + "Do not move or enter combat for 5 seconds.");
			
			Location loc = new Location(Bukkit.getServer().getWorld("TheBoys"), -59978, 150, -60029, 0, 0);
			loc.setY(loc.getWorld().getHighestBlockYAt(loc));
			loc.getChunk().load();
			
			new BukkitRunnable() {
				int delay = 5;
				@Override
				public void run() {
					if(plugin.getQue().contains(id)) {
						if(delay == 0) {
							p.teleport(loc);
							loc.getChunk().unload(true);
							plugin.removeQue(id);
							this.cancel();
						} else {
							p.sendMessage(ChatColor.GOLD + "Teleporting in " + delay-- + " second(s).");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Teleport cancelled.");
						plugin.removeQue(id);
						this.cancel();
					}
					
				}
				
			}.runTaskTimer(plugin, 0, 20);
		}
		
		return false;
	}
}
