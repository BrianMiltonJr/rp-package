package com.johnwillikers.rp;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;

import com.johnwillikers.rp.commands.Commands;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.listeners.EntryListener;

import net.md_5.bungee.api.ChatColor;

public class Core extends JavaPlugin{
	
	/**
	 * The name of this plugin
	 */
	public static String name = Codes.CORE.toString();
	/**
	 * The Directory of Rp_Core
	 */
	public static String dir = "./plugins/RP_Core/";
	/**
	 * This holds the settings of this plugin into a {@code String[]}
	 */
	public static String[] settings;
	/**
	 * This String determines whether debug logs are printed or not
	 */
	public static String debugState = "true";
	/**
	 * This String determines how to store and retrieve data
	 */
	public static String dataMethod = "mysql";
	/**
	 * The name of the town
	 */
	public static String townName = "The Encampment";
	
	public static Core plugin;
	/**
	 * When another dependable plugin is loaded they switch their respective indice to 1 to allow smooth creation process and logging
	 */
	public static int[] dependables = {0,0};
	
	public ConversationFactory factory = new ConversationFactory(this);
	
	public static String[] gameMasters;
	
	/**
	 * This method is in charge of logging messages to console with that feature 
	 * color coding by using a Codes enum. This enum can be found in the enum package.
	 * 
	 * @param name The Plugin name
	 * @param type The type of message e.g Commands, Listeners, etc.
	 * @param msg The message to pass to the console.
	 * @since 0.0.1
	 */
	public static void log(String name, String type, String msg){
		//System.out.println("[" + name + "]|" + "[" + level + "] " + msg); deprecated code, old method of sending messages to console, but doesn't support coloring. Left for the lulz
		Server server = Bukkit.getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		console.sendMessage(ChatColor.WHITE + "[" + name + ChatColor.WHITE + "]|" + "[" + type + ChatColor.WHITE + "] " + msg);
	}
	
	/**
	 * This method is in charge of logging debug messages to console
	 * 
	 * @param name The Plugin name
	 * @param location What Class and function the debug call is called from. e.g Core.debug, Core.log, Core.first_launch
	 * @param msg The debug message to pass to console
	 * @since 0.0.1
	 */
	public static void debug(String name, String location, String msg){
		if(debugState.equalsIgnoreCase("true")){
			log(name, location, msg);
		}
	}
	
	@Override
	public void onEnable(){
		log(name, Codes.STARTUP.toString(), "Pre-Initialization");
		PlayerBase.createPlayerBaseDir();
		Utilities.createSettingsPath();
		settings = Utilities.getSettings();
		debugState = settings[1];
		townName = settings[2];
		dataMethod = settings[3];
		debug(name, Codes.DEBUG + "Core.onEnable", "debugState = " + settings[1]);
		debug(name, Codes.DEBUG + "Core.onEnable", "dataMethod = " + settings[3]);
		log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		log(name, Codes.STARTUP.toString(), "Initialization");
		log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("ae356784901ldnvld0-083lwe").setExecutor(new Commands(this));
		this.getCommand("player").setExecutor(new Commands(this));
		this.getCommand("toggle_debug").setExecutor(new Commands(this));
		this.getCommand("rp").setExecutor(new Commands(this));
		log(name, Codes.LISTENERS.toString(), "Registering Listeners");
		getServer().getPluginManager().registerEvents(new EntryListener(), this);
		log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		
	}
}
