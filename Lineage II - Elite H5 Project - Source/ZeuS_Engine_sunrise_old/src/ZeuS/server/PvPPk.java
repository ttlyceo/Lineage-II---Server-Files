package ZeuS.server;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.AbnormalEffect;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.pintar;
import ZeuS.procedimientos.opera;

public class PvPPk{

	private static final String SELECT_TOP_PKPVP = "call sp_top_pvppk(?,?,?)";
	private static final String INSERT_LOG_FIGHT = "call sp_log_fight(?,?,?)";
	@SuppressWarnings("unused")
	private static Map<Integer, Boolean> CAN_ATTACK = new HashMap<Integer, Boolean>();
	private static final Logger _log = Logger.getLogger(PvPPk.class.getName());

	public static boolean _canScape(L2PcInstance activeChar,L2ItemInstance item){
		if(!general._activated()){
			return true;
		}
		if(activeChar != null){
			if (!general.ALLOW_BLESSED_ESCAPE_PVP && !activeChar.isGM())
			{
				// Players can't use BSOE while flagged.
				if (activeChar.getPvpFlag() != 0 && isBSOE(item)){
					activeChar.sendMessage("Can not Use BSOE while you Flag");
					return false;
				}
			}
			if(!general.CAN_USE_BSOE_PK && !activeChar.isGM()){
				if(activeChar.getKarma()>0 && isBSOE(item)){
					activeChar.sendMessage("Can not Use BSOE while you have Karma");
					return false;					
				}
			}
		}
		return true;
	}
	
	public static boolean isBSOE(L2ItemInstance item){
		if ((opera.getIdItem(item) == 1538) || // Blessed Scroll of Escape
		(opera.getIdItem(item) == 3958) || // Blessed Scroll of Escape (Event)
		(opera.getIdItem(item) == 5858) || // Blessed Scroll of Escape: Clan Hall
		(opera.getIdItem(item) == 5859) || // Blessed Scroll of Escape: Castle
		(opera.getIdItem(item) == 9156) || // Blessed Scroll of Escape (Event)
		(opera.getIdItem(item) == 10130) || // Blessed Scroll of Escape: Fortress
		(opera.getIdItem(item) == 13258) || // Gran Kain's Blessed Scroll of Escape
		(opera.getIdItem(item) == 13731) || // Blessed Scroll of Escape: Gludio
		(opera.getIdItem(item) == 13732) || // Blessed Scroll of Escape: Dion
		(opera.getIdItem(item) == 13733) || // Blessed Scroll of Escape: Giran
		(opera.getIdItem(item) == 13734) || // Blessed Scroll of Escape: Oren
		(opera.getIdItem(item) == 13735) || // Blessed Scroll of Escape: Aden
		(opera.getIdItem(item) == 13736) || // Blessed Scroll of Escape: Innadril
		(opera.getIdItem(item) == 13737) || // Blessed Scroll of Escape: Goddard
		(opera.getIdItem(item) == 13738) || // Blessed Scroll of Escape: Rune
		(opera.getIdItem(item) == 13739) || // Blessed Scroll of Escape: Schuttgart
		(opera.getIdItem(item) == 20583) || // Blessed Scroll of Escape (event)
		(opera.getIdItem(item) == 21195)){
			return true;
		} // Blessed Scroll of Escape
		return false;
	}

