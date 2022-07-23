package me.kevingeorge.BedCount;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.kevingeorge.BedCount.listeners.PlayerListeners;

public class BedCount extends JavaPlugin{
	
	private int playerCount;
	private int bedCount;
	private boolean day;
	
	@Override
	public void onEnable() {
		this.playerCount = Bukkit.getServer().getOnlinePlayers().size();
		this.bedCount = 0;
		getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void addPlayerCount() {
		this.playerCount++;
	}
	
	public void removePlayerCount() {
		this.playerCount--;
	}
	
	public void addBedCount() {
		this.bedCount++;
	}
	
	public void removeBedCount() {
		this.bedCount--;
	}
	
	public int getBedCount() {
		return bedCount;
	}

	public boolean getDay() {
		return day;
	}

	public void setDay(boolean day) {
		this.day = day;
	}
}
