package com.johnwillikers.rp;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;

import com.johnwillikers.rp.commands.KarmaCommands;
import com.johnwillikers.rp.enums.Codes;


public class Karma extends JavaPlugin{
	
	public static Karma plugin;
	public static String name = Codes.KARMA.toString();
	public static String dir = Core.dir + "RP_Karma";
	public ConversationFactory factory = new ConversationFactory(this);
	
	@Override
	public void onEnable(){
		Core.dependables[1] = 1;
		Core.log(Core.name, Codes.DEPENDENCY.toString(), "Rp Karma has been recognized. Allowing Rp Karma to use Rp Core.");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		KarmaBase.createKarmaBaseDir();
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		Core.log(name, Codes.STARTUP.toString(), "Initialization");
		Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("0e812e08h02v8he0182vhe1").setExecutor(new KarmaCommands(this));
		this.getCommand("negate").setExecutor(new KarmaCommands(this));
		this.getCommand("karma").setExecutor(new KarmaCommands(this));
		Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		
	}
}
