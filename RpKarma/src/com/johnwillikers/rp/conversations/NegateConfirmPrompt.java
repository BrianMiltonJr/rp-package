package com.johnwillikers.rp.conversations;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.KarmaBase;
import com.johnwillikers.rp.KarmaLogic;
import com.johnwillikers.rp.PlayerBase;
import com.johnwillikers.rp.Utilities;
import com.johnwillikers.rp.enums.Codes;
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
		Core.debug(Karma.name, Codes.DEBUG.toString() + "NegateConfirmPrompt.acceptInput", "ans = " + ans);
		@SuppressWarnings("unused")
		Player offender = Bukkit.getPlayer(this.uuid);
		if(ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("yes")){
			Core.debug(Karma.name, Codes.DEBUG.toString() + "NegateConfimPrompt.acceptInput", "Answer was equal to y || yes");
			//Analyze args[2] for negation amount and such
			for(KarmaNegation kn : KarmaNegation.values()){
				String[] values = kn.value();
				if(offense.equalsIgnoreCase(values[1])){
					//Update offenders karma file and inform them of this
					String[] gmInfo = PlayerBase.getPlayerInfo(this.player);
					JSONObject incident = new JSONObject().put("gm", gmInfo[1] + " " + gmInfo[2]).put("date", Utilities.getDate()).put("desc", desc).put("actions", values[2]);
					try {
						KarmaLogic.negate(this.uuid, Integer.valueOf(values[0]), incident);
						JSONObject kInfo = KarmaBase.getKarmaInfo(this.uuid);
						@SuppressWarnings("unused")
						String reprimandMsg = "You were reprimanded by: " + gmInfo[1].toString() + " " + gmInfo[2].toString() + "\n" +
								   "Crime: " + values[1].toString() + "\n" + 
								   "Description: " + this.desc + "\n" +
								   "Actions: " + values[2].toString() + "\n" + 
								   "New Karma: " + String.valueOf(kInfo.getInt("karma"));
						//TODO figure out a way to send a msg to the offender from here.
						//KarmaLogic.sendOffenderMsg(offender, reprimandMsg);
						con.setSessionData("gmMessage", name + " has been successfully negated. Good Work " + gmInfo[1].toString() + " " + gmInfo[2].toString() + "!");
						return new NegateCongratsPrompt();
					} catch (NumberFormatException e) {
						e.printStackTrace();
						player.sendMessage("Random NumberFormatException");
						return Prompt.END_OF_CONVERSATION;
					} catch (IOException e) {
						e.printStackTrace();
						player.sendMessage("Player does not exist or file is in use. Please try again laster if latter.");
						return Prompt.END_OF_CONVERSATION;
					}
				}
			}
		}else{
			player.sendMessage("You did not confirm the report.");
			return Prompt.END_OF_CONVERSATION;	
		}
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return question;
	}

}
