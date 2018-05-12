package com.willikers.rp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.johnwillikers.rp.MmoBaseMySql;
import com.johnwillikers.rp.PlayerBaseMySql;

public class MmoListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		int playerId = PlayerBaseMySql.getPlayerId(player.getUniqueId().toString());
		if(!MmoBaseMySql.exists(playerId)) {
			MmoBaseMySql.createNewToon(playerId);
		}
	}
}
