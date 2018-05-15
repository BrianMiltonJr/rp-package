package com.johnwillikers.rp.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.ToonBaseLocal;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

public class DamageListener implements Listener{

	@SuppressWarnings("unused")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			LivingEntity entity = (LivingEntity) e.getEntity();
			double remainingHp = entity.getHealth();
			final Player p = (Player) e.getDamager();
			
			//That base time for weapon cooldown
			int baseCooldownTick = 80;
			//Where our improved cooldown is stored
			int cooldownTicks = 0;
			
			//To let us know if the player is still on Global Cooldown
			int currentCooldownTicks = p.getCooldown(p.getInventory().getItemInMainHand().getType());
			Core.debug(Mmo.name, "DamageListener.onDamage", "cooldownTicks = " + currentCooldownTicks);
			boolean isCooling;
			if(currentCooldownTicks>=1) {
				isCooling=true;
			}else {
				isCooling=false;
			}
			Core.debug(Mmo.name, "DamageListener.onDamage", "isCooling = " + isCooling);
			
			//Loads the player from the Live DB 
			JSONObject toonData = ToonBaseLocal.readToon(p.getUniqueId().toString());
			int[] stats = {toonData.getInt("strength"), toonData.getInt("agility"), toonData.getInt("dexterity"), toonData.getInt("constitution"), toonData.getInt("spirit")};
			int[] skills = {toonData.getInt("sword"), toonData.getInt("shield"), toonData.getInt("axe"), toonData.getInt("bow"), toonData.getInt("light_armor"), toonData.getInt("heavy_armor")};
			
			//Grab the item in hand and get it's Material and Weapon ID
			ItemStack weapon = p.getInventory().getItemInMainHand();
			ItemMeta weaponMeta = weapon.getItemMeta();
			List<String> lore = weaponMeta.getLore();
			int str = Integer.valueOf(lore.get(1)); 
			int agi = Integer.valueOf(lore.get(3));
			int dex = Integer.valueOf(lore.get(5));
			Material weaponType = weapon.getType();

			
			//Formula in charge of adding up damage
			double attack = 0;
			double attackBonus = 0;
			double meleeFormula = (stats[0]*2);
			double rangedFormula = (stats[1]*2);
			double swordBonusFormula = (str*2) + ((stats[1] + agi)/2);
			double axeBonusFormula = (str*2) + ((stats[1] + agi)/4);
			double bowBonusFormula = (agi*2) + ((stats[0] + str)/2);
			//If we meet our weapons dexterity requirement
			if(stats[2]>=dex) {
				Core.debug(Mmo.name, "DamageListener.onDamage", "Meets dex requirments");
				if(weaponType==Material.WOOD_SWORD || weaponType==Material.IRON_SWORD || weaponType==Material.GOLD_SWORD || weaponType==Material.DIAMOND_SWORD) {
					attack = meleeFormula;
					attackBonus = (attackBonus + swordBonusFormula);
					if(skills[0]==0) {
						Core.debug(Mmo.name, "DamageListener.onDamage", "Skills detected at 0 doubling cooldownTick");
						cooldownTicks = (baseCooldownTick * 2);
					}else {
						cooldownTicks = (baseCooldownTick - (skills[0]*10));
					}
				}
				if(weaponType==Material.WOOD_AXE || weaponType==Material.IRON_AXE || weaponType==Material.GOLD_AXE || weaponType==Material.DIAMOND_AXE) {
					attack = meleeFormula;
					attackBonus = (attackBonus + axeBonusFormula);
					if(skills[2]==0) {
						Core.debug(Mmo.name, "DamageListener.onDamage", "Skills detected at 0 doubling cooldownTick");
						cooldownTicks = (baseCooldownTick * 2);
					}else {
						cooldownTicks = (baseCooldownTick - (skills[2]*10));
					}
				}
			}else {
				Core.debug(Mmo.name, "DamageListener.onDamage", "Does not Meet dex requirments");
				if(weaponType==Material.WOOD_SWORD || weaponType==Material.IRON_SWORD || weaponType==Material.GOLD_SWORD || weaponType==Material.DIAMOND_SWORD) {
					attack = meleeFormula;
					attackBonus = (attackBonus - swordBonusFormula);
					if(skills[0]==0) {
						Core.debug(Mmo.name, "DamageListener.onDamage", "Skills detected at 0 doubling cooldownTick");
						cooldownTicks = (baseCooldownTick * 2);
					}else {
						cooldownTicks = (baseCooldownTick - (skills[0]*10));
					}
				}
				if(weaponType==Material.WOOD_AXE || weaponType==Material.IRON_AXE || weaponType==Material.GOLD_AXE || weaponType==Material.DIAMOND_AXE) {
					attack = meleeFormula;
					attackBonus = (attackBonus - axeBonusFormula);
					if(skills[2]==0) {
						Core.debug(Mmo.name, "DamageListener.onDamage", "Skills detected at 0 doubling cooldownTick");
						cooldownTicks = (baseCooldownTick * 2);
					}else {
						cooldownTicks = (baseCooldownTick - (skills[2]*10));
					}
				}
			}
			Core.debug(Mmo.name, "DamageListener.onDamage", "Entity Hp = " + remainingHp);
			Core.debug(Mmo.name, "DamageListener.onDamage", "attack = " + attack);
			Core.debug(Mmo.name, "DamageListener.onDamage", "attackBonus = " + attackBonus);
			double dmg = attack + attackBonus;
			if(dmg<=0) {
				dmg = 0;
			}
			e.setDamage(dmg);
			if(isCooling) {
				e.setDamage(0.0);
			}
			Core.debug(Mmo.name, "DamageListener.onDamage", "cooldownTicks = " + cooldownTicks);
			Core.debug(Mmo.name, "DamageListener.onDamage", "cooldownTicks seconds = " + (cooldownTicks/20));
			p.setCooldown(p.getInventory().getItemInMainHand().getType(), cooldownTicks);
			Core.debug(Mmo.name, "DamageListener.onDamage", "dmg = " + dmg);
			Core.debug(Mmo.name, "DamageListener.onDamage", "Estimated Entity Remaining Hp = " + (remainingHp - dmg));
		}
	}
}