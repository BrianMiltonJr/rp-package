package com.johnwillikers.rp;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;

public class KarmaLogic {
	
	
	
	public static void aid(Player player, int amount) throws IOException{
		JSONObject derp = null;
		KarmaBase.updateKarma(player.getUniqueId().toString(), amount, derp);
	}
	
	public static void negate(String uuid, int amount, JSONObject incident) throws IOException{
		KarmaBase.updateKarma(uuid, amount, incident);
	}
	
	public static void sendOffenderMsg(Player player, String msg){
		player.sendMessage(msg);
	}
	
	public static String lookUp(String uuid){
		Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookUp", "lookUp() was triggered.");
		JSONObject info = KarmaBase.getKarmaInfo(uuid);
		if(info.getInt("status") == 1){
			ChatColor karmaColor = null;
			if(info.getInt("karma") > 0){
				karmaColor = ChatColor.GREEN;
			}else if(info.getInt("karma") < 0){
				karmaColor = ChatColor.RED;
			}else{
				karmaColor = ChatColor.YELLOW;
			}
			String success = ChatColor.GOLD + "Karma: " + karmaColor + info.getInt("karma") + ChatColor.GOLD + "\nIncidents: \n";
			JSONArray incidents = info.getJSONArray("incidents");
			int index = 0;
			while(index <= incidents.length()){
				JSONObject contents = incidents.getJSONObject(index);
				String date = contents.getString("Date");
				String desc = contents.getString("desc");
				String actions = contents.getString("actions");
				String gm = contents.getString("gm");
				success = success + "Date: " + date + "\nDescription: " + desc + "\nActions: " + actions + "GameMaster: " + gm;
			}
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.lookup", "Success = " + success);
			return success;
		}
		String fail = "User does not exist, or bypassed being logged by the Karma System.";
		return fail;
	}
}
