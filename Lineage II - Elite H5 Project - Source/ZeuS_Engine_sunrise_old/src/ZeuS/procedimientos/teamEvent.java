package ZeuS.procedimientos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;

public class teamEvent {

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(teamEvent.class.getName());


	private final static String btnbackMain = "<button value=\"BACK\" action=\"bypass -h admin_zeus_op coliseevent backmain\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

	private static Vector<Integer>team1 =  new Vector<Integer>();
	private static Vector<Integer>team2 =  new Vector<Integer>();
	private static Vector<Integer>team3 =  new Vector<Integer>();
	private static Vector<Integer>team4 =  new Vector<Integer>();
	private static Vector<Integer>team5 =  new Vector<Integer>();
	private static Vector<Integer>team6 =  new Vector<Integer>();
	private static Vector<Integer>team7 =  new Vector<Integer>();
	private static Vector<Integer>team8 =  new Vector<Integer>();
	private static Vector<Integer>team9 =  new Vector<Integer>();
	private static Vector<Integer>team10 =  new Vector<Integer>();

	private static Map<Integer, Integer>Reward1 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward2 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward3 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward4 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward5 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward6 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward7 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward8 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward9 = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>Reward10 = new HashMap<Integer, Integer>();

	private static Vector<Integer> AllCharTeam = new Vector<Integer>();
	private static Map<Integer, L2PcInstance> InstancePlayer = new HashMap<Integer, L2PcInstance>();
	private static int Equipos=2, maxEnchant=12 , maxPlayerForTeam = 10, MinLevel=40, MaxLevel=85;
	private static Vector<Integer> blockItem = new Vector<Integer>();
	private static boolean eventoStart=false, eventoRegister=false, IsTeamEvent=true, IsOnlyNoble=false, IsOnlyHero=false, TeleportIntoRegisterPlayer = false;
	private static int lastSelect = 0, rechazo = 0;

	private static int[] OlympCoor= {149453,46774,-3412};
	private static int[] OutOlympCoor= {153668,46726,-3434};

	static String[] GradeNames = {"","D","C","B","A","S","S80","S84"};

	private static Vector<String>GradesAllow = new Vector<String>();

	private static void setReward(int team,int idItem, int cantidad){
		if(team==1){
			Reward1.put(idItem, cantidad);
		}else if(team==2){
			Reward2.put(idItem, cantidad);
		}else if(team==3){
			Reward3.put(idItem, cantidad);
		}else if(team==4){
			Reward4.put(idItem, cantidad);
		}else if(team==5){
			Reward5.put(idItem, cantidad);
		}else if(team==6){
			Reward6.put(idItem, cantidad);
		}else if(team==7){
			Reward7.put(idItem, cantidad);
		}else if(team==8){
			Reward8.put(idItem, cantidad);
		}else if(team==9){
			Reward9.put(idItem, cantidad);
		}else if(team==10){
			Reward10.put(idItem, cantidad);
		}
	}



