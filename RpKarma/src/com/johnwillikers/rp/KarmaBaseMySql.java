package com.johnwillikers.rp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class KarmaBaseMySql {
	
	public static String[] getInfo(Player p) {
		String uuid = p.getUniqueId().toString();
		String karmaQuery = "SELECT * FROM karma WHERE uuid='" + uuid + "';";
		String reportsIdQuery = "SELECT id FROM reports WHERE uuid='" + uuid + "';";
		ResultSet karmaResult = executeQuery(karmaQuery);
		String karma;
		try {
			if(karmaResult.next()) {
				karma = karmaResult.getString(3);
				karmaResult.close();
				Core.debug(Karma.name, "KarmaBaseMySql.getInfo", "karma = " + karma);
				String[] reportIds = {karma};
				ResultSet reportsResult = executeQuery(reportsIdQuery);
				int i = 1;
				while(reportsResult.next()) {
					String d = reportsResult.getString(1);
					Core.debug(Karma.name, "KarmaBaseMySql.getInfo", "i = " + i);
					Core.debug(Karma.name, "KarmaBaseMySql.getInfo", "Report Id = " + d);
					Core.debug(Karma.name, "KarmaBaseMySql.getInfo", "Before Appending reportIds = " + reportIds[0]);
					reportIds[i] = d;
					Core.debug(Karma.name, "KarmaBaseMySql.getInfo", "After Appending reportIds = " + reportIds[0] + reportIds[1]);
					i++;
				}
				karmaResult.close();
				reportsResult.close();
				return reportIds;
			}else {
				String[] derp = null;
				return derp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] derp = null;
		return derp;
	}
	
	public static void createTables() {
		String gamemastersTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db +"`.`gamemasters` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String karmaTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`karma` ( `id` INT NOT NULL AUTO_INCREMENT , `player_id` INT NOT NULL , `karma` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String reportsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`reports` ( `id` INT NOT NULL AUTO_INCREMENT , `gm_id` INT NOT NULL , `player_id` INT NOT NULL , `body` VARCHAR(200) NOT NULL , `actions` INT NOT NULL , `date` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		executeUpdate(gamemastersTableQuery);
		executeUpdate(karmaTableQuery);
		executeUpdate(reportsTableQuery);
	}
	
	public static void complain(Player gm, int player_id, String[] data) {
		int gmId = getGmId(gm);
		Core.debug(Karma.name, "KarmaBaseMySql.complain", "gmId = " + gmId);
		Core.debug(Karma.name, "KarmaBaseMySql.complain", "uuid = " + player_id);
		Core.debug(Karma.name, "KarmaBaseMySql.complain", "data[0] = " + data[0]);
		Core.debug(Karma.name, "KarmaBaseMySql.complain", "data[1] = " + data[1]);
		Core.debug(Karma.name, "KarmaBaseMySql.complain", "data[2] = " + data[2]);
		String query = "INSERT INTO reports ( gm_id, player_id, body, actions, date ) VALUES ( " + gmId + ", " + player_id + 
				", '" + data[0] + "', " + data[1] + ", '" + data[2] + "' )";
		executeUpdate(query);
	}
	
	public static String getGmName(int id) {
		String query = "SELECT name FROM gamemasters WHERE id=" + id + ";";
		ResultSet rs = executeQuery(query);
		try {
			if(rs.next()) {
				String name = rs.getString(3);
				rs.close();
				return name;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	public static int getGmId(Player p) {
		String query = "SELECT id FROM gamemasters WHERE uuid='" + p.getUniqueId() + "';";
		ResultSet rs = executeQuery(query);
		try {
			if(rs.next()) {
				int id = rs.getInt(1);
				rs.close();
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -4000;
	}
	
	public static boolean exists(int player_id){
		String query = "SELECT * FROM karma WHERE player_id=" + player_id + ";";
		ResultSet rs = executeQuery(query);
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
	
	public static void createKarma(Player p) {
		String query = "INSERT INTO karma (player_id, karma) VALUES ( " + PlayerBaseMySql.getPlayerId(p.getUniqueId().toString()) + ", 0);";
		executeUpdate(query);
		
	}
	
	public static int getKarma(int player_id) {
		String query = "SELECT * FROM karma WHERE player_id='" + player_id + "';";
		ResultSet rs = executeQuery(query);
		try {
			if(rs.next()) {
				int karma = rs.getInt(3);
				rs.close();
				return karma;
			}else {
				return -4000;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -4000;
		}
	}
	
	public static void updateKarma(int player_id, int karma) {
		String query = null;
		int currentKarma = getKarma(player_id);
		Core.debug(Karma.name, "KarmaBaseMySql.updateKarma", "currentKarma = " + currentKarma);
		if(!(currentKarma == -4000)) {
			query = "UPDATE karma SET karma=" + (currentKarma + karma) + " WHERE player_id='" + player_id + "';";
			executeUpdate(query);
		}
	}
	
	public static ResultSet executeQuery(String query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Core.driver);
			Statement stmt = conn.createStatement();
			Core.debug(Karma.name, "KarmaBaseMysql.executeQuery", "Query String = " + query);
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return null;
	}
	
	public static void executeUpdate(String query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Core.driver);
			Statement stmt = conn.createStatement();
			Core.debug(Karma.name, "KarmaBaseMysql.executeUpdate", "Query String = " + query);
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}
}