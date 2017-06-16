package com.johnwillikers.rp;

import com.johnwillikers.rp.enums.Codes;

public class ChatLogic {
	
	public static int whisperDistance = 5;
	public static int talkDistance = 10;
	public static int yellDistance = 20;
	
	public static void startUp(){
		int[] settings = ChatBase.getSettings();
		if(settings[0] == 1){
			int whisper = settings[1];
			int talk = settings[2];
			int yell = settings[3];
			assignDistances(whisper, talk, yell);
		}
	}
	
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
