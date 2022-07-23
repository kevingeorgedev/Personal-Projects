package me.kevingeorge.Wild;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import me.kevingeorge.Wild.commands.WildCommand;
import me.kevingeorge.Wild.listeners.PlayerListeners;


public class Wild extends JavaPlugin{
	private List<UUID> que;
	
	@Override
	public void onEnable() {
		this.que = new ArrayList<>();
		
		getCommand("wild").setExecutor(new WildCommand(this));
		getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
	}
	
	public void onDisable() {
		
	}
	
	public List<UUID> getQue(){
		return this.que;
	}
	
	public void addQue(UUID id) {
		this.que.add(id);
	}
	
	public void removeQue(UUID id) {
		this.que.remove(id);
	}
}
