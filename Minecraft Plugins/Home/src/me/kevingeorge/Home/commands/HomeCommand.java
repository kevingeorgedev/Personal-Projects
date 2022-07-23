package me.kevingeorge.Home.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kevingeorge.Home.Home;

public class HomeCommand implements CommandExecutor {
	private final Home plugin;
	
	public HomeCommand(Home plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			UUID id = p.getUniqueId();
			
			if(!plugin.hasHome(id)) {
				p.sendMessage(ChatColor.RED + "You don't have a home set.");
			} else {
				plugin.addQue(id);
				p.sendMessage(ChatColor.RED + "Do not move or enter combat for 5 seconds");
				plugin.gethome(id).getChunk().load();
				new BukkitRunnable() {
					int delay = 5;
					@Override
					public void run() {
						if(plugin.isQued(id)) {
							if(delay == 0) {
								p.sendMessage(ChatColor.GOLD + "Teleporting.");
								p.teleport(plugin.gethome(id));
								plugin.gethome(id).getChunk().unload(true);
								plugin.cancelQue(id);
								this.cancel();
							} else {
								p.sendMessage(ChatColor.GOLD + "Teleporting in " + delay-- + " second(s).");
							}
						} else {
							p.sendMessage(ChatColor.RED + "Teleportation canceled.");
							this.cancel();
						}
					}
				}.runTaskTimer(plugin, 0, 20);
				
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		
		return false;
	}
	
}
