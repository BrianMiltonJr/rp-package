package com.johnwillikers.rp.conversations;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.johnwillikers.rp.Codes;
import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.KarmaBase;
import com.johnwillikers.rp.KarmaLogic;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.Utilities;
import com.johnwillikers.rp.enums.KarmaNegation;

public class NegateConfirmPrompt extends StringPrompt{

	public String question;
	public String offense;
	public String uuid;
	public String desc;
	public Player player;
	public String name;
	
	public NegateConfirmPrompt(String question, String offense, String uuid, String desc, Player player, String name){
		this.question = question;
		this.offense = offense;
		this.uuid = uuid;
		this.desc = desc;
		this.player = player;
		this.name = name;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String ans) {
		Core.debug(Karma.name, Codes.DEBUG + "NegateConfirmPrompt.acceptInput", "ans = " + ans);
		if(ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("yes")){
			Core.debug(Karma.name, Codes.DEBUG + "NegateConfimPrompt.acceptInput", "Answer was equal to y || yes");
			//Analyze args[2] for negation amount and such
			for(KarmaNegation kn : KarmaNegation.values()){
				String[] values = kn.value();
				if(offense.equalsIgnoreCase(values[1])){
					//Update offenders karma file and inform them of this
					String[] gmInfo = PlayerBase.getPlayerInfo(player);
					JSONObject incident = new JSONObject().put("gm", gmInfo[1] + " " + gmInfo[2]).put("date", Utilities.getDate()).put("desc", desc).put("actions", values[2]);
					try {
						KarmaLogic.negate(uuid, Integer.valueOf(values[0]), incident);
						JSONObject kInfo = KarmaBase.getKarmaInfo(Bukkit.getPlayer(uuid));
						Bukkit.getPlayer(uuid).sendMessage("You were reprimanded by: " + gmInfo[1] + " " + gmInfo[2] + "\n" +
														   "Crime: " + values[1].toString() + "\n" + 
														   "Description: " + desc + "\n" +
														   "Actions: " + values[2].toString() + "\n" + 
														   "New Karma: " + kInfo.getInt("karma"));
						con.setSessionData("gmMessage", name + " has been successfully negated. Good Work " + gmInfo[1].toString() + " " + gmInfo[2].toString() + "!");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			
		}
		return new NegateCongratsPrompt();
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return question;
	}

}
