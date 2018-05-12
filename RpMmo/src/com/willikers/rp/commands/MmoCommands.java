package com.willikers.rp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Mmo;
import com.johnwillikers.rp.PlayerBaseMySql;
import com.willikers.rp.objects.Axe;
import com.willikers.rp.objects.Bow;
import com.willikers.rp.objects.Shield;
import com.willikers.rp.objects.Sword;
import com.willikers.rp.objects.Toon;

public class MmoCommands implements CommandExecutor {
	
	Mmo plugin;
	
	public MmoCommands(Mmo instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("mmo")){
			if(args.length==2) {
				String targetUuid = PlayerBaseMySql.getUuid(args[0], args[1]);
				int targetId = PlayerBaseMySql.getPlayerId(targetUuid);
				Toon toon = new Toon(targetId);
				String msg = toon.generateToonMsg();
				player.sendMessage(msg);
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("item")) {
			if(args.length==2) {
				if(args[0].equalsIgnoreCase("sword")) {
					Sword sword = new Sword(Integer.valueOf(args[1]));
					player.getInventory().addItem(sword.getItem());
				}else if(args[0].equalsIgnoreCase("axe")) {
					Axe axe = new Axe(Integer.valueOf(args[1]));
					player.getInventory().addItem(axe.getItem());
				}else if(args[0].equalsIgnoreCase("bow")) {
					Bow bow = new Bow(Integer.valueOf(args[1]));
					player.getInventory().addItem(bow.getItem());
				}else if(args[0].equalsIgnoreCase("shield")) {
					Shield shield = new Shield(Integer.valueOf(args[1]));
					player.getInventory().addItem(shield.getItem());
				}else {
					player.sendMessage("Item type does not exist");
				}
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("character")) {
			int playerId = PlayerBaseMySql.getPlayerId(player.getUniqueId().toString());
			Toon toon = new Toon(playerId);
			String msg = toon.generateToonMsg();
			player.sendMessage(msg);
			return true;
		}
		return false;
		
	}
}