	private static int[] getInfoPvPBD(L2PcInstance player){
		Connection con= null;
		CallableStatement statement = null;
		ResultSet rs = null;
		int Respuesta=0;
		int NumeroPosi=0;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareCall(SELECT_TOP_PKPVP);
			statement.setInt(1, 1);
			statement.setString(2, player.getName());
			statement.setInt(3, 5);
			rs = statement.executeQuery();
			while (rs.next())
			{
				Respuesta = rs.getInt("resp");
				NumeroPosi = rs.getInt("posi");
			}
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("Problemas Carga Top PK PVP" + e.getMessage());
		}
		finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		int[]Retorno = {Respuesta,NumeroPosi};
		return Retorno;
	}

	public static void Ver_TopPVP_PK(L2PcInstance player){
		Ver_TopPVP_PK(player, true);
	}

	public static void Ver_TopPVP_PK(L2PcInstance player, boolean Anunciar){
		if(!general._activated()){
			return;
		}
		if(!general.PVP_PK_GRAFICAL_EFFECT && !general.ANNOUCE_TOP_PPVPPK_ENTER){
			return;
		}
		int[]Obtener = getInfoPvPBD(player);

		if(general.PVP_PK_GRAFICAL_EFFECT && opera.TOP_PVP_PK_EFFECT(player)){
			_getGraficEffect(player, Obtener[0], Obtener[1]);
		}
		if(Anunciar){
			if(general.ANNOUCE_TOP_PPVPPK_ENTER && (Obtener[1]==1) && opera.TOP_PVP_PK_ANNOU(player) ){
				_AnunciarEntradaTOP_PVP_PK(player.getName());
			}
		}

	}

	private static void _getGraficEffect(L2PcInstance player, int Respuesta, int NumeroPosi){
		if(!general._activated()){
			return;
		}
		if (!general.PVP_PK_GRAFICAL_EFFECT || player.isGM() || !opera.TOP_PVP_PK_EFFECT(player) ) {
			return;
		}

		if (Respuesta == 1)
		{
			if (NumeroPosi == 1)
			{
				player.startSpecialEffect(AbnormalEffect.ARCANE_SHIEL_STIGMA_SHILIEN.getMask());
			}
			if (NumeroPosi == 2)
			{
				player.startSpecialEffect(AbnormalEffect.S_STIGMA_SHILIEN.getMask());
			}
			if (NumeroPosi == 3)
			{
				player.startAbnormalEffect(AbnormalEffect.VITALITY.getMask());
			}
			if (NumeroPosi == 4)
			{
				player.startAbnormalEffect(AbnormalEffect.WINDREDCIRCLE.getMask());
			}
			if (NumeroPosi == 5)
			{
				player.startAbnormalEffect(AbnormalEffect.REDCIRCLELLAMA.getMask());
			}
		}

	}

	private static void _AnunciarEntradaTOP_PVP_PK(String NombrePlayer){
		if(!general._activated()){
			return;
		}
		String Retorno = general.MENSAJE_ENTRADA_PJ_TOPPVPPK.replace("%CHAR_NAME%", NombrePlayer);
		opera.AnunciarTodos("Assassin", Retorno);
	}

	public static void _AnunciarEntradaKarma(L2PcInstance player){
		if(!general._activated()){
			return;
		}
		if (!general.ANNOUCE_PJ_KARMA || (player.getKarma()==0)){
			return;
		}
		if(player.getKarma()>=general.ANNOUCE_PJ_KARMA_CANTIDAD){
			String Mensaje = general.MENSAJE_ENTRADA_PJ_KARMA;
			Mensaje = Mensaje.replace("%CHAR_NAME%", player.getName());
			Mensaje = Mensaje.replace("%KARMA%", String.valueOf(player.getKarma()));
			opera.AnunciarTodos("Karma PJ", Mensaje);
		}
	}



	public static boolean _protectionLvL_PVPPK(L2PcInstance PlayerAtacante, L2PcInstance PlayerTarget){
		if(general.PVP_PK_PROTECTION_LVL == 0){
			return false;
		}
		
		if(PlayerTarget.isOnEvent() || PlayerTarget.isInsideZone(ZoneIdType.PVP)){
			return false;
		}
		
		if(PlayerAtacante.isGM()){
			return false;
		}

		if ((PlayerTarget.getPvpFlag() > 0) || (PlayerTarget.getKarma() > 0))
		{
			return false;
		}

		if(!(PlayerAtacante instanceof L2PcInstance) && !(PlayerTarget instanceof L2PcInstance)){
			return false;
		}

		if (PlayerTarget.isInOlympiadMode() ||  PlayerTarget.isInsideZone(ZoneIdType.SIEGE)){
			return false;
		}

		if((PlayerTarget.getClan() !=null) && (PlayerAtacante.getClan() !=null)){
			if (PlayerAtacante.getClan().isAtWarWith(PlayerTarget.getClan().getId())){
				return false;
			}
		}

		//if(PlayerTarget.getLevel() >= PlayerAtacante.getLevel()){
			if(isLifetimeProtected(PlayerAtacante, PlayerTarget)){
				return true;
			}
			//return false;
		//}

		int Diferencia = PlayerAtacante.getLevel() - PlayerTarget.getLevel();
		if(Diferencia >= general.PVP_PK_PROTECTION_LVL){
			return true;
		}

		return false;
	}
	
	
	private static boolean isLifetimeProtected(L2PcInstance PlayerAtacante, L2PcInstance PlayerTarget){
		if(general.PVP_PK_PROTECTION_LIFETIME_MINUTES>0){
			long vidaTarget = PlayerTarget.getOnlineTime();
			long vidaParaPvP = general.PVP_PK_PROTECTION_LIFETIME_MINUTES * 60;
			if(vidaTarget < vidaParaPvP){
				if(ZeuS.isInSameAlly(PlayerAtacante, PlayerTarget)){
					return false;
				}else if(ZeuS.isInSameChannel(PlayerAtacante, PlayerTarget)){
					return false;
				}else if(ZeuS.isInSameParty(PlayerAtacante, PlayerTarget)){
					return false;
				}else if(ZeuS.isInSameClan(PlayerAtacante, PlayerTarget)){
					return false;
				}else if(ZeuS.isInSameClanWar(PlayerAtacante, PlayerTarget)){
					return false;
				}
				return true;
			}else{
				return false;
			}					
		}
		return false;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getColorPvP_PK(L2PcInstance player){
		if(!general._activated()){
			return;
		}
		if((!general.PVP_COLOR_SYSTEM_ENABLED && !general.PK_COLOR_SYSTEM_ENABLED) || player.isGM()){
			return;
		}

		if(general.PVP_COLOR_SYSTEM_ENABLED){
			int _pvp = player.getPvpKills();
			int color = -1;
			
			Iterator itr = general. COLOR_SYSTEM_PVP_NAME.entrySet().iterator();
			while(itr.hasNext() && color==-1){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int pvpDesde = (int)Entrada.getKey();
		    	Map<Integer, String> Tem = (Map<Integer, String>) Entrada.getValue();
		    	Iterator itr2 = Tem.entrySet().iterator();
		    	Map.Entry Entrada2 = (Map.Entry)itr2.next();
		    	int pvpHasta = (int)Entrada2.getKey();
		    	String colorT = (String)Entrada2.getValue();
		    	if(_pvp >= pvpDesde && _pvp <= pvpHasta){
		    		color = Integer.valueOf(colorT);
		    	}
		    }			
			if(color!=-1){
				pintar.ColorNombre(player,color);
			}
		}
		if(general.PK_COLOR_SYSTEM_ENABLED){
			int _PK = player.getPkKills();
			int color = -1;
			
			Iterator itr = general. COLOR_SYSTEM_PK_NAME.entrySet().iterator();
			while(itr.hasNext() && color==-1){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int pkDesde = (int)Entrada.getKey();
		    	Map<Integer, String> Tem = (Map<Integer, String>) Entrada.getValue();
		    	Iterator itr2 = Tem.entrySet().iterator();
		    	Map.Entry Entrada2 = (Map.Entry)itr2.next();
		    	int pkHasta = (int)Entrada2.getKey();
		    	String colorT = (String)Entrada2.getValue();
		    	if(_PK >= pkDesde && _PK <= pkHasta){
		    		color = Integer.valueOf(colorT);
		    	}
		    }			
		
			if(color!=-1){
				pintar.ColorTitulo(player,color);
			}
		}
		player.broadcastUserInfo();
	}


	public static void _logPvPPK(L2PcInstance Player_Asesino, L2PcInstance Player_Asesinado, String Tipo){
		if(!general._activated()){
			return;
		}
		
		if(ZeuS.isDualBox_pc(Player_Asesino, Player_Asesinado)){
			return;
		}
		
		/*Limpiar Variables de Ciclos*/
			general.charPVPCOUNT.put(Player_Asesinado, 0);
			general.charPKCOUNT.put(Player_Asesinado, 0);
			//Player_Asesinado.ContPVP=0;
			//Player_Asesinado.ContPK=0;
		/*Limpiar Variables de Ciclos*/
		if (!general.LOG_FIGHT_PVP_PK ) {
			return;
		}
			int Atacante, Asesinado;
			Atacante = Player_Asesino.getObjectId();
			Asesinado = Player_Asesinado.getObjectId();

			Connection con = null;
			CallableStatement statement;
			ResultSet rs;
			try
			{
				con = L2DatabaseFactory.getInstance().getConnection();
				statement = con.prepareCall(INSERT_LOG_FIGHT);
				statement.setInt(1, Atacante);
				statement.setInt(2, Asesinado);
				statement.setString(3, Tipo);
				rs = statement.executeQuery();
				rs.close();
				statement.close();
			}
			catch (Exception e)
			{
				_log.warning("Problemas al Crear log Fight" + e.getMessage());
			}
			finally
			{
				try
				{
					if (con != null)
					{
						con.close();
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
	}
}
