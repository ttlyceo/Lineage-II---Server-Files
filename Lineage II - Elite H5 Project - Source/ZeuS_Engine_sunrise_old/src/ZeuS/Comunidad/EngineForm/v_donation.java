package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.data.xml.impl.SkillTreesData;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.L2SkillLearn;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.PledgeSkillList;
import l2r.gameserver.util.Broadcast;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._donaGift;
import ZeuS.Config.general;
import ZeuS.Config.premiumPersonalData;
import ZeuS.Config.premiumsystem;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.charPanel;
import ZeuS.interfase.clanNomCambio;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;

public class v_donation {
	
	private static Logger _log = Logger.getLogger(v_donation.class.getName());
	
	private static Map<Integer, _donainfo> ByPassSimple = new HashMap<Integer, _donainfo>();
	private static Map<Integer, _donaInfoItemEnchant> ByPassEnchantItem = new HashMap<Integer, _donaInfoItemEnchant>();
	private static Map<Integer, Integer> ChatPremiun = new HashMap<Integer, Integer>();
	
	private static int[] VectorLocaciones = {
			Inventory.PAPERDOLL_HEAD,
			Inventory.PAPERDOLL_CHEST,
			Inventory.PAPERDOLL_LEGS,
			Inventory.PAPERDOLL_GLOVES,
			Inventory.PAPERDOLL_FEET,
			Inventory.PAPERDOLL_UNDER,
			Inventory.PAPERDOLL_BELT,
			Inventory.PAPERDOLL_LEAR,
			Inventory.PAPERDOLL_REAR,
			Inventory.PAPERDOLL_LFINGER,
			Inventory.PAPERDOLL_RFINGER,
			Inventory.PAPERDOLL_NECK,
			Inventory.PAPERDOLL_LBRACELET,
			Inventory.PAPERDOLL_RHAND,
			Inventory.PAPERDOLL_LHAND,
			};
	
	private static String[] VectorElementos = {
			"Fire",
			"Water",
			"Wind",
			"Earth",
			"Holy",
			"Dark"
				
	};
	
