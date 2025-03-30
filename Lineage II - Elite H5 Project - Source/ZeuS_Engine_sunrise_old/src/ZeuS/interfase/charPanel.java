package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.Config.premiumsystem;
import ZeuS.language.language;
import ZeuS.procedimientos.autoPots;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class charPanel {

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(charPanel.class.getName());

	public static void delegar(L2PcInstance player, String params){
		if(params == null){
			getCharPanel(player);
		}else{
			if(params.length()==0){
				getCharPanel(player);
			}else{
				String[] parametros = params.split(" ");
				opera.setPlayerConfig(parametros[0],player);
				getCharPanel(player);
			}
		}
	}



	@SuppressWarnings("unused")
	public static void getCharInfo(L2PcInstance player){
		//MyCharInformation.html
		boolean isPremiumChar = opera.isPremium_Player(player);
		boolean isPremiumClan = opera.isPremium_Clan(player);
		String PremiumCharInicio = "NONE", PremiumCharTermino = "NONE";
		String PremiumClanInicio = "NONE", PremiumClanTermino = "NONE";		
		NpcHtmlMessage _HTML = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/MyCharInformation.html");
		String NombrePremium ="";
		premiumsystem PS = null;
		if(isPremiumChar){
			if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()) !=null){
				//int idPremium = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
				PS = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData();
				NombrePremium = PS.getName();
				int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
				int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
				PremiumCharInicio = opera.getDateFromUnixTime(Inicio);
				PremiumCharTermino = opera.getDateFromUnixTime(Termino);
			}
		}

		_HTML.replace("%PREMIUM_ACCOUNT_NAME%", NombrePremium);
		_HTML.replace("%IS_ACCOUNT_PREMIUM%", (isPremiumChar ? "YES" :  "NO"));
		_HTML.replace("%ACCOUNT_EXP_RATE%", (isPremiumChar ? String.valueOf(PS.getexp(false)) : "0"));
		_HTML.replace("%ACCOUNT_SP_RATE%", (isPremiumChar ? String.valueOf(PS.getsp(false)) : "0"));
		_HTML.replace("%ACCOUNT_ADENA_DROP_CHANCE%", (isPremiumChar ? String.valueOf(PS.getadena_chance(0)) : "0"));
		_HTML.replace("%ACCOUNT_ADENA_DROP_RATE%", (isPremiumChar ? String.valueOf(PS.getadena_rate()) : "0"));
		_HTML.replace("%ACCOUNT_SPOIL_CHANCE%", (isPremiumChar ? String.valueOf(PS.getSpoil_chance(0)) : "0"));
		_HTML.replace("%ACCOUNT_SPOIL_RATE%", (isPremiumChar ? String.valueOf(PS.getSpoil_rate()) : "0"));
		_HTML.replace("%ACCOUNT_ITEM_DROP_CHANCE%", (isPremiumChar ? String.valueOf(PS.getDrop_chance(0)) : "0"));
		_HTML.replace("%ACCOUNT_ITEM_DROP_RATE%", (isPremiumChar ? String.valueOf(PS.getDrop_rate()) : "0"));
		_HTML.replace("%ACCOUNT_EPAULETTE_RATE%", (isPremiumChar ? String.valueOf(PS.getEpaulette()) : "0"));
		_HTML.replace("%ACCOUNT_NORMAL_CRAFT_BONUS%", (isPremiumChar ? String.valueOf(PS.getCraft()) : "0"));
		_HTML.replace("%ACCOUNT_MW_CRAFT_BONUS%", (isPremiumChar ? String.valueOf(PS.get_mwCraft()) : "0"));
		_HTML.replace("%ACCOUNT_SOUL_CRYSTAL_BONUS%", (isPremiumChar ? String.valueOf(PS.getSoulCrystal()) : "0"));
		_HTML.replace("%ACCOUNT_BUFF_PREMIUM%", (isPremiumChar ?  ( PS.canUseBuffPremium() ? "YES" : "NO" ) : "NO"));
		_HTML.replace("%ACCOUNT_BUFF_PREMIUM_TIME%", (isPremiumChar ?  ( PS.canUseBuffPremium() ? String.valueOf(PS.getBuffDuration()) : "0" ) : "0"));		
		_HTML.replace("%ACCOUNT_HERO_STATUS%", isPremiumChar ? ( PS.isHero() ? "YES" : "NO" ) : "NO" );
		_HTML.replace("%ACCOUNT_BEGIN_DATE%", isPremiumChar ? PremiumCharInicio : "");
		_HTML.replace("%ACCOUNT_END_DATE%", isPremiumChar ? PremiumCharTermino: "");
		_HTML.replace("%ACCOUNT_CP_BONUS%", isPremiumChar ? String.valueOf(PS.CP_bonus()) : "0" );
		_HTML.replace("%ACCOUNT_HP_BONUS%", isPremiumChar ? String.valueOf(PS.HP_bonus()) : "0" );
		_HTML.replace("%ACCOUNT_MP_BONUS%", isPremiumChar ? String.valueOf(PS.MP_bonus()) : "0" );		
		_HTML.replace("%ACCOUNT_CP_REST%", isPremiumChar ? ( PS.CP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");
		_HTML.replace("%ACCOUNT_HP_REST%", isPremiumChar ? ( PS.HP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");
		_HTML.replace("%ACCOUNT_MP_REST%", isPremiumChar ? ( PS.MP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");
		
		PS = null;
		String NombreClanPremium = "";
		if(isPremiumClan){
			int idClan = player.getClan().getId();
			if(general.getPremiumDataFromPlayerOrClan(idClan) !=null){
				PS = general.getPremiumDataFromPlayerOrClan(idClan).getPremiumData();
				NombreClanPremium = PS.getName();
				int Inicio = general.getPremiumDataFromPlayerOrClan(idClan).getBegin();
				int Termino = general.getPremiumDataFromPlayerOrClan(idClan).getEnd();
				PremiumClanInicio = opera.getDateFromUnixTime(Inicio);
				PremiumClanTermino = opera.getDateFromUnixTime(Termino);
			}
		}
		
		_HTML.replace("%PREMIUM_CLAN_NAME%", NombreClanPremium);
		_HTML.replace("%IS_CLAN_PREMIUM%", (isPremiumClan ? "YES" :  "NO"));
		_HTML.replace("%CLAN_EXP_RATE%", (isPremiumClan ? String.valueOf(PS.getexp(false)) : "0"));
		_HTML.replace("%CLAN_SP_RATE%", (isPremiumClan ? String.valueOf(PS.getsp(false)) : "0"));
		_HTML.replace("%CLAN_ADENA_DROP_CHANCE%", (isPremiumClan ? String.valueOf(PS.getadena_chance(0)) : "0"));
		_HTML.replace("%CLAN_ADENA_DROP_RATE%", (isPremiumClan ? String.valueOf(PS.getadena_rate()) : "0"));
		_HTML.replace("%CLAN_SPOIL_CHANCE%", (isPremiumClan ? String.valueOf(PS.getSpoil_chance(0)) : "0"));
		_HTML.replace("%CLAN_SPOIL_RATE%", (isPremiumClan ? String.valueOf(PS.getSpoil_rate()) : "0"));
		_HTML.replace("%CLAN_ITEM_DROP_CHANCE%", (isPremiumClan ? String.valueOf(PS.getDrop_chance(0)) : "0"));
		_HTML.replace("%CLAN_ITEM_DROP_RATE%", (isPremiumClan ? String.valueOf(PS.getDrop_rate()) : "0"));
		_HTML.replace("%CLAN_EPAULETTE_RATE%", (isPremiumClan ? String.valueOf(PS.getEpaulette()) : "0"));
		_HTML.replace("%CLAN_NORMAL_CRAFT_BONUS%", (isPremiumClan ? String.valueOf(PS.getCraft()) : "0"));
		_HTML.replace("%CLAN_MW_CRAFT_BONUS%", (isPremiumClan ? String.valueOf(PS.get_mwCraft()) : "0"));
		_HTML.replace("%CLAN_SOUL_CRYSTAL_BONUS%", (isPremiumClan ? String.valueOf(PS.getSoulCrystal()) : "0"));
		_HTML.replace("%CLAN_BUFF_PREMIUM%", (isPremiumClan ?  ( PS.canUseBuffPremium() ? "YES" : "NO" ) : "NO"));
		_HTML.replace("%CLAN_BUFF_PREMIUM_TIME%", (isPremiumClan ?  ( PS.canUseBuffPremium() ? String.valueOf(PS.getBuffDuration()) : "0" ) : "0"));		
		_HTML.replace("%CLAN_HERO_STATUS%", isPremiumClan ? ( PS.isHero() ? "YES" : "NO" ) : "NO" );
		_HTML.replace("%CLAN_BEGIN_DATE%", isPremiumClan ? PremiumClanInicio : "");
		_HTML.replace("%CLAN_END_DATE%", isPremiumClan ? PremiumClanTermino: "");
		_HTML.replace("%CLAN_CP_BONUS%", isPremiumClan ? String.valueOf(PS.CP_bonus()) : "0");
		_HTML.replace("%CLAN_HP_BONUS%", isPremiumClan ? String.valueOf(PS.HP_bonus()) : "0");
		_HTML.replace("%CLAN_MP_BONUS%", isPremiumClan ? String.valueOf(PS.MP_bonus()) : "0");
		_HTML.replace("%CLAN_CP_REST%", isPremiumClan ? ( PS.CP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");
		_HTML.replace("%CLAN_HP_REST%", isPremiumClan ? ( PS.HP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");
		_HTML.replace("%CLAN_MP_REST%", isPremiumClan ? ( PS.MP_bonus_restric_olys() ? "YES" : "NO" ) : "UNK");

		String GridFormato = opera.getGridFormatFromHtml(_HTML, 1, "%ALL_CONECTION_DATA%");
		String AllData = "";
		String consulta = "SELECT * FROM zeus_connection WHERE zeus_connection.charID = ? ORDER BY zeus_connection.date DESC LIMIT 0,10";
		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		int MaximoColumnas = 5;
		String ColWidth = String.valueOf(750 / MaximoColumnas);
		boolean haveNext = false;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, player.getObjectId());
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				AllData += GridFormato.replace("%CONECTION_DATE%",rss.getString(6)).replace("%IP_LAN%", rss.getString(2)).replace("%IP_WAN%", rss.getString(1));
				Contador++;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
		_HTML.replace("%ALL_CONECTION_DATA%", AllData);

		central.sendHtml(player, _HTML.getHtml());
	}

	private static void getCharPanel(L2PcInstance player){
		String btnChange ="<button value=\"Change\" action=\"bypass -h voice %COMANDO%\" width=60 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String tablaConfigBox = "<table width=280><tr><td width=270 align=CENTER>%TABLA1%</td></tr></table>";
		String tablaConfigIndi = "<table width=260><tr><td width=200 align=LEFT>%DESCRIP%</td><td width=60 align=CENTER>"+btnChange+"</td></tr></table>";
		tablaConfigIndi += "<table><tr><td width=260 align=LEFT>%ESTADO%</td></tr></table><br>";

		tablaConfigBox = tablaConfigBox.replace("%TABLA1%", tablaConfigIndi);

		String GrillaPrincipal = central.LineaDivisora(1) + central.headFormat( tablaConfigBox ) + central.LineaDivisora(1);

		Vector<String> SetConfig = new Vector<String>();
		SetConfig.add("Block Experience:;.CharPnl expsp 0 0;" + (!general.getCharConfigEXPSP(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Trade Refusal:;.CharPnl tradec 0 0;" + (general.getCharConfigTRADE(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Badbuff Protection:;.CharPnl noBuff 0 0;" + (general.getCharConfigBADBUFF(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Hide Stores:;.CharPnl hideStore 0 0;" + (general.getCharConfigHIDESTORE(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Party Matching Refusal:;.CharPnl partymatching 0 0;" + (general.getCharConfigPartyMatching(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Show My Stat:;.CharPnl showmystat 0 0;" + (general.getCharConfigSHOWSTAT(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Show Top pvp/pk Effect:;.CharPnl effectpvppk 0 0;" + (general.getCharConfigEFFECT(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Announce top pvp/pk entrance:;.CharPnl annoupvppk 0 0;" + (general.getCharConfigANNOU(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Refusal Mode:;.CharPnl Refusal 0 0;" + (general.getCharConfigREFUSAL(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		
		SetConfig.add("Auto Cp Potion:;.autocp Show;" + (autoPots.isAutoCp(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Auto Mp Potion:;.automp Show;" + (autoPots.isAutoMp(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Auto Hp Potion:;.autohp Show;" + (autoPots.isAutoHp(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		
		if(player.isNoble() && general.OLY_CAN_USE_SCHEME_BUFFER){
			SetConfig.add("Use Oly Scheme Buffer:;.CharPnl olyBuffScheme 0 0;" + (general.getCharConfigOlyScheme(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		}
		SetConfig.add("Read Oly Winner:;.CharPnl readolywinner 0 0;" + (general.getCharConfigReadOlyWinner(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));

		String Box = "";
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/charPanel.html");
		
		for(String secciones: SetConfig){
			String[] Seccc = secciones.split(";");
			String paraGrilla =  GrillaPrincipal;
			paraGrilla = paraGrilla.replace("%DESCRIP%", Seccc[0]).replace("%COMANDO%", Seccc[1]).replace("%ESTADO%", Seccc[2]);
			Box += paraGrilla+"<br1>";
		}
		html.replace("%SERVER_LOGO%",  opera.getImageLogo(player));
		html.replace("%DATA%", Box);

		opera.enviarHTML(player, html.getHtml());

	}

}
