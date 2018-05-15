package com.johnwillikers.rp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.johnwillikers.rp.callbacks.MySqlCallback;
import com.johnwillikers.rp.enums.Codes;

public class Weapon {

	public Weapon(String table, int id, Player player) {
		String query = "SELECT type, material, name, strength, agility, dexterity FROM " + table + " WHERE id=" + id + ";";
		DbHandler.executeQuery(Mmo.plugin, query, Mmo.name, "Weapon.createWeapon", new MySqlCallback() {

			@Override
			public void onQueryDone(ResultSet rs) {
				try {
					if(rs.next()) {
						String[] data = {rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
						String[] type = {rs.getString(1), rs.getString(2)};
						rs.close();
						Material material = resolveMaterial(type);
						Core.debug(Mmo.name, Codes.DEBUG.toString() + "Weapon.Constructor", "Weapon Material = " + material.toString());
						Core.debug(Mmo.name, Codes.DEBUG.toString() + "Weapon.Constructor", "Came to this conclusion cause type = " + type[0] + " " + type[1]);
						ItemStack weapon = new ItemStack(resolveMaterial(type));
						ItemMeta weaponMeta = weapon.getItemMeta();
						weaponMeta.setDisplayName(data[0]);
						ArrayList<String> lore = new ArrayList<String>();
						lore.add("Strength");
						lore.add(data[1]);
						lore.add("Agility");
						lore.add(data[2]);
						lore.add("Dexterity");
						lore.add(data[3]);
						weaponMeta.setLore(lore);
						weapon.setItemMeta(weaponMeta);
						player.getInventory().addItem(weapon);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	public Material resolveMaterial(String[] type) {
		if(type[0].equalsIgnoreCase("sword")) {
			switch(type[1]){
				case "wood":
					return Material.WOOD_SWORD;
				case "iron":
					return Material.IRON_SWORD;
				case "gold":
					return Material.GOLD_SWORD;
				case "diamond":
					return Material.DIAMOND_SWORD;
				default:
					return Material.STICK;
			}
		}
		if(type[0].equalsIgnoreCase("axe")) {
			switch(type[1]){
				case "wood":
					return Material.WOOD_AXE;
				case "iron":
					return Material.IRON_AXE;
				case "gold":
					return Material.GOLD_AXE;
				case "diamond":
					return Material.DIAMOND_AXE;
				default:
					return Material.STICK;
			}
		}
		if(type[0].equalsIgnoreCase("bow")) {
			switch(type[1]){
				case "wood":
					return Material.WOOD_AXE;
				case "iron":
					return Material.IRON_AXE;
				case "gold":
					return Material.GOLD_AXE;
				case "diamond":
					return Material.DIAMOND_AXE;
				default:
					return Material.STICK;
			}
		}
		return Material.STICK;
	}
}