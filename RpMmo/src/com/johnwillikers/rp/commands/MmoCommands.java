package com.johnwillikers.rp.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.MmoFormulas;
import com.johnwillikers.rp.MySqlCallback;
import com.johnwillikers.rp.ToonBaseLocal;
import com.johnwillikers.rp.Weapon;

public class MmoCommands implements CommandExecutor {
	
	Mmo plugin;
	
	public MmoCommands(Mmo instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player player = (Player) sender;
		final String[] finalArgs = args;
		
		if(cmd.getName().equalsIgnoreCase("mmo")){
			if(args.length==2) {
				String query = "SELECT id FROM players WHERE first LIKE '" + args[0] + "' AND last LIKE '" + args[1] + "';";
				DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/mmo)", new MySqlCallback() {
					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								@SuppressWarnings("unused")
								final int id = rs.getInt(1);
								rs.close();
								//TODO Add Gui that shows player stats
							}else {
								player.sendMessage(finalArgs[0] + " " + finalArgs[1] + " does not exist.");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				return true;
			}
			if(args[0].equalsIgnoreCase("xp")) {
				String query = "SELECT uuid FROM players WHERE first LIKE '" + args[1] + "' AND last LIKE '" + args[2] + "';";
				DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/mmo xp)", new MySqlCallback() {

					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								final String uuid = rs.getString(1);
								rs.close();
								JSONObject toonData = ToonBaseLocal.readToon(uuid);
								int oldLevel = toonData.getInt("level");
								int oldXp = toonData.getInt("xp");
								int[] newLevelXp = MmoFormulas.formulateLevelUp(oldXp, oldLevel, Integer.valueOf(finalArgs[3]));
								player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " used to be level " + oldLevel + " with their xp at " + oldXp + ".");
								player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " is now level " + newLevelXp[0] + " with their new xp at " + newLevelXp[1] + ".");
								toonData.put("xp", newLevelXp[1]);
								toonData.put("level", newLevelXp[0]);
								ToonBaseLocal.updateToon(toonData, uuid);
							}else {
								player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " does not exist.");
							}	
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				});
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("item")) {
			if(args.length==2) {
				if(args[0].equalsIgnoreCase("sword")) {
					new Weapon("swords", Integer.valueOf(args[1]), player);
				}else if(args[0].equalsIgnoreCase("axe")) {
					new Weapon("axes", Integer.valueOf(args[1]), player);
				}else if(args[0].equalsIgnoreCase("bow")) {
					new Weapon("bows", Integer.valueOf(args[1]), player);
				}else if(args[0].equalsIgnoreCase("shield")) {
					new Weapon("shields", Integer.valueOf(args[1]), player);
				}else {
					player.sendMessage("Item type does not exist");
				}
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("character")) {
			String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
			DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/character)", new MySqlCallback() {
				@Override
				public void onQueryDone(ResultSet rs) {
					try {
						if(rs.next()) {
							@SuppressWarnings("unused")
							final int id = rs.getInt(1);
							rs.close();
							//TODO Insert Gui Method to render your character Sheet
						}else {
							player.sendMessage(finalArgs[0] + " " + finalArgs[1] + " does not exist.");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			});
			return true;
		}else if(cmd.getName().equalsIgnoreCase("level")) {
			String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
			DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/level)", new MySqlCallback() {

				@Override
				public void onQueryDone(ResultSet rs) {
					try {
						if(rs.next()) {
							@SuppressWarnings("unused")
							final int id = rs.getInt(1);
							rs.close();
							//TODO Add Gui method for leveling up skills and stats
						}
					} catch (NumberFormatException | SQLException e) {
						e.printStackTrace();
					}
				}
				
			});
		}else if(cmd.getName().equalsIgnoreCase("03f9147b09743nf09n71")) {
			String query = "SELECT id FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
			DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/03f9147b09743nf09n71)", new MySqlCallback() {

				@Override
				public void onQueryDone(ResultSet rs) {
					
					try {
						if(rs.next()) {
							final int id = rs.getInt(1);
							String query = "SELECT * FROM toons WHERE player_id=" + id + ";";
							DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/03f9147b09743nf09n71)", new MySqlCallback() {

								@Override
								public void onQueryDone(ResultSet rs) {
									
									try {
										if(!rs.next()) {
											String toonsQuery = "INSERT INTO toons ( player_id, xp, level, stat_points, skill_points ) VALUES ( " + id + ", 0, 1, 3, 5 );";
											DbHandler.executeUpdate(toonsQuery, Mmo.name);
											String getNewToonIdQuery = "SELECT id FROM toons WHERE player_id=" + id + ";";
											DbHandler.executeQuery(Mmo.plugin, getNewToonIdQuery, Mmo.name, "MmoCommands.onCommand (/03f9147b09743nf09n71)", new MySqlCallback() {

												@Override
												public void onQueryDone(ResultSet rs) {
													try {
														if(rs.next()) {
															final int toonId = rs.getInt(1);
															rs.close();
															String statsQuery = "INSERT INTO stats ( toon_id, strength, agility, dexterity, constitution, spirit ) VALUES ( " + toonId + ", 1, 1, 1, 5, 1 );";
															String skillsQuery = "INSERT INTO skills ( toon_id, sword, shield, axe, bow, light_armor, heavy_armor ) VALUES ( " + toonId + ", 0, 0, 0, 0, 0, 0 );";
															DbHandler.executeUpdate(statsQuery, Mmo.name);
															DbHandler.executeUpdate(skillsQuery, Mmo.name);
															ToonBaseLocal.storeToon(id, player.getUniqueId().toString());
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
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			});
			return true;
		}
		return false;
		
	}
}
