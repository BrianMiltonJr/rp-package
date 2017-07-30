package com.johnwillikers.rp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.KarmaBase;
import com.johnwillikers.rp.KarmaLogic;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.conversations.NegateConfirmPrompt;
import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;

public class KarmaCommands implements CommandExecutor{
	
	Karma plugin;
	public static String answer = null;
	
	public KarmaCommands(Karma instance){
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("0e812e08h02v8he0182vhe1")){
			KarmaBase.writeKarma(player.getUniqueId().toString(), 0, null);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("negate")){
			if(args.length == 0){
				return false;
			}else{
				String desc = String.join(" ", args);
				int length = args[0].length() + args[1].length() + args[2].length() + 3;
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "Length = " + length);
				desc = desc.substring(length);
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "desc = " + desc);
				//Get offender's UUID
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "First: " + args[0] + " Last: " + args[1]);
				String[] uuidRaw = PlayerBase.checkMasteFile(args[0] + "_" + args[1]);
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "uuidRaw = " + String.join(" ", uuidRaw));
				if(uuidRaw[0] == String.valueOf(0)){
					player.sendMessage("[Karma] You have entered an invalid name.");
					return true;
				}else{
					Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "UUID = " + uuidRaw[1]);
					String uuid = uuidRaw[1];
					//Check if offender exists
					if(KarmaBase.exists(uuid)){
						String msg = "Is this information correct?\n " +
								 "Name: " + args[0] + " " + args[1] + "\n" +
								 "Offense: " + args[2] + "\n" + 
								 "Description: " + desc +
								 "[y/n]";
						Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaCommands.onCommand.negate", "User exists, starting converation with new NegateConfirmPrompt()");
						//Confirm with Gm that the information is correct
						Conversation conv = plugin.factory.withFirstPrompt(new NegateConfirmPrompt(msg, args[2], uuid, desc, player, args[0] + " " + args[1])).thatExcludesNonPlayersWithMessage("Get away, or be BURNED MORTAL.").buildConversation(((Conversable) sender));
						conv.begin();
						return true;
					}
				}
				return false;
			}
		}else if(cmd.getName().equalsIgnoreCase("karma")){
			if(!(args.length <= 1)){
				String[] payload = PlayerBase.checkMasteFile(args[0] + "_" + args[1]);
			if(Integer.valueOf(payload[0]) == 1){
					if(KarmaBase.exists(payload[1])){
						//JSONObject kfile = KarmaBase.getKarmaInfo(payload[1]);
						String msg = KarmaLogic.lookUp(payload[1]);
						player.sendMessage(ChatColor.GOLD + "Name: " + args[0] + " " + args[1] + "\n" + msg);
					}else{
						player.sendMessage("Are you sure " + args[0] + " " + args[1] + " is spelled correctly?");
					}
					return true;
				}else{
					player.sendMessage("Are you sure " + args[0] + " " + args[1] + " is spelled correctly?");
					return true;
				}
			}
		}
		return false;
	}
	
}