package com.johnwillikers.rp.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.ToonBaseLocal;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;

public class DamageListener implements Listener{

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			final Player p = (Player) e.getDamager();
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
			int attack = 0;
			int attackBonus = 0;
			int meleeFormula = (stats[0] * 2) + (stats[1]/2);
			int rangedFormula = (stats[1]*2) + (stats[0]/4);
			int swordBonusFormula = (str*2) + (agi*skills[0]);
			int axeBonusFormula = (str*2) + (agi*skills[1]);
			int bowBonusFormula = (agi*2) + (str*skills[2]);
			if(stats[2]>=dex) {
				Core.debug(Mmo.name, "DamageListener.onDamage", "Meets dex requirments");
				switch(weaponType){
				case IRON_SWORD: 
					attack = meleeFormula;
					attackBonus = (attackBonus + swordBonusFormula);
					break;
				case IRON_AXE: 
					attack = meleeFormula;
					attackBonus = (attackBonus + axeBonusFormula);
					break;
				default:
					attackBonus = 0;
				}
			
			}else {
				Core.debug(Mmo.name, "DamageListener.onDamage", "Does not Meet dex requirments");
				switch(weaponType){
				case IRON_SWORD: 
					attack = meleeFormula;
					attackBonus = (attackBonus - str);
					break;
				case IRON_AXE: 
					attack = meleeFormula;
					attackBonus = (attackBonus - str);
					break;
				default:
					attackBonus = 0;
				}
			}
			Core.debug(Mmo.name, "DamageListener.onDamage", "attack = " + attack);
			Core.debug(Mmo.name, "DamageListener.onDamage", "attackBonus = " + attackBonus);
			int dmg = attack + attackBonus;
			Core.debug(Mmo.name, "DamageListener.onDamage", "dmg = " + dmg);
			if(dmg<=0) {
				dmg = 0;
			}
			if(e.getCause().equals(DamageCause.PROJECTILE)) {
				attack = rangedFormula;
				attackBonus = (attackBonus + bowBonusFormula);
				dmg = (attack + attackBonus);
			}
			e.setDamage(Double.valueOf(dmg));
		}
	}
}