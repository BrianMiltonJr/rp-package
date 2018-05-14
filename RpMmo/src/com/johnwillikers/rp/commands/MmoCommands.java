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

import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.MmoFormulas;
import com.johnwillikers.rp.MySqlCallback;
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
				if(args.length==4) {
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
				if(args.length==2) {
					JSONObject toonData = ToonBaseLocal.readToon(player.getUniqueId().toString());
					int oldLevel = toonData.getInt("level");
					int oldXp = toonData.getInt("xp");
					int[] newLevelXp = MmoFormulas.formulateLevelUp(oldXp, oldLevel, Integer.valueOf(finalArgs[3]));
					player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " used to be level " + oldLevel + " with their xp at " + oldXp + ".");
					player.sendMessage(finalArgs[1] + " " + finalArgs[2] + " is now level " + newLevelXp[0] + " with their new xp at " + newLevelXp[1] + ".");
					toonData.put("xp", newLevelXp[1]);
					toonData.put("level", newLevelXp[0]);
					ToonBaseLocal.updateToon(toonData, player.getUniqueId().toString());
					return true;
				}
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
			int[] toonData = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
			//TODO add a way to figure how to play ' in plural cases
			Inventory characterInv = Bukkit.getServer().createInventory(player, 18, player.getDisplayName() + "'s Character Sheet");
			characterInv.addItem(Utilities.createGuiItem("Strength", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[4]), "Affects Melee Damage")), Material.ANVIL));
			characterInv.addItem(Utilities.createGuiItem("Agility", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Affects Ranged Damage")), Material.BOW));
			characterInv.addItem(Utilities.createGuiItem("Dexterity", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Your Weapon Equip stat")), Material.SLIME_BALL));
			characterInv.addItem(Utilities.createGuiItem("Constitution", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Your Health")), Material.APPLE));
			characterInv.addItem(Utilities.createGuiItem("Spirit", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "unimplemented PLACEHOLDER")), Material.GHAST_TEAR));
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
				statsInv.addItem(Utilities.createGuiItem("Dexterity", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Your Weapon Equip stat")), Material.SLIME_BALL));
				statsInv.addItem(Utilities.createGuiItem("Constitution", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "Your Health")), Material.APPLE));
				statsInv.addItem(Utilities.createGuiItem("Spirit", new ArrayList<String>(Arrays.asList("Level " + String.valueOf(toonData[5]), "unimplemented PLACEHOLDER")), Material.GHAST_TEAR));
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
