package com.johnwillikers.rp.conversations;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.johnwillikers.rp.Core;

public class WelcomePrompt extends MessagePrompt{

	@Override
	public String getPromptText(ConversationContext con) {
		Conversable c = con.getForWhom();
		Player player = (Player) c;
		if(Core.dependables[0] == 1){
			player.chat("/uujdj123123412o458f1nd1");
		}
		if(Core.dependables[0] == 1){
			player.chat("/0e812e08h02v8he0182vhe1");
		}
		if(Integer.valueOf(con.getSessionData("gender").toString()) == 0){
			Bukkit.broadcastMessage("Welcome " + con.getSessionData("first").toString() + " " + con.getSessionData("last").toString() + " to " + Core.townName);
			return "\n\n\n\n\n\n\nWelcome to " + Core.townName + " Ms. " + player.getDisplayName() + ".";
		}else{
			Bukkit.broadcastMessage("Welcome " + con.getSessionData("first").toString() + " " + con.getSessionData("last").toString() + " to " + Core.townName);
			return "\n\n\n\n\n\n\nWelcome to " + Core.townName + " Mr. " + player.getDisplayName() + ".";
		}
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext arg0) {
		// TODO Auto-generated method stub
		return Prompt.END_OF_CONVERSATION;
	}

}
