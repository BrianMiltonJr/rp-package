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
import com.johnwillikers.rp.KarmaLogic;
import com.johnwillikers.rp.MySqlCallback;
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
		final String[] finalArgs = args;
		if(cmd.getName().equalsIgnoreCase("0e812e08h02v8he0182vhe1")){
			String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
			DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand(/0e812e08h02v8he0182vhe1)", new MySqlCallback() {

				@Override
				public void onQueryDone(ResultSet rs) {
					try {
						if(rs.next()) {
							final int id = rs.getInt(1);
							rs.close();
							String query = "INSERT INTO karma ( player_id, karma ) VALUES ( " + id + ", 0 );";
							DbHandler.executeUpdate(query, Karma.name);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
						
				}		
			});
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
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("karma")){
			if((args.length == 2 && !args[0].equalsIgnoreCase("report"))){					
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
															
														String[] reportIds = {"derp"};
														try {
															for(int i=0;rs.next();i++) {
																reportIds[i] = rs.getString(1);
															}
															rs.close();
															player.sendMessage(KarmaLogic.buildLookUpMessage(karma, playerName, reportIds));
														} catch (SQLException e) {
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
							}else {
								player.sendMessage(args[0] + " " + args[1] + " does not exist.");;
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}	
				});
				return true;
			}
			if(args[0].equalsIgnoreCase("report")) {
				String query = "SELECT * FROM reports WHERE id=" + args[1] + ";";
				DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand (/karma report)", new MySqlCallback() {

					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								final String[] reportData = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
								rs.close();
								KarmaLogic.sendReportMessage(reportData, player);
							}else {
								player.sendMessage("Report " + args[1] + " does not exist.");
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("gamemaster")) {
			if(args[0].equalsIgnoreCase("add")) {
				if(args.length==5) {
					String query = "SELECT uuid FROM players WHERE first LIKE '" + args[1] + "' AND last LIKE '" + args[2] + "';";
					DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand (/gamemaster add)", new MySqlCallback() {
	
						@Override
						public void onQueryDone(ResultSet rs) {
							try {
								if(rs.next()) {
									final String uuid = rs.getString(1);
									rs.close();
									String query = "SELECT * FROM gamemasters WHERE uuid='" + uuid +"';";
									DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand (/gamemaster add)", new MySqlCallback() {

										@Override
										public void onQueryDone(ResultSet rs) {
											try {
												if(!rs.next()) {
													rs.close();
													String query = "INSERT INTO gamemasters ( uuid, name ) VALUES ( '" + uuid + "', '" + finalArgs[3] + " " + finalArgs[4] + "' );";
													DbHandler.executeUpdate(query, Karma.name);
													player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " has been made a Gamemaster named " + finalArgs[3] + " " + finalArgs[4]);
												}else {
													player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " is already a GameMaster");
												}
											} catch (SQLException e) {
												e.printStackTrace();
											}
										}
									});	
								}else {
									player.sendMessage(finalArgs[1] + " " + finalArgs[1] + " doesn't exist.");
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
					});
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				if(args.length==3) {
					String query = "SELECT * FROM gamemasters WHERE name='" + args[1] + " " + args[2] +"';";
					DbHandler.executeQuery(Karma.plugin, query, Karma.name, "KarmaCommands.onCommand (/gamemaster remove)", new MySqlCallback() {
	
						@Override
						public void onQueryDone(ResultSet rs) {
							try {
								if(rs.next()) {
									String query = "DELETE FROM gamemasters WHERE id=" + rs.getInt(1) + ";";
									rs.close();
									DbHandler.executeUpdate(query, Karma.name);
									player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " is no longer a Game Master.");
								}else {
									player.sendMessage("No Game Master named " + finalArgs[1] + " " + finalArgs[2] + " exists.");
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
					});
					return true;
				}
			}
		}
		return false;
	}	
}