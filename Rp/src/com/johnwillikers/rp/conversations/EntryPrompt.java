package com.johnwillikers.rp.conversations;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class EntryPrompt extends StringPrompt{

	@Override
	public Prompt acceptInput(ConversationContext con, String ans) {
		Conversable c = con.getForWhom();
		Player player = (Player) c;
		if(ans.equalsIgnoreCase("man")){
			con.setSessionData("gender", 1);
			return new First();
		}else if(ans.equalsIgnoreCase("lady")){
			con.setSessionData("gender", 0);
			return new First();
		}
		player.sendMessage("Please Type Either Man or Woman.");
		return new EntryPrompt();
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nWho is this? \nAre you a Man or Lady?";
	}

}
