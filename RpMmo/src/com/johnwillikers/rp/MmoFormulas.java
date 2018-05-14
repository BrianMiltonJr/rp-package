package com.johnwillikers.rp;

public class MmoFormulas {

	public static int[] formulateLevelUp(int currentXp, int currentLevel, int addedXp) {
		int level = currentLevel;
		int xp = currentXp + addedXp;
		if(level<=4) {
			if(xp % 500 == 0) {
				level = level + (xp/500);
				xp = 0;
			}else {
				int surplus = xp % 500;
				int levelXp = xp - surplus;
				level = level + (levelXp/500);
				xp = surplus;
			}
		}
		if(level>=5 && level <=60) {
			if(xp % 1000 == 0) {
				level = level + (xp/1000);
				if(level>60) {
					level = 60;
				}
				xp = 0;
			}else {
				int surplus = xp % 1000;
				int levelXp = xp - surplus;
				level = level + (levelXp/1000);
				if(level>60) {
					level = 60;
				}
				xp = surplus;
			}
		}
		int[] newLevelXp = {level, xp};
		return newLevelXp;
	}
}
