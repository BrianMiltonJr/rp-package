package com.willikers.rp.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Material;

import com.johnwillikers.rp.MmoBaseMySql;

public class Bow extends Weapon{
	
	public Bow(int bowId) {
		this.id = bowId;
		this.material = Material.BOW;
		this.table = "bows";
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
