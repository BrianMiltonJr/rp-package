package com.johnwillikers.rp.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.johnwillikers.rp.ToonBaseLocal;

import net.minecraft.server.v1_12_R1.Material;

public class MmoInventoryListener implements Listener {
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
    public void inventoryClick(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		
		if(e.getInventory().getTitle().contains("Stat")) {
			int[] stats = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
			e.setCancelled(true);
			if((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
				return;
			}
			String itemDisplayName = e.getCurrentItem().getItemMeta().getDisplayName();
			
			if(stats[2]<=0) {
				player.sendMessage("You do not have enough Stat Points to do that");
				player.closeInventory();
				return;
			}
			
			if(itemDisplayName=="Strength") {
				stats[4]++;
				stats[2]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Agility") {
				stats[5]++;
				stats[2]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Dexterity") {
				stats[6]++;
				stats[2]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Constitution") {
				stats[7]++;
				stats[2]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Spirit") {
				stats[8]++;
				stats[2]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Exit") {
				player.closeInventory();
			}
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		}
		
		if(e.getInventory().getTitle().contains("Skill")) {
			int[] stats = ToonBaseLocal.getToonDataIntArray(ToonBaseLocal.readToon(player.getUniqueId().toString()));
			e.setCancelled(true);
			
			if((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
				return;
			}
			String itemDisplayName = e.getCurrentItem().getItemMeta().getDisplayName();
			
			if(stats[3]<=0) {
				player.sendMessage("You do not have enough Skill Points to do that");
				player.closeInventory();
				return;
			}
			
			if(itemDisplayName=="Swords") {
				stats[9]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Shields") {
				stats[10]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Axes") {
				stats[11]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Bows") {
				stats[12]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Light Armor") {
				stats[13]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Heavy Armor") {
				stats[14]++;
				stats[3]--;
				ToonBaseLocal.updateToon(ToonBaseLocal.getToonDataJSONObject(stats), player.getUniqueId().toString());
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Exit") {
				player.closeInventory();
			}
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		}
		
		if(e.getInventory().getTitle().contains("Character")) {
			e.setCancelled(true);
			
			if((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
				return;
			}
			
			String itemDisplayName = e.getCurrentItem().getItemMeta().getDisplayName();

			if(itemDisplayName=="Stat Points") {
				player.closeInventory();
				player.chat("/level stat");
			}
			if(itemDisplayName=="Skill Points") {
				player.closeInventory();
				player.chat("/level skill");
			}
			if(itemDisplayName=="Exit") {
				player.closeInventory();
			}
			player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
		}
	}
}