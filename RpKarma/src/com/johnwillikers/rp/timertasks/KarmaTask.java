package com.johnwillikers.rp.timertasks;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.KarmaBase;
import com.johnwillikers.rp.KarmaLogic;

public class KarmaTask extends TimerTask{

	@Override
	public void run() {
		Core.log(Karma.name, "KarmaTask", "Checking Karma");
		for(Player player : Bukkit.getOnlinePlayers()){
			KarmaLogic.aid(player, Karma.karmaUpAmount);
			JSONObject kFile = KarmaBase.getKarmaInfo(player.getUniqueId().toString());
			KarmaLogic.karmaCheck(player, kFile.getInt("karma"));
		}
	}

}
