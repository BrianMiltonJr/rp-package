package com.johnwillikers.rp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.johnwillikers.rp.Chat;
import com.johnwillikers.rp.ChatBase;
import com.johnwillikers.rp.ChatLogic;
import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener{

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
		for (Player r : Bukkit.getOnlinePlayers()){
			Location rLoc = r.getLocation();
			Location pLoc = e.getPlayer().getLocation();
			int rLocX = rLoc.getBlockX();
			int rLocZ = rLoc.getBlockZ();
			int pLocX = pLoc.getBlockX();
			int pLocZ = pLoc.getBlockZ();
			Core.debug(Chat.name, Codes.DEBUG + "ChatListener.onAsyncPlayerChat", "SenderX: " + pLocX + " SenderZ: " + pLocZ);
			Core.debug(Chat.name, Codes.DEBUG + "ChatListener.onAsyncPlayerChat", r.getDisplayName() + "X: " + pLocX + " " + r.getDisplayName() + "Z: " + pLocZ);
			int distanceXRaw = rLocX - pLocX;
			int distanceZRaw = rLocZ - pLocZ;
			int distanceX = Math.abs(distanceXRaw);
			int distanceZ = Math.abs(distanceZRaw);
			Core.debug(Chat.name, Codes.DEBUG + "ChatListener.onAsyncPlayerChat", "xDistance: " + distanceX + " ZDistance: " + distanceZ);
			int distance = ChatLogic.determineDistance(ChatBase.getPlayerDistance(e.getPlayer()));
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatListener.onAsyncPlayerChat", "Distance: " + distance);
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatListener.onAsyncPlayerChat", "Whisper: " + ChatLogic.whisperDistance + " Talk: " + ChatLogic.talkDistance + " Yell: " 
					+ ChatLogic.yellDistance);
			if(distanceX > distance || distanceZ > distance){
				Core.debug(Chat.name, Codes.DEBUG + "ChatListener.onAsyncPlayerChat", "Removing" + r.getDisplayName());
				e.getRecipients().remove(r);
			}
		}
		Core.debug(Chat.name, Codes.DEBUG + "ChatListener.onAsyncPlayerChat", "Recipients List: " + e.getRecipients().toString());
		int distance = ChatLogic.determineDistance(ChatBase.getPlayerDistance(e.getPlayer()));
		String msg = e.getMessage();
		if(distance == ChatLogic.whisperDistance){
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatListener.onAsyncPlayerChat", "AQUA");
			String newMsg = ChatColor.AQUA + msg;
			e.setMessage(newMsg);
		}else if(distance == ChatLogic.talkDistance){
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatListener.onAsyncPlayerChat", "GOLD");
			String newMsg = ChatColor.GOLD + msg;
			e.setMessage(newMsg);
		}else if(distance == ChatLogic.yellDistance){
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatListener.onAsyncPlayerChat", "RED");
			String newMsg = ChatColor.RED + msg;
			e.setMessage(newMsg);
		}
	}
}
