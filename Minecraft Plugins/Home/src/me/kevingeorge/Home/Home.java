package me.kevingeorge.Home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import me.kevingeorge.Home.commands.HomeCommand;
import me.kevingeorge.Home.commands.SetHomeCommand;
import me.kevingeorge.Home.listeners.PlayerListeners;

public class Home extends JavaPlugin{
	
	private HashMap<UUID, Location> homes;
	private HomeFiles files;
	private List<UUID> que;
	
	@Override
	public void onEnable() {
		this.homes = new HashMap<>();
		this.files = new HomeFiles(this);
		this.que = new ArrayList<>();
		this.files.init();
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		getCommand("sethome").setExecutor(new SetHomeCommand(this));
		getCommand("home").setExecutor(new HomeCommand(this));
		getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
	}
	
	public void onDisable() {
		this.files.terminate();
	}
	
	public void addHome(UUID id, Location location) {
		this.homes.put(id, location);
	}
	
	public Location gethome(UUID id) {
		return this.homes.get(id);
	}
	
	public boolean hasHome(UUID id) {
		return this.homes.containsKey(id);
	}
	
	public HashMap<UUID, Location> getHomes(){
		return homes;
	}
	
	public HomeFiles getFiles() {
		return files;
	}
	
	public void addQue(UUID id) {
		this.que.add(id);
	}
	
	public void cancelQue(UUID id) {
		try {
			this.que.remove(id);
		} catch (Exception e) {
			//Do nothing
		}
	}
	
	public boolean isQued(UUID id) {
		return this.que.contains(id);
	}
	
}
