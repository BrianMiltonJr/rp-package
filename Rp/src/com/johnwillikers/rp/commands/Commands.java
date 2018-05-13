package com.johnwillikers.rp.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.MySqlCallback;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.Utilities;
import com.johnwillikers.rp.conversations.EntryPrompt;
import com.johnwillikers.rp.enums.Codes;

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
				if(Core.dataMethod.equalsIgnoreCase("mysql")) {
					String query = "SELECT * FROM players WHERE first LIKE '" + args[0] + "' AND last LIKE '" + args[1] + "';";
					//Asyncronously Calls for the data and when it arrives sends it to the player
					DbHandler.executeQuery(Core.plugin, query, Core.name, new MySqlCallback() {
						@Override
						public void onQueryDone(ResultSet rs) {
							try {
								if(rs.next()) {
									String[] details = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)};
									rs.close();
									String first = details[2];
									String last = details[3];
									String playerName = details[4];
									String gender;
									if(Integer.valueOf(details[5]) == 0){
										gender = "Female";
									}else{
										gender = "Male";
									}
									String originalIp = details[6];
									String lastIp = details[7];
									String createdAt = details[8];
									String updatedAt = details[9];
									String reply = ChatColor.GOLD + "--------------------\n" + ChatColor.GREEN + "Name: " + ChatColor.AQUA + first + " " + last + "\n" + ChatColor.GREEN + "Real Name: "
											+ ChatColor.AQUA + playerName + "\n" + ChatColor.GREEN +"Gender: " + ChatColor.AQUA + gender + "\n" + ChatColor.GREEN + "Original Ip: " + ChatColor.AQUA + originalIp +
											"\n" + ChatColor.GREEN + "Last Ip: " + ChatColor.AQUA + lastIp + ChatColor.GOLD + "\n" + ChatColor.GREEN + "Created At: " + ChatColor.AQUA + createdAt + "\n" + ChatColor.GREEN + "Updated At: " + ChatColor.AQUA + updatedAt + ChatColor.GOLD + "\n --------------------";
									player.sendMessage(reply);
								}else {
									player.sendMessage(args[0] + " " + args[1] + " does not exist.");
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					});
					return true;
				}else {
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
			}else if(cmd.getName().equalsIgnoreCase("toggle_debug")) {
				if(sender.isOp()) {
					if(Core.debugState == "true") {
						Core.debugState = "false";
						sender.sendMessage("Debug State has been disabled");
						Core.log(Core.name, Codes.DEBUG.toString(), "Debug State has been disabled");
					}else {
						Core.debugState = "true";
						sender.sendMessage("Debug State has been enabled");
						Core.log(Core.name, Codes.DEBUG.toString(), "Debug State has been enabled.");
					}
					return true;
				}else {
					sender.sendMessage("You must be a Server Operator to use this command.");
					return true;
				}
			}else if(cmd.getName().equalsIgnoreCase("rp")) {
				if(args[0].equalsIgnoreCase("reload")) {
					Core.log(Core.name, Codes.RELOAD.toString(), "Reloading " + Core.name + " Settings");
					Core.settings = Utilities.getSettings();
					Core.debugState = Core.settings[1];
					Core.townName = Core.settings[2];
					Core.dataMethod = Core.settings[3];
					sender.sendMessage("You have updated " + Core.name + " settings");
					return true;
				}
			}
		}
		return false;
		
	}
}
