package com.willikers.rp.objects;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Weapon{
	protected int id;
	protected String name;
	protected String table;
	protected Material material;
	protected int str;
	protected int agi;
	protected int dex;
	
	public ItemStack getItem() {
		ItemStack weapon = new ItemStack(this.material);
		ItemMeta weaponMeta = weapon.getItemMeta();
		weaponMeta.setDisplayName(this.name);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Strength:");
		lore.add(String.valueOf(this.str));
		lore.add("Agility:");
		lore.add(String.valueOf(this.agi));
		lore.add("Dexterity:");
		lore.add(String.valueOf(this.dex));
		weaponMeta.setLore(lore);
		weapon.setItemMeta(weaponMeta);
		return weapon;
	}
}
