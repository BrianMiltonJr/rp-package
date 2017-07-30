package com.johnwillikers.rp;

import java.util.Timer;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import com.johnwillikers.rp.commands.KarmaCommands;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.timertasks.KarmaTask;


public class Karma extends JavaPlugin{
	
	public static Karma plugin;
	public static String name = Codes.KARMA.toString();
	public static String dir = Core.dir + "Rp_Karma";
	public ConversationFactory factory = new ConversationFactory(this);
	//How many seconds should the timer start after the server starting
	public long startTime = 5;
	public long startModifier = startTime * 1000;
	//How long between karma checks on the server
	public long delayTime = 1;
	public long delayModifier = delayTime * 1000 * 60 * 60;
	public Timer karmaUpdate = new Timer("karma-update", true);
	public static int karmaUpAmount = 50;
	
	@Override
	public void onEnable(){
		Core.dependables[1] = 1;
		Core.log(Core.name, Codes.DEPENDENCY.toString(), "Rp Karma has been recognized. Allowing Rp Karma to use Rp Core.");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		KarmaBase.createKarmaBaseDir();
		JSONObject settings = KarmaBase.getSettings();
		this.startTime = settings.getInt("startTime");
		this.delayTime = settings.getInt("delayTime");
		karmaUpAmount = settings.getInt("karmaUpAmount");
		Core.debug(name, Codes.DEBUG.toString() + "Karma.onEnable", "startTime: " + startTime + " | delayTime: " + delayTime + " | karmaUpAmount: " + karmaUpAmount);
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		Core.log(name, Codes.STARTUP.toString(), "Initialization");
		Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("0e812e08h02v8he0182vhe1").setExecutor(new KarmaCommands(this));
		this.getCommand("negate").setExecutor(new KarmaCommands(this));
		this.getCommand("karma").setExecutor(new KarmaCommands(this));
		Core.log(name, Codes.STARTUP.toString(), "Starting up Timers");
		karmaUpdate.schedule(new KarmaTask(), this.startModifier , this.delayModifier);
		Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		Core.log(name, Codes.SHUTDOWN.toString(), "Disabling Timers");
		karmaUpdate.cancel();
		karmaUpdate.purge();
	}
}
