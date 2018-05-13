package com.johnwillikers.rp.listeners;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.MySqlCallback;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.enums.Codes;

public class EntryListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if(Core.dataMethod.equalsIgnoreCase("mysql")) {
			String query = "SELECT * FROM players WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "';";
			DbHandler.executeQuery(Core.plugin, query, Core.name, new MySqlCallback() {

				@Override
				public void onQueryDone(ResultSet rs) {
					try {
						if(!rs.next()){
							Core.log(Core.name, Codes.ENTRYLISTENER.toString(), "This is the first time " + e.getPlayer().getDisplayName() + " has joined the server, starting new character process.");
							e.getPlayer().chat("/ae356784901ldnvld0-083lwe");
							
						}else{
							//MySql way of updating the player
							String query = "SELECT first, last FROM players WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "';";
							DbHandler.executeQuery(Core.plugin, query, Core.name, new MySqlCallback() {

								@Override
								public void onQueryDone(ResultSet rs) {
									try {
									String[] player = {rs.getString(1), rs.getString(2)};
									Core.debug(Core.name, "EntryListener.onPlayerJoin", "Attempting to update " + e.getPlayer().getDisplayName() + "'s PlayerBase Entry [MySql]");
									Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " just joined, attempting to update them in the PlayerBase [MySQL]");
									String query = "UPDATE players SET last_ip = '" + e.getPlayer().getAddress().toString() + "', player_name = '" + e.getPlayer().getDisplayName() + "' WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "';";
									DbHandler.executeUpdate(query, Core.name);
									Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " has been updated in the PlayerBase [MySql]");
									Core.debug(Core.name, "EntryListener.onPlayerJoin", "PlayerBase Entry successfully updated. [MySql]");
									e.getPlayer().setDisplayName(player[1] + " " + player[2]);
									}catch(SQLException e) {
										e.printStackTrace();
									}
								}
								
							});
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				
			});
			// Checks to see if the player exists in the playerbase. If they do they are updated and if they aren't they are registered
			
		}else {
			if(!PlayerBase.exists(e.getPlayer())){
				Core.log(Core.name, Codes.ENTRYLISTENER.toString(), "This is the first time " + e.getPlayer().getDisplayName() + " has joined the server, starting new character process.");
				e.getPlayer().chat("/ae356784901ldnvld0-083lwe");
				
			}else{
				try {
					String[] player = PlayerBase.getPlayerInfo(e.getPlayer());
					Core.debug(Core.name, "EntryListener.onPlayerJoin", "Attempting to update " + e.getPlayer().getDisplayName() + "'s PlayerBase Entry");
					Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " just joined, attempting to update their PlayerBase [Json]");
					PlayerBase.updatePlayer(e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().toString(), e.getPlayer().getName().toString());
					Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " has been updated in the PlayerBase [Json]");
					Core.debug(Core.name, "EntryListener.onPlayerJoin", "PlayerBase Entry successfully updated.");
					e.getPlayer().setDisplayName(player[1] + " " + player[2]);
				} catch (IOException e1) {
					String[] player = PlayerBase.getPlayerInfo(e.getPlayer());
					e1.printStackTrace();
					Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " Entry has failed updating.");
					Core.debug(Core.name, "EntryListener.onPlayerJoin", "PlayerBase Entry failed updating. Check stacktrace above.");
				}
			}
		}
	}
}
