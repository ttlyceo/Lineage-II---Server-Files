package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.ClassListData;
import l2r.gameserver.data.xml.impl.ExperienceData;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.data.xml.impl.SkillTreesData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.UserInfo;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.event.ClanReputationEvent;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.clanNomCambio;
import ZeuS.interfase.delevel;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class v_MiscelaniusOption {
	
	private static Logger _log = Logger.getLogger(v_MiscelaniusOption.class.getName());
	@SuppressWarnings("unused")
	private static Map<Integer, Boolean> IsOcupado = new HashMap<Integer, Boolean>();
	private static Map<Integer, Boolean> IsInDonation = new HashMap<Integer, Boolean>();
	public static Map<Integer, String>TEMPORALDATA = new HashMap<Integer, String>();
	
	public enum loadByPass{
		Lv85,
		Noble,
		Fame,
		ClanLv,
		ReducePK,
		SexChange,
		AIONormal,
		AIONormal30,
		ChangeCharName,
		ChangeClanName,
		Delevel
	}
	
	private static Vector<String> getNormalItem(String Pide){
		//3470,10;57,1
		Vector<String>vt = new Vector<String>();
		for(String t : Pide.split(";")){
			String Nombre = central.getNombreITEMbyID(Integer.valueOf(t.split(",")[0]));
			String Cantidad = t.split(",")[1];
			vt.add(opera.getFormatNumbers(Cantidad) + " " + Nombre);
		}
		return vt;
	}
	@SuppressWarnings("unused")
	private static void showWindowsPrice(L2PcInstance player, String ByPassSeccion, boolean showDonation){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-miscelanius-show-information.htm");
		Vector<String>Requerimientos = new Vector<String>();
		Vector<String>NormalItem = new Vector<String>();
		Vector<String>DonationCoin = new Vector<String>();
		Vector<String>Obtener = new Vector<String>();
		String NameSec = "";
		if(ByPassSeccion.equals(loadByPass.Lv85.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			Obtener.add("Your character will go up to level 85");
			NameSec = "Char Level 85";
		}else if(ByPassSeccion.equals(loadByPass.Noble.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			Obtener.add("Your character will be noble");
			NameSec = "Char Noble";
		}else if(ByPassSeccion.equals(loadByPass.Fame.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_FAME_PRICE);
			Requerimientos.add("NO HAVE");
			Obtener.add("Your character will get " + String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) + " of Fame");
			NameSec = "Char Fame";
		}else if(ByPassSeccion.equals(loadByPass.ClanLv.name())){
			Obtener.add("To Give");
			Obtener.add("Clan Level " + String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE));
			Obtener.add("Clan Reputation " + String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE));
			Requerimientos.add("Min Player:" + String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER));
			Requerimientos.add("All members online:" + (general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE ? "Yes" : "No") );
			NameSec = "Clan Level";
		}else if(ByPassSeccion.equals(loadByPass.ReducePK.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_REDUCE_PK_PRICE);
			Requerimientos.add("NO HAVE");
			Obtener.add("You PK point will be reduce by " + String.valueOf(general.OPCIONES_CHAR_REDUCE_AMOUNT) + "");
			NameSec = "Char Reduce PK";
		}else if(ByPassSeccion.equals(loadByPass.SexChange.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			Obtener.add("You gonna change your Character Sex");
			NameSec = "Char Sex Change";
		}else if(ByPassSeccion.equals(loadByPass.AIONormal.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE);
			Requerimientos.add("Min Level to Create: " + String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL));
			Obtener.add("Your gonna get a Normal AIO char Buffer");
			NameSec = "AIO Char (Normal)";
		}else if(ByPassSeccion.equals(loadByPass.AIONormal30.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30);
			Requerimientos.add("Min Level to Create: " + String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL));
			Obtener.add("Your gonna get a +30/+15 AIO char Buffer");
			NameSec = "AIO Char (Skill Enchant)";
		}else if(ByPassSeccion.equals(loadByPass.ChangeCharName.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE);
			Requerimientos.add("Only Noble: " + ( general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE ? "Yes" : "No" ));
			Requerimientos.add("Min Level: " + String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL));
			Obtener.add("Your gonna change your character name");
			NameSec = "Change Char Name";
		}else if(ByPassSeccion.equals(loadByPass.ChangeClanName.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE);
			Requerimientos.add("Min Clan Level: " + String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL));
			Obtener.add("Your gonna change your clan name");
			NameSec = "Change Clan Name";
		}else if(ByPassSeccion.equals(loadByPass.Delevel.name())){
			Requerimientos.add("Only Noble: " + ( general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE ? "Yes" : "No" ));
			NormalItem = getNormalItem(general.DELEVEL_PRICE);
			Obtener.add("You'll reduce your level");
			NameSec = "Char Delevel";
		}
		
		String requi = opera.getGridFormatFromHtml(html, 1, "%REQUE_DATA%");
		
		String imgLogo = opera.getImageLogo(player);
		html.replace("%SECTION_NAME%", NameSec);
		html.replace("%IMG_SERVER%", imgLogo);
		
		String ColorBG[] = {"1C1C1C","353535"};
		int Contador = 0;
		
		String strDataReque = "";
		for(String T : Requerimientos){
			strDataReque += requi.replace("%REQUIREMENTS%", T).replace("%BG_COLOR%", ColorBG[Contador%2]);
		}
		html.replace("%REQUE_DATA%", strDataReque);
		
		String itemRequest = opera.getGridFormatFromHtml(html, 2, "%ITEM_REQUEST_DATA%");
		String strItemRequest = "";
		for(String T : NormalItem){
			strItemRequest += itemRequest.replace("%ITEM_REQUEST%", T).replace("%BG_COLOR%", ColorBG[Contador%2]);//"<table with=270 border=0 bgcolor="+  +"><tr><td fixwidth=270 align= LEFT><font color=FFFABB>"+ T +"</font><br1></td></tr></table>";
		}
		
		html.replace("%ITEM_REQUEST_DATA%", strItemRequest);
		
		
		String ItemReceive = opera.getGridFormatFromHtml(html, 3, "%ITEM_RECEIVE_DATA%");
		String strItemReceive = "";
		for(String T : Obtener){
			strItemReceive += ItemReceive.replace("%RECEIVE_ITEM%", T).replace("%BG_COLOR%", ColorBG[Contador%2]);
		}
		
		html.replace("%ITEM_RECEIVE_DATA%", strItemReceive);
		
		central.sendHtml(player, html.getHtml());
		
	}
	
	private static String getMainWindows(L2PcInstance player, boolean isDonation){
		
		String ByPassDisabled = "";//general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";0;0;0;0;0;0";
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-miscelanius.htm");

		html.replace("%BYPASS%",general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%BYPASS_INFO_LEVEL85%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Lv85.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_NOBLE%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Noble.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_FAMA%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Fame.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_CLAN_LVL%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ClanLv.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_REDUCEPK%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ReducePK.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_SEX%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.SexChange.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_AIO_NORMAL%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.AIONormal.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_AIO_30%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.AIONormal30.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_CHANGE_CHAR%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ChangeCharName.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_CHANGE_CLAN%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ChangeClanName.name() + ";0;0;0;0;0");
		html.replace("%BYPASS_INFO_DELEVEL%", "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Delevel.name() + ";0;0;0;0;0");

		
		
		String ByPassLv85_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Lv85.name() +";ask;0;0;0;0;0";
		String ByPassNoble_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Noble.name() +";ask;0;0;0;0;0";
		String ByPassDelevel = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Delevel.name() +";normal;0;0;0;0;0";
		String ByPassFame_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Fame.name() +";ask;0;0;0;0;0";
		String ByPassClan_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ClanLv.name() +";ask;0;0;0;0;0";
		String ByPassReducePK_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ReducePK.name() +";ask;0;0;0;0;0";
		String ByPassSex_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.SexChange.name() +";ask;0;0;0;0;0";
		String ByPassAIO_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal.name() +";ask;0;0;0;0;0";
		String ByPassAIO30_normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal30.name() +";ask;0;0;0;0;0";
		String ByPassCCName_normal = "Write Z_CHANGE_CHAR_NAME_NORMAL Set _ txtCCName txtCCName txtCCName";
		String ByPassCCLName_normal = "Write Z_CHANGE_CLAN_NAME_NORMAL Set _ txtCCLName txtCCLName txtCCLName";
		
		if(!general.OPCIONES_CHAR_LVL85){
			ByPassLv85_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_NOBLE){
			ByPassNoble_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_DELEVEL){
			ByPassDelevel = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_FAME){
			ByPassFame_normal = ByPassDisabled;
		}
		if(!general.EVENT_REPUTATION_CLAN){
			ByPassClan_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_REDUCE_PK){
			ByPassReducePK_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_SEXO){
			ByPassSex_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_BUFFER_AIO){
			ByPassAIO_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_BUFFER_AIO_30){
			ByPassAIO30_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CHAR_CAMBIO_NOMBRE){
			ByPassCCName_normal = ByPassDisabled;
		}
		if(!general.OPCIONES_CLAN_CAMBIO_NOMBRE){
			ByPassCCLName_normal = ByPassDisabled;
		}
		
		html.replace("%BTN_LEVEL85%", (ByPassLv85_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_LEVEL85%", ByPassLv85_normal);
		
		html.replace("%BTN_NOBLE%", (ByPassNoble_normal == "" ? "DISABLED": "Do It Now!!!"));
		html.replace("%BYPASS_NOBLE%", ByPassNoble_normal);
		
		html.replace("%BTN_FAME%", (ByPassFame_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_FAME%", ByPassFame_normal);
		
		html.replace("%BTN_CLAN%", (ByPassClan_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_CLAN%", ByPassClan_normal);
		
		html.replace("%BTN_PK_COUNTER%", (ByPassReducePK_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_PK_COUNTER%", ByPassReducePK_normal);
		
		html.replace("%BTN_SEX%", (ByPassSex_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_SEX%", ByPassSex_normal);
		
		html.replace("%BTN_AIO_NORMAL%", (ByPassAIO_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_AIO_NORMAL%", ByPassAIO_normal);
		
		html.replace("%BTN_AIO_30%", (ByPassAIO30_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_AIO_30%", ByPassAIO30_normal);
		
		html.replace("%BTN_CHANGE_CHAR%", (ByPassCCName_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_CHANGE_CHAR%", ByPassCCName_normal);
		
		html.replace("%BTN_CHANGE_CLAN%", (ByPassCCLName_normal == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_CHANGE_CLAN%", ByPassCCLName_normal);
		
		html.replace("%BTN_DELEVEL%", (ByPassDelevel == "" ? "DISABLED" : "Do It Now!!!"));
		html.replace("%BYPASS_DELEVEL%",ByPassDelevel);
	
		return html.getHtml();
	}
	
	private static void sendDelevelWindows(L2PcInstance player){
		String SendHtml = delevel.htmlDelevel(player);
		central.sendHtml(player, SendHtml);
	}
	
	
	private static String mainHtml(L2PcInstance player, String Params){
		return getMainWindows(player, IsInDonation.get(player.getObjectId()));
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_VARIAS_OPCIONES) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}			
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			IsInDonation.put(player.getObjectId(),false);
			return mainHtml(player,params);
		}else if(parm1.equals("1")){
			IsInDonation.put(player.getObjectId(),true);
			return mainHtml(player,params);			
		}else if(parm1.equals("ShowInfo")){
			IsInDonation.put(player.getObjectId(),false);
			cbManager.separateAndSend(mainHtml(player, params), player);
			showWindowsPrice(player, parm2, false);
			return mainHtml(player,params);
		}else if(parm1.equals(loadByPass.Lv85.name())){
			int LvPlayer = player.getLevel();
			if(LvPlayer>=85){
				central.msgbox(language.getInstance().getMsg(player).YOU_ARE_ALREADY_LEVEL_85, player);
				return mainHtml(player,params);
			}
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_LVL85){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);
				}else{
					if(opera.haveItem(player, general.OPCIONES_CHAR_LVL85_ITEM_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, player);
						opera.set85(player);
						cbFormato.cerrarCB(player);
					}else{
						return mainHtml(player,params);
					}
				}
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_INSCREISE_YOU_LVL_TO_85, IdDialog.MISCELANIUS_lv85);
				mainHtml(player,params);
			}
		}else if(parm1.equals(loadByPass.Noble.name())){
			if(player.isNoble()){
				central.msgbox(language.getInstance().getMsg(player).YOU_ARE_ALREADY_NOBLE, player);
				return mainHtml(player,params);
			}
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_NOBLE){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);
				}
				if(player.isNoble()){
					central.msgbox(language.getInstance().getMsg(player).YOU_ARE_ALREADY_NOBLE, player);
					return mainHtml(player,params);
				}
				if(opera.haveItem(player, general.OPCIONES_CHAR_NOBLE_ITEM_PRICE)){
					opera.removeItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, player);
					opera.setNoble(player);
					cbFormato.cerrarCB(player);
				}else{
					return mainHtml(player,params);
				}
				
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_GET_NOBLE_STATUS, IdDialog.MISCELANIUS_NOBLE);
				mainHtml(player,params);				
			}
		}else if(parm1.equals(loadByPass.Fame.name())){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_FAME){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);				
				}else{
					if(general.OPCIONES_CHAR_FAME_NOBLE && !player.isNoble()){
						central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
						return mainHtml(player,params);
					}
					if(general.OPCIONES_CHAR_FAME_LVL> player.getLevel()){
						central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.OPCIONES_CHAR_FAME_LVL)), player);
						return mainHtml(player,params);
					}
					if(opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_FAME_PRICE, player);
						player.setFame( player.getFame() + general.OPCIONES_CHAR_FAME_GIVE );
						player.broadcastStatusUpdate();
						player.broadcastInfo();
						central.msgbox(language.getInstance().getMsg(player).MIS_YOU_ACQUIRED_FAME.replace("$fame", String.valueOf(general.OPCIONES_CHAR_FAME_GIVE)) , player);
						return mainHtml(player,params);
					}else{
						return mainHtml(player,params);
					}
				}
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_ACQUIRE_FAME.replace("$fame",String.valueOf(general.OPCIONES_CHAR_FAME_GIVE)), IdDialog.MISCELANIUS_FAME);
				mainHtml(player,params);
			}
		}else if(parm1.equals(loadByPass.ClanLv.name())){
			if(parm2.equals("normal")){
				if(!general.EVENT_REPUTATION_CLAN){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);						
				}
				if(!ClanReputationEvent.GiveReward(player)){
					return mainHtml(player,params);
				}else{
					cbFormato.cerrarCB(player);
				}
				return mainHtml(player,params);
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_CLAN_REWARD, IdDialog.MISCELANIUS_CLAN);
				mainHtml(player,params);			
			}
		}else if(parm1.equals(loadByPass.ReducePK.name())){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_REDUCE_PK){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);
				}
				
				if(!opera.haveItem(player, general.OPCIONES_CHAR_REDUCE_PK_PRICE)){
					return mainHtml(player,params);
				}
				
				if(player.getPkKills()>0){
					int newPkCount = player.getPkKills() - general.OPCIONES_CHAR_REDUCE_AMOUNT;
					if(newPkCount<0){
						newPkCount = 0;
					}
					opera.removeItem(general.OPCIONES_CHAR_REDUCE_PK_PRICE, player);
					player.setPkKills(newPkCount);
					player.broadcastStatusUpdate();
					player.broadcastInfo();
					player.broadcastUserInfo();
				}else{
					central.msgbox(language.getInstance().getMsg(player).MIS_YOU_DONT_HAVE_PK_KILL, player);
					return mainHtml(player,params);
				}
				
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_REDUCE_PK_KILL.replace("$pk",String.valueOf(general.OPCIONES_CHAR_REDUCE_AMOUNT)), IdDialog.MISCELANIUS_REDUCEPK);
				mainHtml(player,params);
			}
		}else if(parm1.equals(loadByPass.SexChange.name())){
			if(parm2.equals("normal")){
				if(general.OPCIONES_CHAR_SEXO){
					if(opera.haveItem(player, general.OPCIONES_CHAR_SEXO_ITEM_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, player);
						opera.changeSex(player);
					}
				}else{
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);					
				}
				return mainHtml(player,params);
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_CHANGE_YOUR_SEX, IdDialog.MISCELANIUS_SEX);
				mainHtml(player,params);				
			}
		}else if(parm1.equals(loadByPass.AIONormal.name())){
			if(player.getName().startsWith("[BUFF]")){
				central.msgbox(language.getInstance().getMsg(player).MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN, player);
				return mainHtml(player,params);
			}
			
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_BUFFER_AIO){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);					
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE)){
					return mainHtml(player,params);
				}
				
				if(general.OPCIONES_CHAR_BUFFER_AIO_LVL > player.getLevel()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL)), player);
					return mainHtml(player,params);
				}

				if(aioChar.setNewAIO(player, true)){
					opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, player);
					cbFormato.cerrarCB(player);
				}
				return mainHtml(player,params);
				
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_BE_A_AIOCHAR_TYPE.replace("$type", "Normal"), IdDialog.MISCELANIUS_AIONORMAL);
				mainHtml(player,params);
			}
		}else if(parm1.equals(loadByPass.AIONormal30.name())){
			if(player.getName().startsWith("[BUFF]")){
				central.msgbox(language.getInstance().getMsg(player).MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN, player);
				return mainHtml(player,params);
			}			
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_BUFFER_AIO_30){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);					
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30)){
					return mainHtml(player,params);
				}
				
				if(general.OPCIONES_CHAR_BUFFER_AIO_LVL > player.getLevel()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL)), player);
					return mainHtml(player,params);
				}
				if(aioChar.setNewAIO(player, false)){
					opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30, player);
					cbFormato.cerrarCB(player);
				}
				return mainHtml(player,params);
				
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_BE_A_AIOCHAR_TYPE.replace("$type", "+15/+30 Skill"), IdDialog.MISCELANIUS_AIO30);
				mainHtml(player,params);
			}
		}else if(parm1.equals(loadByPass.ChangeCharName.name())){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_CAMBIO_NOMBRE){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);						
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE)){
					return mainHtml(player,params);
				}
				if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE && !player.isNoble()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
					return mainHtml(player,params);
				}
				if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL > player.getLevel()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL)), player);
					return mainHtml(player,params);
				}
				
				if(!opera.isValidName(parm3)){
					central.msgbox(language.getInstance().getMsg(player).THE_NAME_ENTERED_IS_NOT_VALID,player);
					return mainHtml(player,params);
				}
				
				if(cambionombre.changeName_Char(player, parm3)){
					opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE, player);
					cbFormato.cerrarCB(player);
				}
			}else if(parm2.equals("ask")){
				Dlg.sendDlg(player, language.getInstance().getMsg(player).MIS_YOU_WANT_TO_CHANGE_YOUR_NAME, IdDialog.MISCELANIUS_CHANGE_CHAR_NAME);
				mainHtml(player,params);				
			}
		}else if(parm1.equals(loadByPass.ChangeClanName.name())){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CLAN_CAMBIO_NOMBRE){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);					
				}
				
				if(!player.isClanLeader()){
					central.msgbox(language.getInstance().getMsg(player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION, player);
					return mainHtml(player,params);
				}
				if(player.getClan().getLevel() <= general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_HAVE_A_CLAN_EQUAL_OR_GREATER_THAN.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL)), player);
					return mainHtml(player,params);
				}
				
				if(!opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE)){
					return mainHtml(player,params);
				}
				
				if(clanNomCambio.changeNameClan(parm3, player)){
					opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE, player);
					cbFormato.cerrarCB(player);
				}
			}
		}else if(parm1.equals(loadByPass.Delevel.name() )){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_DELEVEL){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,params);
				}else if(player.getLevel() < general.DELEVEL_LVL_MAX){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.DELEVEL_LVL_MAX)), player);
					return mainHtml(player,params);
				}else if(general.DELEVEL_NOBLE && !player.isNoble()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
					return mainHtml(player,params);
				}
				sendDelevelWindows(player);
			}else if(parm2.equals("APPLY")){
				int toLevel = Integer.valueOf(parm3);
				boolean removeItem = true;
				try{
					player.getStat().setLevel((byte) toLevel);
					player.getStat().setExp(ExperienceData.getInstance().getExpForLevel(toLevel));
					restoreSkills(player);
					player.checkPlayerSkills();
					player.broadcastUserInfo();
					player.sendPacket(new UserInfo(player));
					player.store();
				}catch (Exception e) {
					_log.warning("DELEVEL->" + e.getMessage());
					removeItem=false;
				}
				if(removeItem){
					opera.removeItem(general.DELEVEL_PRICE, player);
				}
			}
			
		}
		return mainHtml(player,params);
	}
	
	private static void restoreSkills(L2PcInstance player)
	{
		final String RESTORE_SKILLS_FOR_CHAR = "SELECT skill_id,skill_level FROM character_skills WHERE charId=? AND class_index=?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(RESTORE_SKILLS_FOR_CHAR))
		{
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, player.getClassIndex());
			final ResultSet rset = statement.executeQuery();

			while (rset.next())
			{
				final int id = rset.getInt("skill_id");
				final int level = rset.getInt("skill_level");

				final L2Skill skill = SkillData.getInstance().getInfo(id, level);

				if (skill == null)
				{
					_log.warning("ZeuS->Skipped null skill Id: " + id + " Level: " + level + " while restoring player skills for playerObjId: " + player.getObjectId() +", Name: " + player.getName());
					continue;
				}

				player.addSkill(skill);

				if (general.DELEVEL_CHECK_SKILL)
				{
					if (!SkillTreesData.getInstance().isSkillAllowed(player, skill))
					{
						String Msn = language.getInstance().getMsg(player).DELEVEL_REMOVE_INVALID_SKILL;
						Msn = Msn.replace("$classname", ClassListData.getInstance().getClass(player.getClassId()).getClassName()).replace("$idlevel", String.valueOf(skill.getLevel())) .replace("$nameskill", skill.getName());
						player.removeSkill(skill);
					}
				}
			}
			rset.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "ZeuS->Could not restore character " + player.getName() +"("+ player.getObjectId() +")" + " skills: " + e.getMessage(), e);
		}
	}	
	
}
