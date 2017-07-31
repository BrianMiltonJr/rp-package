package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.johnwillikers.rp.enums.Codes;

public class ChatBase {
	
	/**
	 * ChatBase file location
	 */
	public static File chatBase = new File(Chat.chatBase);
	/**
	 * Rp_Chat's settings dir
	 */
	public static File settings = new File(Utilities.settingsDir + "/chat_settings.json");
	/**
	 * Rp_Chat's settings default
	 */
	public static String settingsDefault = "{\"whisper\":5,\"talk\":10,\"yell\":20}";
	/**
	 * Rp_Chat's distance default
	 */
	public static String distanceDefault = "\"distance\":\"talk\",\"ooc\":\"true\"";
	
	/**
	 * Creates ChatBase
	 * 
	 * @since 0.0.1
	 */
	public static void createFilePath(){
		Core.log(Chat.name, Codes.STARTUP.toString(), "Checking if ChatBase exists.");
		if(!chatBase.exists()){
			Core.log(Chat.name, Codes.FIRST_LAUNCH.toString(), "Attempting to create ChatBase.");
			chatBase.mkdirs();
			if(!chatBase.exists()){
				Core.log(Chat.name, Codes.FIRST_LAUNCH.toString(), "ChatBase creation failed.");
				return;
			}else{
				Core.log(Chat.name, Codes.FIRST_LAUNCH.toString(), "ChatBase created successfully.");
				createFiles();
			}
		}else{
			Core.log(Chat.name, Codes.STARTUP.toString(), "ChatBase exists.");
		}
	}
	
	/**
	 * Creates Rp_Chat's Settings file
	 * 
	 * @since 0.0.1
	 */
	public static void createFiles(){
		Core.log(Chat.name, Codes.STARTUP.toString(), "Checking if settings.json exists.");
		if(!settings.exists()){
			Core.log(Chat.name, Codes.FIRST_LAUNCH.toString(), "Attempting to create settings.json.");
			try{
				PrintWriter pr = new PrintWriter(settings);
				pr.println(settingsDefault);
				pr.close();
				Core.log(Chat.name, Codes.FIRST_LAUNCH.toString(), "settings.json created successfully.");
			}catch(IOException e){
				e.printStackTrace();
				Core.log(Chat.name, Codes.ERROR.toString(), "There was a failure in attempting to create settings.json. Read the Stacktrace above or report it.");
			}
		}
	}
	
