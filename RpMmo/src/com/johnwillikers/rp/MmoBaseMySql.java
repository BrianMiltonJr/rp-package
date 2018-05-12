package com.johnwillikers.rp;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.johnwillikers.rp.Utilities;

public class MmoBaseMySql {
	
	static int[] defaultSkills = {0, 0, 0, 0, 0, 0};
	static int[] defaultStats = {1, 1, 1, 5, 1};
	static int[] startingPoints = {5, 3};
	
	public static void createTables() {
		String toonsTableQuery = "CREATE TABLE IF NOT EXISTS`" + Core.db + "`.`toons` ( `id` INT NOT NULL AUTO_INCREMENT , `player_id` INT NOT NULL , `xp` INT NOT NULL , `level` INT NOT NULL , `stat_points` INT NOT NULL , `skill_points` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String statsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`stats` ( `id` INT NOT NULL AUTO_INCREMENT , `toon_id` INT NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , `constitution` INT NOT NULL , `spirit` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String skillsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`skills` ( `id` INT NOT NULL AUTO_INCREMENT , `toon_id` INT NOT NULL , `sword` INT NOT NULL , `shield` INT NOT NULL , `axe` INT NOT NULL , `bow` INT NOT NULL , `light_armor` INT NOT NULL , `heavy_armor` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String swordsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`swords` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String shieldsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`shields` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String axesTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`axes` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;"; 
		String bowsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`bows` ( `id` INT NOT NULL AUTO_INCREMENT , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;"; 
		Utilities.executeUpdate(toonsTableQuery, Mmo.name);
		Utilities.executeUpdate(statsTableQuery, Mmo.name);
		Utilities.executeUpdate(skillsTableQuery, Mmo.name);
		Utilities.executeUpdate(swordsTableQuery, Mmo.name);
		Utilities.executeUpdate(shieldsTableQuery, Mmo.name);
		Utilities.executeUpdate(axesTableQuery, Mmo.name);
		Utilities.executeUpdate(bowsTableQuery, Mmo.name);
	}
	
	public static boolean exists(int player_id){
		String query = "SELECT * FROM toons WHERE player_id=" + player_id + ";";
		ResultSet rs = Utilities.executeQuery(query, Mmo.name);
		try {
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static int getToonId(int playerId) {
		String query = "SELECT id FROM toons WHERE player_id=" + playerId + ";";
		ResultSet rs = Utilities.executeQuery(query, Mmo.name);
		try {
			if(rs.next()) {
				int id = rs.getInt(1);
				rs.close();
				return id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -4000;
	}
	
	public static int[] getSkills(int toonId) {
		ResultSet rs = getMmoTable(toonId, "skills");
		try {
			if(rs.next()) {
				int[] skills = {rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8)};
				rs.close();
				return skills;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static int[] getStats(int toonId) {
		ResultSet rs = getMmoTable(toonId, "stats");
		try {
			if(rs.next()) {
				int[] stats = {rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7)};
				rs.close();
				return stats;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int[] getToon(int toonId) {
		ResultSet rs = getMmoTable(toonId, "toons");
		try {
			if(rs.next()) {
				int[] toon = {rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)};
				rs.close();
				return toon;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet getWeaponData(int weaponId, String table) {
		String query = "SELECT * FROM " + table + " WHERE id=" + weaponId + ";";
		return Utilities.executeQuery(query, Mmo.name);
	}
	
	public static ResultSet getMmoTable(int toonId, String table) {
		String query;
		if(table.equalsIgnoreCase("toons")) {
			query = "SELECT * FROM " + table + " WHERE id=" + toonId + ";";
		}else{
			query = "SELECT * FROM " + table + " WHERE toon_id=" + toonId + ";";
		}
		return Utilities.executeQuery(query, Mmo.name);
	}
	
	/**
	 * 
	 * @param toonId The id in toons Table
	 * @param table the name of the table (toons, stats, skills)
	 * @param data payload with column name and new value in {0=CN, 1=V | 2=CN, 3=V}}
	 */
	public static void updateMmoTable(int toonId, String table, String[] data) {
		String query = "UPDATE " + table + " SET ";
		for(int i = 0; i<(data.length-1); i++){
			query = query + data[i] + " = ";
			i++;
			if(i<data.length-1) {
				query = query  + data[i] + ", ";
			}
			if(i == data.length-1) {
				query = query + data[i] + " ";
			}
		}
		if(table.equals("toons")) {
			query = query + "WHERE id=" + toonId + ";";
		}else {
			query = query + "WHERE toon_id=" + toonId + ";";
		}
		
		Utilities.executeUpdate(query, Mmo.name);
	}
	
	public static void createNewToon(int playerId) {
		createToon(playerId);
		int toonId = getToonId(playerId);
		createSkill(toonId);
		createStat(toonId);
	}
	
	public static void createSkill(int toonId) {
		String query = "INSERT INTO skills ( toon_id, sword, shield, axe, bow, light_armor, heavy_armor ) VALUES ( " + toonId + ", " + defaultSkills[0] + ", " + defaultSkills[1] + ", " + defaultSkills[2] + ", " + defaultSkills[3] + ", " + defaultSkills[4] + ", " + defaultSkills[5] + " );";
		Utilities.executeUpdate(query, Mmo.name);
	}
	
	public static void createStat(int toonId) {
		String query = "INSERT INTO stats ( toon_id, strength, agility, dexterity, constitution, spirit ) VALUES ( " + toonId + ", " + defaultStats[0] + ", " + defaultStats[1] + ", " + defaultStats[2] + ", " + defaultStats[3] + ", " + defaultStats[4] + " );";
		Utilities.executeUpdate(query, Mmo.name);
	}
	
	public static void createToon(int playerId) {
		String query = "INSERT INTO toons ( player_id, xp, level, stat_points, skill_points ) VALUE ( " + playerId + ", 0, 1, " + startingPoints[0] + ", " + startingPoints[1] +" );";
		Utilities.executeUpdate(query, Mmo.name);
	}
}