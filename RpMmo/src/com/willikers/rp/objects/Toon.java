package com.willikers.rp.objects;

import com.johnwillikers.rp.MmoBaseMySql;

public class Toon {

	private final int skillLevel = 3;
	private final int statLevel = 5;
	
	private int id;
	private int playerId;
	
	private int str;
	private int agi;
	
	private int dex;
	private int cons;
	private int spirit;
	
	private int xp;
	private int level;
	
	private int sword;
	private int shield;
	private int axe;
	private int bow;
	
	private int larm;
	private int harm;
	
	private int skillPoints;
	private int statPoints;
	
	public Toon(int playerId) {
		this.playerId = playerId;
		this.id = MmoBaseMySql.getToonId(this.playerId);
		int[] toon = MmoBaseMySql.getToon(this.id);
		int[] skill = MmoBaseMySql.getSkills(this.id);
		int[] stat = MmoBaseMySql.getStats(this.id);
		this.xp = toon[1];
		this.level = toon[2];
		this.statPoints = toon[3];
		this.skillPoints = toon[4];
		this.sword = skill[1];
		this.shield = skill[2];
		this.axe = skill[3];
		this.bow = skill[4];
		this.larm = skill[5];
		this.harm = skill[6];
		this.str = stat[1];
		this.agi = stat[2];
		this.dex = stat[3];
		this.cons = stat[4];
		this.spirit = stat[5];
		
	}
	
	public int[] getStats() {
		int[] stats = {this.str, this.agi, this.dex, this.cons, this.spirit};
		return stats;
	}
	
	public int[] getSkills() {
		int[] skills = {this.sword, this.shield, this.axe, this.bow, this.larm, this.harm};
		return skills;
	}
	
	
	public String generateToonMsg() {
		String msg = "Your Resources:"
				+ "\n*  HP: " + this.cons *2
				+ "\nYour Stats:"
				+ "\n*  Strength: " + this.str
				+ "\n*  Agility: " + this.agi
				+ "\n*  Dexterity: " + this.dex
				+ "\n*  Constitution: " + this.cons
				+ "\n*  Spirit: " + this.spirit
				+ "\nYour Skills:"
				+ "\n*  Swords: " + this.sword
				+ "\n*  Shields: " + this.shield
				+ "\n*  Axes: " + this.axe
				+ "\n*  Bows: " + this.bow
				+ "\n*  Light Armor: " + this.larm
				+ "\n*  Heavy Armor: " + this.harm;
		return msg;
	}
	
	public void update() {
		String[] toonData = {"xp", String.valueOf(this.xp), "level", String.valueOf(this.level), "stat_points", String.valueOf(this.statPoints), "skill_points", String.valueOf(this.skillPoints)};
		String[] skillData = {"strength", String.valueOf(this.str), "dexterity", String.valueOf(this.dex), "agility", String.valueOf(this.agi), "constitution", String.valueOf(this.cons), "spirit", String.valueOf(this.spirit)};
		String[] statData = {"sword", String.valueOf(this.sword), "shield", String.valueOf(this.shield), "axe", String.valueOf(this.axe), "bow", String.valueOf(this.bow), "light_armor", String.valueOf(this.larm), "heavy_armor", String.valueOf(this.harm)};
		MmoBaseMySql.updateMmoTable(this.id, "toons", toonData);
		MmoBaseMySql.updateMmoTable(this.id, "skills", skillData);
		MmoBaseMySql.updateMmoTable(this.id, "stats", statData);
	}
	
	public boolean levelStat(int statPoints, String stat) {
		if(skillPoints>this.skillPoints || skillPoints==0) {
			return false;
		}else {
			this.skillPoints = this.skillPoints - skillPoints;
			switch (stat) {
				case "str": this.str = this.str + skillPoints;
						break;
				case "agi": this.agi = this.agi + skillPoints;
						break;
				case "dex": this.dex = this.dex + skillPoints;
						break;
				case "cons": this.cons = this.cons + skillPoints;
						break;
				case "spirit": this.spirit = this.spirit + skillPoints;
						break;
			}
			return true;
		}
	}
	
	public boolean levelSkill(int skillPoints, String skill) {
		if(skillPoints>this.skillPoints || skillPoints==0) {
			return false;
		}else {
			this.skillPoints = this.skillPoints - skillPoints;
			switch (skill) {
				case "sword": this.sword = this.sword + skillPoints;
						break;
				case "shield": this.shield = this.shield + skillPoints;
						break;
				case "axe": this.axe = this.axe + skillPoints;
						break;
				case "bow": this.bow = this.bow + skillPoints;
						break;
				case "larm": this.larm = this.larm + skillPoints;
						break;
				case "harm": this.harm = this.harm + skillPoints;
						break;
			}
			return true;
		}
	}
	
	public void experience(int xp) {
		this.xp = this.xp + xp;
		if(this.level<=5) {
			if(xp>=500) {
				this.xp = this.xp - 500;
			}
		}else {
			if(xp>=1000) {
				this.xp = this.xp - 1000;
			}
		}
		this.level++;
		this.statPoints = this.statPoints + this.statLevel;
		this.skillPoints = this.skillPoints + this.skillLevel;
	}
}
