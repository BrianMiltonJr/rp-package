package com.willikers.rp;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.enums.Codes;


public class Mmo extends JavaPlugin{
	
	public static Mmo plugin;
	/**
	 * Rp_Mmo's name
	 */
	public static String name = Codes.MMO.toString();
	/**
	 * Rp_Mmo's dir
	 */
	public static String dir = Core.dir + "Rp_Mmo";
	public ConversationFactory factory = new ConversationFactory(this);
	
	@Override
	public void onEnable(){
		Core.dependables[1] = 1;
		Core.log(Core.name, Codes.DEPENDENCY.toString(), "Rp Karma has been recognized. Allowing Rp Karma to use Rp Core.");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		Core.log(name, Codes.STARTUP.toString(), "Initialization");
		Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
		//this.getCommand("negate").setExecutor(new KarmaCommands(this));
		Core.log(name, Codes.STARTUP.toString(), "Starting up Timers");
		Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		Core.log(name, Codes.SHUTDOWN.toString(), "Disabling Timers");
	}
}
