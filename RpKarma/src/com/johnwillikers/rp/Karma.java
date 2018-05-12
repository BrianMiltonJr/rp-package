package com.johnwillikers.rp;

import java.util.Timer;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import com.johnwillikers.rp.commands.KarmaCommands;
import com.johnwillikers.rp.enums.Codes;


public class Karma extends JavaPlugin{
	
	public static Karma plugin;
	/**
	 * Rp_Karma's name
	 */
	public static String name = Codes.KARMA.toString();
	/**
	 * Rp_Karma's dir
	 */
	public static String dir = Core.dir + "Rp_Karma";
	public ConversationFactory factory = new ConversationFactory(this);
	
	@Override
	public void onEnable(){
		Core.dependables[1] = 1;
		Core.log(Core.name, Codes.DEPENDENCY.toString(), "Rp Karma has been recognized. Allowing Rp Karma to use Rp Core.");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		KarmaBase.createKarmaBaseDir();
		JSONObject settings = KarmaBase.getSettings();
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		Core.log(name, Codes.STARTUP.toString(), "Initialization");
		Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("0e812e08h02v8he0182vhe1").setExecutor(new KarmaCommands(this));
		this.getCommand("negate").setExecutor(new KarmaCommands(this));
		this.getCommand("karma").setExecutor(new KarmaCommands(this));
		Core.log(name, Codes.STARTUP.toString(), "Starting up Timers");
		Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		Core.log(name, Codes.SHUTDOWN.toString(), "Disabling Timers");
	}
}
