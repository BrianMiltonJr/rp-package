package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;

import com.johnwillikers.rp.enums.Codes;

public class Utilities {
	
	/**
	 * The Settings dir
	 */
	public static File settingsDir = new File(Core.dir + "Settings");
	/**
	 * Rp_Core's setting file
	 */
	public static File settingsFile = new File(settingsDir + "/core_settings.json");
	/**
	 * The default JSONObject to be written on first launch
	 */
	public static JSONObject settingsDefault = new JSONObject("{\"debugState\":true,\"townName\":\"The Encampment\"}");
	
	/**
	 * A helper method that converts a String version of a UUID into a full fledged UUID
	 * 
	 * @param uuid the {@code String} version of the UUID you want to convert
	 * @return Returns a {@code UUID}
	 * @since 0.0.1
	 */
	public static UUID returnUUID(String uuid){
		return UUID.fromString(uuid);
	}
	
	/**
	 * Gets the current date
	 * 
	 * @return Returns a {@code String} of the current date
	 * @since 0.0.1
	 */
	public static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dateRaw = new Date();
		String date = dateFormat.format(dateRaw);
		return date;
	}
	
	/**
	 * Grabs Rp_Core's settings and puts them into a String[]
	 * 
	 * @return Returns a {@code String[]}
	 * @since 0.0.1
	 */
	public static String[] getSettings(){
		try{
			FileReader fr = new FileReader(settingsFile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject data = new JSONObject(json);
			boolean debugState = data.getBoolean("debugState");
			String townName = data.getString("townName");
			String[] goodReply = {"1", String.valueOf(debugState), townName};
			Core.debug(Core.name, Codes.DEBUG + "Utilities.getSettings", "Returning goodReply contains: failCode: " + goodReply[0] + " debugState: " + goodReply[1]);
			return goodReply;
		} catch (IOException e) {
			String[] badReply = {"0", "true"};
			e.printStackTrace();
			Core.debug(Core.name, Codes.DEBUG + "Utilities.getSettings", "Returning badReply. Check Stacktrace above.");
			return badReply;
		}
	}
	
	/**
	 * Creates the Rp_Cores settings
	 * 
	 * @since 0.0.1
	 */
	public static void createSettingsFile(){
		Core.log(Core.name, Codes.STARTUP.toString(), "Checking to see if settings.json exists.");
		if(!settingsFile.exists()){
			try {
				Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Attempting to create settings.json.");
				settingsFile.createNewFile();
				Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Assigning default values to settings.json");
				PrintWriter pr = new PrintWriter(settingsFile);
				pr.println(settingsDefault);
				pr.close();
				Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "settings.json created successfully.");
			} catch (IOException e) {
				e.printStackTrace();
				Core.log(Core.name, Codes.STARTUP.toString(), "Creation of settings.json failed, check the stacktrace above.");
			}
		}
		Core.log(Core.name, Codes.STARTUP.toString(), "settings.json exists");
	}
	
	/**
	 * Creates the Settings filepath
	 * 
	 * @since 0.0.1
	 */
	public static void createSettingsPath(){
		Core.log(Core.name, Codes.STARTUP.toString(), "Checking to see if settings Directory exists");
		if(!settingsDir.exists()){
			Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Creating settings Directory.");
			settingsDir.mkdir();
			Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Creation of settings Directory was successfull");
			createSettingsFile();
		}else{
			Core.log(Core.name, Codes.STARTUP.toString(), "settings Directory exists.");
		}
	}
}
