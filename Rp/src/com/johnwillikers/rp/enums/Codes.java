package com.johnwillikers.rp.enums;

import net.md_5.bungee.api.ChatColor;

public enum Codes {
	FIRST_LAUNCH(ChatColor.GREEN + "First Launch"),
	STARTUP(ChatColor.LIGHT_PURPLE + "Startup"),
	SHUTDOWN(ChatColor.LIGHT_PURPLE + "Shutdown"),
	COMMANDS(ChatColor.GOLD + "Commands"),
	ERROR(ChatColor.DARK_RED + "Error"),
	LISTENERS(ChatColor.RED + "Listeners"),
	ENTRYLISTENER(ChatColor.BLUE + "EntryListener"),
	DEBUG(ChatColor.LIGHT_PURPLE + "Debug" + ChatColor.WHITE + " - "),
	PLAYERBASE(ChatColor.DARK_PURPLE + "PlayerBase"),
	CHATBASE(ChatColor.DARK_AQUA + "ChatBase"),
	PLAYERNAME(ChatColor.DARK_BLUE + "PlayerName"),
	DEPENDENCY(ChatColor.DARK_GREEN + "Dependency"),
	CORE(ChatColor.DARK_GREEN + "Rp_Core"),
	CHAT(ChatColor.AQUA + "Rp_Chat"),
	KARMA(ChatColor.RED + "Rp_Karma"),
	RELOAD(ChatColor.DARK_GREEN + "Reload");
	
	private final String text;
	
	private Codes(final String text){
		this.text = text;
	}
	
	@Override
	public String toString(){
		return text;
	}
}
