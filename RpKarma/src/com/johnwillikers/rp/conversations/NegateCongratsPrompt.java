package com.johnwillikers.rp.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class NegateCongratsPrompt extends MessagePrompt{

	@Override
	public String getPromptText(ConversationContext con) {
		// TODO Auto-generated method stub
		return (String) con.getSessionData("gmMessage");
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext con) {
		// TODO Auto-generated method stub
		return Prompt.END_OF_CONVERSATION;
	}

}
