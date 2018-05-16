package com.johnwillikers.rp;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

public class MmoFormulas {

	public static double getRangeDamage(ItemStack weapon, UUID uuid) {
		Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Loading Toon Data");
		JSONObject toonData = ToonBaseLocal.readToon(uuid.toString());
		int[] stats = {toonData.getInt("strength"), toonData.getInt("agility"), toonData.getInt("dexterity"), toonData.getInt("constitution"), toonData.getInt("spirit")};
		int[] skills = {toonData.getInt("sword"), toonData.getInt("shield"), toonData.getInt("axe"), toonData.getInt("bow"), toonData.getInt("light_armor"), toonData.getInt("heavy_armor")};
		
		Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Grabbing Weapon Stats");
		ItemMeta weaponMeta = weapon.getItemMeta();
		List<String> lore = weaponMeta.getLore();
		int str = Integer.valueOf(lore.get(1)); 
		int agi = Integer.valueOf(lore.get(3));
		int dex = Integer.valueOf(lore.get(5));
		Material weaponType = weapon.getType();
		
		double attack = 0;
		double attackBonus = 0;
		double rangedFormula = (stats[1]*2);
		double bowBonusFormula = (agi*2) + ((stats[0] + str)/2);
		double dmg = 0;
		
		//That base time for weapon cooldown
		int baseCooldownTick = 80;
		//Where our improved cooldown is stored			
		int cooldownTicks = 0;
				
		if(stats[2]>=dex) {
			Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Meets dex requirments");
			if(weaponType == Material.BOW) {
				attack = rangedFormula;
				attackBonus = attackBonus + bowBonusFormula;
				if(skills[3]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[0]*10));
				}
			}
		}else {
			Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Does not meet dex requirments");
			if(weaponType == Material.BOW) {
				attack = rangedFormula;
				attackBonus = attackBonus - bowBonusFormula;
				if(skills[3]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getRangeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[0]*10));
				}
			}
		}
		dmg = attack + attackBonus;
		if(dmg<=0) {
			dmg = 0;
		}
		Bukkit.getPlayer(uuid).setCooldown(weapon.getType(), cooldownTicks);
		return dmg;
		
	}
	
	@SuppressWarnings("unused")
	public static double getMeleeDamage(ItemStack weapon, UUID uuid) {
		
		Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Loading Toon Data");
		JSONObject toonData = ToonBaseLocal.readToon(uuid.toString());
		int[] stats = {toonData.getInt("strength"), toonData.getInt("agility"), toonData.getInt("dexterity"), toonData.getInt("constitution"), toonData.getInt("spirit")};
		int[] skills = {toonData.getInt("sword"), toonData.getInt("shield"), toonData.getInt("axe"), toonData.getInt("bow"), toonData.getInt("light_armor"), toonData.getInt("heavy_armor")};
		
		Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Grabbing Weapon Stats");
		ItemMeta weaponMeta = weapon.getItemMeta();
		List<String> lore = weaponMeta.getLore();
		int str = Integer.valueOf(lore.get(1)); 
		int agi = Integer.valueOf(lore.get(3));
		int dex = Integer.valueOf(lore.get(5));
		Material weaponType = weapon.getType();
		
		double attack = 0;
		double attackBonus = 0;
		double meleeFormula = (stats[0]*2);
		
		double swordBonusFormula = (str*2) + ((stats[1] + agi)/2);
		double axeBonusFormula = (str*2) + ((stats[1] + agi)/4);
		double dmg = 0;
		
		//That base time for weapon cooldown
		int baseCooldownTick = 80;
		//Where our improved cooldown is stored
		int cooldownTicks = 0;
		
		Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Checking Dex Requirements");
		if(stats[2]>=dex) {
			Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Meets dex requirments");
			if(weaponType==Material.WOOD_SWORD || weaponType==Material.IRON_SWORD || weaponType==Material.GOLD_SWORD || weaponType==Material.DIAMOND_SWORD) {
				Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Weapon =  Sword");
				attack = meleeFormula;
				attackBonus = (attackBonus + swordBonusFormula);
				if(skills[0]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[0]*10));
				}
			}
			if(weaponType==Material.WOOD_AXE || weaponType==Material.IRON_AXE || weaponType==Material.GOLD_AXE || weaponType==Material.DIAMOND_AXE) {
				Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Weapon =  Sword");
				attack = meleeFormula;
				attackBonus = (attackBonus + axeBonusFormula);
				if(skills[2]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[2]*10));
				}
			}
		}else {
			Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Does not Meet dex requirments");
			if(weaponType==Material.WOOD_SWORD || weaponType==Material.IRON_SWORD || weaponType==Material.GOLD_SWORD || weaponType==Material.DIAMOND_SWORD) {
				Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Weapon =  Axe");
				attack = meleeFormula;
				attackBonus = (attackBonus - swordBonusFormula);
				if(skills[0]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[0]*10));
				}
			}
			if(weaponType==Material.WOOD_AXE || weaponType==Material.IRON_AXE || weaponType==Material.GOLD_AXE || weaponType==Material.DIAMOND_AXE) {
				Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Weapon =  Axe");
				attack = meleeFormula;
				attackBonus = (attackBonus - axeBonusFormula);
				if(skills[2]==0) {
					Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "Skills detected at 0 doubling cooldownTick");
					cooldownTicks = (baseCooldownTick * 2);
				}else {
					cooldownTicks = (baseCooldownTick - (skills[2]*10));
				}
			}
		}
		Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "attack = " + attack);
		Core.debug(Mmo.name, "MmoFormulas.getMeleeDamage", "attackBonus = " + attackBonus);
		dmg = attack + attackBonus;
		if(dmg<=0) {
			dmg = 0;
		}
		Bukkit.getPlayer(uuid).setCooldown(weapon.getType(), cooldownTicks);
		return dmg;
	}
	
	public static int[] formulateLevelUp(int currentXp, int currentLevel, int addedXp) {
		int level = currentLevel;
		int xp = currentXp + addedXp;
		if(level<=4) {
			if(xp % 500 == 0) {
				level = level + (xp/500);
				xp = 0;
			}else {
				int surplus = xp % 500;
				int levelXp = xp - surplus;
				level = level + (levelXp/500);
				xp = surplus;
			}
		}
		if(level>=5 && level <=60) {
			if(xp % 1000 == 0) {
				level = level + (xp/1000);
				if(level>60) {
					level = 60;
				}
				xp = 0;
			}else {
				int surplus = xp % 1000;
				int levelXp = xp - surplus;
				level = level + (levelXp/1000);
				if(level>60) {
					level = 60;
				}
				xp = surplus;
			}
		}
		int[] newLevelXp = {level, xp};
		return newLevelXp;
	}
}
