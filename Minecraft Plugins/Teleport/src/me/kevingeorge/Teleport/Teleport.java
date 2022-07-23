package me.kevingeorge.Teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kevingeorge.Teleport.commands.TeleportAcceptCommand;
import me.kevingeorge.Teleport.commands.TeleportCommand;
import me.kevingeorge.Teleport.commands.TeleportDenyCommand;
import me.kevingeorge.Teleport.listeners.PlayerListeners;

public class Teleport extends JavaPlugin{
	private HashMap<UUID, UUID> que;
	private HashMap<UUID, BukkitTask> timers;
	private List<UUID> active;
	
	@Override
	public void onEnable() {
		
		this.que = new HashMap<>();
		this.timers = new HashMap<>();
		this.active = new ArrayList<>();
		
		getCommand("tpa").setExecutor(new TeleportCommand(this));
		getCommand("tpaccept").setExecutor(new TeleportAcceptCommand(this));
		getCommand("tpdeny").setExecutor(new TeleportDenyCommand(this));
		getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
	}
	
	public void onDisable() {
		
	}
	
	public HashMap<UUID, UUID> getQue() {
		return this.que;
	}
	
	public void addQue(UUID id, UUID toId) {
		this.que.put(id, toId);
	}
	
	public void cancelQue(UUID id) {
		this.que.remove(id);
	}
	
	public boolean isQued(UUID id) {
		return this.que.containsKey(id);
	}
	public HashMap<UUID, BukkitTask> getTimers(){
		return this.timers;
	}
	public BukkitTask removeTimer(UUID id) {
		try {
			return this.timers.remove(id);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void removeVal(UUID id) {
		for(Map.Entry<UUID, UUID> element : que.entrySet()) {
			UUID value = (UUID) element.getValue();
			if(value.equals(id)) {
				//cancelQue(element.getKey());
				this.que.remove(element.getKey());
			}
		}
	}
	
	
	public UUID recentRequests(UUID id){
		for(Map.Entry<UUID, UUID> entry : que.entrySet()) {
			if(entry.getValue().equals(id)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public void addTimer(UUID id, BukkitTask task) {
		this.timers.put(id, task);
	}
	
	public List<UUID> getActive(){
		return this.active;
	}
	public void removeActive(UUID id) {
		try {
			this.active.remove(id);
		} catch (Exception e) {
			//do nothing
		}
	}
	public void addActive(UUID id) {
		this.active.add(id);
	}
}
