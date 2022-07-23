package me.kevingeorge.Point;

import org.bukkit.plugin.java.JavaPlugin;

import me.kevingeorge.Point.commands.SetDirectionCommand;

public class Point extends JavaPlugin{

	public void onEnable() {
		getCommand("setdirection").setExecutor(new SetDirectionCommand(this));
	}
}
