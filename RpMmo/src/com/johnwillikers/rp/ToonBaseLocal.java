package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class ToonBaseLocal {
	
	public static String dir = "ToonBase/";
	
	
	public static void storeToon(int playerId, String playerUuid) {
		String query = "SELECT * FROM toons WHERE player_id=" + playerId + ";";
		DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "ToonBaseLocal.storeToon", new MySqlCallback() {

			@Override
			public void onQueryDone(ResultSet rs) {
				try {
					if(rs.next()) {
						final int[] toon = {rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)};
						rs.close();
						String query = "SELECT strength, agility, dexterity, constitution, spirit FROM stats WHERE toon_id=" + toon[0] + ";";
						DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "ToonBaseLocal.storeToon", new MySqlCallback() {

							@Override
							public void onQueryDone(ResultSet rs) {
								try {
									if(rs.next()) {
										final int[] stats = {rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5)};
										rs.close();
										String query = "SELECT sword, shield, axe, bow, light_armor, heavy_armor FROM skills WHERE toon_id=" + toon[0] + ";";
										DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "ToonBaseLocal.storeToon", new MySqlCallback() {

											@Override
											public void onQueryDone(ResultSet rs) {
												try {
													if(rs.next()) {
														final int[] skills = {rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)};
														rs.close();
														writeToon(toon, stats, skills, playerUuid);
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
	}
	
	public static void uploadToon(JSONObject toonDataJson, int playerId) {
		final int[] toonData = getToonDataIntArray(toonDataJson);
		final String toonsQuery = "INSERT INTO toons SET xp = " + toonData[0] + ", level = " + toonData[1] + ", stat_points = " + toonData[2] + ", skill_points = "  + toonData[3] + " WHERE toon_id=" + playerId + ";";
		final String statsQuery = "INSERT INTO stats SET strength = " + toonData[4] + ", agility = " + toonData[5] + ", dexterity = " + toonData[6] + ", constitution = " + toonData[7] + ", spirit = " + toonData[8] + " WHERE toon_id=" + toonData[15] + ";";
		final String skillsQuery = "INSERT INTO skills SET sword = " + toonData[9] + ", shield = " + toonData[10] + ", axe = " + toonData[11] + ", bow = " + toonData[12] + ", light_armor = " + toonData[13] + ", heavy_armor = " + toonData[14] + " WHERE toon_id=" + toonData[15]+ ";";
		DbHandler.executeUpdate(toonsQuery, Mmo.name);
		DbHandler.executeUpdate(statsQuery, Mmo.name);
		DbHandler.executeUpdate(skillsQuery, Mmo.name);
	}
	
	public static void deleteToon(int playerId) {
		File toonFile = new File(ToonBaseLocal.dir + playerId + ".json");
		if(toonFile.exists())
			toonFile.delete();
	}
	
	public static int[] getToonDataIntArray(JSONObject toonDataJson) {
		final int[] toonData = {toonDataJson.getInt("xp"), toonDataJson.getInt("level"), toonDataJson.getInt("stat_points"), toonDataJson.getInt("skill_points"), toonDataJson.getInt("strength"),
				toonDataJson.getInt("agility"), toonDataJson.getInt("dexterity"), toonDataJson.getInt("constitution"), toonDataJson.getInt("spirit"), toonDataJson.getInt("sword"),
				toonDataJson.getInt("shield"), toonDataJson.getInt("axe"), toonDataJson.getInt("bow"), toonDataJson.getInt("light_armor"), toonDataJson.getInt("heavy_armor"), toonDataJson.getInt("toon_id")};
		return toonData;
	}
	
	public static JSONObject readToon(String playerUuid) {
		File toonFile = new File(ToonBaseLocal.dir + playerUuid + ".json");
		if(!toonFile.exists())
			return null;
		try {
			FileReader fr = new FileReader(toonFile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject toonData = new JSONObject(json);
			return toonData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeToon(int[] toon, int[] stats, int[] skills, String playerUuid) {
		JSONObject toonData = new JSONObject();
		toonData.put("toon_id", toon[0]);
		if(toon.length == 5) {
			toonData.put("xp", toon[2]);
			toonData.put("level", toon[3]);
			toonData.put("stat_points", toon[4]);
			toonData.put("skill_points", toon[5]);
		}
		if(toon.length == 4) {
			toonData.put("xp", toon[0]);
			toonData.put("level", toon[1]);
			toonData.put("stat_points", toon[2]);
			toonData.put("skill_points", toon[3]);
		}
		toonData.put("strength", stats[0]);
		toonData.put("agility", stats[1]);
		toonData.put("dexterity", stats[2]);
		toonData.put("constitution", stats[3]);
		toonData.put("spirit", stats[4]);
		toonData.put("sword", skills[0]);
		toonData.put("shield", skills[1]);
		toonData.put("axe", skills[2]);
		toonData.put("bow", skills[3]);
		toonData.put("light_armor", skills[4]);
		toonData.put("heavy_armor", skills[5]);
		File toonFile = new File(ToonBaseLocal.dir + playerUuid + ".json");
		if(toonFile.exists())
			toonFile.delete();
		try {
			PrintWriter pr = new PrintWriter(toonFile);
			pr.println(toonData.toString());
			pr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void createPath() {
		File dir = new File(ToonBaseLocal.dir);
		if(!dir.exists())
			dir.mkdirs();
	}
}
