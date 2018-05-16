package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import com.johnwillikers.rp.actionbar.Actionbar;
import com.johnwillikers.rp.actionbar.Actionbar_1_12_R1;
import com.johnwillikers.rp.commands.Commands;
import com.johnwillikers.rp.enums.Codes;
import com.johnwillikers.rp.listeners.EntryListener;

import net.md_5.bungee.api.ChatColor;

public class Core extends JavaPlugin{
	
	public static boolean[] isInit = {false, false, false, false};
	/**
	 * The name of this plugin
	 */
	public static String name = Codes.CORE.toString();
	/**
	 * The Directory of Rp_Core
	 */
	public static String dir = "./plugins/Rp/";
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
	public static String dataMethod = "json";
	/**
	 * MySql host
	 */
	public static String host = "localhost";
	/**
	 * MySql table
	 */
	public static String db = "mctest";
	/**
	 * MySql Root credential
	 * 
	 */
	public static String user = "root";
	/**
	 * MySql Passwoord credential
	 */
	public static String password = "";
	/**
	 *  SSL status
	 */
	public static String ssl = "false";
	/**
	 *  Driver Path for Minecraft Mysql Server
	 */
	public static String driver = "jdbc:mysql://" + host + "/" + db + "?user=" + user + "&password=" + password + "&useSSL=" + ssl;
	/**
	 * The name of the town
	 */
	public static String townName = "The Encampment";
	/**
	 * Reference to this instance
	 */
	public static Core plugin;
	/**
	 * When another dependable plugin is loaded they switch their respective indice to 1 to allow smooth creation process and logging
	 */
	public static int[] dependables = {0,0,0};
	
	public ConversationFactory factory = new ConversationFactory(this);
	
	public static Actionbar actionBar;
	
	
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
		plugin=this;
		PlayerBase.createPlayerBaseDir();
		Utilities.createSettingsPath();
		settings = Utilities.getSettings();
		debugState = settings[1];
		townName = settings[2];
		dataMethod = settings[3];
		host = settings[4];
		db = settings[5];
		user = settings[6];
		password = settings[7];
		ssl = settings[8];
		driver = "jdbc:mysql://" + host + "/" + db + "?user=" + user + "&password=" + password + "&useSSL=false";
		if(Core.dataMethod.equalsIgnoreCase("mysql")) {
			DbHandler.createTables();
		}
		setupActionbar();
		debug(name, Codes.DEBUG + "Core.onEnable", "debugState = " + settings[1]);
		debug(name, Codes.DEBUG + "Core.onEnable", "dataMethod = " + settings[3]);
		debug(name, Codes.DEBUG + "Core.onEnable", "Driver = " + driver);
		log(name, Codes.STARTUP.toString(), "Pre-Initialization Completed.");
		log(name, Codes.STARTUP.toString(), "Initialization");
		log(name, Codes.COMMANDS.toString(), "Registering Commands");
		this.getCommand("ae356784901ldnvld0-083lwe").setExecutor(new Commands(this));
		this.getCommand("player").setExecutor(new Commands(this));
		this.getCommand("toggle_debug").setExecutor(new Commands(this));
		this.getCommand("rp").setExecutor(new Commands(this));
		log(name, Codes.LISTENERS.toString(), "Registering Listeners");
		getServer().getPluginManager().registerEvents(new EntryListener(), this);
		log(name, Codes.STARTUP.toString(), "Checking if Rp_Core is up to date");
		isCurrent();
		log(name, Codes.STARTUP.toString(), "Initializtion Completed.");
		isInit[0]=true;
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public void setupActionbar() {

	        String version = "0";

	        try {
	            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
	        } catch (ArrayIndexOutOfBoundsException e) {
	           e.printStackTrace();
	        }
	        Core.debug(name, Codes.DEBUG + "Core.setupActionbar", "Server is running version: " + version);
	        if (version.equals("v1_12_R1")) {
	            //server is running 1.8-1.8.1 so we need to use the 1.8 R1 NMS class
	            actionBar = new Actionbar_1_12_R1();
	        }
	}
	//TODO work on getting this going 
	public void isCurrent() {
		try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=42348")
                            .getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(
                    con.getInputStream())).readLine();
            debug(name, Codes.DEBUG.toString() + "Core.isCurrent", "Latest Version = " + version);
            if (version.length() <= 7) {
                if(!version.equalsIgnoreCase(this.getDescription().getVersion())) {
                	log(Core.name, Codes.STARTUP.toString(), "The Rp_plugins are out of date. Grab the latest update at");
                	log(Core.name, Codes.STARTUP.toString(), "https://www.spigotmc.org/resources/johns-rp-bundle.42348/");
                }
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
           log(name, Codes.ERROR.toString(), "Failed to check for a update on spigot.");
        }
	}
}