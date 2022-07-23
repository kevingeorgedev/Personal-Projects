package me.kevingeorge.Teleport.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.kevingeorge.Teleport.Teleport;

public class TeleportAcceptCommand implements CommandExecutor {
	private final Teleport plugin;
	
	public TeleportAcceptCommand(Teleport plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player accept = (Player) sender;
			UUID acceptId = accept.getUniqueId();
			if(plugin.getQue().containsValue(acceptId)) {
				UUID fromId = plugin.recentRequests(acceptId);
				Player request = Bukkit.getServer().getPlayer(fromId);
				BukkitTask timer = plugin.removeTimer(fromId);
				timer.cancel();
				plugin.cancelQue(fromId);
				plugin.addActive(fromId);
				accept.sendMessage(ChatColor.GOLD + "Teleportation accepted.");
				request.sendMessage(ChatColor.GOLD + "Teleportation to " + ChatColor.RESET + ChatColor.RED + accept.getDisplayName() + 
									ChatColor.RESET + ChatColor.GOLD + " was accepted." + "\n" + ChatColor.RESET + ChatColor.RED + "Don't move or enter combat for 5 seconds.");
				
				new BukkitRunnable() {
					int delay = 5;
					@Override
					public void run() {
						if(plugin.getActive().contains(fromId)) {
							if(delay == 0) {
								Location loc = accept.getLocation();
								loc.getChunk().load();
								request.sendMessage(ChatColor.GREEN + "Teleporting...");
								accept.sendMessage(ChatColor.GREEN + "Teleporting " + ChatColor.RESET + ChatColor.BLUE + request.getDisplayName() + 
												   ChatColor.RESET + ChatColor.GREEN + " to you.");
								request.teleport(loc);
								loc.getChunk().unload(true);
								plugin.removeTimer(fromId);
								this.cancel();
							}
						} else {
							this.cancel();
						}
						delay--;
					}
					
				}.runTaskTimer(plugin, 0, 20);
			} else {
				accept.sendMessage(ChatColor.YELLOW + "You have no ongoing requests.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		return false;
	}
}
