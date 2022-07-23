package me.kevingeorge.BedCount.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kevingeorge.BedCount.BedCount;

public class PlayerListeners implements Listener{
	
	private final BedCount plugin;
	
	public PlayerListeners(BedCount plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		plugin.removePlayerCount();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		plugin.addPlayerCount();
	}
	
	@EventHandler
	public void playerBedEvent(PlayerBedEnterEvent e) {
		
		if(e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) {
			plugin.setDay(false);
			plugin.addBedCount();
			int bedCount    = plugin.getBedCount();
			double playerCount = plugin.getPlayerCount();
			int majority = (int) Math.round(playerCount/2.0);
			
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Players in bed " + ChatColor.RESET + ChatColor.RED + bedCount + "/" + majority);
			
			if(bedCount >= majority) {
				plugin.setDay(true);
				for(World world : Bukkit.getServer().getWorlds()) {
					world.setTime(0);
				}
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Time set to dawn.");
			}
		}
	}
	
	@EventHandler
	public void playerLeaveBedEvent(PlayerBedLeaveEvent e) {
		plugin.removeBedCount();
		int bedCount    = plugin.getBedCount();
		double playerCount = plugin.getPlayerCount();
		int majority = (int) Math.round(playerCount/2.0);
		if(!plugin.getDay()) {
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Players in bed " + ChatColor.RESET + ChatColor.RED + bedCount + "/" + majority);
		}
	}
}
