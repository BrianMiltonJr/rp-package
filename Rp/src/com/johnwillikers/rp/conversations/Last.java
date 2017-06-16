package com.johnwillikers.rp.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class Last extends StringPrompt{

	@Override
	public Prompt acceptInput(ConversationContext con, String s) {
		con.setSessionData("last", s);
		return new Confirmation();
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return "\n\n\n\n\n\n\n\nWhat is your last name?";
	}

}
