package me.kevingeorge.Teleport.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kevingeorge.Teleport.Teleport;

public class TeleportDenyCommand implements CommandExecutor {
	private final Teleport plugin;
	
	public TeleportDenyCommand(Teleport plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player deny = (Player) sender;
			UUID denyId = deny.getUniqueId();
			if(plugin.getQue().containsValue(denyId)) {
				plugin.removeVal(denyId);
				deny.sendMessage(ChatColor.YELLOW + "All teleportation requests " + ChatColor.RESET + ChatColor.RED + "denied.");
			} else {
				deny.sendMessage(ChatColor.RED + "You have no ongoing requests.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		return false;
	}
}
