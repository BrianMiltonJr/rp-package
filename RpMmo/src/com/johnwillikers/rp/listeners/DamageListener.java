package com.johnwillikers.rp.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.MmoFormulas;

public class DamageListener implements Listener{


	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		//If the player is attacking(melee)
		if(e.getDamager() instanceof Player){
			LivingEntity entity = (LivingEntity) e.getEntity();
			double remainingHp = entity.getHealth();
			final Player p = (Player) e.getDamager();
			int currentCooldownTicks = p.getCooldown(p.getInventory().getItemInMainHand().getType());

			Core.debug(Mmo.name, "DamageListener.onDamage(Melee)", "Entity Hp = " + remainingHp);
			Core.debug(Mmo.name, "DamageListener.onDamage(Melee)", "Current cooldownTicks = " + currentCooldownTicks);
			
			boolean isCooling;
			
			if(currentCooldownTicks>=1) {
				isCooling=true;
			}else {
				isCooling=false;
			}
			double dmg = MmoFormulas.getMeleeDamage(p.getInventory().getItemInMainHand(), p.getUniqueId());
			Core.debug(Mmo.name, "DamageListener.onDamage(Melee)", "isCooling = " + isCooling);
			e.setDamage(dmg);
			if(isCooling) {
				e.setDamage(0);
			}
			
			Core.debug(Mmo.name, "DamageListener.onDamage(Melee)", "dmg = " + dmg);
			Core.debug(Mmo.name, "DamageListener.onDamage(Melsee)", "Estimated Entity Remaining Hp = " + (remainingHp - dmg));
			return;
		}
		//Checking for Projectiles
		if(e.getDamager() instanceof Projectile) {
			Projectile arrow = (Projectile) e.getDamager();
			//Are they from the player
			if(arrow.getShooter() instanceof Player) {
				Player player = (Player) arrow.getShooter();
				LivingEntity entity = (LivingEntity) e.getEntity();
				
				double remainingHp = entity.getHealth();
				int currentCooldownTicks = player.getCooldown(player.getInventory().getItemInMainHand().getType());
				Core.debug(Mmo.name, "DamageListener.onDamage(Projectile)", "Entity Hp = " + remainingHp);
				Core.debug(Mmo.name, "DamageListener.onDamage(Projectile)", "Current cooldownTicks = " + currentCooldownTicks);
				boolean isCooling;
				
				if(currentCooldownTicks>=1) {
					isCooling=true;
				}else {
					isCooling=false;
				}
				double dmg = MmoFormulas.getRangeDamage(player.getInventory().getItemInMainHand(), player.getUniqueId());
				Core.debug(Mmo.name, "DamageListener.onDamage(Projectile)", "isCooling = " + isCooling);
				e.setDamage(dmg);
				if(isCooling) {
					e.setDamage(0);
				}
				Core.debug(Mmo.name, "DamageListener.onDamage(Projectile)", "dmg = " + dmg);
				Core.debug(Mmo.name, "DamageListener.onDamage(Projectile)", "Estimated Entity Remaining Hp = " + (remainingHp - dmg));
				return;
			}
		}
	}
	
	@SuppressWarnings("unused")
	//Going to be used later for awesome effects and MAGIC :D
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
	}
}