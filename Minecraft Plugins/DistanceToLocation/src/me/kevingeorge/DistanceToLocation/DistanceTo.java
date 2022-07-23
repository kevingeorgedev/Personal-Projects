package me.kevingeorge.DistanceToLocation;

import org.bukkit.plugin.java.JavaPlugin;

import me.kevingeorge.DistanceToLocation.commands.GetDistance;

public class DistanceTo extends JavaPlugin{
	
	@Override
	public void onEnable() {
		getCommand("getdistance").setExecutor(new GetDistance(this));
	}
}
