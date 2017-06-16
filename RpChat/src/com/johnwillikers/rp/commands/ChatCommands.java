package com.johnwillikers.rp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Chat;
import com.johnwillikers.rp.ChatBase;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.Utilities;

import net.md_5.bungee.api.ChatColor;

public class ChatCommands implements CommandExecutor {
	
	Chat plugin;
	
	public ChatCommands(Chat instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("whisper") || cmd.getName().equalsIgnoreCase("w")){
			ChatBase.setPlayerTalkDistance(player, "whisper");
			player.sendMessage(ChatColor.AQUA + "You are now whispering.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("talk") || cmd.getName().equalsIgnoreCase("t")){
			ChatBase.setPlayerTalkDistance(player, "talk");
			player.sendMessage(ChatColor.GOLD + "You are talking normally.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("yell") || cmd.getName().equalsIgnoreCase("y")){
			ChatBase.setPlayerTalkDistance(player, "yell");
			player.sendMessage(ChatColor.RED + "You are yelling loudly.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("uujdj123123412o458f1nd1")){
			ChatBase.setPlayerTalkDistance(player, "talk");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("color")){
			if(args.length == 0){
				player.sendMessage("The /color commands is used to change the color of your name.");
				player.sendMessage("Usage: /color <color>");
				player.sendMessage("Available Colors:");
				player.sendMessage(ChatColor.AQUA + "Blue");
				player.sendMessage(ChatColor.RED + "Red");
				player.sendMessage(ChatColor.GOLD + "Gold");
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Pink");
				player.sendMessage(ChatColor.YELLOW + "Yellow");
				player.sendMessage("As always you can use the first letter of the word to represent the color.");
				return true;
			}else if(args.length == 3 && player.hasPermission("rp.color.admin")){
				String[] user = PlayerBase.checkMasteFile(args[1] + " " + args[2]);
				if(user.length < 2){
					player.sendMessage("User " + args[1] + "_" + args[2] + " does not exist.");
					return true;
				}
				Player userP = Bukkit.getPlayer(Utilities.returnUUID(user[1]));
				String[] info = PlayerBase.getPlayerInfo(Bukkit.getPlayer(Utilities.returnUUID(user[1])));
				if(args[0].equalsIgnoreCase("blue") || args[0].equalsIgnoreCase("b")){
					Bukkit.getPlayer(userP.getUniqueId()).setDisplayName(ChatColor.AQUA + info[1] + " " + info[2] + ChatColor.WHITE);
					sender.sendMessage("You have changed " + info[1] + " " + info[2] + "('s)" + " color to " + ChatColor.AQUA + "blue" + ChatColor.WHITE + ".");
					userP.sendMessage("Your name has been colored " + ChatColor.AQUA + "blue");
					return true;
				}else if(args[0].equalsIgnoreCase("red") || args[0].equalsIgnoreCase("r")){
					Bukkit.getPlayer(userP.getUniqueId()).setDisplayName(ChatColor.RED + info[1] + " " + info[2] + ChatColor.WHITE);
					sender.sendMessage("You have changed " + info[1] + " " + info[2] + "('s)" + " color to " + ChatColor.RED + "red" + ChatColor.WHITE + ".");
					userP.sendMessage("Your name has been colored " + ChatColor.RED+ "red");
					return true;
				}else if(args[0].equalsIgnoreCase("gold") || args[0].equalsIgnoreCase("g")){
					Bukkit.getPlayer(userP.getUniqueId()).setDisplayName(ChatColor.GOLD + info[1] + " " + info[2] + ChatColor.WHITE);
					sender.sendMessage("You have changed " + info[1] + " " + info[2] + "('s)" + " color to " + ChatColor.GOLD + "gold" + ChatColor.WHITE + ".");
					userP.sendMessage("Your name has been colored " + ChatColor.GOLD + "gold");
					return true;
				}else if(args[0].equalsIgnoreCase("pink") || args[0].equalsIgnoreCase("p")){
					Bukkit.getPlayer(userP.getUniqueId()).setDisplayName(ChatColor.LIGHT_PURPLE + info[1] + " " + info[2] + ChatColor.WHITE);
					sender.sendMessage("You have changed " + info[1] + " " + info[2] + "('s)" + " color to " + ChatColor.LIGHT_PURPLE + "pink" + ChatColor.WHITE + ".");
					userP.sendMessage("Your name has been colored " + ChatColor.LIGHT_PURPLE + "pink");
					return true;
				}else if(args[0].equalsIgnoreCase("yellow") || args[0].equalsIgnoreCase("y")){
					Bukkit.getPlayer(userP.getUniqueId()).setDisplayName(ChatColor.YELLOW + info[1] + " " + info[2] + ChatColor.WHITE);
					sender.sendMessage("You have changed " + info[1] + " " + info[2] + "('s)" + " color to " + ChatColor.YELLOW + "yellow" + ChatColor.WHITE + ".");
					userP.sendMessage("Your name has been colored " + ChatColor.YELLOW + "yellow");
					return true;
				}else{
					return false;
				}
			}else{
				String[] info = PlayerBase.getPlayerInfo(player);
				if(args[0].equalsIgnoreCase("blue") || args[0].equalsIgnoreCase("b")){
					Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.AQUA + info[1] + " " + info[2] + ChatColor.WHITE);
					player.sendMessage("You have changed your name color to " + ChatColor.AQUA + "blue" + ChatColor.WHITE + ".");
					return true;
				}else if(args[0].equalsIgnoreCase("red") || args[0].equalsIgnoreCase("r")){
					Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.RED + info[1] + " " + info[2] + ChatColor.WHITE);
					player.sendMessage("You have changed your name color to " + ChatColor.RED + "red" + ChatColor.WHITE + ".");
					return true;
				}else if(args[0].equalsIgnoreCase("gold") || args[0].equalsIgnoreCase("g")){
					Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.GOLD + info[1] + " " + info[2] + ChatColor.WHITE);
					player.sendMessage("You have changed your name color to " + ChatColor.GOLD + "gold" + ChatColor.WHITE + ".");
					return true;
				}else if(args[0].equalsIgnoreCase("pink") || args[0].equalsIgnoreCase("p")){
					Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.LIGHT_PURPLE + info[1] + " " + info[2] + ChatColor.WHITE);
					player.sendMessage("You have changed your name color to " + ChatColor.LIGHT_PURPLE + "pink" + ChatColor.WHITE + ".");
					return true;
				}else if(args[0].equalsIgnoreCase("yellow") || args[0].equalsIgnoreCase("y")){
					Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.YELLOW + info[1] + " " + info[2] + ChatColor.WHITE);
					player.sendMessage("You have changed your name color to " + ChatColor.YELLOW + "yellow" + ChatColor.WHITE + ".");
					return true;
				}
			}
		}
		return false;
	}
}
