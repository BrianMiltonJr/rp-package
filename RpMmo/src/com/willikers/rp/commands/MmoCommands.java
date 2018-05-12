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
			if(args[0].equalsIgnoreCase("xp")) {
				String targetUuid = PlayerBaseMySql.getUuid(args[1], args[2]);
				int targetId = PlayerBaseMySql.getPlayerId(targetUuid);
				Toon toon = new Toon(targetId);
				toon.experience(Integer.valueOf(args[3]));
				player.sendMessage("Awarded " + args[3] + " XP to " + args[1] + " " + args[1]);
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
		}else if(cmd.getName().equalsIgnoreCase("level")) {
			int playerId = PlayerBaseMySql.getPlayerId(player.getUniqueId().toString());
			Toon toon = new Toon(playerId);
			if(args.length==0) {
				player.sendMessage(toon.unspentPoints());
				return true;
			}else {
				int amount = Integer.valueOf(args[2]);
				if(args[0].equalsIgnoreCase("stat")) {
					if(args[1].equalsIgnoreCase("str") || args[1].equalsIgnoreCase("strength")) {
						toon.levelStat(amount, "str");
					}else if(args[1].equalsIgnoreCase("agi") || args[1].equalsIgnoreCase("agility")) {
						toon.levelStat(amount, "agi");
					}else if(args[1].equalsIgnoreCase("dex") || args[1].equalsIgnoreCase("dexterity")) {
						toon.levelStat(amount, "dex");
					}else if(args[1].equalsIgnoreCase("cons") || args[1].equalsIgnoreCase("constitution")) {
						toon.levelStat(amount, "cons");
					}else if(args[1].equalsIgnoreCase("spr") || args[1].equalsIgnoreCase("spirit")) {
						toon.levelStat(amount, "spirit");
					}
					toon.update();
					return true;
				}else if(args[0].equalsIgnoreCase("skill")) {
					if(args[1].equalsIgnoreCase("swords") || args[1].equalsIgnoreCase("swd")) {
						toon.levelSkill(amount, "sword");
					}else if(args[1].equalsIgnoreCase("shields") || args[1].equalsIgnoreCase("shd")) {
						toon.levelSkill(amount, "shield");
					}else if(args[1].equalsIgnoreCase("axes") || args[1].equalsIgnoreCase("axe")) {
						toon.levelSkill(amount, "axe");
					}else if(args[1].equalsIgnoreCase("bows") || args[1].equalsIgnoreCase("bow")) {
						toon.levelSkill(amount, "bow");
					}else if(args[1].equalsIgnoreCase("lightarmor") || args[1].equalsIgnoreCase("larm")) {
						toon.levelSkill(amount, "larm");
					}else if(args[1].equalsIgnoreCase("heavyarmor") || args[1].equalsIgnoreCase("harm")) {
						toon.levelSkill(amount, "harm");
					}
					toon.update();
					return true;
				}else {
					player.sendMessage("try /level [stat/skill]");
					return true;
				}
			}
		}
		return false;
		
	}
}
