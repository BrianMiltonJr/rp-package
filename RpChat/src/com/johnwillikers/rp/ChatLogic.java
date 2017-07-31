package com.johnwillikers.rp;

import com.johnwillikers.rp.enums.Codes;

public class ChatLogic {
	
	/**
	 * The current range for hearing a whisper
	 */
	public static int whisperDistance = 5;
	/**
	 * The current range for hearing normal talking
	 */
	public static int talkDistance = 10;
	/**
	 * The current range for hearing someone yelling
	 */
	public static int yellDistance = 20;
	
	/**
	 * Loads the set distance values for chat
	 */
	public static void startUp(){
		int[] settings = ChatBase.getSettings();
		if(settings[0] == 1){
			int whisper = settings[1];
			int talk = settings[2];
			int yell = settings[3];
			assignDistances(whisper, talk, yell);
		}
	}
	
	/**
	 * returns the int version of the string verion of distance
	 * 
	 * @param distance the string version of distance
	 * @return {@code int}
	 * @since 0.0.1
	 */
	public static int determineDistance(String distance){
		Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatLogic.determineDistance", "Distance: " + distance);
		if(distance.equalsIgnoreCase("whisper")){
			return whisperDistance;
		}else if(distance.equalsIgnoreCase("yell")){
			return yellDistance;
		}else if(distance.equalsIgnoreCase("talk")){
			return talkDistance;
		}
		Core.debug(Chat.name, Codes.DEBUG.toString() + "ChatLogic.determineDistance", "Returning 10");
		return 10;
	}
	
	/**
	 * Assigns the distance values
	 * 
	 * @param whisper the whisper distance
	 * @param talk the talk distance
	 * @param yell the yell distance
	 */
	public static void assignDistances(int whisper, int talk, int yell){
		Core.log(Chat.name, Codes.STARTUP.toString(), "Attempting to assign Distance values for chat.");
		whisperDistance = whisper;
		Core.log(Chat.name, Codes.STARTUP.toString(), "Whisper Distance = " + whisperDistance);
		talkDistance = talk;	
		Core.log(Chat.name, Codes.STARTUP.toString(), "Talk Distance = " + talkDistance);
		yellDistance = yell;
		Core.log(Chat.name, Codes.STARTUP.toString(), "Yell Distance = " + yellDistance);
		Core.log(Chat.name, Codes.STARTUP.toString(), "Distance values succesfully assigned");
	}
}
