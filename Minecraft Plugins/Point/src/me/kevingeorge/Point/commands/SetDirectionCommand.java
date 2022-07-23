package me.kevingeorge.Point.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kevingeorge.Point.Point;
import org.bukkit.ChatColor;

public class SetDirectionCommand implements CommandExecutor{
	
	private final Point plugin;
	
	public SetDirectionCommand(Point plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			int size = args.length;
			switch(size) {
				case 0: 
					p.sendMessage(ChatColor.RED + "Incorrect command usage, do: '/setdirection [x] [z]'");
					break;
				case 1: 
					p.sendMessage(ChatColor.RED + "Incorrect command usage, do: '/setdirection [x] [z]'");
					break;
				case 2: 
					try {
						setDirection(p, Double.parseDouble(args[0]), Double.parseDouble(args[1]));
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "Your inputs for [x] and [z] must be numerical.");
					}
					break;
				default:
					p.sendMessage(ChatColor.YELLOW + "Using first two arguments as [x] and [z].");
					try {
						setDirection(p, Double.parseDouble(args[0]), Double.parseDouble(args[1]));
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "Your inputs for [x] and [z] must be numerical.");
					}
					break;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
		}
		return false;
	}
	
	public void setDirection(Player p, double toX, double toZ) {
		double deltaZ = toZ - p.getLocation().getZ();
		double deltaX = toX - p.getLocation().getX();
		float angle = (float) Math.atan(deltaZ/deltaX);
		Location pos = p.getLocation();
		pos.setPitch(0);
		angle = (float) (angle * (180/Math.PI));
		if(deltaZ > 0 && deltaX > 0) {
			//first quadrant south-east
			pos.setYaw(-90 + angle);
			
		} else if(deltaZ < 0 && deltaX > 0) {
			//second quadrant or north-east
			pos.setYaw(-90 + angle);
			
		} else if(deltaZ < 0 && deltaX < 0) {
			//third quadrant or north-west
			pos.setYaw(180 - (90 - angle));
			
		} else if(deltaZ > 0 && deltaX < 0) {
			//fourth quadrant or south-west
			pos.setYaw(90 + angle);
			
		} 
		
		p.sendMessage(ChatColor.GREEN + "Pointing at: " + toX + ", " + toZ);
		p.teleport(pos);
	}
}
