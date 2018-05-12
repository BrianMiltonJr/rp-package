package com.willikers.rp;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.PlayerBaseMySql;
import com.johnwillikers.rp.Utilities;

public class MmoBaseMySql {
	
	public static void createTables() {
		String toonsTableQuery = "CREATE TABLE IF NOT EXISTS `" + Core.db +"`.`gamemasters` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(200) NOT NULL , `name` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;";
		Utilities.executeUpdate(toonsTableQuery, Mmo.name);
	}
	
	public static void createToon(String uuid, int statPoints, int skillPoints) {
		String query = "INSERT INTO toons ( player_id, xp, level, stat_points, skill_points ) VALUE ( " + PlayerBaseMySql.getPlayerId(uuid) + ", 0, 1, " + statPoints + ", " + skillPoints +" );";
		Utilities.executeUpdate(query, Mmo.name);
	}
	
	public static boolean exists(int player_id){
		String query = "SELECT * FROM karma WHERE player_id=" + player_id + ";";
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
}