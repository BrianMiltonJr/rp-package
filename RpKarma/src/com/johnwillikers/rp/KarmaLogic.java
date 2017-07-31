package com.johnwillikers.rp;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;

public class KarmaLogic {
	
	/**
	 * Checks to see what the recommended action be for a player
	 * 
	 * @param player the player
	 * @param karma their karma
	 * @since 0.0.2
	 */
	public static void karmaCheck(Player player, int karma){
		switch(karma){
		case(-500):
			//Recommend for ban
			break;
		case(-250):
			//Recommend for kick
			break;
		case(0):
			//Recommend for admin review
			break;
		case(250):
			//miniscule reward
			break;
		case(500):
			//give permission to color name
			break;
		case(600):
			//medium reward
			break;
		case(700):
			//medium reward
			break;
		case(800):
			//large reward
			break;
		}
	}
	
	/**
	 * Adds karma to a player
	 * 
	 * @param player the player
	 * @param amount the amount
	 */
	public static void aid(Player player, int amount){
		JSONObject derp = null;
		try {
			KarmaBase.updateKarma(player.getUniqueId().toString(), amount, derp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes away karma and logs the incident
	 * 
	 * @param uuid the player
	 * @param amount the amount
	 * @param incident the latest incident
	 * @throws IOException Error in writing new karma file
	 */
	public static void negate(String uuid, int amount, JSONObject incident) throws IOException{
		KarmaBase.updateKarma(uuid, amount, incident);
	}
	
	public static void sendOffenderMsg(Player player, String msg){
		player.sendMessage(msg);
	}
	
	/**
	 * Looks up the player in the KarmaBase and formats it into a message
	 * 
	 * @param uuid the player
	 * @return String
	 */
	public static String lookUp(String uuid){
		Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "lookUp() was triggered.");
		JSONObject info = KarmaBase.getKarmaInfo(uuid);
		Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "JSONObject info loaded");
		Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Assigning Karma Coloring");
		if(info.getInt("status") == 1){
			ChatColor karmaColor = null;
			if(info.getInt("karma") > 0){
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Assigning" + ChatColor.GREEN + " Green");
				karmaColor = ChatColor.GREEN;
			}else if(info.getInt("karma") < 0){
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Assigning" + ChatColor.RED + " Red");
				karmaColor = ChatColor.RED;
			}else{
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Assigning" + ChatColor.YELLOW + " YELLOW");
				karmaColor = ChatColor.YELLOW;
			}
			String success = ChatColor.GOLD + "Karma: " + karmaColor + info.getInt("karma") + ChatColor.GOLD + "\nIncidents: \n";
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Assigning incidents into JSONArray");
			JSONArray incidents = info.getJSONArray("incidents");
			int index = 0;
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "Looping over incidents JSONArray");
			while(index < incidents.length()){
				JSONObject contents = incidents.getJSONObject(index);
				String date = contents.getString("date");
				String desc = contents.getString("desc");
				String actions = contents.getString("actions");
				String gm = contents.getString("gm");
				success = success + "--------------------------------------------------\n    Date: " + date + "\n    Description: " + desc + "\n    Actions: " + actions + "    GameMaster: " + gm + "\n";
				Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "End of Incident loop number " + index);
				index++;
			}
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookup", "Success = " + success);
			return success;
		}
		String fail = "User does not exist, or bypassed being logged by the Karma System.";
		return fail;
	}
}
