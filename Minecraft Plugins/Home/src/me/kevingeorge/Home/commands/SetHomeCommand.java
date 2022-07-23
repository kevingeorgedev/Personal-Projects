package me.kevingeorge.Home.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kevingeorge.Home.Home;

public class SetHomeCommand implements CommandExecutor{

	private final Home plugin;
	
	public SetHomeCommand(Home plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			UUID id = p.getUniqueId();
			Location location = p.getLocation();
			
			if(plugin.hasHome(id)) {
				p.sendMessage(ChatColor.RED + "Previous home has been overridden.");
			}
			
			plugin.addHome(id, location);
			
			p.sendMessage(ChatColor.GREEN + "Home set.");
			
			plugin.getFiles().addHome(id, location);
			
			
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		return false;
	}
	
}
