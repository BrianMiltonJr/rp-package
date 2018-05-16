package com.johnwillikers.rp.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.JSONObject;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.MmoFormulas;
import com.johnwillikers.rp.callbacks.MySqlCallback;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.ToonBaseLocal;
import com.johnwillikers.rp.Utilities;
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
			if(args.length==2 && !(args[0].equalsIgnoreCase("xp"))) {
				String query = "SELECT id FROM players WHERE first LIKE '" + args[0] + "' AND last LIKE '" + args[1] + "';";
				DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/mmo)", new MySqlCallback() {
					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								@SuppressWarnings("unused")
								final int id = rs.getInt(1);
								rs.close();
								//TODO Add Gui that shows gm a player's stats
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
				if(args.length==4) {
					String query = "SELECT uuid FROM players WHERE first LIKE '" + args[1] + "' AND last LIKE '" + args[2] + "';";
					DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand (/mmo xp)", new MySqlCallback() {
	
						@Override
						public void onQueryDone(ResultSet rs) {
							try {
								if(rs.next()) {
									final String uuid = rs.getString(1);
									rs.close();
									if(Bukkit.getServer().getPlayer(uuid).isOnline()) {
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
										player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " is not online currently, pleae try again when they are online.");
									}
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
				if(args.length==2) {
					JSONObject toonData = ToonBaseLocal.readToon(player.getUniqueId().toString());
					int oldLevel = toonData.getInt("level");
					int oldXp = toonData.getInt("xp");
					int[] newLevelXp = MmoFormulas.formulateLevelUp(oldXp, oldLevel, Integer.valueOf(args[1]));
					player.sendMessage(player.getDisplayName() + " used to be level " + oldLevel + " with their xp at " + oldXp + ".");
					player.sendMessage(player.getDisplayName() + " is now level " + newLevelXp[0] + " with their new xp at " + newLevelXp[1] + ".");
					toonData.put("xp", newLevelXp[1]);
					toonData.put("level", newLevelXp[0]);
					ToonBaseLocal.updateToon(toonData, player.getUniqueId().toString());
					return true;
				}
			}
		}else if(cmd.getName().equalsIgnoreCase("item")) {
			if(args.length==1) {
				String query = "SELECT * FROM " + args[0] + "s;";
				DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "MmoCommands.onCommand(/item {item_type})", new MySqlCallback() {

					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								//Grabs the Size of the ResultSet
								rs.last();
								int size = rs.getRow();
								Core.debug(Mmo.name, "MmoCommands.onCommand(/item {item_type})", "Size of ResultSet = " + size);
								//Resets the Position of the ResultSet
								rs.beforeFirst();
								rs.next();
								//Create a new MultiDimentsional String array
								String[][] weapons = new String[size][2];
								//Load the first result by hand
								weapons[0][0] = rs.getString(1);
								weapons[0][1] = rs.getString(4);
								//Loop through the rest of the Result set and fill the rest of the Array
								int i = 1;
								while(rs.next()) {
									weapons[i][0] = rs.getString(1);
									weapons[i][1] = rs.getString(4);
									i++;
								}
								//Close the Result Set
								rs.close();
								//Build the Message
								String msg = args[0] + " Items:\n";
								for(String[] weapon : weapons) {
									msg = msg + "* " + weapon[1] + " - " + weapon[0] + "\n";
								}
								//Send the Msg to the player
								player.sendMessage(msg);
							}else {
								player.sendMessage("There are no " + finalArgs[0] + " items, create some!");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				});
				return true;
			}
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
			
			if(args[0].equalsIgnoreCase("create")) {
				String a = "";
				for(int i=6; i<args.length; i++) {
					a = a + " " + args[i];
				}
				final String name = a.substring(1);
				String query = "INSERT INTO " + args[1] + "s ( type, material, strength, agility, dexterity, name ) VALUE ( '" + args[1] + "', '" + args[2] + "', " + args[3] + ", " + args[4] + ", " + args[5] + ", '" 
						+ name + "' );";
				DbHandler.executeUpdate(query, Mmo.name);
				String idQuery = "SELECT id FROM " + args[1] + "s WHERE name='" + name + "' AND strength=" + args[3] + ";";
				DbHandler.executeQuery(Mmo.plugin, idQuery, Mmo.name, "MmoCommands.onCommand (/item create)", new MySqlCallback() {

					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								int id = rs.getInt(1);
								player.sendMessage("Your item was generated successfully. It's Id = " + id);
							}else {
								player.sendMessage("Item creation was not successful. Please check your command.");
							}
						} catch (SQLException e) {
							//TODO Move this to the prior query
							Core.log(Mmo.name, Codes.ERROR.toString(), "An item was not generated successfully.");
							Core.log(Mmo.name, Codes.ERROR.toString(), "This was the command /item create " + args[1] + " " + args[2] + " " + args[3] + " " + args[4] + " " + args[5] + " " + name);
						}
					}
					
				});
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("character")) {
			int[] toonData = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
			Inventory characterInv = Bukkit.getServer().createInventory(player, 18, Utilities.ownership(player.getDisplayName()) + " Character Sheet");
			characterInv.addItem(Utilities.createGuiItem("Strength", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[4]), "Affects Melee Damage")), Material.ANVIL));
			characterInv.addItem(Utilities.createGuiItem("Agility", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Affects Ranged Damage")), Material.BOW));
			characterInv.addItem(Utilities.createGuiItem("Dexterity", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[6]), "Your Weapon Equip stat")), Material.SLIME_BALL));
			characterInv.addItem(Utilities.createGuiItem("Constitution", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[7]), "Your Health")), Material.APPLE));
			characterInv.addItem(Utilities.createGuiItem("Spirit", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[8]), "unimplemented PLACEHOLDER")), Material.GHAST_TEAR));
			characterInv.addItem(Utilities.createGuiItem("Swords", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[9]), "How proficient you are with Swords")), Material.WOOD_SWORD));
			characterInv.addItem(Utilities.createGuiItem("Shields", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[10]), "How proficient you are with Shields")), Material.SHIELD));
			characterInv.addItem(Utilities.createGuiItem("Axes", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[11]), "How proficient you are with Axes")), Material.WOOD_AXE));
			characterInv.addItem(Utilities.createGuiItem("Bows", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[12]), "How proficient you are with Bows")), Material.BOW));
			characterInv.addItem(Utilities.createGuiItem("Light Armor", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[13]), "How proficient you are with Light Armor")), Material.LEATHER_HELMET));
			characterInv.addItem(Utilities.createGuiItem("Heavy Armor", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[14]), "How proficient you are with Heavy Armor")), Material.IRON_HELMET));
			characterInv.addItem(Utilities.createGuiItem("Stat Points", new ArrayList<String>(Arrays.asList(toonData[2] + " remaining")), Material.SEEDS));
			characterInv.addItem(Utilities.createGuiItem("Skill Points", new ArrayList<String>(Arrays.asList(toonData[3] + " remaining")), Material.ARROW));
			characterInv.addItem(Utilities.createGuiItem("Exit", new ArrayList<String>(Arrays.asList("Exit this screen")), Material.REDSTONE_BLOCK));
			player.openInventory(characterInv);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("level")) {
			if(args[0].equalsIgnoreCase("stat") || args[0].equalsIgnoreCase("stats")) {
				int[] toonData = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
				Inventory statsInv = Bukkit.getServer().createInventory(player, 9, "Stat Points: " + toonData[2]);
				statsInv.addItem(Utilities.createGuiItem("Strength", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[4]), "Affects Melee Damage")), Material.ANVIL));
				statsInv.addItem(Utilities.createGuiItem("Agility", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Affects Ranged Damage")), Material.BOW));
				statsInv.addItem(Utilities.createGuiItem("Dexterity", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[6]), "Your Weapon Equip stat")), Material.SLIME_BALL));
				statsInv.addItem(Utilities.createGuiItem("Constitution", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[7]), "Your Health")), Material.APPLE));
				statsInv.addItem(Utilities.createGuiItem("Spirit", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[8]), "unimplemented PLACEHOLDER")), Material.GHAST_TEAR));
				statsInv.addItem(Utilities.createGuiItem("Exit", new ArrayList<String>(Arrays.asList("Exit this screen")), Material.REDSTONE_BLOCK));
				player.openInventory(statsInv);
				return true;
			}
			if(args[0].equalsIgnoreCase("skill") || args[0].equalsIgnoreCase("skills")) {
				int[] toonData = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
				Inventory skillsInv = Bukkit.getServer().createInventory(player, 9, "Skill Points: " + toonData[3]);
				skillsInv.addItem(Utilities.createGuiItem("Swords", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[9]), "How proficient you are with Swords")), Material.WOOD_SWORD));
				skillsInv.addItem(Utilities.createGuiItem("Shields", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[10]), "How proficient you are with Shields")), Material.SHIELD));
				skillsInv.addItem(Utilities.createGuiItem("Axes", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[11]), "How proficient you are with Axes")), Material.WOOD_AXE));
				skillsInv.addItem(Utilities.createGuiItem("Bows", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[12]), "How proficient you are with Bows")), Material.BOW));
				skillsInv.addItem(Utilities.createGuiItem("Light Armor", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[13]), "How proficient you are with Light Armor")), Material.LEATHER_HELMET));
				skillsInv.addItem(Utilities.createGuiItem("Heavy Armor", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[14]), "How proficient you are with Heavy Armor")), Material.IRON_HELMET));
				skillsInv.addItem(Utilities.createGuiItem("Exit", new ArrayList<String>(Arrays.asList("Exit this screen")), Material.REDSTONE_BLOCK));
				player.openInventory(skillsInv);
				return true;
			}
			return false;
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
