package com.johnwillikers.rp;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import java.sql.Connection;

public class PlayerBaseMySql {
	
	public static void createTables() {
		String playersTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`players` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `first` VARCHAR(200) NOT NULL , `last` VARCHAR(200) NOT NULL , `player_name` VARCHAR(200) NOT NULL , `gender` INT NOT NULL , `creation_ip` VARCHAR(200) NOT NULL , `last_ip` VARCHAR(200) NOT NULL , `created_at` VARCHAR(200) NOT NULL , `updated_at` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM";
		executeUpdate(playersTableQuery);
	}
	
	public static int getPlayerId(String uuid) {
		String query = "SELECT id FROM players WHERE uuid='" + uuid + "';";
		ResultSet rs = executeQuery(query);
		try {
			if(rs.next()) {
				int id = rs.getInt(1);
				return id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -4000;
	}
	
	public static String getUuid(String first, String last){
		String query = "SELECT uuid FROM players WHERE first LIKE '" + first + "' AND last LIKE '" + last + "';";
		ResultSet rs = executeQuery(query);
		try {
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the players info from the playerbase
	 * 
	 * @param player the player to get their information retrieved
	 * @return Returns a {@code String[]} containing the players info
	 * @since 0.0.1
	 */
	public static String[] getPlayerInfo(Player player){
		String query = "SELECT * FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
		ResultSet rs = executeQuery(query);
		try {
			rs.next();
			String[] details = {"1", rs.getString(2), rs.getString(3), rs.getString(4), String.valueOf(rs.getBoolean(5)), rs.getString(6), rs.getString(7)};
			rs.close();
			return details;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Checks to see if the player is registered in the playerbase
	 * 
	 * @param player The player to be checked
	 * @return a boolean regarding whether it exists or not
	 * @since 0.0.1
	 */
	public static boolean exists(Player player){
		String query = "SELECT * FROM players WHERE uuid='" + player.getUniqueId().toString() + "';";
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
	
	
	/**
	 * This is a dynamic way of building a mysql query to edit columns where uuid = player.uuid
	 * @param data 0 = Columns Name, 1 = Row Data, 2 = Column, 3 = Row Data, etc.
	 * @param player
	 */
	public static void updatePlayer(String[] data, Player player) {
		String query = "UPDATE players SET ";
		for (int i=0; i<data.length-1; i++) {
			Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", "i = " + i);
			query = query + data[i] + "=";
			i++;
			Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", "i = " + i);
			if(i<data.length-1) {
				Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", i+">"+data.length);
				Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", "Adding a , at end of string");
				query = query + "'" + data[i] + "'" + ", ";
			}
			if(i == data.length-1) {
				Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", i+"="+data.length);
				Core.debug(Core.name, "PlayerBaseMySql.updatePlayer", "leaving a clean space for where clause");
				query = query + "'" + data[i] + "'" + " ";
			}
		}
		query = query + "WHERE uuid='" + player.getUniqueId().toString() + "';";
		executeUpdate(query);
	}
	
	public static void createPlayer(String[] data) {
		String query = "INSERT INTO players ( uuid, first, last, player_name, gender, creation_ip, last_ip, created_at, updated_at ) "
				+ "VALUES ('" + data[0] + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', "+ data[4] + ", '" + data[5] +
				"', '" + data[6] + "', '" + data[7] + "', '" + data[8] + "');";
		executeUpdate(query);
		
	}
	
	public static ResultSet executeQuery(String query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Core.driver);
			Statement stmt = conn.createStatement();
			Core.debug(Core.name, "PlayerBaseMysql.executeQuery", "Query String = " + query);
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
			Core.debug(Core.name, "PlayerBaseMysql.executeUpdate", "Query String = " + query);
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