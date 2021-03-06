package com.johnwillikers.rp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;

public class DbHandler {
	
	public static void createTables() {
		//Rp_Core MySql Tables
		String playersTablesQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`players` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `first` VARCHAR(200) NOT NULL , `last` VARCHAR(200) NOT NULL , `player_name` VARCHAR(200) NOT NULL , `gender` INT NOT NULL , `creation_ip` VARCHAR(200) NOT NULL , `last_ip` VARCHAR(200) NOT NULL , `created_at` VARCHAR(200) NOT NULL , `updated_at` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM";
		String gamemastersTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db +"`.`gamemasters` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		//Rp_Karma MySql Tables
		String karmaTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`karma` ( `id` INT NOT NULL AUTO_INCREMENT , `player_id` INT NOT NULL , `karma` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String reportsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`reports` ( `id` INT NOT NULL AUTO_INCREMENT , `gm_id` INT NOT NULL , `player_id` INT NOT NULL , `body` VARCHAR(200) NOT NULL , `actions` INT NOT NULL , `date` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		//Rp_Mmo MySql Tables
		String toonsTableQuery = "CREATE TABLE IF NOT EXISTS`" + Core.db + "`.`toons` ( `id` INT NOT NULL AUTO_INCREMENT , `player_id` INT NOT NULL , `xp` INT NOT NULL , `level` INT NOT NULL , `stat_points` INT NOT NULL , `skill_points` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String statsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`stats` ( `id` INT NOT NULL AUTO_INCREMENT , `toon_id` INT NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , `constitution` INT NOT NULL , `spirit` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String skillsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`skills` ( `id` INT NOT NULL AUTO_INCREMENT , `toon_id` INT NOT NULL , `sword` INT NOT NULL , `shield` INT NOT NULL , `axe` INT NOT NULL , `bow` INT NOT NULL , `light_armor` INT NOT NULL , `heavy_armor` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String swordsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`swords` ( `id` INT NOT NULL AUTO_INCREMENT , `type` VARCHAR(200) NOT NULL , `material` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String shieldsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`shields` ( `id` INT NOT NULL AUTO_INCREMENT , `type` VARCHAR(200) NOT NULL , `material` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		String axesTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`axes` ( `id` INT NOT NULL AUTO_INCREMENT , `type` VARCHAR(200) NOT NULL , `material` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;"; 
		String bowsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db + "`.`bows` ( `id` INT NOT NULL AUTO_INCREMENT , `type` VARCHAR(200) NOT NULL , `material` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , `strength` INT NOT NULL , `agility` INT NOT NULL , `dexterity` INT NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;"; 
		if(!Core.isInit[0])
			executeUpdate(playersTablesQuery, Core.name);
		if(Core.dependables[1] == 1) {
			if(!Core.isInit[2]) {
				executeUpdate(gamemastersTableQuery, ChatColor.RED + "Rp_Karma");
				executeUpdate(karmaTableQuery, ChatColor.RED + "Rp_Karma");
				executeUpdate(reportsTableQuery, ChatColor.RED + "Rp_Karma");
			}
		}
		if(Core.dependables[2] == 1) {
			if(!Core.isInit[3]) {
				executeUpdate(toonsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(statsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(skillsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(swordsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(shieldsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(axesTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
				executeUpdate(bowsTableQuery, ChatColor.LIGHT_PURPLE + "Rp_Mmo");
			}
		}
	}
	
	public static void executeQuery(Plugin plugin, String query, String pluginName, String location, MySqlCallback callback) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection(Core.driver);
					Statement stmt = conn.createStatement();
					Core.debug(pluginName, Codes.DEBUG + "DbHandler.executeQuery | " + location, "Query String = " + query);
					ResultSet rs = stmt.executeQuery(query);
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						@Override
						public void run() {
							callback.onQueryDone(rs);
						}
					});
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					
				}
			}
			
		});
	}
	
	public static void executeUpdate(String query, String pluginName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Core.driver);
			Statement stmt = conn.createStatement();
			Core.debug(pluginName, "DbHandler.executeUpdate", "Query String = " + query);
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
