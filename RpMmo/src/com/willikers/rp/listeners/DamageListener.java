package com.willikers.rp.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.PlayerBaseMySql;
import com.willikers.rp.objects.Toon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DamageListener implements Listener{

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			int playerId = PlayerBaseMySql.getPlayerId(p.getUniqueId().toString());
			//Creates a New Toon Instance and puts the skills and stats in handy arrays for later
			Toon toon = new Toon(playerId);
			int[] stats = toon.getStats();
			int[] skills = toon.getSkills();
			//GrAab the item in hand and get it's Material and Weapon ID
			ItemStack weapon = p.getInventory().getItemInMainHand();
			ItemMeta weaponMeta = weapon.getItemMeta();
			List<String> lore = weaponMeta.getLore();
			int str = Integer.valueOf(lore.get(0)); 
			int agi = Integer.valueOf(lore.get(1));
			int dex = Integer.valueOf(lore.get(2));
			Material weaponType = weapon.getType();
			
			//Formula in charge of adding up damage
			int attack = 0;
			int attackBonus = 0;
			if(dex>=stats[2]) {
				switch(weaponType){
				case IRON_SWORD: 
					attack = str;
					attackBonus = (attackBonus + str) + skills[0];
					break;
				case IRON_AXE: 
					attack = str;
					attackBonus = (attackBonus + str) + skills[0];
					break;
				case BOW:
					attack = agi;
					attackBonus = (attackBonus + agi) + skills[1];
					break;
				default:
					attackBonus = 0;
				}
			
			}
			Core.debug(Mmo.name, "DamageListener.onDamage", "attack =" + attack);
			Core.debug(Mmo.name, "DamageListener.onDamage", "attackBonus" + attackBonus);
			e.setDamage(Double.valueOf(attack+attackBonus));
		}
	}
}