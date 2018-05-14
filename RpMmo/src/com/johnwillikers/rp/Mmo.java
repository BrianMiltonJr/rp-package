package com.johnwillikers.rp;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.commands.MmoCommands;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.listeners.DamageListener;
import com.johnwillikers.rp.listeners.MmoEntryListener;

import net.md_5.bungee.api.ChatColor;


public class Mmo extends JavaPlugin{
	
	public static Mmo plugin;
	/**
	 * Rp_Mmo's name
	 */
	public static String name = Codes.MMO.toString();
	/**
	 * Rp_Mmo's dir
	 */
	public static String dir = Core.dir + "Rp_Mmo/";
	public ConversationFactory factory = new ConversationFactory(this);
	
	@Override
	public void onEnable(){
		plugin = this;
		Core.dependables[2] = 1;
		if(Core.dataMethod.equalsIgnoreCase("mysql")) {
			Core.log(Core.name, Codes.DEPENDENCY.toString(), name + ChatColor.WHITE + " has been recognized. Allowing " + name + ChatColor.WHITE + " to use Rp Core.");
			Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization");
			Core.debug(name, "Mmo.onEnable", "Dir = " + dir);
			ToonBaseLocal.createPath();
			DbHandler.createTables();
			Core.log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
			Core.log(name, Codes.STARTUP.toString(), "Initialization");
			Core.log(name, Codes.COMMANDS.toString(), "Registering Commands");
			this.getCommand("mmo").setExecutor(new MmoCommands(this));
			this.getCommand("item").setExecutor(new MmoCommands(this));
			this.getCommand("character").setExecutor(new MmoCommands(this));
			this.getCommand("level").setExecutor(new MmoCommands(this));
			this.getCommand("03f9147b09743nf09n71").setExecutor(new MmoCommands(this));
			Core.log(name, Codes.LISTENERS.toString(), "Registering Listeners");
			getServer().getPluginManager().registerEvents(new DamageListener(), this);
			getServer().getPluginManager().registerEvents(new MmoEntryListener(), this);
			Core.log(name, Codes.STARTUP.toString(), "Initialization Completed.");
			Core.isInit[3] = true;
		}
	}
	
	@Override
	public void onDisable(){
		Core.log(name, Codes.SHUTDOWN.toString(), "Starting Proccess of uploading all online players to MySql DB");
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			JSONObject toonData = ToonBaseLocal.readToon(player.getUniqueId().toString());
			ToonBaseLocal.uploadToon(toonData);
			ToonBaseLocal.deleteToon(player.getUniqueId().toString());
			Core.log(name, Codes.SHUTDOWN.toString(), player.getDisplayName() + " has been successfully uploaded.");
		}
		Core.log(name, Codes.SHUTDOWN.toString(), "All Online players have been uploaded. Exiting plugin");
	}
}