	/**
	 * Returns Talk distance values
	 * 
	 * @return {@code int[]}
	 * @since 0.0.1
	 */
	public static int[] getSettings(){
		try{
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatBase.getSettings", "Attempting to read settings.json");
			FileReader fr = new FileReader(settings);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject settings = new JSONObject(json);
			int whisper = settings.getInt("whisper");
			int talk = settings.getInt("talk");
			int yell = settings.getInt("yell");
			int[] goodReply = {1, whisper, talk, yell};
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatBase.getSettings", "Sending goodReply. failCode: " + goodReply[0] + " Whisper: " + goodReply[1] + " Talk: "
					+ goodReply[2] + " Yell: " + goodReply[3]);
			return goodReply;
		}catch(IOException e){
			int[] badReply = {0};
			e.printStackTrace();
			Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatBase.getSettings", "Sending badReply. Check stacktrace above.");
			return badReply;
		}
	}
	
	/**
	 * Gets the players current talking distance
	 * 
	 * @param p the player
	 * @return {@code String}
	 * @since 0.0.1
	 */
	public static String getPlayerDistance(Player p){
		File pfile = new File(Chat.chatBase + "/" + p.getUniqueId().toString() + ".json");
		try{
			FileReader fr = new FileReader(pfile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject pData = new JSONObject(json);
			Core.debug(Chat.name, Codes.DEBUG + "ChatBase.getPlayerDistance", "pData: " + pData.toString());
			return pData.get("distance").toString();
		}catch(IOException e){
			e.printStackTrace();
			Core.log(Chat.name, Codes.ERROR.toString(), "Tried to retrieve " + p.getDisplayName() + " talk distance, but Failed. Read StackTrace above or report it.");
			Core.log(Chat.name, Codes.ERROR.toString(), "Sending talk to keep plugin from crashing, fix immediately.");
			return "talk";
		}
	}
	
	/**
	 * Sets the players current talking distance
	 * 
	 * @param p the player
	 * @param distance the desired distance
	 * @since 0.0.1
	 */
	public static void setPlayerTalkDistance(Player p, String distance){
		File pfile = new File(Chat.chatBase + "/" + p.getUniqueId().toString() + ".json");
		if(!pfile.exists()){
			Core.log(Chat.name, Codes.CHATBASE.toString(), "New entry " + p.getDisplayName() + " is being added to the ChatBase.");
			try {
				Core.log(Chat.name, Codes.CHATBASE.toString(), "Attempting to create " + pfile.toString() + ".");
				String rawData = "{" + distanceDefault + "}";
				JSONObject data = new JSONObject(rawData);
				PrintWriter pr = new PrintWriter(pfile);
				pr.println(data.toString());
				pr.close();
				Core.log(Chat.name, Codes.CHATBASE.toString(), p.getDisplayName() + " has been successfully added to the chatbase.");
			} catch (IOException e) {
				e.printStackTrace();
				Core.log(Chat.name, Codes.CHATBASE.toString(), p.getDisplayName() + " was not succesfully added to the chatbase. Check stacktrace above or report it.");
			}
		}
		
		try{
			FileReader fr = new FileReader(pfile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject pData = new JSONObject(json);
			if(distance.equalsIgnoreCase("whisper")){
				pData.put("distance", "whisper");
			}else if(distance.equalsIgnoreCase("yell")){
				pData.put("distance", "yell");
			}else{
				pData.put("distance", "talk");
			}
			pfile.delete();
			PrintWriter pr = new PrintWriter(pfile);
			pr.println(pData.toString());
			pr.close();
		}catch(IOException e){
			e.printStackTrace();
			Core.log(Chat.name, Codes.ERROR.toString(), "Something went wrong when setting the distance for " + p.getDisplayName() + ". Check the Stacktrace above or report it.");
		}
	}
	
	/**
	 * Checks for the players Ooc state
	 * 
	 * @param p The player
	 * @return a boolean
	 * @throws IOException if the file is no found
	 * @since 0.0.3
	 */
	public static boolean checkOoc(Player p) throws IOException{
		File pfile = new File(Chat.chatBase + "/" + p.getUniqueId().toString() + ".json");
		try{
			FileReader fr = new FileReader(pfile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject pData = new JSONObject(json);
			return pData.getBoolean("ooc");
		}catch(IOException e){
			e.printStackTrace();
			Core.log(Chat.name, Codes.ERROR.toString(), "Tried to retrieve " + p.getDisplayName() + " talk distance, but Failed. Read StackTrace above or report it.");
			Core.log(Chat.name, Codes.ERROR.toString(), "Sending talk to keep plugin from crashing, fix immediately.");
		}
		return true;
	}
	
	/**
	 * Sets the players Ooc state
	 * 
	 * @param p the player
	 * @param state Whether the player is or isnt in Ooc
	 * @throws IOException Cant write the .json file
	 * @since 0.0.3
	 */
	public static void setOoc(Player p, boolean state) throws IOException{
		File pfile = new File(Chat.chatBase + "/" + p.getUniqueId().toString() + ".json");
		try{
			FileReader fr = new FileReader(pfile);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject pData = new JSONObject(json);
			pData.put("ooc", state);
			pfile.delete();
			PrintWriter pr = new PrintWriter(pfile);
			pr.println(pData.toString());
			pr.close();
		}catch(IOException e){
			e.printStackTrace();
			Core.log(Chat.name, Codes.ERROR.toString(), "Something went wrong when setting the state for ooc for " + p.getDisplayName() + ". Check the Stacktrace above or report it.");
		}
	}
}
