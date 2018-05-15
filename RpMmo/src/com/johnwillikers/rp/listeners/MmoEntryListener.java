package com.johnwillikers.rp.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONObject;

import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.callbacks.MySqlCallback;
import com.johnwillikers.rp.ToonBaseLocal;

public class MmoEntryListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e){
		final Player player = e.getPlayer();
		String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
		DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoEntryListener.onPlayerJoin", new MySqlCallback() {

			@Override
			public void onQueryDone(ResultSet rs) {
				try {
					if(rs.next()) {
						final int id = rs.getInt(1);
						rs.close();
						ToonBaseLocal.storeToon(id, player.getUniqueId().toString());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		JSONObject toonData = ToonBaseLocal.readToon(player.getUniqueId().toString());
		ToonBaseLocal.uploadToon(toonData);
		ToonBaseLocal.deleteToon(e.getPlayer().getUniqueId().toString());
	}
}
