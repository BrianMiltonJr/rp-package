package com.johnwillikers.rp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

import com.johnwillikers.rp.enums.Codes;

public class PlayerBase {

public static String dir = Core.dir;
public static File master_file = new File(dir + "master_file.json");
	
	public static void createMasterFile() throws IOException{
		Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Attempting to create Master File");
		JSONObject obj = new JSONObject();
		obj.put("NULL", "NULL");
		saveMasterFile(obj.toString());
		if(master_file.exists()){
			Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Master File created successfully");
		}else{
			Core.log(Core.name, Codes.FIRST_LAUNCH.toString(), "Master File not successfully created, check StackTrace above.");
		}
	}
	
	public static void saveMasterFile(String json) throws IOException{
		PrintWriter mf = new PrintWriter(master_file);
		mf.println(json);
		mf.close();
	}
	
	public static JSONObject loadMasterFile() throws IOException{
		FileReader fr = new FileReader(master_file);
		BufferedReader br = new BufferedReader(fr);
		String json = br.readLine();
		br.close();
		fr.close();
		JSONObject data = new JSONObject(json);
		return data;
	}
	
	public static void appendMasterFile(String UUID, String name) throws IOException{
		try{
			JSONObject data = loadMasterFile();
			data.put(name, UUID);
			saveMasterFile(data.toString());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a String[] payload. Index 0 is the Success code and if successful, UUID is assigned into Index 1.
	 */
	public static String[] checkMasteFile(String name){
		JSONObject data;
		try {
			data = loadMasterFile();
			String UUID = data.getString(name);
			String[] good = {"1", UUID};
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.checkMasterFile", "Sending Good Payload. UUID = " + good[1]);
			return good;
		} catch (JSONException e) {
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.checkMasterFile", "Sending Bad Payload.");
			String[] bad = {"0"};
			return bad;
		}catch(IOException e){
			e.printStackTrace();
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.checkMasterFile", "Some sort of IOException, check Stacktrace above, and report it to John Willikers.");
			String[] bad = {"0"};
			return bad;
		}
	}
	public static void createPlayerBaseDir(){
		File pb = new File(dir);
		Core.debug(Core.name, Codes.STARTUP.toString(), "Checking Whether Player Base Location Exists.");
		if(!pb.exists()){
			Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Player Base Location Doesn't Exist. Attempting to create Location.");
			boolean status = pb.mkdirs();
			if(status){
				Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Player Base Location created.");
			}else{
				Core.debug(Core.name, Codes.FIRST_LAUNCH.toString(), "Player Base Location creation failed.");
			}
			Core.debug(Core.name, Codes.FIRST_LAUNCH.toString() , "Attempting to create Master File");
			try {
				createMasterFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Core.debug(Core.name, Codes.STARTUP.toString(), "Player Base Location Exists.");
		}
	}
	
	public static void writePlayer(String UUID, String first, String last, String playerName, String gender, String originalIp, String lastIp) throws IOException{
		JSONObject pfile = new JSONObject().put("first", first).put("last", last).put("playerName", playerName).put("gender", gender).put("originalIp", originalIp).put("lastIp", lastIp);
		PrintWriter write = new PrintWriter(new File(dir + UUID + ".json"));
		write.println(pfile.toString());
		write.close();
		appendMasterFile(UUID, first + "_" + last);
	}
	
	public static void updatePlayer(String UUID, String newIp, String playerName) throws IOException{
		File pfile = new File(dir + UUID + ".json");
		FileReader fr = new FileReader(pfile);
		BufferedReader br = new BufferedReader(fr);
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Read " + pfile.toString());
		String json = br.readLine();
		br.close();
		fr.close();
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Assigning data into JSONObject");
		JSONObject data = new JSONObject(json);
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Updating playerName from " + data.getString("playerName") + " to " + playerName);
		data.put("playerName", playerName);
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Updating lastIp from " + data.getString("lastIp") + " to " + newIp + " for " + playerName);
		data.put("lastIp", newIp);
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Checking to see if " + pfile.toString() + " exists");
		if(pfile.exists()){
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", pfile.toString() + " exists, deleting and writing a new " + pfile.toString());
			pfile.delete();
			writePlayer(UUID, data.get("first").toString(), data.get("last").toString(), data.get("playerName").toString(), data.get("gender").toString(), data.get("originalIp").toString(), data.get("lastIp").toString());
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", pfile.toString() + " Successfully updated.");
		}else{
			Core.debug(Core.name, Codes.DEBUG + "PlayerBase.updatePlayer", "Idk know bro, this is kinda hard. This shouldn't happen. The file had to be there to get most of this data so. I doubt we will see this.");
			writePlayer(UUID, data.get("first").toString(), data.get("last").toString(), data.get("playerName").toString(), data.get("gender").toString(), data.get("originalIp").toString(), data.get("lastIp").toString());
		}
	}
	
	public static boolean exists(Player player){
		File user = new File(dir + player.getUniqueId().toString() + ".json");
		if(user.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	public static String[] getPlayerInfo(Player player){
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.getPlayerInfo", dir + player.getUniqueId().toString() + ".json");
		File user = new File(dir + player.getUniqueId().toString() + ".json");
		try{
			FileReader fr = new FileReader(user);
			BufferedReader br = new BufferedReader(fr);
			String json = br.readLine();
			br.close();
			fr.close();
			JSONObject obj = new JSONObject(json);
			String first = obj.getString("first");
			String last = obj.getString("last");
			String playerName = obj.getString("playerName");
			String gender = obj.getString("gender");
			String originalIp = obj.getString("originalIp");
			String lastIp = obj.getString("lastIp");
			Core.debug(Core.name, "PlayerBase.getPlayerInfo", "Ip at assignment LastIp: " + lastIp + " Original Ip: " + originalIp);
			String[] info = {"1", first, last, playerName, gender, originalIp, lastIp};
			Core.debug(Core.name, "PlayerBase.getPlayerInfo", first + " " + last + " " + playerName + " " + gender + " " + originalIp + " " + lastIp);
			Core.debug(Core.name, "PlayerBase.getPlayerInfo", "Returning Info String[]");
			return info;
		}catch(IOException e){
			e.printStackTrace();
		}
		String[] fail = {"0"};
		Core.debug(Core.name, Codes.DEBUG + "PlayerBase.getPlayerInfo", "Returning Fail String[]");
		return fail;
	}
}
