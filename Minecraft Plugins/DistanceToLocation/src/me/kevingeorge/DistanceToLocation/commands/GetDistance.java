package me.kevingeorge.DistanceToLocation.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kevingeorge.DistanceToLocation.DistanceTo;
import org.bukkit.ChatColor;

public class GetDistance implements CommandExecutor{
	
	private final DistanceTo plugin;
	
	public GetDistance(DistanceTo plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			int size = args.length;
			switch(size) {
				case 2:
					try {
						double toX = Double.parseDouble(args[0]);
						double toZ = Double.parseDouble(args[1]);
						Location to = new Location(p.getWorld(), toX, p.getLocation().getY(), toZ);
						int distance = (int) p.getLocation().distance(to);
						p.sendMessage(ChatColor.GREEN + "(x: " + (int) toX + ", z: " + (int) toZ + ") is about " + 
									  ChatColor.RESET + ChatColor.AQUA + distance + ChatColor.RESET + ChatColor.GREEN + " blocks away.");
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "Your inputs for [x] and [z] must be numerical.");
					}
					break;
				case 3:
					try {
						double toX = Double.parseDouble(args[0]);
						double toY = Double.parseDouble(args[1]);
						double toZ = Double.parseDouble(args[2]);
						Location to = new Location(p.getWorld(), toX, toY, toZ);
						int distance = (int) p.getLocation().distance(to);
						p.sendMessage(ChatColor.GREEN + "(x: " + (int) toX + ", y: " + (int) toY + ", z: " + (int) toZ + ") is about " + 
								      ChatColor.RESET + ChatColor.AQUA + distance + ChatColor.RESET + ChatColor.GREEN + " blocks away.");
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "Your inputs for [x], [y], and [z] must be numerical.");
					}
					break;
				default:
					p.sendMessage(ChatColor.RED + "Incorrect command usage, do: '/getdistance [x] [z]' or /getdistance [x] [y] [z]");
					break;
			}
		}
		return false;
	}
	
}
