package com.johnwillikers.rp.listeners;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.PlayerBaseMySql;
import com.johnwillikers.rp.enums.Codes;

public class EntryListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if(Core.dataMethod.equalsIgnoreCase("mysql")) {
			// Checks to see if the player exists in the playerbase. If they do they are updated and if they aren't they are registered
			if(!PlayerBaseMySql.exists(e.getPlayer())){
				Core.log(Core.name, Codes.ENTRYLISTENER.toString(), "This is the first time " + e.getPlayer().getDisplayName() + " has joined the server, starting new character process.");
				e.getPlayer().chat("/ae356784901ldnvld0-083lwe");
				
			}else{
				//MySql way of updating the player
				String[] player = PlayerBaseMySql.getPlayerInfo(PlayerBaseMySql.getPlayerId(e.getPlayer().getUniqueId().toString()));
				Core.debug(Core.name, "EntryListener.onPlayerJoin", "Attempting to update " + e.getPlayer().getDisplayName() + "'s PlayerBase Entry [MySql]");
				Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " just joined, attempting to update them in the PlayerBase [MySQL]");
				String[] pData = {"last_ip", e.getPlayer().getAddress().toString(), "player_name", e.getPlayer().getDisplayName()};
				PlayerBaseMySql.updatePlayer(pData, e.getPlayer());
				Core.log(Core.name, Codes.ENTRYLISTENER.toString(), player[1] + " " + player[2] + " has been updated in the PlayerBase [MySql]");
				Core.debug(Core.name, "EntryListener.onPlayerJoin", "PlayerBase Entry successfully updated. [MySql]");
				e.getPlayer().setDisplayName(player[1] + " " + player[2]);
			}
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
