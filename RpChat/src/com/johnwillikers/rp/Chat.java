package com.johnwillikers.rp;

import org.bukkit.plugin.java.JavaPlugin;

import com.johnwillikers.rp.commands.ChatCommands;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.listeners.ChatListener;

public class Chat extends JavaPlugin{
	
	public static Chat plugin;
	public static String name = Codes.CHAT.toString();
	public static String dir = Core.dir + "Rp_Chat";
	public static String chatBase = dir + "/ChatBase";
	
	@Override
	public void onEnable(){
		Core.dependables[0] = 1;
		Core.log(Core.name, Codes.DEPENDENCY.toString(), "Rp Chat has been recognized. Allowing Rp Chat to use Rp Core.");
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		ChatBase.createFilePath();
		ChatLogic.startUp();
		Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		Core.log(name, Codes.STARTUP.toString(), "Initialization");
		Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("whisper").setExecutor(new ChatCommands(this));
		this.getCommand("talk").setExecutor(new ChatCommands(this));
		this.getCommand("yell").setExecutor(new ChatCommands(this));
		this.getCommand("color").setExecutor(new ChatCommands(this));
		this.getCommand("uujdj123123412o458f1nd1").setExecutor(new ChatCommands(this));
		Core.log(name, Codes.LISTENERS.toString(), "Registering Listeners");
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		Core.log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		
	}
}
