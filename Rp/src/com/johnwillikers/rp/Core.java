package com.johnwillikers.rp;

import java.io.File;

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
	
	public static String name = Codes.CORE.toString();
	public static String dir = "./plugins/RP_Core/";
	public static String playerBase = dir + "PlayerBase/";
	public static String[] settings;
	public static String debugState = "true";
	public static String townName = "The Encampment";
	public static Core plugin;
	public static int[] dependables = {0,0};
	public ConversationFactory factory = new ConversationFactory(this);
	
	public static void debug(String name, String location, String msg){
		if(debugState.equalsIgnoreCase("true")){
			log(name, location, msg);
		}
	}
	
	public static void log(String name, String level, String msg){
		//System.out.println("[" + name + "]|" + "[" + level + "] " + msg); deprecated code, old method of sending messages to console, but doesn't support coloring. Left for the lulz
		Server server = Bukkit.getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		console.sendMessage(ChatColor.WHITE + "[" + name + ChatColor.WHITE + "]|" + "[" + level + ChatColor.WHITE + "] " + msg);
	}
	
	public static void firstLaunch(){
		File pb = new File(playerBase);
		log(name, Codes.STARTUP.toString(), "Does PlayerBase Exist?");
		if(!pb.exists()){
			log(name, Codes.STARTUP.toString(), "Creating PlayerBase.");
			pb.mkdirs();
		}else{
			log(name, Codes.STARTUP.toString(), "Playerbase Exists.");
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
		debug(name, Codes.DEBUG + "Core.onEnable", "debugState = " + settings[1]);
		log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		log(name, Codes.STARTUP.toString(), "Initialization");
		log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("ae356784901ldnvld0-083lwe").setExecutor(new Commands(this));
		this.getCommand("player").setExecutor(new Commands(this));
		log(name, Codes.LISTENERS.toString(), "Registering Listeners");
		getServer().getPluginManager().registerEvents(new EntryListener(), this);
		log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
	}
	
	@Override
	public void onDisable(){
		
	}
}
