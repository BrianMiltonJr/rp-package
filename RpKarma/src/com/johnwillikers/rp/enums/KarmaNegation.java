package com.johnwillikers.rp.enums;

public enum KarmaNegation {
	
	SPAMMING(-100, "Spamming", "Karma - 100");
	
	
	private final int amount;
	private final String name;
	private final String action;
	
	private KarmaNegation(final int amount, final String name, final String action){
		this.amount = amount;
		this.name = name;
		this.action = action;
	}
	
	public String[] value(){
		String[] data = {String.valueOf(amount), name, action};
		return data;
	}

}
