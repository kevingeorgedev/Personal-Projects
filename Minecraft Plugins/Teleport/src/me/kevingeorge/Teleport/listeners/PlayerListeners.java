package me.kevingeorge.Teleport.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kevingeorge.Teleport.Teleport;

public class PlayerListeners implements Listener {
	
	private final Teleport plugin;
	
	public PlayerListeners(Teleport plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
			Player p = e.getPlayer();
			UUID id = p.getUniqueId();
			if(plugin.isQued(id)) plugin.cancelQue(id);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			UUID id = p.getUniqueId();
			if(plugin.isQued(id)) {
				plugin.cancelQue(id);
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		UUID id = e.getPlayer().getUniqueId();
		
		if(plugin.getQue().containsKey(id)) plugin.cancelQue(id);
		
		if(plugin.getTimers().containsKey(id)) plugin.removeTimer(id).cancel();
		
		if(plugin.getActive().contains(id)) plugin.removeActive(id);
	}
}