	public static void setChar(L2PcInstance player, String Msje) {
		if(ZeuS.isPremium(player)) {
			boolean isPrClan = opera.isPremium_Clan(player);
			boolean isPrAccount = opera.isPremium_Player(player);
			
			int ChatDelayClan = -1;
			int ChatDelayAccount = -1;
			
			int idChat = -1;
			
			if(isPrClan) {
				if(general.getPremiumDataFromPlayerOrClan(player.getClan().getId()).getPremiumData().hasChatPremium()) {
					ChatDelayClan = general.getPremiumDataFromPlayerOrClan(player.getClanId()).getPremiumData().chatReusePremium();
					idChat = general.getPremiumDataFromPlayerOrClan(player.getClanId()).getPremiumData().chatIDPremium();
					_log.warning("Clan Delay: " + ChatDelayClan);
				}
			}
			
			if(isPrAccount) {
				if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData().hasChatPremium()) {
					ChatDelayAccount = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData().chatReusePremium();
					idChat = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData().chatIDPremium();
					_log.warning("Char Delay: " + ChatDelayAccount);
				}
			}
			
			
			
			if(ChatDelayClan > 0 || ChatDelayAccount >0) {
				boolean sendMsg = false;
				int tmpChatDelay = 0;
				if(ChatDelayClan > 0 && ChatDelayAccount > 0) {
					tmpChatDelay = ChatDelayAccount <= ChatDelayClan ? ChatDelayAccount : ChatDelayClan;
				}else if(ChatDelayClan > 0 && ChatDelayAccount < 0){
					tmpChatDelay = ChatDelayClan;
				}else if(ChatDelayAccount > 0 && ChatDelayClan < 0) {
					tmpChatDelay = ChatDelayAccount;
				}
				if(ChatPremiun != null) {
					if(ChatPremiun.containsKey(player.getObjectId())) {
						int NextChat = ChatPremiun.get(player.getObjectId());
						if(opera.getUnixTimeNow() >= NextChat) {
							sendMsg = true;
						}
					}else {
						sendMsg = true;
					}
				}else {
					sendMsg = true;
				}
				if(sendMsg) {
					int chatAgain = tmpChatDelay + opera.getUnixTimeNow();
					ChatPremiun.put(player.getObjectId(), chatAgain);
					CreatureSay strMensaje = new CreatureSay(0, idChat, player.getName(), Msje);
					Broadcast.toAllOnlinePlayers(strMensaje);
				}else {
					String msje = "Action failed. You are able to use premium chat only once per " + String.valueOf(tmpChatDelay) + " seconds";
					central.msgbox(msje, player);
				}
			}
		}
	}

	public static void setDonationInformation(L2PcInstance player, String Quantity, String Type, String Observ){
		String Query = "INSERT INTO zeus_dona_espera(id, dona_monto, dona_char, dona_medio, dona_email, dona_obser) VALUES(?,?,?,?,?,?)";
		String IdForMd5 = String.valueOf(opera.getUnixTimeL2JServer()) + String.valueOf(player.getObjectId());
		String Encript = opera.toMD5(IdForMd5);
		Connection con = null;
		PreparedStatement ins = null;
		boolean sendEmail = false;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Query);
			ins.setString(1, Encript);
			ins.setString(2, Quantity);
			ins.setString(3, String.valueOf(player.getObjectId()));
			ins.setString(4, Type);
			ins.setString(5, opera.getUserMail(player.getAccountName()));
			ins.setString(6, Observ);
			try{
				ins.executeUpdate();
				sendEmail = true;
			}catch(SQLException e){
				_log.warning("Error on Saving Donation Information from ->" + player.getName() + ": " + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error on Saving Donation Information (2) from ->" + player.getName() + ": " + a.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){

		}
		
		if(sendEmail){
			central.msgbox_Lado(language.getInstance().getMsg(player).SENDING_DONATION_VOUCHER_TO_ADMINISTRATOR, player);
			if(jMail.sendDonationInformation(player, Encript, Type, opera.getFormatNumbers(Quantity), Observ)){
				central.msgbox_Lado(language.getInstance().getMsg(player).VOUCHER_SENT_SUCCESSFULLY, player);
			}else{
				central.msgbox_Lado(language.getInstance().getMsg(player).VOUCHER_SENT_ERROR, player);
			}
			
		}
		
	}
	
	private static void sendNotificacionDonacion(L2PcInstance player){
		if(!general._activated()){
			return;
		}
		
		if(!EmailRegistration.hasEmailRegister(player)){
			central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL.replace("$command", ".acc_register"), player);
			return;
		}
		String EmailUsuario = opera.getUserMail(player.getAccountName());

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/donation_notification.html");
		html.replace("%EMAIL%", EmailUsuario);
		html.replace("%DONATION_TYPE_LIST%", language.getInstance().getMsg(player).DONATION_TYPE);
		central.sendHtml(player, html);
	}	
	
	
	public static void checkDonationPoint(L2PcInstance player){
		String Qry = "select cuenta, creditos from zeus_dona_creditos where cuenta=? and entregados='NO'";
		Connection conn = null;
		int Points = 0;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			psqry.setString(1, player.getAccountName());
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						Points += rss.getInt(2);
					}catch(SQLException e){
						
					}
				}
				conn.close();
			}catch(SQLException a){
				_log.warning("Error on Get Donation Points->->" + a.getMessage());
			}
		try{
			conn.close();
		}catch (Exception e) {

		}
		
		if(Points>0){
			Qry = "update zeus_dona_creditos set entregados='SI', fechaEntregado = NOW(), emailed='true' where cuenta=? and entregados='NO'";
			try{
				conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement psqry = conn.prepareStatement(Qry);
				psqry.setString(1, player.getAccountName());
				psqry.executeUpdate();
			}catch(SQLException a){
				_log.warning("Error on update Donation ->->" + a.getMessage());
			}
			try{
				conn.close();
			}catch (Exception e) {

			}			
			
			opera.giveReward(player, general.DONA_ID_ITEM, Points);
			central.msgbox_Lado(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOU_GET_DONATION_ITEMS.replace("$quantity", opera.getFormatNumbers(Points)), player);
			if(general.DONATION_EXTRA_GIFT && Points >= general.DONATION_EXTRA_GIFT_FROM_DONATION_COIN){
				try{
					_donaGift _temp = new _donaGift(player);
					general.setDonationGift(_temp);
				}catch(Exception a){
					_log.warning("Error on create Gift: " + a.getMessage());
				}
			}
		}
		
	}
	
	
	private static String mainHtml(L2PcInstance player, String TipoBusqueda,int Pagina){
		checkDonationPoint(player);

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%BYPASS_ENGINE_LINK%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%COST_CHANGE_NAME%", String.valueOf(general.DONATION_CHANGE_CHAR_NAME_COST));
		html.replace("%COST_CHANGE_CLAN_NAME%", String.valueOf(general.DONATION_CHANGE_CLAN_NAME_COST));
		html.replace("%COST_255_RECO%", String.valueOf(general.DONATION_255_RECOMMENDS));
		html.replace("%COST_NOBLE%", String.valueOf(general.DONATION_NOBLE_COST));
		html.replace("%COST_CHANGE_CHAR_SEX%", String.valueOf(general.DONATION_CHANGE_SEX_COST));
		html.replace("%COST_AIO_NORMAL%", String.valueOf(general.DONATION_AIO_CHAR_SIMPLE_COSTO));
		html.replace("%COST_AIO_30%", String.valueOf(general.DONATION_AIO_CHAR_30_COSTO));
		
		return html.getHtml();		
	}
	
	private static void sendHtmRequest(L2PcInstance player, String Comando){
		NpcHtmlMessage html = null;
		String Bypass = "bypass -h ZeuS changenames "+ Comando +" $txtNewName";
		
		if(Comando.equals("CHANGE_CHAR_NAME")){
			html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-change-player-name.htm");
		}else{
			html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-change-clan-name.htm");
		}
		html.replace("%CHANGE_BYPASS%", Bypass);
		central.sendHtml(player, html.getHtml());
	}
	
	private static int ElementalToId(String El){
		int IDElement =0;
		switch(El){
			case "Fire":
				IDElement = 0;
				break;
			case "Water":
				IDElement = 1;
				break;
			case "Wind":
				IDElement = 2;
				break;
			case "Earth":
				IDElement = 3;
				break;
			case "Holy":
				IDElement = 4;
				break;
			case "Dark":
				IDElement = 5;
				break;				
		}
		return IDElement;
	}	
	
	private static int idTypeToElemental(String El){
		int IDItemEnchant =0;
		switch(El){
			case "Fire":
				IDItemEnchant = 9558;
				break;
			case "Water":
				IDItemEnchant = 9559;
				break;
			case "Wind":
				IDItemEnchant = 9561;
				break;
			case "Earth":
				IDItemEnchant = 9560;
				break;
			case "Dark":
				IDItemEnchant = 9562;
				break;
			case "Holy":
				IDItemEnchant = 9563;
				break;
		}
		return IDItemEnchant;
	}
	
	
	private static String getElementalList(L2PcInstance player, L2ItemInstance It){
		String Retorno = "";
		for(String El : VectorElementos){
			if(Retorno.length()>0){
				Retorno += ";";
			}
			if(It.getElementals()!=null){
				if(It.getElementals().length<3){
					if(v_AugmentSpecial.CanUseStoneOnThis(player, It.getObjectId(), idTypeToElemental(El))){
						Retorno += El;
					}
				}else{
					int IdEle = ElementalToId(El);
					for(Elementals IdT : It.getElementals()){
						if(IdT.getElement()==IdEle){
							Retorno += El;						
						}
					}
				}
			}else{
				if(v_AugmentSpecial.CanUseStoneOnThis(player, It.getObjectId(), idTypeToElemental(El))){
					Retorno += El;
				}				
			}
		}
		return Retorno;
	}
	
	@SuppressWarnings("rawtypes")
	private static void getArmorElementalWindows(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-elemental-armor-and-weapon.htm");
		boolean armorActive = true;
		boolean weaponActive = true;
		
		String cmbEnchantArmor = "";
		String cmbEnchantWeapon = "";
		
		String ValEnchantArmor = "";
		String ValEnchantWeapon = "";
		
		int MaxArmorEnchantElemental =0;
		int MaxWeaponEnchantElemental =0;
		
		String _Color = opera.getGridFormatFromHtml(html, 1, "");
		String ColorFontForEnchant = _Color.split(":")[0].split(",")[1];
		String ColorFontForEnchantPrice = _Color.split(":")[1].split(",")[1];
		html.replace("%COLOR_ARMOR_WEAPON_TYPE%", ColorFontForEnchant);
		html.replace("%COLOR_ARMOR_WEAPON_COST%", ColorFontForEnchantPrice);
		
		String formatToShowEnchantAndPriceForArmor = opera.getGridFormatFromHtml(html, 2, "%ARMOR_PRICE%");
		String formatToShowEnchantAndPriceForWeapon = opera.getGridFormatFromHtml(html, 3, "%WEAPON_PRICE%");
		
		if(general.DONATION_ELEMENTAL_ITEM_ARMOR==null){
			armorActive = false;
		}else{
			Iterator itr = general.DONATION_ELEMENTAL_ITEM_ARMOR.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int EnchantElemental = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantArmor.length()>0){
					cmbEnchantArmor+= ";";
					ValEnchantArmor+= " , ";
				}
				if(EnchantElemental > MaxArmorEnchantElemental){
					MaxArmorEnchantElemental = EnchantElemental;
				}
				cmbEnchantArmor += String.valueOf(EnchantElemental);
				ValEnchantArmor +=  formatToShowEnchantAndPriceForArmor.replace("%ARMOR_ELEMENTAL_PRICE%", String.valueOf(CantidadDona)).replace("%ARMOR_ELEMENTAL_VALUE%", String.valueOf(EnchantElemental));  // "<font color="+ ColorFontForEnchant +">+"+ String.valueOf(EnchantElemental) +"</font> = <font color="+ ColorFontForEnchantPrice +">"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		html.replace("%ARMOR_PRICE%", ValEnchantArmor);
		
		
		
		if(general.DONATION_ELEMENTAL_ITEM_WEAPON==null){
			weaponActive = false;
		}else{
			Iterator itr = general.DONATION_ELEMENTAL_ITEM_WEAPON.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int EnchantElemental = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantWeapon.length()>0){
					cmbEnchantWeapon+= ";";
					ValEnchantWeapon+= " , ";
				}
				if(EnchantElemental > MaxWeaponEnchantElemental){
					MaxWeaponEnchantElemental = EnchantElemental;
				}
				cmbEnchantWeapon += String.valueOf(EnchantElemental);
				ValEnchantWeapon += formatToShowEnchantAndPriceForWeapon.replace("%WEAPON_ELEMENTAL_PRICE%", String.valueOf(CantidadDona)) .replace("%WEAPON_ELEMENTAL_VALUE%", String.valueOf(EnchantElemental)); // "<font color=FE642E>+"+ String.valueOf(EnchantElemental) +"</font> = <font color=FE9A2E>"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		html.replace("%WEAPON_PRICE%", ValEnchantWeapon);
		
		if(!armorActive && !weaponActive){
			return;
		}

		String BypassArmor = "bypass -h ZeuS donation En_E_Armor %IDSLOT% %ID_OBJECT% $cmbEn_%ID% $cmbEnVal_%ID%";
		String BypassWeapon = "bypass -h ZeuS donation En_E_Weapon %IDSLOT% %ID_OBJECT% $cmbEn_%ID% $cmbEnVal_%ID%";
		
		String GridFormat = opera.getGridFormatFromHtml(html, 5, "%GRID_ITEM_DATA%");
		
		String GrillaArmor = GridFormat.replace("%ELEMENTAL_BYPASS%", BypassArmor).replace("%TYPE_VALUE%", cmbEnchantArmor); 
		String GrillaWeapon = GridFormat.replace("%ELEMENTAL_BYPASS%", BypassWeapon) .replace("%TYPE_VALUE%", cmbEnchantWeapon);

		String GridDataAll = "";
		
		int cont = 0;
		
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				L2ItemInstance It = player.getInventory().getPaperdollItem(Info);
				if(It.isEnchantable()==0 || !It.isElementable()){
					continue;
				}		
				
				String TypeElementalStr = getElementalList(player,It);
				
				String I_Ima = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				I_Name = ( I_Name.length() > 24 ? I_Name.substring(0, 24) + "..." : I_Name );
				int I_Enchant = player.getInventory().getPaperdollItem(Info).getEnchantLevel();
				int powerElemen = It.getAttackElementPower();
				if(It.isWeapon()){
					if(powerElemen >= MaxWeaponEnchantElemental){
						continue;
					}
					GridDataAll += GrillaWeapon.replace("%TYPE_ELEMENTAL%", TypeElementalStr).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));
				}else{
					if(powerElemen >= MaxArmorEnchantElemental){
						continue;
					}
					GridDataAll += GrillaArmor.replace("%TYPE_ELEMENTAL%", TypeElementalStr).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));					
				}
				cont++;
			}
		}
		
		html.replace("%GRID_ITEM_DATA%", GridDataAll);
		central.sendHtml(player, html.getHtml());
	}
	
	private static void updatePremiumTable(){
		String DeleteInfo = "DELETE FROM zeus_premium WHERE end_date <= ?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
			{
				statementt.setInt(1, opera.getUnixTimeNow());
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}	
	}	
	
	private static boolean savePremiumOnBd(L2PcInstance player, int idPremium){
		if(general.getPremiumServices()==null){
			central.msgbox(language.getInstance().getMsg(player).DONATION_NO_PREMIUM_DATA_ADDED, player);
			return false;
		}
		if(!general.getPremiumServices().containsKey(idPremium)){
			central.msgbox(language.getInstance().getMsg(player).DONATION_NO_PREMIUM_DATA_ADDED, player);
			return false;
		}
		premiumsystem t = general.getPremiumServices().get(idPremium);
		
		if(!t.IsAccount()){
			if(player.getClan()==null){
				central.msgbox(language.getInstance().getMsg(player).DONATION_NO_HAVE_CLAN_FOR_PREMIUM_DATA, player);
				return false;
			}
			if(!player.isClanLeader()){
				central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_NEED_TO_BE_CLAN_LEADER_TO_GET_PREMIUM_DATA, player);
				return false;				
			}
		}
		
		String TipoPremium = t.IsAccount() ? "ACCOUNT" : "CLAN";
		String IdPpl_Clan = t.IsAccount() ? player.getAccountName() : String.valueOf(player.getClan().getId());
		
		int Days = t.getDays();
		int DiasUnixDar = Days * 86400;
		int NowUnix = opera.getUnixTimeNow();
		
		updatePremiumTable();
		
		String consulta = "INSERT INTO zeus_premium(id,start_date,end_date,tip,idPremium) "+
			"values(?,?,?,?,?) "+
			"ON DUPLICATE KEY UPDATE end_date = end_date + ?";
		
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setString(1, IdPpl_Clan);
			psqry.setInt(2, NowUnix);
			psqry.setInt(3, NowUnix + DiasUnixDar);
			psqry.setString(4, TipoPremium);
			psqry.setInt(5, idPremium);
			psqry.setInt(6, DiasUnixDar);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e){
				_log.warning("Error Premium Save Info->" + e.getMessage());
				central.msgbox(language.getInstance().getMsg(player).DONATION_ERROR_CREATING_PREMIUM, player);
				return false;
			}
		}catch(SQLException a){
			_log.warning("Error Premium Creating Info->" + a.getMessage());
			central.msgbox(language.getInstance().getMsg(player).DONATION_ERROR_CREATING_PREMIUM, player);
			return false;			
		}
		try{
			conn.close();
		}catch(SQLException a){

		}
		
		int intTermino = NowUnix + DiasUnixDar;
		int intInicio = NowUnix;
		
		if(t.IsAccount()){
			if(opera.isPremium_Player(player)){
				intTermino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd() + DiasUnixDar;
				intInicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
			}
		}else{
			if(opera.isPremium_Clan(player)){
				intTermino = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getEnd() + DiasUnixDar;
				intInicio = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getBegin();
			}
		}
		
		premiumPersonalData Pd = new premiumPersonalData(IdPpl_Clan,intInicio,intTermino,t.IsAccount(),t.getIDPremium(),t);
		
		general.setNewPremiumPersonalData(IdPpl_Clan, Pd);
		
		if(t.isHero()){
			player.setHero(true);
			player.broadcastInfo();//To the World
			player.broadcastUserInfo();//To Me
		}
		
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	private static String getComboEnchant(boolean isArmor, int EnchantPeace){
		String EnchantCombo = "";
		Iterator itr;
		if(isArmor){
			itr = general.DONATION_ENCHANT_ITEM_ARMOR.entrySet().iterator();
		}else{
			itr = general.DONATION_ENCHANT_ITEM_WEAPON.entrySet().iterator();
		}
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int Enchant = (int) Entrada.getKey();
			if(EnchantCombo.length()>0){
				EnchantCombo+= ";";
			}
			if(Enchant > EnchantPeace){
				EnchantCombo+= ";" + String.valueOf(Enchant);
			}
		}
		return EnchantCombo;
	}
	
	@SuppressWarnings("rawtypes")
	private static void getArmorEnchantWindows(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-enchant-armor-and-weapon.htm");
		boolean armorActive = true;
		boolean weaponActive = true;
		
		String ValEnchantArmor = "";
		String ValEnchantWeapon = "";
		
		int MaxArmorEnchant =0;
		int MaxWeaponEnchant =0;
		
		String _Color = opera.getGridFormatFromHtml(html, 1, "");
		String ColorFontForEnchant = _Color.split(":")[0].split(",")[1];
		String ColorFontForEnchantPrice = _Color.split(":")[1].split(",")[1];
		html.replace("%COLOR_ARMOR_WEAPON_TYPE%", ColorFontForEnchant);
		html.replace("%COLOR_ARMOR_WEAPON_COST%", ColorFontForEnchantPrice);
		
		String formatToShowEnchantAndPriceForArmor = opera.getGridFormatFromHtml(html, 2, "%ARMOR_PRICE%");
		String formatToShowEnchantAndPriceForWeapon = opera.getGridFormatFromHtml(html, 3, "%WEAPON_PRICE%");		
		
		if(general.DONATION_ENCHANT_ITEM_ARMOR==null){
			armorActive = false;
		}else{
			Iterator itr = general.DONATION_ENCHANT_ITEM_ARMOR.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(ValEnchantArmor.length()>0){
					ValEnchantArmor+= " , ";
				}
				if(Enchant > MaxArmorEnchant){
					MaxArmorEnchant = Enchant;
				}
				ValEnchantArmor += formatToShowEnchantAndPriceForArmor.replace("%ARMOR_ENCHANT_PRICE%", String.valueOf(CantidadDona)).replace("%ARMOR_ENCHANT_VALUE%", String.valueOf(Enchant));
			}			
		}
		
		html.replace("%ARMOR_PRICE%", ValEnchantArmor);
		
		if(general.DONATION_ENCHANT_ITEM_WEAPON==null){
			weaponActive = false;
		}else{
			Iterator itr = general.DONATION_ENCHANT_ITEM_WEAPON.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(ValEnchantWeapon.length()>0){
					ValEnchantWeapon+= " , ";
				}
				if(Enchant > MaxWeaponEnchant){
					MaxWeaponEnchant = Enchant;
				}
				ValEnchantWeapon += formatToShowEnchantAndPriceForWeapon.replace("%WEAPON_ENCHANT_PRICE%", String.valueOf(CantidadDona)) .replace("%WEAPON_ENCHANT_VALUE%", String.valueOf(Enchant));
			}			
		}
		
		html.replace("%WEAPON_PRICE%", ValEnchantWeapon);
		
		if(!armorActive && !weaponActive){
			return;
		}
		String BypassArmor = "bypass -h ZeuS donation EnArmor %IDSLOT% %ID_OBJECT% $cmbEn_%ID%";
		String BypassWeapon = "bypass -h ZeuS donation EnWeapon %IDSLOT% %ID_OBJECT% $cmbEn_%ID%";

		String GridData = opera.getGridFormatFromHtml(html, 4, "%GRID_DATA%");
		
		String GrillaArmor = GridData.replace("%BYPASS_DATA%", BypassArmor);
		String GrillaWeapon = GridData.replace("%BYPASS_DATA%", BypassWeapon);
		
		String GridAllData = "";
		int cont = 0;
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				L2ItemInstance It = player.getInventory().getPaperdollItem(Info);
				
				if(It.isEnchantable()==0){
					continue;
				}
				
				if(general.DONATION_BLACK_LIST != null) {
					if(general.DONATION_BLACK_LIST.size() > 0) {
						if(general.DONATION_BLACK_LIST.contains( It.getItem().getId() ) ) {
							continue;
						}
					}
				}
				
				String I_Ima = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				I_Name = ( I_Name.length() > 24 ? I_Name.substring(0, 24) + "..." : I_Name );
				int I_Enchant = player.getInventory().getPaperdollItem(Info).getEnchantLevel();
				if(It.isWeapon()){
					if(I_Enchant >= MaxWeaponEnchant){
						continue;
					}
					GridAllData += GrillaWeapon.replace("%COMBO_DATA%", getComboEnchant(false,I_Enchant)).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));
				}else{
					if(I_Enchant >= MaxArmorEnchant){
						continue;
					}
					GridAllData += GrillaArmor.replace("%COMBO_DATA%", getComboEnchant(true,I_Enchant)).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));					
				}
				cont++;
			}
		}
		
		html.replace("%GRID_DATA%", GridAllData);

		central.sendHtml(player, html.getHtml());
	}
	
	@SuppressWarnings("rawtypes")
	private static void getPremiumShowAllWindows(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-premium-list.htm");
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_SEE;%IDPRE%;0;0;0;0;0";
		String GridFormat = opera.getGridFormatFromHtml(html, 1, "%GRID_DATA%").replace("%BYPASS%", ByPass);

		String GridAllData = "";

		Iterator itr = general.getPremiumServices().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idPremium = (int) Entrada.getKey();
			premiumsystem PremiumInfo = (premiumsystem) Entrada.getValue();
			if(!PremiumInfo.isEnabled()){
				continue;
			}else if(!PremiumInfo.isForPlayerSee() ){
				continue;
			}
			GridAllData += GridFormat.replace("%IDPRE%", String.valueOf(idPremium)).replace("%APPLI%", PremiumInfo.getAplicableA()) .replace("%DC%", String.valueOf(PremiumInfo.getCost())) .replace("%DAYS%", String.valueOf(PremiumInfo.getDays())) .replace("%NAME%", PremiumInfo.getName()) .replace("%IMAGEN%", PremiumInfo.getIcono());
		}
		html.replace("%GRID_DATA%", GridAllData);
		central.sendHtml(player, html.getHtml());
	}
	
	private static String getInfoFromXML(String Grilla, String Nombre, String Valor, boolean isJustForRate){
		String NomOut = "";
		String ValOut = "";
		NomOut = Nombre.replace("_", " ");		
		switch(Nombre.toLowerCase()) {
			case "hero_status":
			case "just_for_test":
			case "buff_premium":
			case "buff_duration":
			case "item_drop_chance":
			case "item_drop_rate":
			case "mp_bonus":
			case "hp_bonus":
			case "cp_bonus":
			case "mp_oly":
			case "hp_oly":
			case "cp_oly":
			case "chat":
				ValOut = "";
				break;
			default:
				if(isJustForRate) {
					ValOut = " (" + Valor.replace("_", " ") + ")";
				}else {
					ValOut = " +" + Valor.replace("_", " ") + "%";
				}
				break;
		}
		
		/*
		if(!Nombre.equalsIgnoreCase("hero_status") && !Nombre.equalsIgnoreCase("Just_For_Test") && !Nombre.equalsIgnoreCase("buff_premium") && !Nombre.equalsIgnoreCase("buff_duration") && !Nombre.equalsIgnoreCase("item_drop_chance") && !Nombre.equalsIgnoreCase("item_drop_rate")){
			NomOut = Nombre.replace("_", " ");
			if(isJustForRate) {
				ValOut = " (" + Valor.replace("_", " ") + ")";
			}else {
				ValOut = " +" + Valor.replace("_", " ") + "%";
			}
		}else{
			NomOut = " " + Nombre.replace("_", " ");
			ValOut = "";			
		}*/
		
		return Grilla.replace("%NAME%", NomOut).replace("%ORIGINAL_VALUE%", ValOut);
	} 
	
	private static void getPremiumShowInfoWindows(L2PcInstance player, int idPremium, boolean isFromGM){
		getPremiumShowInfoWindows(player, idPremium, false, "", isFromGM);
	}
	
	public static void getPremiumShowInfoWindows(L2PcInstance player, int idPremium, boolean fromPremiumChar, String PremiumCharName, boolean isFromGM){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-premium-explain.htm");		
		if(general.getPremiumServices()==null){
			return;
		}
		if(!general.getPremiumServices().containsKey(idPremium)){
			return;
		}

		premiumsystem p = general.getPremiumServices().get(idPremium);
		
		String ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM;0;0;0;0;0;0";//
		String ByPassBuy = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_BUY;%IDPREMIUM%;0;0;0;0;0";//

		String gridChar = opera.getGridFormatFromHtml(html, 1, "%INFO_CHAR%");
		
		if(fromPremiumChar){
			html.replace("%INFO_CHAR%", gridChar.replace("%CHAR_NAME%", PremiumCharName));
		}else {
			html.replace("%INFO_CHAR%", "");
		}

		html.replace("%IMG_ICON%", p.getIcono());
		html.replace("%PREMIUM_NAME%", p.getName());
		html.replace("%APPLICATE_TO%", p.getAplicableA());
		html.replace("%DAYS%", String.valueOf(p.getDays()));
		html.replace("%COST_COUNT%", String.valueOf(p.getCost()));
		html.replace("%COST_ITEM_NAME%", central.getNombreITEMbyID(general.DONA_ID_ITEM));
		
		String Grilla = opera.getGridFormatFromHtml(html, 2, "%PREMIUM_DATA_SYSTEM%");
		String GrillaData = "";
		
		boolean haveDropItemChancePremium = false;
		boolean haveDropItemRatePremium = false;
		
		boolean isJustRate = p.useValueForNewRate();
		
		for(String d : p.getInfoShow()){
			String Nombre = d.split(":")[0];
			String ValOfi = d.split(":")[1];
			String tG = "";
			String FinalValue = "";
			switch(Nombre.toLowerCase()){
				case "exp":
					if(isJustRate) {
						tG = getInfoFromXML(Grilla, Nombre, String.valueOf(Config.RATE_XP), isJustRate);
					}else {
						tG = getInfoFromXML(Grilla, Nombre, ValOfi, isJustRate);
					}
					FinalValue = "x" + String.valueOf(p.getexp(!isJustRate));
					break;
				case "sp":
					if(isJustRate) {
						tG = getInfoFromXML(Grilla, Nombre, String.valueOf(Config.RATE_SP), isJustRate);
					}else {
						tG = getInfoFromXML(Grilla, Nombre, ValOfi, isJustRate);
					}		
					FinalValue = "x" + String.valueOf(p.getsp(!isJustRate));
					break;
				case "mp_bonus":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = String.valueOf(p.MP_bonus()) + "(O.R.:"+ (p.MP_bonus_restric_olys() ? "Y" : "N") +")";
					break;					
				case "cp_bonus":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = String.valueOf(p.CP_bonus()) + "(O.R.:"+ (p.CP_bonus_restric_olys() ? "Y" : "N") +")";
					break;
				case "hp_bonus":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = String.valueOf(p.HP_bonus()) + "(O.R.:"+ (p.HP_bonus_restric_olys() ? "Y" : "N") +")";
					break;						
				case "adena_chance":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "x" + String.valueOf(p.getadena_chance(0));
					break;
				case "adena_rate":
					if(isJustRate) {
						if(Config.RATE_DROP_ITEMS_ID.containsKey(57)) {
							tG = getInfoFromXML(Grilla, Nombre, String.valueOf( Config.RATE_DROP_ITEMS_ID.get(57)), isJustRate);
						}else {
							tG = getInfoFromXML(Grilla, Nombre, "0", isJustRate);
						}
					}else {
						tG = getInfoFromXML(Grilla, Nombre, ValOfi, isJustRate);
					}
					FinalValue = "x" + String.valueOf(p.getadena_rate());
					break;					
				case "drop_chance":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "x" + String.valueOf(p.getDrop_chance(0));
					break;
				case "drop_rate":
					if(isJustRate) {
						tG = getInfoFromXML(Grilla, Nombre, String.valueOf( Config.RATE_DROP_ITEMS ) , isJustRate);
					}else {
						tG = getInfoFromXML(Grilla, Nombre, ValOfi, isJustRate);
					}
					FinalValue = "x" + String.valueOf(p.getDrop_rate());
					break;					
				case "spoil_chance":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "x" + String.valueOf(p.getSpoil_chance(0));
					break;
				case "spoil_rate":
					if(isJustRate) {
						tG = getInfoFromXML(Grilla, Nombre, String.valueOf(Config.RATE_DROP_SPOIL), isJustRate);
					}else {
						tG = getInfoFromXML(Grilla, Nombre, ValOfi, isJustRate);
					}
					FinalValue = "x" + String.valueOf(p.getSpoil_rate());
					break;					
				case "epaulette":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "Drop +%" + String.valueOf(p.getEpaulette());
					break;	
				case "craft":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "Recipe +%" + String.valueOf(p.getCraft());
					break;
				case "mw_craft":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "Recipe +%" + String.valueOf(p.get_mwCraft());
					break;
				case "buff_premium":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = ( p.canUseBuffPremium() ? "Yes" : "No" ) ;
					break;
				case "buff_duration":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = String.valueOf(p.getBuffDuration()) + " Min." ;
					break;
				case "soul_crystal":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "Chance +%" + String.valueOf(p.getSoulCrystal());
					break;
				case "weight_limit":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = "+%" + String.valueOf(p.getWeight(false,0));
					break;
				case "item_drop_chance":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					haveDropItemChancePremium = p.hasPremiumItemChance();
					FinalValue = (p.hasPremiumItemChance() ? "Yes" : "No");
					break;
				case "item_drop_rate":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					haveDropItemRatePremium = p.hasPremiumItemRate();
					FinalValue = (p.hasPremiumItemRate() ? "Yes" : "No");
					break;					
				case "justfortest":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = (p.isTest() ? "Yes" : "No");
					break;
				case "hero_status":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = (p.isHero() ? "Yes" : "No");
					break;
				case "chat":
					tG = getInfoFromXML(Grilla, Nombre, ValOfi, false);
					FinalValue = (p.hasChatPremium() ? "Yes" : "No");
					break;					
			}
			GrillaData += tG.replace("%FINAL_RESULT%", FinalValue);
		}
		html.replace("%PREMIUM_DATA_SYSTEM%", GrillaData);
		String GridItemChance = opera.getGridFormatFromHtml(html, 3, "%PREMIUM_ITEM_GRID%");
		String GridItemRate = opera.getGridFormatFromHtml(html, 4, "%PREMIUM_ITEM_RATE%");		
		if(!isFromGM){
			if(haveDropItemChancePremium){
				String ByPassShowPremiumItem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_SHOW_ITEM_CHANCE;"+ String.valueOf(p.getIDPremium()) +";0;0;0;0;0";;
				html.replace("%PREMIUM_ITEM_GRID%", GridItemChance.replace("%BYPASS_ITEMS_CHANCE%", ByPassShowPremiumItem));
			}
			
			if(haveDropItemRatePremium){
				String ByPassShowPremiumItem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_SHOW_ITEM_RATE;"+ String.valueOf(p.getIDPremium()) +";0;0;0;0;0";;
				html.replace("%PREMIUM_ITEM_RATE%", GridItemRate.replaceAll("%BYPASS_ITEM_RATE%", ByPassShowPremiumItem));
			}
			html.replace("%BYPASS_BUY_PREMIUM%", ByPassBuy.replace("%IDPREMIUM%", String.valueOf(idPremium)));
			html.replace("%BYPASS_BACK%", ByPassBack);
		}
		html.replace("%PREMIUM_ITEM_GRID%", "");
		html.replace("%PREMIUM_ITEM_RATE%", "");
		central.sendHtml(player, html);
	}
	

	
	@SuppressWarnings("rawtypes")
	private static void PrivateHtmlRequest(L2PcInstance player, String Command, Map<Integer,Integer> Op){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-charlevel-charfame-charpk-clanlevel-clanskill-clanreputation.htm");
		String CharLevelData = opera.getGridFormatFromHtml(html, 1, "");
		String CharFameData = opera.getGridFormatFromHtml(html, 2, "");
		String CharPKData = opera.getGridFormatFromHtml(html, 3, "");
		String ClanLvLData = opera.getGridFormatFromHtml(html, 4, "");
		String ClanSkillData = opera.getGridFormatFromHtml(html, 5, "");
		String CharReputationData = opera.getGridFormatFromHtml(html, 6, "");
		Map<Integer, String>ID_IMA_ICON = new HashMap<Integer, String>();
		String _ByPass = "bypass -h ZeuS donation "+ Command +"_ASK %DATA_NUM% %CANTIDAD%";
		String TituloSeccion = "";
		String TituloShowGrilla = "";
		String NombreDonation = central.getNombreITEMbyID(general.DONA_ID_ITEM);
		
		boolean RepiteImagen = true;
		String ImagenRepite = "";
		switch(Command){
			case "CHAR_LEVEL":
				ImagenRepite =  CharLevelData.split(";")[0].split(",")[1];
				TituloShowGrilla = CharLevelData.split(";")[1].split(",")[1];
				TituloSeccion = CharLevelData.split(";")[2].split(",")[1];
				break;
			case "CHAR_FAME":
				ImagenRepite =  CharFameData.split(";")[0].split(",")[1];
				TituloShowGrilla = CharFameData.split(";")[1].split(",")[1];
				TituloSeccion = CharFameData.split(";")[2].split(",")[1];
				break;
			case "CHAR_PK":
				ImagenRepite =  CharPKData.split(";")[0].split(",")[1];
				TituloShowGrilla = CharPKData.split(";")[1].split(",")[1];
				TituloSeccion = CharPKData.split(";")[2].split(",")[1];
				break;
			case "CLAN_LEVEL":
				ImagenRepite =  "";
				RepiteImagen = false;
				ID_IMA_ICON.put(1, ClanLvLData.split(";")[0].split(",")[1]);
				ID_IMA_ICON.put(2, ClanLvLData.split(";")[1].split(",")[1]);
				ID_IMA_ICON.put(3, ClanLvLData.split(";")[2].split(",")[1]);
				ID_IMA_ICON.put(4, ClanLvLData.split(";")[3].split(",")[1]);
				ID_IMA_ICON.put(5, ClanLvLData.split(";")[4].split(",")[1]);
				ID_IMA_ICON.put(6, ClanLvLData.split(";")[5].split(",")[1]);
				ID_IMA_ICON.put(7, ClanLvLData.split(";")[6].split(",")[1]);
				ID_IMA_ICON.put(8, ClanLvLData.split(";")[7].split(",")[1]);
				ID_IMA_ICON.put(9, ClanLvLData.split(";")[8].split(",")[1]);
				ID_IMA_ICON.put(10, ClanLvLData.split(";")[9].split(",")[1]);
				ID_IMA_ICON.put(11, ClanLvLData.split(";")[10].split(",")[1]);
				TituloShowGrilla = ClanLvLData.split(";")[11].split(",")[1];
				TituloSeccion = ClanLvLData.split(";")[12].split(",")[1];
				break;
			case "CLAN_SKILL":
				ImagenRepite =  "";
				RepiteImagen = false;
				ID_IMA_ICON.put(1, ClanSkillData.split(";")[0].split(",")[1]);
				ID_IMA_ICON.put(2, ClanSkillData.split(";")[1].split(",")[1]);
				ID_IMA_ICON.put(3, ClanSkillData.split(";")[2].split(",")[1]);
				ID_IMA_ICON.put(4, ClanSkillData.split(";")[3].split(",")[1]);
				ID_IMA_ICON.put(5, ClanSkillData.split(";")[4].split(",")[1]);
				ID_IMA_ICON.put(6, ClanSkillData.split(";")[5].split(",")[1]);
				ID_IMA_ICON.put(7, ClanSkillData.split(";")[6].split(",")[1]);
				ID_IMA_ICON.put(8, ClanSkillData.split(";")[7].split(",")[1]);
				ID_IMA_ICON.put(9, ClanSkillData.split(";")[8].split(",")[1]);
				ID_IMA_ICON.put(10, ClanSkillData.split(";")[9].split(",")[1]);
				ID_IMA_ICON.put(11, ClanSkillData.split(";")[10].split(",")[1]);
				TituloShowGrilla = ClanSkillData.split(";")[11].split(",")[1];
				TituloSeccion = ClanSkillData.split(";")[12].split(",")[1];
				break;
			case "CLAN_REPUTATION":
				TituloShowGrilla = CharReputationData.split(";")[1].split(",")[1];
				TituloSeccion = CharReputationData.split(";")[2].split(",")[1];
				ImagenRepite = CharReputationData.split(";")[0].split(",")[1];
				break;
		}
		html.replace("%WINDOWS_TITLE%", TituloSeccion);
		String GridFormat = opera.getGridFormatFromHtml(html, 7, "%GRID_DATA%").replace("%BYPASS_DOIT%", _ByPass);
		
		String DataGrid = ""; 
		
		Iterator itr = Op.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int Valor = (int) Entrada.getKey();
			int CantidadDona = (int) Entrada.getValue();
			//String ByPass = "bypass -h ZeuS donation "+ Command +" %DATA_NUM% %CANTIDAD%";
			DataGrid += GridFormat.replace("%DONATION_ITEM_NAME%", NombreDonation) .replace("%GRID_TITLE%", TituloShowGrilla) .replace("%IDIMG%", ( RepiteImagen ? ImagenRepite : ID_IMA_ICON.get(Valor) )).replace("%CANTIDAD%", String.valueOf(CantidadDona)).replace("%DATA_NUM%", String.valueOf(Valor));
		}
		html.replace("%GRID_DATA%", DataGrid);
		central.sendHtml(player, html.getHtml());
	}
	
	public static String bypass(L2PcInstance player, String params){
		//general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;255_R;0;0;0;0;0
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		if(parm1.equals("0")){
			return mainHtml(player,"",0);
		}else if(parm1.equals("SEND_NOTY_WINDOWS")){
			sendNotificacionDonacion(player);
			return "";
		}else if(parm1.equals("CHANGE_CHAR_NAME") || parm1.equals("CHANGE_CLAN_NAME")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			sendHtmRequest(player, parm1);
			return "";
		}else if(parm1.equals("255_R_ASK")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DONATION_YOU_GONNA_BUY_255_RECOMMENDS, IdDialog.MAKE_DONATION_RECOS);
			return"";
		}else if(parm1.equals("255_R")){
			if(player.getRecomHave()<255){
				if(opera.haveDonationItem(player, general.DONATION_255_RECOMMENDS)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_255_RECOMMENDS, player);
					player.setRecomHave( 255 - player.getRecomHave());
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					player.storeRecommendations();
					cbFormato.cerrarCB(player);
				}
			}else{
				central.msgbox("You have full recos", player);
			}
		}else if(parm1.equals("NOBLE_ASK")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			if(general.DONATION_NOBLE_COST==0){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";					
			}
			if(player.isNoble()){
				central.msgbox(language.getInstance().getMsg(player).YOU_ARE_ALREADY_NOBLE, player);
				return "";
			}
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DONATION_YOU_WANT_TO_BE_NOBLE, IdDialog.MAKE_DONATION_NOBLE_CHAR);
		}else if(parm1.equals("NOBLE")){
			if(opera.haveDonationItem(player, general.DONATION_NOBLE_COST)){
				opera.removeItem(general.DONA_ID_ITEM, general.DONATION_NOBLE_COST, player);
				opera.setNoble(player);
				player.broadcastUserInfo();
				player.broadcastStatusUpdate();				
				cbFormato.cerrarCB(player);
			}else{
				return "";
			}
		}else if(parm1.equals("SEX_ASK")){
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DONATION_YOU_wANT_TO_CHANGE_YOUR_SEX, IdDialog.MAKE_DONATION_CHANGE_SEX);
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			return "";
		}else if(parm1.equals("SEX")){
			if(general.DONATION_CHANGE_SEX_COST==0){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";
			}
			if(opera.haveDonationItem(player, general.DONATION_CHANGE_SEX_COST)){
				opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_SEX_COST, player);
				opera.changeSex(player);
				player.broadcastUserInfo();
				player.broadcastStatusUpdate();
				cbFormato.cerrarCB(player);
			}
			return "";
		}else if(parm1.equals("AIO_NORMAL_ASK")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			if(general.DONATION_AIO_CHAR_SIMPLE_COSTO==0){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";	
			}
			
			if(general.DONATION_AIO_CHAR_LV_REQUEST>player.getLevel()){
				central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.DONATION_AIO_CHAR_LV_REQUEST)), player);
				return "";					
			}
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DONATION_YOU_GONNA_TRANSFORM_THIS_CHAR_INTO_A_AIO_BUFFER.replace("$type","Normal"), IdDialog.MAKE_DONATION_AIO_CHAR_SIMPLE);
			return "";
		}else if(parm1.equals("AIO_NORMAL")){
			if(opera.haveDonationItem(player, general.DONATION_AIO_CHAR_SIMPLE_COSTO)){
				if(aioChar.setNewAIO(player, true)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_AIO_CHAR_SIMPLE_COSTO, player);
					cbFormato.cerrarCB(player);
				}
			}
			return "";
		}else if(parm1.equals("AIO_30_ASK")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			if(general.DONATION_AIO_CHAR_30_COSTO ==0){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";	
			}
			
			if(general.DONATION_AIO_CHAR_LV_REQUEST>player.getLevel()){
				central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.DONATION_AIO_CHAR_LV_REQUEST)), player);
				return "";					
			}
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DONATION_YOU_GONNA_TRANSFORM_THIS_CHAR_INTO_A_AIO_BUFFER.replace("$type"," with +15/+30 Skill"), IdDialog.MAKE_DONATION_AIO_CHAR_30);
			return "";
		}else if(parm1.equals("AIO_30")){
			if(opera.haveDonationItem(player, general.DONATION_AIO_CHAR_30_COSTO)){
				if(aioChar.setNewAIO(player, false)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_AIO_CHAR_30_COSTO, player);
					cbFormato.cerrarCB(player);
				}
			}
			return "";
		}else if(parm1.equals("CHAR_LEVEL")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_LEVEL);
			return "";
		}else if(parm1.equals("CHAR_FAME")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_FAME_POINT);
			return "";
		}else if(parm1.equals("CHAR_PK")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_PK_POINT);
			return "";
		}else if(parm1.equals("CLAN_LEVEL")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_LEVEL);
			return "";
		}else if(parm1.equals("CLAN_SKILL")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_SKILL);
			return "";
		}else if(parm1.equals("CLAN_REPUTATION")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_REPUTATION);
			return "";
		}else if(parm1.equals("ENCHANT_ITEM")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			getArmorEnchantWindows(player);
			return "";
		}else if(parm1.equals("ELEMENTAL_ITEM")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			getArmorElementalWindows(player);
			return "";
		}else if(parm1.equals("PREMIUM")){
			cbManager.separateAndSend(mainHtml(player,"",0), player);
			getPremiumShowAllWindows(player);
			return "";
		}else if(parm1.equals("PREMIUM_SHOW_ITEM_CHANCE")){
			//cbManager.separateAndSend(mainHtml(player,"",0), player);
			int IdPremium = Integer.valueOf(parm2);
			general.getPremiumServices().get(IdPremium).showWindowsPremiumChance(player);
			return "";
		}else if(parm1.equals("PREMIUM_SHOW_ITEM_RATE")){
			//cbManager.separateAndSend(mainHtml(player,"",0), player);
			int IdPremium = Integer.valueOf(parm2);
			general.getPremiumServices().get(IdPremium).showWindowsPremiumRate(player);
			return "";			
		}else if(parm1.equals("PREMIUM_SEE")){
			getPremiumShowInfoWindows(player,Integer.valueOf(parm2),false);
			return "";
		}else if(parm1.equals("PREMIUM_SEE_GM")){
			getPremiumShowInfoWindows(player,Integer.valueOf(parm2),true);
			return "";			
		}else if(parm1.equals("PREMIUM_BUY")){
			int idSelectedPremium = Integer.valueOf(parm2);
			setPremium(idSelectedPremium,player);
		}
		return mainHtml(player,"",0);
	}
	
	private static String setPremium(int idSelectedPremium, L2PcInstance player){
		return setPremium(idSelectedPremium, player, true, null, false);
	}
	
	public static String setPremium(int idSelectedPremium, L2PcInstance player, boolean isBuy, L2PcInstance GMChar, boolean isMassive){
		if(opera.isPremium_Player(player)){
			premiumPersonalData PA = general.getPremiumDataFromPlayerOrClan(player.getAccountName());
			premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
			if(PA.isActive()){
				if(idSelectedPremium != PA.getIdPremiumUse()){
					if(isBuy){
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT, player);
					}else{
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT, player);
						if(!isMassive){
							central.msgbox("The selected player " + player.getName() + " has Another Premium Account.", GMChar);
						}
					}
					return "";
				}
			}
			if(PA_OrinalData==null){
				if(isBuy){
					central.msgbox(language.getInstance().getMsg(player).DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW, player);
				}else{
					central.msgbox(language.getInstance().getMsg(player).DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW, GMChar);
				}
				return "";
			}
			int DC = PA_OrinalData.getCost();
			if(isBuy){
				if(!opera.haveDonationItem(player, DC)){
					return "";
				}
			}
			if(savePremiumOnBd(player, idSelectedPremium)){
				if(isBuy){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
					cbFormato.cerrarCB(player);
				}
				central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_HAS_MORE_DAYS.replace("$type",PA_OrinalData.getAplicableA()).replace("$name",PA_OrinalData.getName()) , player);
				charPanel.getCharInfo(player);
			}else{
				return "";
			}
		}else if(opera.isPremium_Clan(player)){
			premiumPersonalData PA = general.getPremiumDataFromPlayerOrClan( String.valueOf(player.getClanId()) );
			premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
			if(PA.isActive()){
				if(idSelectedPremium != PA.getIdPremiumUse()){
					if(isBuy){
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT, player);
					}else{
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT, player);
					}
					return "";
				}
			}
			if(PA_OrinalData==null){
				central.msgbox(language.getInstance().getMsg(player).DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW, player);
				return "";
			}
			int DC = PA_OrinalData.getCost();
			if(!opera.haveDonationItem(player, DC)){
				return "";
			}
			if(savePremiumOnBd(player, idSelectedPremium)){
				if(isBuy){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
					cbFormato.cerrarCB(player);
				}
				central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_HAS_MORE_DAYS.replace("$type",PA_OrinalData.getAplicableA()).replace("$name",PA_OrinalData.getName()) , player);
				if(GMChar!=null){
					if(!isMassive){
						central.msgbox("Premium System for Target (" + PA_OrinalData.getAplicableA() + " - " + PA_OrinalData.getName() + ") has More Days." , GMChar);
					}
				}
				charPanel.getCharInfo(player);
			}else{
				return "";
			}				
		}else{
			premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
			int DC = PA_OrinalData.getCost();
			if(isBuy){
				if(!opera.haveDonationItem(player, DC)){
					return "";
				}
			}
			if(savePremiumOnBd(player, idSelectedPremium)){
				if(isBuy){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
				}
				central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_ITS_NOW_ON.replace("$type",PA_OrinalData.getAplicableA()).replace("$name",PA_OrinalData.getName()) , player);
				if(GMChar!=null && !isMassive){
					central.msgbox("Selected player now has Premium Services ("+ PA_OrinalData.getAplicableA() + " - " + PA_OrinalData.getName() +")", GMChar);
				}
				charPanel.getCharInfo(player);
				cbFormato.cerrarCB(player);
				return "";
			}
		}
		return "";
	}
	
	@SuppressWarnings("unused")
	private static void setElement(L2PcInstance player, byte type, int value, int armorType)
	{
		// get the target
		
		L2ItemInstance itemInstance = null;
		
		// only attempt to enchant if there is a weapon equipped
		L2ItemInstance parmorInstance = player.getInventory().getPaperdollItem(armorType);
		if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == armorType))
		{
			itemInstance = parmorInstance;
		}
		
		if (itemInstance != null)
		{
			String old, current;
			Elementals element = itemInstance.getElemental(type);
			if (element == null)
			{
				old = "None";
			}
			else
			{
				old = element.toString();
			}
			
			// set enchant value
			player.getInventory().unEquipItemInSlot(armorType);
			if (type == -1)
			{
				itemInstance.clearElementAttr(type);
			}
			else
			{
				itemInstance.setElementAttr(type, value);
			}
			player.getInventory().equipItem(itemInstance);
			
			if (itemInstance.getElementals() == null)
			{
				current = "None";
			}
			else
			{
				current = itemInstance.getElemental(type).toString();
			}
			
			// send packets
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(itemInstance);
			player.sendPacket(iu);
		}
	}
	
	
	private static int getDCFromFastMap (Map<Integer, Integer> DC, int val, L2PcInstance player){
		if(DC!=null){
			if(DC.containsKey(val)){
				if(DC.get(val)>0){
					return DC.get(val);
				}else{
					central.msgbox("Disable Option.", player);
				}
			}
		}
		central.msgbox(language.getInstance().getMsg(player).TRY_AGAIN_MAYBE_THE_ADMINGM_CHANGE_SOMETHING, player);
		return -1;
	}
	
	private static Map<Integer, String>TemporalData = new HashMap<Integer, String>();
	
	public static void SetTemporalData(L2PcInstance player, String Data){
		try{
			TemporalData.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		TemporalData.put(player.getObjectId(), Data);
	}
	public static String getTemporalData(L2PcInstance player){
		return TemporalData.get(player.getObjectId());
	}
	
	public static void ByPassFromDlg(L2PcInstance player){
		_donainfo tmp = ByPassSimple.get(player.getObjectId());
		String DataToSend = tmp.getCommand().replace("_ASK", "") + " " + tmp.getQuantity() + " " + tmp.getDataNum();
		bypass_voice(player, DataToSend);
	}
	
	public static void bypass_voice(L2PcInstance player, String params){
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning(params);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox(params, player);
		}
		
			if(params.split(" ")[0].equals("En_E_Armor")){
				int Parte = Integer.valueOf(params.split(" ")[1]);
				int IdObjeto = Integer.valueOf(params.split(" ")[2]);
				byte element = Elementals.getElementId(params.split(" ")[3]);
				int EnchantPower = Integer.valueOf(params.split(" ")[4]);
				
				int dc = getDCFromFastMap(general.DONATION_ELEMENTAL_ITEM_ARMOR ,EnchantPower,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Parte);
					
					String I_Name = It.getName();
					
					if(It.isEnchantable()==0){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;
					}else if(It.getObjectId()!=IdObjeto){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;						
					}
					
					int Att = It.getElementDefAttr(element);
					
					if(EnchantPower <= Att){
						central.msgbox("Worg Elemental Power", player);
						doIt = false;
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						_donaInfoItemEnchant tempD = new _donaInfoItemEnchant(Parte, IdObjeto, EnchantPower, element, dc);
						ByPassEnchantItem.put(player.getObjectId(), tempD);
						String Question = language.getInstance().getMsg(player).DONATION_ASK_ENCHANT_YOU_WANT_TO_ELEMENTAL_THE_ITEM_$item_TO_$enchant_$elemental_$cost.replace("$item", It.getName()).replace("$elemental", params.split(" ")[3].trim().toUpperCase()).replace("$enchant", String.valueOf(EnchantPower)).replace("$cost", String.valueOf(dc) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM));
						Dlg.sendDlg(player, Question, IdDialog.MAKE_DONATION_ELEMENTAL_ITEM,30);
					}
					getArmorElementalWindows(player);
				}
			}else if(params.split(" ")[0].equals("En_E_Weapon")){
				int Parte = Integer.valueOf(params.split(" ")[1]);
				int IdObjeto = Integer.valueOf(params.split(" ")[2]);
				byte element = Elementals.getElementId(params.split(" ")[3]);
				int EnchantPower = Integer.valueOf(params.split(" ")[4]);
				
				int dc = getDCFromFastMap(general.DONATION_ELEMENTAL_ITEM_WEAPON ,EnchantPower,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Parte);
					
					String I_Name = It.getName();
					
					if(It.isEnchantable()==0){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;
					}else if(It.getObjectId()!=IdObjeto){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;						
					}

					int Att = It.getAttackElementPower();
					
					if(EnchantPower <= Att){
						central.msgbox("Worg Elemental Power", player);
						doIt = false;
					}
					
					
					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
					}
					
					if(doIt){
						_donaInfoItemEnchant tempD = new _donaInfoItemEnchant(Parte, IdObjeto, EnchantPower, element, dc);
						ByPassEnchantItem.put(player.getObjectId(), tempD);
						String Question = language.getInstance().getMsg(player).DONATION_ASK_ENCHANT_YOU_WANT_TO_ELEMENTAL_THE_ITEM_$item_TO_$enchant_$elemental_$cost.replace("$item", It.getName()).replace("$elemental", params.split(" ")[3].trim().toUpperCase()).replace("$enchant", String.valueOf(EnchantPower)).replace("$cost", String.valueOf(dc) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM));
						Dlg.sendDlg(player, Question, IdDialog.MAKE_DONATION_ELEMENTAL_ITEM,30);
					}
					getArmorElementalWindows(player);
				}				
			}else if(params.split(" ")[0].equals("EnWeapon")){
				int Slot = Integer.valueOf(params.split(" ")[1]);
				int idObj = Integer.valueOf(params.split(" ")[2]);
				int Enchant = Integer.valueOf(params.split(" ")[3]);
				int dc = getDCFromFastMap(general.DONATION_ENCHANT_ITEM_WEAPON,Enchant,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Slot);
					
					if(It.isEnchantable()==0){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;
					}else if(It.getEnchantLevel() == Enchant){
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_HAVE_THE_SAME_ENCHANT_ON_YOUR_ITEM, player);
						doIt=false;
					}else if(It.getObjectId()!=idObj){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;						
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						_donaInfoItemEnchant tempDataItem = new _donaInfoItemEnchant(Slot, idObj, Enchant, dc);
						ByPassEnchantItem.put(player.getObjectId(), tempDataItem);
						String Question = language.getInstance().getMsg(player).DONATION_ASK_ENCHANT_YOU_WANT_TO_ENCHANT_THE_ITEM_$item_TO_$enchant_$cost.replace("$item", It.getItemName()).replace("$enchant", String.valueOf(Enchant)).replace("$cost", central.getNombreITEMbyID(general.DONA_ID_ITEM));
						Dlg.sendDlg(player, Question, IdDialog.MAKE_DONATION_ENCHANT_ITEM, 30);						
					}
					getArmorEnchantWindows(player);
				}
				
			}else if(params.split(" ")[0].equals("EnArmor")){
				int Slot = Integer.valueOf(params.split(" ")[1]);
				int idObj = Integer.valueOf(params.split(" ")[2]);
				int Enchant = Integer.valueOf(params.split(" ")[3]);
				int dc = getDCFromFastMap(general.DONATION_ENCHANT_ITEM_ARMOR,Enchant,player);
				if(dc>0){
					
					L2ItemInstance It = player.getInventory().getPaperdollItem(Slot);
					boolean doIt = true;
					if(It.isEnchantable()==0){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;
					}else if(It.getEnchantLevel() == Enchant){
						central.msgbox(language.getInstance().getMsg(player).DONATION_YOU_HAVE_THE_SAME_ENCHANT_ON_YOUR_ITEM, player);
						doIt=false;
					}else if(It.getObjectId()!=idObj){
						central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
						doIt=false;						
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						_donaInfoItemEnchant tempDataItem = new _donaInfoItemEnchant(Slot, idObj, Enchant, dc);
						ByPassEnchantItem.put(player.getObjectId(), tempDataItem);
						String Question = language.getInstance().getMsg(player).DONATION_ASK_ENCHANT_YOU_WANT_TO_ENCHANT_THE_ITEM_$item_TO_$enchant_$cost.replace("$item", It.getItemName()).replace("$enchant", String.valueOf(Enchant)).replace("$cost", String.valueOf(dc) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM));
						Dlg.sendDlg(player, Question, IdDialog.MAKE_DONATION_ENCHANT_ITEM, 30);
					}
					getArmorEnchantWindows(player);
				}
			}else if(params.split(" ")[0].equals("CHANGE_CHAR_NAME")){
				if(general.DONATION_CHANGE_CHAR_NAME_COST==0){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return;				
				}
				if(!opera.isValidName(params.split(" ")[1])){
					central.msgbox(language.getInstance().getMsg(player).THE_NAME_ENTERED_IS_NOT_VALID,player);
					return;
				}
				
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CHAR_NAME_COST)){
					return;
				}
				
				if(cambionombre.changeName_Char(player, params.split(" ")[1])){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CHAR_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}				
			}else if(params.split(" ")[0].equals("CHANGE_CLAN_NAME")){
				if(general.DONATION_CHANGE_CLAN_NAME_COST==0){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return;	
				}
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CLAN_NAME_COST)){
					return;
				}
				if(!opera.isValidName(params.split(" ")[1])){
					central.msgbox(language.getInstance().getMsg(player).THE_NAME_ENTERED_IS_NOT_VALID,player);
					return;
				}				
				
				if(clanNomCambio.changeNameClan(params.split(" ")[1], player)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CLAN_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}
			}else if(params.split(" ")[0].equals("CHAR_LEVEL_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CHAR_LEVEL.replace("$level", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);				
			}else if(params.split(" ")[0].equals("CHAR_LEVEL")){
				int LevelRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CHARACTERS_LEVEL,LevelRequest,player);
				if(dc>0){
					if(player.getLevel()>=LevelRequest){
						central.msgbox("Wrog Level. Please Check", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					if(LevelRequest!=85){
						opera.setLevel(player,LevelRequest);
					}else{
						opera.set85(player);
					}
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_NEW_LEVEL_IS.replace("$level", String.valueOf(LevelRequest)), player);
				}
			}else if(params.split(" ")[0].equals("CHAR_FAME_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CHAR_FAME.replace("$fame", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);
			}else if(params.split(" ")[0].equals("CHAR_FAME")){
				int FameRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CHARACTERS_FAME_POINT,FameRequest,player);
				if(dc>0){
					if(player.getFame() == Config.MAX_PERSONAL_FAME_POINTS || ( (player.getFame() + FameRequest) > Config.MAX_PERSONAL_FAME_POINTS )){
						central.msgbox("Wrog Fame. Please Check", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM,dc,player);
					player.setFame( player.getFame() + FameRequest );
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_ADQUIERE_FAME.replace("$fame", String.valueOf(FameRequest)), player);
				}
			}else if(params.split(" ")[0].equals("CHAR_PK_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CHAR_REDUCE_PK.replace("$pk", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);				
			}else if(params.split(" ")[0].equals("CHAR_PK")){
				int pkReduceRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CHARACTERS_PK_POINT,pkReduceRequest,player);
				if(dc>0){
					if(player.getPkKills()==0 || ( (player.getPkKills() - pkReduceRequest)< 0 )){
						central.msgbox("Wrog Pk Reduce Quantity", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM,dc,player);
					player.setPkKills( player.getPkKills() - pkReduceRequest );
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_PK_HAS_REDUCE_TO.replace("$pk", String.valueOf(player.getPkKills()) ), player);
				}
			}else if(params.split(" ")[0].equals("CLAN_LEVEL_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CLAN_LEVEL.replace("$level", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);				
			}else if(params.split(" ")[0].equals("CLAN_LEVEL")){
				int clanLvl = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CLAN_LEVEL,clanLvl,player);
				if(dc>0){
					if(!player.isClanLeader()){
						central.msgbox(language.getInstance().getMsg(player).DONATION_ONLY_CLAN_LEADER_CAN_LVL_UP, player);
						return;
					}
					if(player.getClan().getLevel() >= clanLvl){
						central.msgbox("Wrog Clan Lv.", player);
						return;						
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					player.getClan().changeLevel(clanLvl);
					player.getClan().broadcastClanStatus();
					player.getClan().broadcastToOnlineMembers(null);
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_CLAN_LVL_HAS_INCREASE.replace("$level", String.valueOf(clanLvl)), player);
				}
			}else if(params.split(" ")[0].equals("CLAN_REPUTATION_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CLAN_REPUTATION.replace("$reputation", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);				
			}else if(params.split(" ")[0].equals("CLAN_REPUTATION")){
				int clanReput = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CLAN_REPUTATION,clanReput,player);
				if(dc>0){
					if(player.getClan()==null){
						central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_HAVE_A_CLAN, player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					player.getClan().addReputationScore(clanReput, true);
					player.getClan().broadcastClanStatus();
					player.getClan().broadcastToOnlineMembers(null);
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_CLAN_REPUTATION_HAS_INCREASE.replace("$repu", String.valueOf(clanReput)), player);
				}
			}else if(params.split(" ")[0].equals("CLAN_SKILL_ASK")){
				_donainfo temp = new _donainfo(params.split(" ")[2],params.split(" ")[1],params.split(" ")[0]);
				ByPassSimple.put(player.getObjectId(),temp);
				String msgbox = language.getInstance().getMsg(player).DONATION_ASK_CLAN_SKILL.replace("$clanlevel", params.split(" ")[1]).replace("$Amount", params.split(" ")[2]).replace("$DonationItemName",central.getNombreITEMbyID(general.DONA_ID_ITEM));
				Dlg.sendDlg(player, msgbox, IdDialog.ENGINE_DONATION_PART_1);				
			}else if(params.split(" ")[0].equals("CLAN_SKILL")){
				int lvlClanRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromFastMap(general.DONATION_CLAN_SKILL,lvlClanRequest,player);
				if(dc>0){
					if(player.getClan()==null){
						central.msgbox(language.getInstance().getMsg(player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION, player);
						return;
					}
					if(!player.isClanLeader()){
						central.msgbox(language.getInstance().getMsg(player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION, player);
						return;						
					}
					if(player.getClan().getLevel() < lvlClanRequest){
						central.msgbox("Wrog input Data", player);
						return;
					}
					
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					
					final L2Clan clan = player.getClan();
					final Map<Integer, L2SkillLearn> _skills = SkillTreesData.getInstance().getMaxPledgeSkills(clan, true, lvlClanRequest);
					int Contador = 0;

					for (L2SkillLearn s : _skills.values())
					{
						clan.addNewSkill(SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()));
						Contador++;							
					}
					
					// Notify target and active char
					clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
					for (L2PcInstance member : clan.getOnlineMembers(0))
					{
						member.sendSkillList();
					}
					//clan.store();
					//activeChar.sendMessage("You gave " + skills.size() + " skills to " + player.getName() + "'s clan " + clan.getName() + ".");
					central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_RECEIVED_CLAN_SKILLS.replace("$quantity",String.valueOf(Contador)), player);					
				}
			}
	}
	public static void dlgEnchantItem(L2PcInstance player) {
		_donaInfoItemEnchant tempD = ByPassEnchantItem.get(player.getObjectId());
		int dc = tempD.getCost();
		if(!opera.haveDonationItem(player, dc)){
			return;
		}
		L2ItemInstance It = player.getInventory().getPaperdollItem(tempD.getIdLocation());
		if(It.getObjectId() != tempD.getIdObject()) {
			central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
			return;
		}
		opera.removeItem(general.DONA_ID_ITEM, dc, player);
		It.setEnchantLevel( tempD.getEnchant() );
		player.broadcastUserInfo();
		player.getInventory().reloadEquippedItems();
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(It);
		player.sendPacket(iu);
		String I_Name = It.getName();//central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Slot).getItem().getId());
		central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ENCHANT.replace("$item", I_Name).replace("$value",String.valueOf(tempD.getEnchant())),player);
		getArmorEnchantWindows(player);
	}
	public static void dlgElementalItem(L2PcInstance player) {
		_donaInfoItemEnchant tempD = ByPassEnchantItem.get(player.getObjectId());
		int dc = tempD.getCost();
		if(!opera.haveDonationItem(player, dc)){
			return;
		}
		L2ItemInstance It = player.getInventory().getPaperdollItem(tempD.getIdLocation());
		if(It.getObjectId() != tempD.getIdObject()) {
			central.msgbox(language.getInstance().getMsg(player).DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE, player);
			return;
		}
		opera.removeItem(general.DONA_ID_ITEM, dc, player);	
		setElement(player, tempD.getElement(), tempD.getEnchant(), tempD.getIdLocation());
		central.msgbox(language.getInstance().getMsg(player).DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ELEMENTAL_POWER.replace("$value", String.valueOf(tempD.getEnchant())).replace("$item", It.getItemName()).replace("$element", Elementals.getElementName( tempD.getElement())), player);
		getArmorElementalWindows(player);
	}
}
class _donaInfoItemEnchant{
	private final int ID_LOCATION;
	private final int ID_OBJECT;
	private final int ENCHANT;
	private final byte ELEMENT;
	private final int COST;
	public _donaInfoItemEnchant(int _idLocation, int _idObject, int _enchant, int _cost) {
		this.ID_LOCATION = _idLocation;
		this.ID_OBJECT = _idObject;
		this.ENCHANT = _enchant;
		this.ELEMENT = 0;
		this.COST = _cost;
	}
	public _donaInfoItemEnchant(int _idLocation, int _idObject, int _enchant, byte _element, int _cost) {
		this.ID_LOCATION = _idLocation;
		this.ID_OBJECT = _idObject;
		this.ENCHANT = _enchant;
		this.ELEMENT = _element;
		this.COST = _cost;
	}
	
	public final int getCost() {
		return this.COST;
	}
	
	public final int getIdLocation() {
		return this.ID_LOCATION;
	}
	public final int getIdObject() {
		return this.ID_OBJECT;
	}
	public final int getEnchant() {
		return this.ENCHANT;
	}
	public final byte getElement() {
		return this.ELEMENT;
	}
	
}
class _donainfo{
	private String DataNum;
	private String Quantity;
	private String Command;
	public _donainfo(String _DataNum, String _Quantity, String _Command){
		this.DataNum = _DataNum;
		this.Quantity = _Quantity;
		this.Command = _Command;
	}
	
	public final String getDataNum(){
		return this.DataNum;
	}
	public final String getQuantity(){
		return this.Quantity;
	}
	public final String  getCommand(){
		return this.Command;
	}
}