	public static void getShowRewardbyTeamMain(L2PcInstance player, int team){
		String ValIdItem = "<edit var=\"idItem\" width=80>";
		String ValCantidad = "<edit var=\"idCant\" width=80>";

		String TablaIngreso = "<table width=280><tr><td width=140 align=center>Id Item</td><td width=140 align=left>" + ValIdItem +"</td></tr>";
		TablaIngreso +="<tr><td width=140 align=center>Amount</td><td width=140 align=left>" + ValCantidad + "</td></tr></table>";

		String BypassAddItem = "bypass -h admin_zeus_op coliseevent addreward "+ String.valueOf(team) +" $iditem $idCant "+ String.valueOf(team) ;
		String btnAddItem = "<button value=\"Add Reward\" action=\""+ BypassAddItem +"\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypass = "bypass -h admin_zeus_op coliseevent ShowRe %SHOWID%";
		String BtnVer = "<button value=\"Rew.%SHOWID%\" action=\""+ btnBypass +"\" width=52 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String botones = "<table width=280><tr>";

		for(int i=1;i<=10;i++){
			if(i==6){
				botones +="</tr><tr>";
			}
			botones += "<td width=40>"+BtnVer.replace("%SHOWID%", String.valueOf(i))+"</td>";
		}

		if(botones.endsWith("</td>")){
			botones+="</tr></table>";
		}else{
			botones+="</table>";
		}



		String html="<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		html += central.LineaDivisora(1) + central.headFormat("You now see the Reward number " + String.valueOf(team))+central.LineaDivisora(1);
		html += central.headFormat(botones);
		html += central.LineaDivisora(1) + central.headFormat(TablaIngreso+"<br>"+btnAddItem+"<br1>") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getRewardTeamShow(team)) + central.LineaDivisora(1);
		html += btnbackMain + central.getPieHTML() + "</center></body></html>";
		opera.enviarHTML(player, html);
	}

	@SuppressWarnings("rawtypes")
	public static String getRewardTeamShow(int team){
		Map<Integer, Integer> toShow = new HashMap<Integer, Integer>();
		if(team==1){
			toShow=Reward1;
		}else if(team==2){
			toShow=Reward2;
		}else if(team==3){
			toShow=Reward3;
		}else if(team==4){
			toShow=Reward4;
		}else if(team==5){
			toShow=Reward5;
		}else if(team==6){
			toShow=Reward6;
		}else if(team==7){
			toShow=Reward7;
		}else if(team==8){
			toShow=Reward8;
		}else if(team==9){
			toShow=Reward9;
		}else if(team==10){
			toShow=Reward10;
		}

		String BypassRemoveItem = "bypass -h admin_zeus_op coliseevent remitem %IDITEM% "+ String.valueOf(team) ;
		String btnRemoItem = "<button value=\"Remove\" action=\""+ BypassRemoveItem +"\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		Iterator itr = toShow.entrySet().iterator();
		String html="";
		int Cont=0;
		if(toShow !=null) {
			html = "<center><table width=280 align=center>";
			while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				if(Cont==0){
					html+="<tr>";
				}
				String idItem = e.getKey().toString();
				String Cantidad = e.getValue().toString();
				html +="<td width=140 align=center fixwidth=138>"+ Cantidad+ " of " + central.getNombreITEMbyID(Integer.valueOf(idItem)) +"<br>"+ btnRemoItem.replace("%IDITEM%", idItem) +"<br1></td>";
				Cont++;
				if((Cont%2)==0){
					Cont=0;
					html+="</tr>";
				}
			}
			if(html.endsWith("</td>")){
				html+="<td width=140></td></tr>";
			}
			if(html.endsWith("</tr>")){
				html+="</table></center><br>";
			}
		}
		return html;
	}

	private static boolean haveItemBlock(L2PcInstance player){
		for(L2ItemInstance Item : player.getInventory().getItems()){
			if(Item.getEnchantLevel()>maxEnchant){
				central.msgbox("You have items in your inventary that exceeds allowable enchanted limit of " + String.valueOf(maxEnchant), player);
				return true;
			}
			if(blockItem!=null){
				if(blockItem.contains( opera.getIdItem(Item) )){
					central.msgbox("You have a locked items in your inventory.("+ central.getNombreITEMbyID(opera.getIdItem(Item)) +")", player);
					return true;
				}
			}
			if(GradesAllow!=null){
				if(GradesAllow.size()>0){
					if(Item.isArmor() || Item.isWeapon() || Item.isEquipable()) {
						if(!GradesAllow.contains(GradeNames[Item.getItem().getCrystalType().getId()])){
							central.msgbox("You have a locked items by grade in your inventory.("+ central.getNombreITEMbyID(opera.getIdItem(Item)) +"), expertise blocked " + GradeNames[Item.getItem().getCrystalType().getCrystalId()], player);
							return true;
						}
					}
				}
			}
			if(player.getLevel()<MinLevel){
				central.msgbox("You level is not allow. Min level(%MIN%), Max level(%MAX%)".replace("%MIN", String.valueOf(MinLevel)).replace("%MAX%", String.valueOf(MaxLevel)), player);
				return true;
			}
			if(player.getLevel()>MaxLevel){
				central.msgbox("You level is not allow. Min level(%MIN%), Max level(%MAX%)".replace("%MIN", String.valueOf(MinLevel)).replace("%MAX%", String.valueOf(MaxLevel)), player);
				return true;
			}
		}
		return false;
	}

	private static boolean haveTeamSpaces(){
		if((Equipos >= 1)){
			if(team1 == null){
				return true;
			}
			if(team1.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 2)){
			if(team2 == null){
				return true;
			}
			if(team2.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 3)){
			if(team3 == null){
				return true;
			}
			if(team3.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 4)){
			if(team4 == null){
				return true;
			}
			if(team4.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 5)){
			if(team5 == null){
				return true;
			}
			if(team5.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 6)){
			if(team6 == null){
				return true;
			}
			if(team6.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 7)){
			if(team7 == null){
				return true;
			}
			if(team7.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 8)){
			if(team8 == null){
				return true;
			}
			if(team8.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 9)){
			if(team9 == null){
				return true;
			}
			if(team9.size() < maxPlayerForTeam){
				return true;
			}
		}
		if((Equipos >= 10)){
			if(team10 == null){
				return true;
			}
			if(team10.size() < maxPlayerForTeam){
				return true;
			}
		}

		return false;
	}

	private static boolean checkTeamSpace(int TeamCheckID){
		if(rechazo < 20){
			if(TeamCheckID == lastSelect){
				rechazo++;
				return false;
			}
		}


		if(TeamCheckID==1){
			if(team1==null){
				return true;
			}
			return team1.size()<maxPlayerForTeam;
		}else if(TeamCheckID==2){
			if(team2==null){
				return true;
			}
			return team2.size()<maxPlayerForTeam;
		}else if(TeamCheckID==3){
			if(team3==null){
				return true;
			}
			return team3.size()<maxPlayerForTeam;
		}else if(TeamCheckID==4){
			if(team4==null){
				return true;
			}
			return team4.size()<maxPlayerForTeam;
		}else if(TeamCheckID==5){
			if(team5==null){
				return true;
			}
			return team5.size()<maxPlayerForTeam;
		}else if(TeamCheckID==6){
			if(team6==null){
				return true;
			}
			return team6.size()<maxPlayerForTeam;
		}else if(TeamCheckID==7){
			if(team7==null){
				return true;
			}
			return team7.size()<maxPlayerForTeam;
		}else if(TeamCheckID==8){
			if(team8==null){
				return true;
			}
			return team8.size()<maxPlayerForTeam;
		}else if(TeamCheckID==9){
			if(team9==null){
				return true;
			}
			return team9.size()<maxPlayerForTeam;
		}else if(TeamCheckID==10){
			if(team10==null){
				return true;
			}
			return team10.size()<maxPlayerForTeam;
		}
		return false;
	}

	private static void removePlayer(int idPlayer){
		removePlayer(idPlayer,true);
	}

	private static void removePlayer(int idPlayer,boolean removeFromTeamVector){
		if(removeFromTeamVector){
			if(team1!=null){
				if(team1.contains(idPlayer)){
					team1.removeElement(idPlayer);
				}
			}
			if(team2!=null){
				if(team2.contains(idPlayer)){
					team2.removeElement(idPlayer);
				}
			}
			if(team3!=null){
				if(team3.contains(idPlayer)){
					team3.removeElement(idPlayer);
				}
			}
			if(team4!=null){
				if(team4.contains(idPlayer)){
					team4.removeElement(idPlayer);
				}
			}
			if(team5!=null){
				if(team5.contains(idPlayer)){
					team5.removeElement(idPlayer);
				}
			}
			if(team6!=null){
				if(team6.contains(idPlayer)){
					team6.removeElement(idPlayer);
				}
			}
			if(team7!=null){
				if(team7.contains(idPlayer)){
					team7.removeElement(idPlayer);
				}
			}
			if(team8!=null){
				if(team8.contains(idPlayer)){
					team8.removeElement(idPlayer);
				}
			}
			if(team9!=null){
				if(team9.contains(idPlayer)){
					team9.removeElement(idPlayer);
				}
			}
			if(team10!=null){
				if(team10.contains(idPlayer)){
					team10.removeElement(idPlayer);
				}
			}
		}

		AllCharTeam.removeElement(idPlayer);
		InstancePlayer.remove(idPlayer);
		L2PcInstance player = opera.getPlayerByID(idPlayer);
		if(player!=null){
			if(player.isOnline()){
				cleanNameColor(player);
				setOutColiseum(player);
				if(removeFromTeamVector){
					central.msgbox("You have been removed from the event by adm / gm", player);
				}else{
					central.msgbox("The event is over. Thanks for coming.", player);
				}
				setInnobilidad(player,false);
			}
		}
	}

	private static void setOutColiseum(L2PcInstance player){
		player.teleToLocation(OutOlympCoor[0], OutOlympCoor[1], OutOlympCoor[2]);
	}

	private static void cleanNameColor(L2PcInstance player){
		player.getAppearance().setNameColor(255,255,255);
		player.broadcastUserInfo();
	}

	private static void setPlayerToTeam(int idTeam, L2PcInstance player){
		int idChar = player.getObjectId();
		if(idTeam==1){
			team1.add(idChar);
		}else if(idTeam==2){
			team2.add(idChar);
		}else if(idTeam==3){
			team3.add(idChar);
		}else if(idTeam==4){
			team4.add(idChar);
		}else if(idTeam==5){
			team5.add(idChar);
		}else if(idTeam==6){
			team6.add(idChar);
		}else if(idTeam==7){
			team7.add(idChar);
		}else if(idTeam==8){
			team8.add(idChar);
		}else if(idTeam==9){
			team9.add(idChar);
		}else if(idTeam==10){
			team10.add(idChar);
		}

		InstancePlayer.put(idChar, player);
		AllCharTeam.add(idChar);
	}

	protected static void remItem(int team, int idItem){
		if(team==1){
			Reward1.remove(idItem);
		}else if(team==2){
			Reward2.remove(idItem);
		}else if(team==3){
			Reward3.remove(idItem);
		}else if(team==4){
			Reward4.remove(idItem);
		}else if(team==5){
			Reward5.remove(idItem);
		}else if(team==6){
			Reward6.remove(idItem);
		}else if(team==7){
			Reward7.remove(idItem);
		}else if(team==8){
			Reward8.remove(idItem);
		}else if(team==9){
			Reward9.remove(idItem);
		}else if(team==10){
			Reward10.remove(idItem);
		}
	}

	private static Map<Integer, Integer>getRewards(int Team){
		if(Team==1){
			return Reward1;
		}else if(Team==2){
			return Reward2;
		}else if(Team==3){
			return Reward3;
		}else if(Team==4){
			return Reward4;
		}else if(Team==5){
			return Reward5;
		}else if(Team==6){
			return Reward6;
		}else if(Team==7){
			return Reward7;
		}else if(Team==8){
			return Reward8;
		}else if(Team==9){
			return Reward9;
		}else if(Team==10){
			return Reward10;
		}
		return null;
	}

	private static Vector<Integer> getTeamVector(int Team){
		if(Team==1){
			return team1;
		}else if(Team==2){
			return team2;
		}else if(Team==3){
			return team3;
		}else if(Team==4){
			return team4;
		}else if(Team==5){
			return team5;
		}else if(Team==6){
			return team6;
		}else if(Team==7){
			return team7;
		}else if(Team==8){
			return team8;
		}else if(Team==9){
			return team9;
		}else if(Team==10){
			return team10;
		}
		return null;
	}

	private static String getNamePlayer(int Team){
		Vector<Integer> Players = getTeamVector(Team);
		String retorno = "";
		if(Players!=null){
			for(int idPlayer : Players){
				if(retorno.length()>0){
					retorno += ",";
				}
				retorno += opera.getPlayerByID(idPlayer).getName();
			}
			return "<font color="+ getTeamColorForHtml(Team) + ">" + retorno + "</font>";
		}
		return "";
	}


	@SuppressWarnings("rawtypes")
	private static String getNameReward(int Reward){
		String NombreReward = "";
		Map<Integer, Integer> reward = getRewards(Reward);
		Iterator itr = reward.entrySet().iterator();
		while (itr.hasNext()) {
			if(NombreReward.length()>0){
				NombreReward+="<br1>";
			}
			Map.Entry e = (Map.Entry)itr.next();
			int idItem = Integer.valueOf(e.getKey().toString());
			int Cantidad = Integer.valueOf(e.getValue().toString());
			NombreReward+= central.getNombreITEMbyID(idItem) + " ("+ String.valueOf(Cantidad) +")";
		}
		return NombreReward;
	}


	private static void showRewardHTML(L2PcInstance player, int team, int rewardT){
		String html="<html><title>" +general.TITULO_NPC() + "</title><body>" ;
		html += central.LineaDivisora(1) + central.headFormat("Reward Team") + central.LineaDivisora(1);
		String btnBypass = "bypass -h admin_zeus_op coliseevent ShowReGive %IDTEAM% %IDREWARD%";
		String BtnVer = "<button value=\"Team%IDTEAM%\" action=\""+ btnBypass +"\" width=52 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BtnVerReward = "<button value=\"Rew.%IDREWARD%\" action=\""+ btnBypass +"\" width=52 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String botones = "<table width=280><tr>";
		for(int i=1;i<=10;i++){
			if(i==6){
				botones +="</tr><tr>";
			}
			botones += "<td width=40>"+BtnVer.replace("%IDTEAM%", String.valueOf(i)).replace("%IDREWARD%", String.valueOf(rewardT))+"</td>";
		}
		if(botones.endsWith("</td>")){
			botones+="</tr></table>";
		}else{
			botones+="</table>";
		}

		botones += central.LineaDivisora(1) + "<table width=280><tr>";
		for(int i=1;i<=10;i++){
			if(i==6){
				botones +="</tr><tr>";
			}
			botones += "<td width=40>"+BtnVerReward.replace("%IDTEAM%", String.valueOf(team)).replace("%IDREWARD%", String.valueOf(i))+"</td>";
		}
		if(botones.endsWith("</td>")){
			botones+="</tr></table>";
		}else{
			botones+="</table>";
		}

		String btnBypassGive = "bypass -h admin_zeus_op coliseevent givereward "+ String.valueOf(team) +" "+String.valueOf(rewardT);
		String BtnGive = "<button value=\"Give Reward\" action=\""+ btnBypassGive +"\" width=150 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";


		html += central.LineaDivisora(1) + central.headFormat("Team " + String.valueOf(team) + ", Reward " + String.valueOf(rewardT) + "<br1>" + botones, getTeamColorForHtml(team)) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Players on team " + getTeamName(team) + "<br1>" +getNamePlayer(team)) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("<font color=LEVEL>Reward " + String.valueOf(rewardT) + "</font><br1>" + getNameReward(rewardT)) + central.LineaDivisora(1);
		html += central.LineaDivisora(2) + central.headFormat(BtnGive) + central.LineaDivisora(1) + central.headFormat(btnbackMain) + central.getPieHTML() + "</body></html>";
		opera.enviarHTML(player, html);
	}

	private static void resetTeam(int team){
		if(team==1){
			team1.clear();
		}else if(team==2){
			team2.clear();
		}else if(team==3){
			team3.clear();
		}else if(team==4){
			team4.clear();
		}else if(team==5){
			team5.clear();
		}else if(team==6){
			team6.clear();
		}else if(team==7){
			team7.clear();
		}else if(team==8){
			team8.clear();
		}else if(team==9){
			team9.clear();
		}else if(team==10){
			team10.clear();
		}
	}


	@SuppressWarnings("rawtypes")
	private static void rewardTeams(L2PcInstance player, int team, int rewardToGive){
		Vector<Integer>Players = new Vector<Integer>();
		Players = getTeamVector(team);
		Map<Integer, Integer> Rewards = new HashMap<Integer, Integer>();
		Rewards = getRewards(rewardToGive);

		String pjName = "";

		if(Players!=null){
			if(Players.size()>0){
				if(Rewards!=null){
					for(int idPlayer : Players){
						L2PcInstance cha = opera.getPlayerByID(idPlayer);
						if(cha!=null){
							if(cha.isOnline() && !cha.isOlympiadStart()){
								Iterator itr = Rewards.entrySet().iterator();
								while (itr.hasNext()) {
									Map.Entry e = (Map.Entry)itr.next();
									int idItem = Integer.valueOf(e.getKey().toString());
									int Cantidad = Integer.valueOf(e.getValue().toString());
									opera.giveReward(cha, idItem, Cantidad);
								}

								if(pjName.length()>0){
									pjName += ",";
								}
								pjName += cha.getName();
								cleanNameColor(cha);
								RewardEfect(cha);
								central.msgbox("Congratulations, you have been rewarded with the reward number " + String.valueOf(rewardToGive), cha);
								//setOutColiseum(cha);
								removePlayer(cha.getObjectId(),false);
							}
						}
					}
					central.msgbox("This players has been rewarded: " + pjName, player);
					central.msgbox("The team " + String.valueOf(team) + " has been rewarded with the reward number " + String.valueOf(rewardToGive) + "." , player);
					resetTeam(Integer.valueOf(team));
				}else{
					central.msgbox("No Awards to give" , player);
				}
			}else{
				central.msgbox("No player in this team for rewarded" , player);
			}
		}else{
			central.msgbox("No player in this team for rewarded" , player);
		}

	}

	private static void RewardEfect(L2PcInstance player){
/*		L2Skill skill = SkillData.  .FrequentSkill.LARGE_FIREWORK.getSkill();
		if (skill != null)
		{
			player.broadcastPacket(new MagicSkillUse(player, player, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
		}*/
	}


	public static void setBlockItem(int idItemtoBlock){
		if(!blockItem.contains(idItemtoBlock)) {
			blockItem.add(idItemtoBlock);
		}
	}

	public static void setRemoveBlockItem(int idItemBlock){
		blockItem.removeElement(idItemBlock);
	}


	public static void getShowBlockArmor(L2PcInstance player){
		String html = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Expertise Allowed") + central.LineaDivisora(1);
		String combo ="";
		for(String Nom : GradeNames){
			if(Nom.length()>0) {
				if(combo.length()>0){
					combo += ";";
				}
				combo += Nom;
			}
		}
		combo = "<combobox width=80 var=cmbGrado list="+combo+">";

		String btnBypass1 = "bypass -h admin_zeus_op coliseevent remexperti %EXPER%";
		String btnExper = "<button value=\"%EXPER%\" action=\""+ btnBypass1 +"\" width=100 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnAgregar  = "<button value=\"Add Expertise\" action=\"bypass -h admin_zeus_op coliseevent addexperti $cmbGrado\" width=100 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String expertises ="";
		for (String Grados : GradesAllow){
			if(Grados.length()>0) {
				expertises += btnExper.replace("%EXPER%", Grados);
			}
		}
		html += central.LineaDivisora(1) + central.headFormat("Select Grade Expertise to allow<br1>" +combo+"<br1>" + btnAgregar ) + central.LineaDivisora(1);
		if(expertises.length()>0){
			html += central.LineaDivisora(1) + central.headFormat(expertises) + central.LineaDivisora(1);
		}
		html += central.headFormat(btnbackMain) +  central.getPieHTML() + "</body></html>";
		opera.enviarHTML(player, html);
	}

	public static void getBlockItemHTML(L2PcInstance player){
		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		html += central.LineaDivisora(1) + central.headFormat("Items Blocked") + central.LineaDivisora(1);
		String ValIngreso = "<edit var=\"valor\" width=160>";
		String btnBypass1 = "bypass -h admin_zeus_op coliseevent %SEC% %VAL%";
		String btnAdd = "<button value=\"Block\" action=\""+ btnBypass1.replace("%SEC%", "additemblock").replace("%VAL%", "$valor") +"\" width=150 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnItemBlock = "<button value=\"%NAME%\" action=\""+ btnBypass1 +"\" width=270 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		html+= central.LineaDivisora(1) + central.headFormat("Enter the ID of item to block<br1>"+ValIngreso+"<br1>"+btnAdd,"LEVEL") + central.LineaDivisora(1);

		String Grilla = "<table width=280>";

		if(blockItem!=null){
			if(blockItem.size()>0){
				for(int idItem : blockItem){
					Grilla += "<tr><td width=280 align=center>"+ btnItemBlock.replace("%NAME%", central.getNombreITEMbyID(idItem)).replace("%SEC%", "remitemblock").replace("%VAL%", String.valueOf(idItem)) +"</td></tr>";
				}
			}
		}

		Grilla += "</table>";

		html+=central.LineaDivisora(1) + central.headFormat("Select item to delete from the list<br>" + Grilla,"LEVEL") + central.LineaDivisora(1);

		html += central.headFormat(btnbackMain) + central.getPieHTML() + "</center></body></html>";
		opera.enviarHTML(player, html);
	}

	public static void removeExpertice(String expert){
		GradesAllow.remove(expert);
	}

	public static void addExpertice(String expert){
		if(!GradesAllow.contains(expert)){
			GradesAllow.add(expert);
		}
	}

	public static void bypass(String param, L2PcInstance player){
		//_log.warning(param);
		String[] event = param.split(" ");
		if(event[2].equals("showexper")){
			getShowBlockArmor(player);
			return;
		}else if(event[2].equals("remexperti")){
			removeExpertice(event[3]);
			getShowBlockArmor(player);
			return;
		}else if(event[2].equals("addexperti")){
			addExpertice(event[3]);
			getShowBlockArmor(player);
			return;
		}else if(event[2].equals("showblockitem")){
			getBlockItemHTML(player);
			return;
		}else if(event[2].equals("remitemblock")){
			setRemoveBlockItem(Integer.valueOf(event[3]));
			getBlockItemHTML(player);
			return;
		}else if(event[2].equals("additemblock")){
			if((event.length>4) || (event.length<4)){
				getBlockItemHTML(player);
				return;
			}
			if(!opera.isNumeric(event[3])){
				central.msgbox("must enter numbers only", player);
				getBlockItemHTML(player);
				return;
			}
			setBlockItem(Integer.valueOf(event[3]));
			getBlockItemHTML(player);
			return;
		}else if(event[2].equals("givereward")){
			rewardTeams(player, Integer.valueOf(event[3]), Integer.valueOf(event[4]));
			showRewardHTML(player,Integer.valueOf(event[3]),Integer.valueOf(event[4]));
			return;
		}else if (event[2].equals("backmain")){
			getHTMLAdminConfig(player);
			return;
		}else if(event[2].equals("remitem")){
			remItem(Integer.valueOf(event[4]),Integer.valueOf(event[3]));
			getShowRewardbyTeamMain(player,Integer.valueOf(event[4]));
			return;
		}else if(event[2].equals("addreward")){
			if((event.length>7) || (event.length<7)){
				getShowRewardbyTeamMain(player,1);
			}else{
				if(!opera.isNumeric(event[4])){
					getShowRewardbyTeamMain(player,1);
					central.msgbox("ID Item most be Numeric", player);
					return;
				}
				if(!opera.isNumeric(event[5])){
					getShowRewardbyTeamMain(player,1);
					central.msgbox("Ammount most be Numeric", player);
					return;
				}
				setReward(Integer.valueOf(event[3]), Integer.valueOf(event[4]), Integer.valueOf(event[5]));
				getShowRewardbyTeamMain(player,Integer.valueOf(event[3]));
				return;
			}
			//getShowRewardbyTeamMain(player,1);
		}else if(event[2].equals("unparaAll")){
			setInnobilidad(Integer.valueOf(event[3]),false);
			getHTMLAdminTeam(player,event[3]);
			return;
		}else if(event[2].equals("paraAll")){
			setInnobilidad(Integer.valueOf(event[3]),true);
			getHTMLAdminTeam(player,event[3]);
			return;
		}else if(event[2].equals("recallAll")){
			Vector<Integer> CharAllTeam = getTeamVector(Integer.valueOf(event[3]));
			if(CharAllTeam==null){
				central.msgbox("The team is empty", player);
				return;
			}else if(CharAllTeam.size()==0){
				central.msgbox("The team is empty", player);
				return;
			}else if(CharAllTeam.size()>0){
				for(int idChar : CharAllTeam){
					L2PcInstance PlayertoRecall = opera.getPlayerByID(idChar);
					if(PlayertoRecall!=null){
						if(PlayertoRecall.isOnline()){
							if(PlayertoRecall.isInOlympiadMode()){
								central.msgbox("The player is in olympiad Mode " + PlayertoRecall.getName(), player);
							}else{
								PlayertoRecall.teleToLocation(player.getX(), player.getY(), player.getZ());
								central.msgbox("You have been Recall it by adm / gm", player);
							}
						}else{
							central.msgbox("The player is not Online", player);
						}
					}else{
						central.msgbox("Error to recall player.", player);
					}
				}
			}
			getHTMLAdminTeam(player,event[3]);
			return;
		}else if(event[2].equals("recall")){
			L2PcInstance PlayertoRecall = opera.getPlayerByID(Integer.valueOf( event[3] ));
			if(PlayertoRecall!=null){
				if(PlayertoRecall.isOnline()){
					if(PlayertoRecall.isInOlympiadMode()){
						central.msgbox("The player is in olympiad Mode", player);
					}else{
						PlayertoRecall.teleToLocation(player.getX(), player.getY(), player.getZ());
						central.msgbox("You have been Recall it by adm / gm", player);
					}
				}else{
					central.msgbox("The player is not Online", player);
				}
			}else{
				central.msgbox("Error to recall player.", player);
			}
			getHTMLAdminTeam(player,event[4]);
			return;
		}else if(event[2].equals("remove")){
			removePlayer(Integer.valueOf(event[3]));
			getHTMLAdminTeam(player,event[4]);
		}else if(event[2].equals("ShowTeam")){
			getHTMLAdminTeam(player,event[3]);
			return;
		}else if(event[2].equals("ShowRe")){
			if((event.length>4) || (event.length<4)){
				getShowRewardbyTeamMain(player,1);
			}else{
				getShowRewardbyTeamMain(player,Integer.valueOf(event[3]));
			}
			return;
		}else if(event[2].equals("ShowReGive")){
			//showRewardHTML(L2PcInstance player, int team, int rewardT){
			showRewardHTML(player,Integer.valueOf(event[3]),Integer.valueOf(event[4]));
			return;
		}else if(event[2].equals("setVal")){
			if(event[3].equals("eventStart")){
				eventoStart = eventoStart ? false:true;
			}else if(event[3].equals("eventRegis")){
				eventoRegister = eventoRegister ? false:true;
			}else if(event[3].equals("eventIsTeam")){
				IsTeamEvent = IsTeamEvent ? false:true;
			}else if(event[3].equals("Noble")){
				IsOnlyNoble = IsOnlyNoble ? false:true;
			}else if(event[3].equals("Equipos")){
				IsTeamEvent = IsTeamEvent ? false:true;
			}else if(event[3].equals("teleUserA")){
				TeleportIntoRegisterPlayer = TeleportIntoRegisterPlayer ? false:true;
			}else if (event[3].equals("Hero")){
				IsOnlyHero = IsOnlyHero ? false:true;
			}
		}else if(event[2].equals("setValTXT")){
			if((event.length<5) || (event.length>5)){
				central.msgbox("Wrong input data.", player);
				return;
			}
			if(event[3].equals("Equipos")){
				if(opera.isNumeric(event[4])){
					Equipos = Integer.valueOf(event[4]);
				}else{
					central.msgbox("Wrong input data.", player);
				}
			}else if(event[3].equals("player4Team")){
				if(opera.isNumeric(event[4])){
					maxPlayerForTeam = Integer.valueOf(event[4]);
				}else{
					central.msgbox("Wrong input data.", player);
				}
			}else if(event[3].equals("minLevel")){
				if(opera.isNumeric(event[4])){
					int lvlM = Integer.valueOf(event[4]);
					if((lvlM <= 0) || (lvlM > 85)|| (lvlM >= MaxLevel)){
						central.msgbox("Wrong input data.", player);
					}else{
						MinLevel = Integer.valueOf(event[4]);
					}
				}else{
					central.msgbox("Wrong input data.", player);
				}
			}else if(event[3].equals("maxLevel")){
				if(opera.isNumeric(event[4])){
					int lvlM = Integer.valueOf(event[4]);
					if((lvlM <= 0) || (lvlM > 85) || (lvlM <= MinLevel)){
						central.msgbox("Wrong input data.", player);
					}else{
						MaxLevel = Integer.valueOf(event[4]);
					}
				}else{
					central.msgbox("Wrong input data.", player);
				}
			}else if(event[3].equals("maxEchant")){
				if(opera.isNumeric(event[4])){
					maxEnchant = Integer.valueOf(event[4]);
				}else{
					central.msgbox("Wrong input data.", player);
				}
			}
		}
		getHTMLAdminConfig(player);
	}

	private static void setInnobilidad(int team, boolean bloquear){
		Vector<Integer> Char = getTeamVector(team);
		if(Char==null){
			return;
		}
		if(Char.size()==0){
			return;
		}

		for(int idChar : Char){
			L2PcInstance activeChar = opera.getPlayerByID(idChar);
			if(activeChar!=null){
				if(activeChar.isOnline() && !activeChar.isOlympiadStart()){
					setInnobilidad(activeChar, bloquear);
				}
			}
		}

	}

	private static void setInnobilidad(L2PcInstance player, boolean innobilidad){
		if(innobilidad){
			opera.setimmobilizeChar(player,true);
		}else{
			opera.setimmobilizeChar(player,false);
		}
	}

	private static Vector<Integer> getTeamPlayerbyTeam(int Team){
		Vector<Integer> Player = new Vector<Integer>();

		if(Team==1){
			Player = team1;
		}else if(Team==2){
			Player = team2;
		}else if(Team==3){
			Player = team3;
		}else if(Team==4){
			Player = team4;
		}else if(Team==5){
			Player = team5;
		}else if(Team==6){
			Player = team6;
		}else if(Team==7){
			Player = team7;
		}else if(Team==8){
			Player = team9;
		}else if(Team==9){
			Player = team9;
		}else if(Team==10){
			Player = team10;
		}

		return Player;
	}

	private static String getPlayerTeamShow(int Team){

		String BypassRecallPlayer = "bypass -h admin_zeus_op coliseevent recall %IDCHAR% "+ String.valueOf(Team) ;
		String BypassRemovePlayer = "bypass -h admin_zeus_op coliseevent remove %IDCHAR% "+ String.valueOf(Team) ;

		String btnRecall = "<button value=\"Rcall\" action=\""+ BypassRecallPlayer +"\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnRemove = "<button value=\"Rem\" action=\""+ BypassRemovePlayer +"\" width=40 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String tableBtn = "<br1><table width=90><tr><td width=50>" + btnRecall + "</td><td width=40>"+ btnRemove +"</td></tr></table>";

		Vector<Integer> Player = new Vector<Integer>();
		Player = getTeamPlayerbyTeam(Team);
		String html = "";
		L2PcInstance cha = null;
		int Cont = 0;
		if(Player!=null){
			html = "<center><table width=280 align=center bgcolor=646262>";
			for(int idChar : Player){
				cha=opera.getPlayerByID(idChar);
				if(cha!=null){
					if(Cont==0){
						html+="<tr>";
					}
					html +="<td width=140 align=center>"+cha.getName()+tableBtn.replace("%IDCHAR%", String.valueOf(idChar) )+"</td>";
					Cont++;
					if((Cont%2)==0){
						Cont=0;
						html+="</tr>";
					}
				}
			}
			if(html.endsWith("</td>")){
				html+="<td width=140></td></tr>";
			}
			if(html.endsWith("</tr>")){
				html+="</table></center><br>";
			}
		}
		return html;

	}

	private static String getConfigBtnYesNo(String Titulo, String Seccion){
		Titulo = Titulo.replace("true", "<font color=04B431>True</font>").replace("false", "<font color=DF3A01>False</font>");
		String btnBypass1 = "bypass -h admin_zeus_op coliseevent setVal %SEC%".replace("%SEC%", Seccion);
		String btnChange = "<button value=\"Change\" action=\""+ btnBypass1 +"\" width=60 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String retorno = "<table width=280><tr>";
		retorno += "<td width=220><font color=LEVEL>"+Titulo+"</font></td><td width=60>"+btnChange+"</td></tr>";
		retorno +="</table>";
		return retorno;
	}

	private static String getConfigInputSimple(String Titulo, String Seccion, String idVal){
		String ValIngreso = "<edit var=\"valor"+idVal+"\" width=160>";
		String btnBypass1 = ("bypass -h admin_zeus_op coliseevent setValTXT %SEC% $valor"+idVal).replace("%SEC%", Seccion);
		String btnChange = "<button value=\"Change\" action=\""+ btnBypass1 +"\" width=60 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String retorno = "<table width=280><tr>";
		retorno += "<td width=220><font color=LEVEL>"+Titulo+"</font></td><td width=60></td></tr>";
		retorno += "<tr><td width=220><font color=LEVEL>"+ValIngreso+"</font></td><td width=60>"+btnChange+"</td></tr>";
		retorno +="</table>";
		return retorno;
	}

	public static void getHTMLAdminPremios(L2PcInstance player){

	}

	public static void getHTMLAdminTeam(L2PcInstance player, String teamToShow){
		String html = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Player Team List") + central.LineaDivisora(1);

		String btnBypass = "bypass -h admin_zeus_op coliseevent ShowTeam %SHOWID%";
		String BtnVer = "<button value=\"Team%SHOWID%\" action=\""+ btnBypass +"\" width=52 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		//String btnBypassRecallALL = "bypass -h admin_zeus_op coliseevent recallAll "+teamToShow;
		String btnBypassALL = "bypass -h admin_zeus_op coliseevent %EVENTO% "+teamToShow;
		String BtnVerRecallALL = "<button value=\"Recall All\" action=\""+ btnBypassALL.replace("%EVENTO%", "recallAll") +"\" width=90 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String BtnVerParaALL = "<button value=\"Para Team\" action=\""+ btnBypassALL.replace("%EVENTO%", "paraAll") +"\" width=90 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BtnVerUnparaALL = "<button value=\"Unpara T.\" action=\""+ btnBypassALL.replace("%EVENTO%", "unparaAll") +"\" width=90 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnVarios = "<table width=280><tr><td width=90 align=center>"+ BtnVerRecallALL +"</td><td width=90 align=center>"+ BtnVerParaALL +"</td><td width=90 align=center>"+ BtnVerUnparaALL +"</td></tr></table>";
		//BtnVerRecallALL

		String botones = "<table width=280><tr>";

		for(int i=1;i<=10;i++){
			if(i==6){
				botones +="</tr><tr>";
			}
			botones += "<td width=40>"+BtnVer.replace("%SHOWID%", String.valueOf(i))+"</td>";
		}

		if(botones.endsWith("</td>")){
			botones+="</tr></table>";
		}else{
			botones+="</table>";
		}

		html += central.LineaDivisora(1) + central.headFormat("Player Team "+teamToShow+" ("+ getTeamName(Integer.valueOf(teamToShow)) +")<br1>"+botones+"<br1>"+btnVarios,getTeamColorForHtml(Integer.valueOf(teamToShow))) + central.LineaDivisora(1);

		String Teams = getPlayerTeamShow(Integer.valueOf(teamToShow));

		html += central.headFormat(Teams+"<br>")+central.LineaDivisora(2);

		html += central.LineaDivisora(1) + central.headFormat(btnbackMain) + central.getPieHTML() + "</body></html>";
		opera.enviarHTML(player, html);
	}


	@SuppressWarnings("unused")
	public static void getHTMLAdminConfig(L2PcInstance player){

		if(GradesAllow.size()==0){
			//_log.warning("Entro a cargar");
			GradesAllow.add("");
			GradesAllow.add("D");
			GradesAllow.add("C");
			GradesAllow.add("B");
			GradesAllow.add("A");
			GradesAllow.add("S");
			GradesAllow.add("S80");
			GradesAllow.add("S84");
		}

		String ValX = "<edit var=\"val_x\" width=80>";
		String ValY = "<edit var=\"val_y\" width=80>";
		String ValZ = "<edit var=\"val_z\" width=80>";

		String ValIngreso = "<edit var=\"valor\" width=160>";
		//String ValDescrip = "<edit var=\"val_desc\" width=160>";

		String btnBypass = "bypass -h admin_zeus_op coliseevent ShowTeam 1";
		String BtnVer = "<button value=\"Team\" action=\""+ btnBypass +"\" width=70 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypassReward = "bypass -h admin_zeus_op coliseevent ShowRe 1";
		String BtnVerReward = "<button value=\"Reward\" action=\""+ btnBypassReward +"\" width=70 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypassRewardGive = "bypass -h admin_zeus_op coliseevent ShowReGive 1 1";
		String BtnVerRewardGive = "<button value=\"Give Reward\" action=\""+ btnBypassRewardGive +"\" width=100 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBlockItem = "<button value=\"Block Item\" action=\"bypass -h admin_zeus_op coliseevent showblockitem\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnBlockArmor = "<button value=\"Block Armor Exp.\" action=\"bypass -h admin_zeus_op coliseevent showexper\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		//

		String btnNes = "<table width=280><tr><td width=90 align=center>"+ BtnVer +"</td><td width=100 align=center>"+BtnVerRewardGive+"</td><td width=90 align=center>"+BtnVerReward+"</td></tr></table>";
		btnNes += "<table width=280><tr><td width=140 align=center>"+ btnBlockItem +"</td><td width=140 align=center>"+ btnBlockArmor +"</td></tr></table>";

		//String btnBypass1 = "bypass -h admin_zeus_op coliseevent setVal %SEC% %VAL% %OTHER%";
		//String BtnAdd1 = "<button value=\"ADD TO MAIN WINDOWS (Auto)\" action=\""+ btnBypass1 +"\" width=220 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		html += central.LineaDivisora(1) + central.headFormat("ZeuS - Coliseum Team Event") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(btnNes) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Event Start: " + String.valueOf(eventoStart), "eventStart")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Event Register: " + String.valueOf(eventoRegister), "eventRegis")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Event Is Team Event: " + String.valueOf(IsTeamEvent), "eventIsTeam")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Only Noble: " + String.valueOf(IsOnlyNoble), "Noble")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Only Hero: " + String.valueOf(IsOnlyHero), "Hero")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigBtnYesNo("Tele. to register users to coliseum: " + String.valueOf(TeleportIntoRegisterPlayer), "teleUserA")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigInputSimple("Teams: " + String.valueOf(Equipos), "Equipos","1")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigInputSimple("Max Player for Team: " + String.valueOf(maxPlayerForTeam), "player4Team","2")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigInputSimple("Min lvl: " + String.valueOf(MinLevel), "minLevel","3")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigInputSimple("Max lvl: " + String.valueOf(MaxLevel), "maxLevel","4")) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(getConfigInputSimple("Max Enchant: " + String.valueOf(maxEnchant), "maxEchant","5")) + central.LineaDivisora(1);
		html += central.getPieHTML() + "</center></body></html>";
		opera.enviarHTML(player, html);

	}

	public static String getHtmlEventNpc(L2PcInstance player){
		//getRegOlyEven
		String byPass = "bypass -h ZeuSNPC getRegOlyEven 0 0 0";
		String btnRegistrar = "<button value=\"Register to the Event\" action=\""+byPass+"\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String html = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		html += central.LineaDivisora(1) + central.headFormat("ZeuS Coliseum Event Manager") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Wellcome.<br1>We need check if you meet all requirements<br1>of this event.","LEVEL") + central.LineaDivisora(1);

		/*
		private static int Equipos=2, maxEnchant=Config.MAX_ENCHANT_LEVEL , maxPlayerForTeam = 10, MinLevel=40, MaxLevel=85;
		private static Vector<Integer> blockItem = new Vector<Integer>();
		private static boolean eventoStart=false, eventoRegister=false, IsTeamEvent=true, IsOnlyNoble=false, IsOnlyHero=false, TeleportIntoRegisterPlayer = false;
		 */

		String expertiseStr = "";

		for(String exper : GradesAllow){
			if(exper.length()>0){
				if(expertiseStr.length()>0){
					expertiseStr +=", ";
				}
				expertiseStr += exper;
			}
		}

		html += central.LineaDivisora(1) + central.headFormat("Event Start: " + (eventoStart ? "Yes":"No") ,"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Join process: " + (eventoRegister ? language.getInstance().getMsg(player).BUTTON_ENABLE:language.getInstance().getMsg(player).BUTTON_DISABLE ) ,"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Min. Level: " + String.valueOf(MinLevel)+",  Max. Level: " + String.valueOf(MaxLevel) ,"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Only Noble: " + (IsOnlyNoble ?"Yes":"No" )+",  Only Hero: " + (IsOnlyHero ? "Yes":"No") ,"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Max. Enchant: " + String.valueOf(maxEnchant),"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Number of teams: " + String.valueOf(Equipos),"FE9A2E") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Expertise Allowed: " + expertiseStr,"FE9A2E") + central.LineaDivisora(1);

		if(isRegister(player)){
			html += central.LineaDivisora(2) + central.headFormat(btnRegistrar.replace("Register", "Get In Back")) + central.LineaDivisora(2);
		}else{
			html += central.LineaDivisora(2) + central.headFormat(btnRegistrar) + central.LineaDivisora(2);
		}

		html += "</center>"+central.getPieHTML()+"</body></html>";
		return html;
	}



	public static boolean RegisterOnTeam(L2PcInstance player){

		if(!eventoRegister){
			central.msgbox("The event is not registering players", player);
			return false;
		}

		if(eventoStart){
			central.msgbox("The event is now in progress", player);
			return false;
		}

		if(isRegister(player)){
			if(TeleportIntoRegisterPlayer){
				goColiseumInsde(player);
				player.getAppearance().setNameColor(getTeamColorForName(getIDTeamPlayer(player.getObjectId())));
			}
			central.msgbox("You need to wait for the activation by the adm/gm for enter to the event on the coliseum.", player);
			return false;
		}

		if(haveItemBlock(player)){
			return false;
		}



		if(!haveTeamSpaces()){
			central.msgbox("All team are complete. We are sorry.", player);
			return false;
		}
		int RandTeam = 0;
		boolean haveSpace = false;
		int Cont=0, ContadorMax = 100000;

		Random aleatorio = new Random();

		while(!haveSpace && (Cont < ContadorMax)){
			RandTeam = aleatorio.nextInt(Equipos+1);
			haveSpace = checkTeamSpace(RandTeam);
			Cont++;
		}

		if(Cont>=ContadorMax){
			central.msgbox("At this time could not select team.", player);
			return false;
		}

		setPlayerToTeam(RandTeam,player);
		goColiseumInsde(player);
		if(IsTeamEvent){
			player.getAppearance().setNameColor(getTeamColorForName(RandTeam));
			central.msgbox("You have entered the event. Your team is the color " + getTeamName(RandTeam) , player);
		}
		lastSelect =  RandTeam;
		rechazo = 0;
		return true;
	}

	private static void goColiseumInsde(L2PcInstance player){
		player.teleToLocation(OlympCoor[0], OlympCoor[1], OlympCoor[2], true);
		central.healAll(player, false);
	}

	private static int getIDTeamPlayer(int idCha){
		if(team1.contains(idCha)){
			return 1;
		}else if(team2.contains(idCha)){
			return 2;
		}else if(team3.contains(idCha)){
			return 3;
		}else if(team4.contains(idCha)){
			return 4;
		}else if(team5.contains(idCha)){
			return 5;
		}else if(team6.contains(idCha)){
			return 6;
		}else if(team7.contains(idCha)){
			return 7;
		}else if(team8.contains(idCha)){
			return 8;
		}else if(team9.contains(idCha)){
			return 9;
		}else if(team10.contains(idCha)){
			return 10;
		}
		return -1;
	}

	private static boolean isRegister(L2PcInstance Player){
		int idCha = Player.getObjectId();
		if(AllCharTeam.contains(idCha)){
			return true;
		}
		return false;
	}

	 @SuppressWarnings("unused")
	private String getDarkColorForHtml(int teamId)
	    {
	        switch(teamId)
	        {
	        case 1: // '\001'
	            return "7C8194";
	        case 2: // '\002'
	            return "987878";
	        case 3: // '\003'
	            return "868F81";
	        case 4: // '\004'
	            return "937D8D";
	        case 5: // '\005'
	            return "93937D";
	        case 6: // '\006'
	            return "D2934D";
	        case 7: // '\007'
	            return "3EC1C1";
	        case 8: // '\b'
	            return "D696D1";
	        case 9: // '\t'
	            return "9B7957";
	        case 10: // '\n'
	            return "949494";
	        }
	        return "8f8f8f";
	    }

	    public static String getTeamColorForHtml(int teamId)
	    {
	        switch(teamId)
	        {
	        case 1: // '\001'
	            return "5083CF";
	        case 2: // '\002'
	            return "D04F4F";
	        case 3: // '\003'
	            return "56C965";
	        case 4: // '\004'
	            return "9F52CD";
	        case 5: // '\005'
	            return "DAC73D";
	        case 6: // '\006'
	            return "D2934D";
	        case 7: // '\007'
	            return "3EC1C1";
	        case 8: // '\b'
	            return "D696D1";
	        case 9: // '\t'
	            return "9B7957";
	        case 10: // '\n'
	            return "949494";
	        }
	        return "FFFFFF";
	    }

	    public static int getTeamColorForName(int teamId)
	    {
	        switch(teamId)
	        {
	        case 1: // '\001'
	            return 0xcf8350;
	        case 2: // '\002'
	            return 0x4f4fd0;
	        case 3: // '\003'
	            return 0x65c956;
	        case 4: // '\004'
	            return 0xcd529f;
	        case 5: // '\005'
	            return 0x3dc7da;
	        case 6: // '\006'
	            return 0x4d93d2;
	        case 7: // '\007'
	            return 0xc1c13e;
	        case 8: // '\b'
	            return 0xd196d6;
	        case 9: // '\t'
	            return 0x57799b;
	        case 10: // '\n'
	            return 0x949494;
	        }
	        return 0;
	    }

	    public static String getTeamName(int teamId)
	    {
	        switch(teamId)
	        {
	        case 1: // '\001'
	            return "Blue";
	        case 2: // '\002'
	            return "Red";
	        case 3: // '\003'
	            return "Green";
	        case 4: // '\004'
	            return "Purple";
	        case 5: // '\005'
	            return "Yellow";
	        case 6: // '\006'
	            return "Orange";
	        case 7: // '\007'
	            return "Teal";
	        case 8: // '\b'
	            return "Pink";
	        case 9: // '\t'
	            return "Brown";
	        case 10: // '\n'
	            return "Grey";
	        }
	        return "No";
	    }

}
