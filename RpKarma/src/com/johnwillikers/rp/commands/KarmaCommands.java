package com.johnwillikers.rp.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.KarmaBase;
import com.johnwillikers.rp.KarmaLogic;
import com.johnwillikers.rp.MySqlCallback;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.conversations.NegateConfirmPrompt;
import com.johnwillikers.rp.enums.Codes;

public class KarmaCommands implements CommandExecutor{
	
	Karma plugin;
	public static String answer = null;
	
	public KarmaCommands(Karma instance){
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		final Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("0e812e08h02v8he0182vhe1")){
			if(Core.dataMethod.equalsIgnoreCase("mysql")) {
				String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
				DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/0e812e08h02v8he0182vhe1)", new MySqlCallback() {

					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								final int id = rs.getInt(1);
								rs.close();
								String query = "INSERT INTO karma ( player_id, karma) VALUES ( " + id + ", 0 );";
								DbHandler.executeUpdate(query, Karma.name);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
			}else {
				KarmaBase.writeKarma(player.getUniqueId().toString(), 0, null);
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("negate")){
			if(args.length == 0){
				return false;
			}else{
				String desc = String.join(" ", args);
				int length = args[0].length() + args[1].length() + args[2].length() + 3;
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "Length = " + length);
				desc = desc.substring(length);
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "desc = " + desc);
				if(Core.dataMethod.equalsIgnoreCase("mysql")) {
					
					final String[] data = {args[0], args[1], args[2], desc};
					
					String query = "SELECT id, uuid FROM players WHERE first LIKE '" + args[0] +"' AND last LIKE '" + args[1]+ "';";
					DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/negate)", new MySqlCallback() {

						@Override
						public void onQueryDone(ResultSet rs) {
							
							try {
								if(rs.next()) {
									final int id = rs.getInt(1);
									final String uuid = rs.getString(2);
									rs.close();
									String query = "SELECT * FROM karma WHERE player_id=" + id + ";";
									DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/negate)", new MySqlCallback() {

										@Override
										public void onQueryDone(ResultSet rs) {
											try {
												if(rs.next()) {
													String msg = "Is this information correct?\n " +
															 "Name: " + data[0] + " " + data[1] + "\n" +
															 "Offense: " + data[2] + "\n" + 
															 "Description: " + data[3] +
															 "\n[y/n]";
													Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "User exists, starting converation with new NegateConfirmPrompt()");
													//Confirm with Gm that the information is correct
													Conversation conv = plugin.factory.withFirstPrompt(new NegateConfirmPrompt(msg, data[2], uuid, data[3], player, args[0] + " " + args[1])).thatExcludesNonPlayersWithMessage("Get away, or be BURNED MORTAL.").buildConversation(((Conversable) sender));
													conv.begin();
												}else {
													player.sendMessage(data[0] + " " + data[1] + " does not exist.");
												}
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										
									});
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					});
					return true;
				}else {
					//Get offender's UUID
					Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "First: " + args[0] + " Last: " + args[1]);
					String[] uuidRaw = PlayerBase.checkMasteFile(args[0] + "_" + args[1]);
					Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "uuidRaw = " + String.join(" ", uuidRaw));
					if(uuidRaw[0] == String.valueOf(0)){
						player.sendMessage("[Karma] You have entered an invalid name.");
						return true;
					}else{
						Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "UUID = " + uuidRaw[1]);
						String uuid = uuidRaw[1];
						//Check if offender exists
						if(KarmaBase.exists(uuid)){
							String msg = "Is this information correct?\n " +
									 "Name: " + args[0] + " " + args[1] + "\n" +
									 "Offense: " + args[2] + "\n" + 
									 "Description: " + desc +
									 "[y/n]";
							Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "User exists, starting converation with new NegateConfirmPrompt()");
							//Confirm with Gm that the information is correct
							Conversation conv = plugin.factory.withFirstPrompt(new NegateConfirmPrompt(msg, args[2], uuid, desc, player, args[0] + " " + args[1])).thatExcludesNonPlayersWithMessage("Get away, or be BURNED MORTAL.").buildConversation(((Conversable) sender));
							conv.begin();
							return true;
						}
					}
				}
			}
		}else if(cmd.getName().equalsIgnoreCase("karma")){
			if(!(args.length <= 1)){
				if(Core.dataMethod.equalsIgnoreCase("mysql")) {
					
					String query = "SELECT id FROM players WHERE first LIKE '" + args[0] + "' AND last LIKE '" + args[1] + "';";
					final String playerName = args[0] + " " + args[1];
					
					DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/karma)", new MySqlCallback() {

						@Override
						public void onQueryDone(ResultSet rs) {
							try {
								if(rs.next()) {
									final int id =  rs.getInt(1);
									rs.close();
									String query = "SELECT karma FROM karma WHERE player_id=" + id + ";";
									DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/karma)", new MySqlCallback() {

										@Override
										public void onQueryDone(ResultSet rs) {
											try {
												if(rs.next()) {
													final int karma = rs.getInt(1);
													rs.close();
													String query = "SELECT id FROM reports WHERE player_id=" + id + ";";
													DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/karma)", new MySqlCallback() {

														@Override
														public void onQueryDone(ResultSet rs) {
															
															String[] reportIds = null;
															try {
																for(int i=0;rs.next();i++) {
																	reportIds[i] = rs.getString(1);
																}
																rs.close();
															} catch (SQLException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
															KarmaLogic.buildLookUpMessage(karma, playerName, reportIds);
														}
														
													});
												}
											} catch (SQLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}
										
									});
								}else {
									player.sendMessage(args[0] + " " + args[1] + " does not exist.");;
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					});
					
				}else {
					/* Deprecated Json Code
					String[] payload = PlayerBase.checkMasteFile(args[0] + "_" + args[1]);
					if(Integer.valueOf(payload[0]) == 1){
						if(KarmaBase.exists(payload[1])){
							//JSONObject kfile = KarmaBase.getKarmaInfo(payload[1]);
							String msg = KarmaLogic.lookUp(payload[1]);
							player.sendMessage(ChatColor.GOLD + "Name: " + args[0] + " " + args[1] + "\n" + msg);
						}else{
							player.sendMessage("Are you sure " + args[0] + " " + args[1] + " is spelled correctly?");
						}
					}*/
				}
				return true;
			}
		}
		return false;
	}	
}