package me.kevingeorge.Teleport.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.kevingeorge.Teleport.Teleport;

public class TeleportCommand implements CommandExecutor {
	private final Teleport plugin;
	
	public TeleportCommand(Teleport plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			UUID id = p.getUniqueId();
			
			if(args.length == 0) {
				p.sendMessage(ChatColor.RED + "Please specify player to teleport to: /tp [name]");
				return false;
			}
			Player to = Bukkit.getServer().getPlayer(args[0]);
			
			if(p == to) {
				p.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			} else if(to == null) {
				p.sendMessage(ChatColor.RED + "Couldn't locate player.");
			}
			else {
				
				if(plugin.isQued(id)) {
					plugin.cancelQue(id);
				}
				UUID toId = to.getUniqueId();
				plugin.addQue(id, toId);
				p.sendMessage(ChatColor.GOLD + "Teleportion request sent to " + ChatColor.RESET + ChatColor.RED + to.getDisplayName() + "\n" + ChatColor.RESET +
							  ChatColor.GOLD + "Teleport will expire in " + ChatColor.RESET + ChatColor.RED + "15 Seconds");
				
				to.sendMessage(ChatColor.RED + p.getDisplayName() + ChatColor.RESET + ChatColor.GOLD + " has requested to teleport to you." + ChatColor.RESET +"\n" + 
							   ChatColor.GOLD + "To accept, type " + ChatColor.RESET + ChatColor.RED + "/tpaccept" + ChatColor.RESET + ChatColor.GOLD + ".\n" + 
							   ChatColor.RESET + ChatColor.GOLD + "To decline this request, type " + ChatColor.RESET + ChatColor.RED + "/tpdeny" + ChatColor.RESET + ChatColor.GOLD + ".\n" +
							   "This request will expire after " + ChatColor.RESET + ChatColor.RED + "15 seconds" + ChatColor.RESET + ChatColor.GOLD + ".");
				plugin.addTimer(id, new BukkitRunnable() {
					int delay = 15;
					@Override
					public void run() {
						if(plugin.isQued(id)) {
							if(delay == 0) {
								p.sendMessage(ChatColor.RED + "Teleport cancelled.");
								plugin.cancelQue(id);
								plugin.removeTimer(id);
								this.cancel();
							}
						} 
						
						else {
							p.sendMessage(ChatColor.RED + "Teleport declined.");
							plugin.removeTimer(id);
							this.cancel();
						}
						delay--;
					}
				}.runTaskTimer(plugin, 0, 20));
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		return false;
	}
}
