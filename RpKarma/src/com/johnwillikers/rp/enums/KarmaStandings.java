package com.johnwillikers.rp.enums;

public enum KarmaStandings {
	
	BAN(-500),
	KICK(-250),
	REVIEW(0),
	STARTER(100),
	KNOWN(250),
	REKNOWN(500),
	MEMBER(600),
	VETERAN(700),
	ELDER(800);
	
	private final int amount;
	
	private KarmaStandings(final int amount){
		this.amount = amount;
	}
	
	public int toInt(){
		return amount;
	}

}
