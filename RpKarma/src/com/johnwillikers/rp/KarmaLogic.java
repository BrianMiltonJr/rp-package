package com.johnwillikers.rp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
		if(karma <= -500) {
			//Recommend for ban
			Bukkit.broadcast(player.getDisplayName() + " has " + String.valueOf(karma) + " Karma. They are being recommended for a ban", "rp.gamemaster");
		}
		if(karma <= -250 && karma != -500) {
			//Recommend for kick
			Bukkit.broadcast(player.getDisplayName() + " has " + String.valueOf(karma) + " Karma. They are being recommended for a kick", "rp.gamemaster");
		}
		if(karma <=0 && karma > -250) {
			//Recommend for admin review
			Bukkit.broadcast(player.getDisplayName() + " has " + String.valueOf(karma) + " Karma. They are being recommended for an admin review", "rp.gamemaster");
		}
		if(karma >= 250 && karma < 500) {
			//miniscule reward
		}
		if(karma >= 500 && karma < 750) {
			//give permission to color name
		}
		if(karma >= 750 && karma < 1000) {
			//medium reward
		}
		if(karma >= 1000 && karma < 1250) {
			//medium reward
		}
		if(karma >= 1250 && karma < 50000) {
			//large reward
		}
		/*To developers who read this online on github. I'd like this to be a thing no one knows. So people who play aren't aiming to be a game master at the end. I'd rather
		 * Game Masters not even know about this option. Just let it ping one day randomly in gm chat and them talk about it. By this karma amount they will have played enough through the 
		 * rp that they are well experienced
		 */
		if(karma >= 50000) {
			//Recommended to become a Game master
			if(!player.hasPermission("rp.gamemaster")) {
				Bukkit.broadcast("You feel a numbing sensation in your toes, and your vision turns dark.\n It then flashes an image of " + player.getDisplayName() + " and you hear a deep voice roar, \"HE IS TO BE THE NEXT GAMEMASTER.\" Your vision returns and your breath is shaking heavily", "rp.gamemaster");
			}
		}
	}
	
	/**
	 * Formats the /karma message
	 * 
	 * @param uuid the player
	 * @return String
	 */
	
	public static String buildLookUpMessage(int karma, String playerName, String[] reportIds){
		ChatColor karmaColor = null;
		if(karma > 0){
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.buildLookUpMessage", "Assigning" + ChatColor.GREEN + " Green");
			karmaColor = ChatColor.GREEN;
		}else if(karma < 0){
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.buildLookUpMessage", "Assigning" + ChatColor.RED + " Red");
			karmaColor = ChatColor.RED;
		}else{
			Core.debug(Karma.name, Codes.DEBUG.toString() + "KarmaLogic.buildLookUpMessage", "Assigning" + ChatColor.YELLOW + " YELLOW");
			karmaColor = ChatColor.YELLOW;
		}
		int length = reportIds.length;
		String msg = ChatColor.GOLD + "Name: " + ChatColor.BLUE + playerName + ChatColor.GOLD + "\nKarma: " + karmaColor + karma;
			
		if(!reportIds.equals(null)) {
			msg = msg + ChatColor.GOLD + "\nNumber of Incidents: " + length + "\n Report Id's:\n";
			for(int i=0; i<=length-1; i++) {
				msg = msg + "* " + reportIds[i] + "\n";
			}
			msg = msg + "Use /report {id} to view the report";
		}else {
			msg = msg + ChatColor.BLUE + "\n" + playerName + ChatColor.GOLD + " has a clean record";
		}
		return msg;
	}
}
