package com.willikers.rp.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Material;

import com.johnwillikers.rp.MmoBaseMySql;

public class Sword extends Weapon{
	
	public Sword(int swordId) {
		this.id = swordId;
		this.material = Material.IRON_SWORD;
		this.table = "swords";
		ResultSet rs = MmoBaseMySql.getWeaponData(this.id, this.table);
		try {
			if(rs.next()) {
				this.name = rs.getString(2);
				this.str = rs.getInt(3);
				this.agi = rs.getInt(4);
				this.dex = rs.getInt(5);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
