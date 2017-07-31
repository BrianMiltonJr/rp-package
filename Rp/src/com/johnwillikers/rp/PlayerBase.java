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

/**
 * The directory where the playerbase files are located
 */
public static String dir = Core.dir;
/**
 * The masterfile location which holds the rp playername to UUID for system tasks
 */
public static File master_file = new File(dir + "master_file.json");
	
/**
 * Creates the masterfile
 * 
 * @throws IOException MasterFile creation errored
 * @since 0.0.1
 */
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
	
	/**
	 * Saves the masterfile
	 * 
	 * @param json The json string to save the masterfile
	 * @throws IOException Error saving the Masterfile
	 * @since 0.0.1
	 */
	public static void saveMasterFile(String json) throws IOException{
		PrintWriter mf = new PrintWriter(master_file);
		mf.println(json);
		mf.close();
	}
	
	/**
	 * Loads the masterfile
	 * 
	 * @return Returns a JSONObject
	 * @throws IOException Error loading the MasterFile
	 * @since 0.0.1
	 */
	public static JSONObject loadMasterFile() throws IOException{
		FileReader fr = new FileReader(master_file);
		BufferedReader br = new BufferedReader(fr);
		String json = br.readLine();
		br.close();
		fr.close();
		JSONObject data = new JSONObject(json);
		return data;
	}
	
	/**
	 * Appends an entry to the masterfile
	 * 
	 * @param UUID The Player's UUID
	 * @param name The Players new rp name
	 * @throws IOException Error Appending MasterFile
	 * @since 0.0.1
	 */
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
	 * Checks the masterfile for the UUID of a player
	 * 
	 * @param name The players Rp name styled with a _ instead of spaces
	 * @return Returns a {@code String[]} with the UUID of the player
	 * @since 0.0.1
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
	
	/**
	 * Creates the Playerbase and calls execution of the creation of the masterfile
	 * 
	 * @since 0.0.1
	 */
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
	
	/**
	 * Writes a new player to the PlayerBase
	 * 
	 * @param UUID The Players UUID
	 * @param first The Players rp first name
	 * @param last The Players rp last name
	 * @param playerName The Players actual minecraft account name
	 * @param gender The Players gender
	 * @param originalIp The Players Original Ip
	 * @param lastIp The Players Last Ip
	 * @throws IOException Error Writing player to playerbase
	 * @since 0.0.1
	 */
	public static void writePlayer(String UUID, String first, String last, String playerName, String gender, String originalIp, String lastIp) throws IOException{
		JSONObject pfile = new JSONObject().put("first", first).put("last", last).put("playerName", playerName).put("gender", gender).put("originalIp", originalIp).put("lastIp", lastIp);
		PrintWriter write = new PrintWriter(new File(dir + UUID + ".json"));
		write.println(pfile.toString());
		write.close();
		appendMasterFile(UUID, first + "_" + last);
	}
	
	/**
	 * Updates a player in the PlayerBase mostly called when logging in
	 * 
	 * @param UUID The Players UUID
	 * @param newIp The Players new latest ip
	 * @param playerName The Players current Minecraft account name
	 * @throws IOException Error updating player to playerbase
	 * @since 0.0.1
	 */
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
	
	/**
	 * Checks to see if the player is registered in the playerbase
	 * 
	 * @param player The player to be checked
	 * @return a boolean regarding whether it exists or not
	 * @since 0.0.1
	 */
	public static boolean exists(Player player){
		File user = new File(dir + player.getUniqueId().toString() + ".json");
		if(user.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Gets the players info from the playerbase
	 * 
	 * @param player the player to get their information retrieved
	 * @return Returns a {@code String[]} containing the players info
	 * @since 0.0.1
	 */
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
