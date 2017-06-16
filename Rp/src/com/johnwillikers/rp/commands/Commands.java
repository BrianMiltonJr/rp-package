package com.johnwillikers.rp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.Utilities;
import com.johnwillikers.rp.conversations.EntryPrompt;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	
	Core plugin;
	
	public Commands(Core instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("ae356784901ldnvld0-083lwe")){
			Conversation conv = plugin.factory.withFirstPrompt(new EntryPrompt()).thatExcludesNonPlayersWithMessage("Get away, or be BURNED MORTAL.").buildConversation(((Conversable) sender));
			conv.begin();
			return true;
		}else if(cmd.getName().equalsIgnoreCase("player")){
			if(args.length > 1 || args.length == 2){
				Core.debug(Core.name, "Commands.onCommand player", "name provided " + args[0] + " " + args[1]);
				String[] data = PlayerBase.checkMasteFile(args[0] + "_" + args[1]);
				if(data.length > 1){
					if(Integer.valueOf(data[0]) == 1){
						Core.debug(Core.name, "Commands.onCommand player", "passing " + data[1] + " to PlayerBase.getPlayerInfo");
						String[] info = PlayerBase.getPlayerInfo(Bukkit.getPlayer(Utilities.returnUUID(data[1])));
						if(Integer.valueOf(info[0]) == 1){
							String first = info[1];
							String last = info[2];
							String playerName = info[3];
							String gender;
							if(Integer.valueOf(info[4]) == 0){
								gender = "Female";
							}else{
								gender = "Male";
							}
							Core.debug(Core.name, "Commands.onCommand player", "The ip is " + info[5] + " before assignment.");
							String originalIp = info[5];
							String lastIp = info[6];
							Core.debug(Core.name, "Commands.onCommand player", "Sending to sender that player does not exist.");
							String reply = ChatColor.GOLD + "--------------------\n" + ChatColor.GREEN + "Name: " + ChatColor.AQUA + first + " " + last + "\n" + ChatColor.GREEN + "Real Name: "
							+ ChatColor.AQUA + playerName + "\n" + ChatColor.GREEN +"Gender: " + ChatColor.AQUA + gender + "\n" + ChatColor.GREEN + "Original Ip: " + ChatColor.AQUA + originalIp +
							"\n" + ChatColor.GREEN + "Last Ip: " + ChatColor.AQUA + lastIp + ChatColor.GOLD + "--------------------";
							player.sendMessage(reply);
							return true;
						}else{
							Core.debug(Core.name, "Commands.onCommand player", "Sending to sender that player does not exist. [Within Second check of if user exists]");
							player.sendMessage(args[0] + " " + args[1] + " does not exist");
							return true;
						}
						
					}else{
						Core.debug(Core.name, "Commands.onCommand player", "Sending to sender that player does not exist. [Within First check of if user exists.");
						player.sendMessage(args[0] + " " + args[1] + " does not exist");
						return true;
					}
				}
			}
		}
		return false;
		
	}
}
