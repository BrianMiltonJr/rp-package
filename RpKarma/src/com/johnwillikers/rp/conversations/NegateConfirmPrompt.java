package com.johnwillikers.rp.conversations;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import com.johnwillikers.rp.Core;
import com.johnwillikers.rp.DbHandler;
import com.johnwillikers.rp.Karma;
import com.johnwillikers.rp.callbacks.MySqlCallback;
import com.johnwillikers.rp.Utilities;

public class NegateConfirmPrompt extends StringPrompt{

	public String question;
	public String offense;
	public String uuid;
	public String desc;
	public Player gm;
	public String name;
	
	public NegateConfirmPrompt(String question, String offense, String uuid, String desc, Player gm, String name){
		this.question = question;
		this.offense = offense;
		this.uuid = uuid;
		this.desc = desc;
		this.gm = gm;
		this.name = name;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String ans) {
		if(ans.equalsIgnoreCase("y") || ans.equalsIgnoreCase("yes")) {
			if(Core.dataMethod.equalsIgnoreCase("mysql")) {
				
				//Data we want to be accessible inside our Callback
				final String [] complaintData = {this.desc, this.offense, Utilities.getDate()};
				final Player guy = this.gm;
				final String offenderName = this.name;
				
				//Asynchronously calling for the player's id in the players table
				String query = "SELECT id FROM players WHERE uuid='" + this.uuid + "';";
				DbHandler.executeQuery(Karma.plugin, query, Karma.name, "NegateConfirmPrompt.acceptInput", new MySqlCallback() {
					@Override
					public void onQueryDone(ResultSet rs) {
						try {
							if(rs.next()) {
								//Makes a Final reference of the id so we can pull it farther in
								final String id = rs.getString(1);
								rs.close();
								
								//Grabbing the players current karma
								String getKarma = "SELECT karma FROM karma WHERE player_id=" + id + ";";
								DbHandler.executeQuery(Karma.plugin, getKarma, Karma.name, "NegateConfirmPrompt.acceptInput", new MySqlCallback() {

									@Override
									public void onQueryDone(ResultSet rs) {
										try {
											if(rs.next()) {
												//Storing the karma then updating it with the offense
												final int karma = rs.getInt(1) + Integer.valueOf(complaintData[1]);
												rs.close();
												String query = "SELECT id FROM gamemasters WHERE uuid='" + guy.getUniqueId().toString() + "';";
												DbHandler.executeQuery(Karma.plugin, query, Karma.name, "NegateConfirmPrompt.acceptInput", new MySqlCallback() {

													@Override
													public void onQueryDone(ResultSet rs) {
														try {
															if(rs.next()) {
																final int gmId = rs.getInt(1);
																rs.close();
																//MySql Queries that update the players karma and insert the complaint
																String negateKarma = "UPDATE karma SET karma = " + karma + " WHERE player_id=" + id + ";";
																String complain = "INSERT INTO reports ( gm_id, player_id, body, actions, date ) VALUES ( " + gmId + ", " + id + ", '" + complaintData[0] +"', '" + complaintData[1] + "', '" + complaintData[2] + "' );";
																DbHandler.executeUpdate(negateKarma, Karma.name);
																DbHandler.executeUpdate(complain, Karma.name);
															}
														} catch (SQLException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														
													}
													
												});
											}
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									
								});
							}else {
								guy.sendMessage(offenderName + " does not exist.");
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

				});
			}else {
				
			}
			return Prompt.END_OF_CONVERSATION;
		}else {
			this.gm.sendMessage("You have cancelled the report");
			return Prompt.END_OF_CONVERSATION;
		}
		/* JSON methodology my dude
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
		*/
		
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return question;
	}

}
