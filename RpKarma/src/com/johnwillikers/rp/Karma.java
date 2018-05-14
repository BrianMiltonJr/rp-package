package com.johnwillikers.rp;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import com.johnwillikers.rp.commands.KarmaCommands;
import com.johnwillikers.rp.enums.Codes;

import net.md_5.bungee.api.ChatColor;


public class Karma extends JavaPlugin{
	
	public static Karma plugin;
	/**
	 * Rp_Karma's name
	 */
	public static String name = Codes.KARMA.toString();
	/**
	 * Rp_Karma's dir
	 */
	public static String dir = Core.dir + "Karma";
	public ConversationFactory factory = new ConversationFactory(this);
	
	@Override
	public void onEnable(){
		plugin = this;
		if(Core.dataMethod.equalsIgnoreCase("mysql")) {
			Core.dependables[1] = 1;
			Core.log(Core.name, Codes.DEPENDENCY.toString(), name + ChatColor.WHITE + " has been recognized. Allowing " + name + ChatColor.WHITE + " to use Rp Core.");
			Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
			DbHandler.createTables();
			Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
			Core.log(name, Codes.STARTUP.toString(), "Initialization");
			Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
			this.getCommand("0e812e08h02v8he0182vhe1").setExecutor(new KarmaCommands(this));
			this.getCommand("negate").setExecutor(new KarmaCommands(this));
			this.getCommand("karma").setExecutor(new KarmaCommands(this));
			this.getCommand("gamemaster").setExecutor(new KarmaCommands(this));
			Core.log(name, Codes.STARTUP.toString(), "Starting up Timers");
			Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
			Core.isInit[2] = true;
		}
	}
	
	@Override
	public void onDisable(){
	}
}
