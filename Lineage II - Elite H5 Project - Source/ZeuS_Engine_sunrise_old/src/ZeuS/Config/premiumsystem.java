package ZeuS.Config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class premiumsystem{
	private boolean USE_VALUE_LIKE_NEW_RATE;
	private int ID;
	private int exp;
	private int sp;
	private int adena_chance;
	private int spoil_chance;
	private int drop_chance;
	
	private int adena_rate;
	private int spoil_rate;
	private int drop_rate;	
	
	private int mp_bonus;
	private int hp_bonus;
	private int cp_bonus;
	
	private boolean mp_oly_restriction;
	private boolean hp_oly_restriction;
	private boolean cp_oly_restriction;	
	
	private int epaulette_rate;
	private int craft_chance;
	private int mw_craft_chance;
	private int soul_crystal_chance;
	private int weight_limite;
	private int day;
	private int cost;
	private boolean isAccount;
	private boolean isEnabled;
	private boolean UseBuffPremium;
	private boolean isForTest;
	private boolean showInPlayerList;
	private boolean heroStatus;
	private boolean isTeleport_immediately;
	private int BuffDuration;
	private String Nombre;
	private String Icono;
	private Map<Integer, Integer>premiumItemChance = new HashMap<Integer, Integer>();
	private Map<Integer, Integer>premiumItemRate = new HashMap<Integer, Integer>();
	private boolean haveChancePremium;
	private boolean haveRatePremium;
	private Vector<String> InfoShow = new Vector<String>();
	
	private boolean chatPremium = false;
	private int chatIDPremium = -1;
	private int chatReusePremium = -1;
	
	private static Logger _log = Logger.getLogger(premiumsystem.class.getName());
	
	public boolean haveItemInChance(int idItem){
		if(this.premiumItemChance != null ){
			if(this.premiumItemChance.size()>0){
				return this.premiumItemChance.containsKey(idItem);
			}
		}
		return false;
	}
	
	public final boolean hasChatPremium() {
		return this.chatPremium;
	}
	public final int chatIDPremium() {
		return this.chatIDPremium;
	}
	public final int chatReusePremium() {
		return this.chatReusePremium;
	}
	
	public final boolean useValueForNewRate() {
		return this.USE_VALUE_LIKE_NEW_RATE;
	}	
	
	public boolean haveItemInRate(int idItem){
		if(this.premiumItemRate != null ){
			if(this.premiumItemRate.size()>0){
				return this.premiumItemRate.containsKey(idItem);
			}
		}
		return false;
	}
	
	public boolean CP_bonus_restric_olys(){
		return this.cp_oly_restriction;
	}
	public int CP_bonus() {
		return this.cp_bonus;
	}
	
	public boolean HP_bonus_restric_olys(){
		return this.hp_oly_restriction;
	}
	public int HP_bonus() {
		return this.hp_bonus;
	}
	
	public boolean MP_bonus_restric_olys(){
		return this.mp_oly_restriction;
	}
	public int MP_bonus() {
		return this.mp_bonus;
	}	
	
	private int getPorcen(float Valor, int porcen){
		int retorno=0;
		
		long porce = (long) ( (porcen / 100.0) * Valor);
		
		retorno = Math.round( porce + Valor );
			
		return retorno;
	}
	
	private double getPorcen(double Valor, int porcen) {
			long retorno=0;
			long porce = (long) ( (porcen / 100.0) * Valor);
			retorno = Math.round( porce + Valor );
			return retorno;
		}
	
	public int getPremiumItemChance(int IdItem){
		if(!hasPremiumItemChance()){
			return 0;
		}
		try{
			return this.premiumItemChance.get(IdItem);
		}catch(Exception a){
			return 0;
		}
	}
	
	public int getPremiumItemRate(int IdItem){
		if(!hasPremiumItemRate()){
			return 0;
		}
		try{
			return this.premiumItemRate.get(IdItem);
		}catch(Exception a){
			return 0;
		}
	}	
	
	@SuppressWarnings("rawtypes")
	public void showWindowsPremiumChance(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-premium-chance-list.htm");
		html.replace("%PREMIUM_NAME%", this.Nombre);
	    String Grilla = opera.getGridFormatFromHtml(html, 1, "%DATA_ITEM%");
	    String EmptyMesagge = opera.getGridFormatFromHtml(html, 2, "");
		if(premiumItemChance==null){
			html.replace("%DATA_ITEM%", EmptyMesagge);
		}else if(premiumItemChance.size()==0){
			html.replace("%DATA_ITEM%", EmptyMesagge);
		}else if(premiumItemChance.size()>0){
			String Retorno = "";
			Iterator itr = premiumItemChance.entrySet().iterator();
			while(itr.hasNext()){
				try{
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idItem = (int)Entrada.getKey();
					int Porcen = (int)Entrada.getValue();
					String NombreItem = central.getNombreITEMbyID(idItem, true);
					String Icono = opera.getIconImgFromItem(idItem, true);
					Retorno += Grilla.replace("%ITEM_NAME%", NombreItem) .replace("%PORCEN%",String.valueOf(Porcen)). replace("%ICON_IMAGE%", Icono);
				}catch(Exception a){
					_log.warning("Error on get item from premium Chance->" + a.getMessage());
				}
			}
			html.replace("%DATA_ITEM%", Retorno);
		}
		
		central.sendHtml(player, html.getHtml(), true);
	}
	
	@SuppressWarnings("rawtypes")
	public void showWindowsPremiumRate(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-donation-premium-rate-list.htm");
		html.replace("%PREMIUM_NAME%", this.Nombre);
	    String Grilla = opera.getGridFormatFromHtml(html, 1, "%DATA_ITEM%");
	    String EmptyMesagge = opera.getGridFormatFromHtml(html, 2, "");
		if(premiumItemRate==null){
			html.replace("%DATA_ITEM%", EmptyMesagge);
		}else if(premiumItemRate.size()==0){
			html.replace("%DATA_ITEM%", EmptyMesagge);
		}else if(premiumItemRate.size()>0){
			String Retorno = "";
			Iterator itr = premiumItemRate.entrySet().iterator();
			while(itr.hasNext()){
				try{
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idItem = (int)Entrada.getKey();
					int Porcen = (int)Entrada.getValue();
					String NombreItem = central.getNombreITEMbyID(idItem, true);
					String Icono = opera.getIconImgFromItem(idItem, true);
					Retorno += Grilla.replace("%ITEM_NAME%", NombreItem) .replace("%PORCEN%",String.valueOf(Porcen)). replace("%ICON_IMAGE%", Icono);
				}catch(Exception a){
					_log.warning("Error on get item from premium Rate->" + a.getMessage());
				}
			}
			html.replace("%DATA_ITEM%", Retorno);
		}
		
		central.sendHtml(player, html.getHtml(), true);
	}	
	
	public boolean hasPremiumItemChance(){
		try{
			if(premiumItemChance==null){
				return false;
			}else if(premiumItemChance.size()==0){
				return false;
			}
		}catch(Exception a){
			return false;
		}
		return this.haveChancePremium;
	}
	
	public boolean hasPremiumItemRate(){
		try{
			if(premiumItemRate==null){
				return false;
			}else if(premiumItemRate.size()==0){
				return false;
			}
		}catch(Exception a){
			return false;
		}
		return this.haveRatePremium;
	}	
	
	public boolean isTeleImmediately(){
		return this.isTeleport_immediately;
	}
	
	public boolean isTest(){
		return this.isForTest;
	}
	
	public boolean isHero(){
		return this.heroStatus;
	}
	
	public boolean isEnabled(){
		return this.isEnabled;
	}
	
	public boolean isForPlayerSee(){
		return this.showInPlayerList;
	}
	
	public Vector<String> getInfoShow(){
		return InfoShow;
	}
	
	public int getIDPremium(){
		return this.ID;
	}
	
	public int get_mwCraft(){
		return this.mw_craft_chance;
	}
	
	public int get_mwCraft(int Value){
		try{
			return getPorcen( Value , this.mw_craft_chance);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getSoulCrystal(){
		return this.soul_crystal_chance;
	}
	
	public int getSoulCrystal(int Value){
		try{
			return getPorcen( Value , this.soul_crystal_chance);
		}catch(Exception a){
			
		}
		return 0;		
	}
	
	public int getEpaulette(){
		//KE = 9912
		return this.epaulette_rate;
	}
	
	public int getEpaulette(int Value){
		try{
			return getPorcen( Value , this.epaulette_rate);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getCraft(){
		return this.craft_chance;
	}
	
	public int getCraft(int Value){
		try{
			return getPorcen( Value , this.craft_chance);
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public int getDrop_chance(float chanceFromServer){
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( chanceFromServer > 0 ? chanceFromServer : Config.RATE_DROP_ITEMS , this.drop_chance);
		}
		return (int) (chanceFromServer > this.drop_chance ? chanceFromServer : this.drop_chance);
	}

	public int getSpoil_chance(float chanceFromServer){
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( chanceFromServer > 0 ? chanceFromServer : Config.RATE_DROP_SPOIL , this.spoil_chance);
		}
		return  (int) (chanceFromServer > this.spoil_chance ?  chanceFromServer : this.spoil_chance);		
	}
	
	public int getDrop_rate(){
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( Config.RATE_DROP_ITEMS , this.drop_rate);
		}
		return this.drop_rate;
	}

	public int getSpoil_rate(){
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( Config.RATE_DROP_SPOIL , this.spoil_rate);
		}
		return this.spoil_rate;
	}	
	
	
	public int getadena_chance(float chanceFromServer){
		float Adena = chanceFromServer;
		if(Config.RATE_DROP_ITEMS_ID!=null){
			if(Config.RATE_DROP_ITEMS_ID.containsKey(57)){
				Adena = Config.RATE_DROP_ITEMS_ID.get(57);
			}
		}
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( chanceFromServer > Adena ? chanceFromServer : Adena , this.adena_chance);
		}
		return this.adena_chance;		
	}
	
	public int getadena_rate(){
		float AdenaLocal = 0;
		if(Config.RATE_DROP_ITEMS_ID!=null){
			if(Config.RATE_DROP_ITEMS_ID.containsKey(57)){
				AdenaLocal = Config.RATE_DROP_ITEMS_ID.get(57);
			}
		}
		if(!this.USE_VALUE_LIKE_NEW_RATE){
			return getPorcen( AdenaLocal , this.adena_rate);
		}
		float Adena = AdenaLocal > this.adena_rate ? AdenaLocal : this.adena_rate;
		return (int) Adena;
	}	
	
	public double getWeight(boolean porcen, double _weight){
		double Retorno = this.weight_limite;
		if(porcen){
			double Valor = _weight;
			double porcen1 = getPorcen(Valor, this.weight_limite);
			Retorno = porcen1;
		}
		return Retorno;
	}
	
	public int getsp(boolean porcen){
		if(porcen){
			return getPorcen( Config.RATE_SP , this.sp);
		}
		return this.sp;		
	}
	
	public int getexp(boolean porcen){
		if(porcen){
			return getPorcen( Config.RATE_XP , this.exp);
		}
		return this.exp;
	}
	
	
	public int getDays(){
		return this.day;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public String getName(){
		return this.Nombre;
	}
	
	public String getIcono(){
		return this.Icono;
	}
	
	public boolean IsAccount(){
		return this.isAccount;
	}
	
	public String getAplicableA(){
		return this.isAccount ? "Account" : "Clan";
	}
	
	public premiumsystem(){
		
	}
	
	public boolean canUseBuffPremium(){
		return UseBuffPremium;
	}
	
	public int getBuffDuration(){
		return BuffDuration;
	}
	
	public void setPremiumRate(int _adenarate, int _droprate, int _spoilrate){
		this.adena_rate = _adenarate;
		this.drop_rate = _droprate;
		this.spoil_rate = _spoilrate;
	}
	
	public premiumsystem(int _id, int _exp, int _sp, int _dropchance, int _adenachance, int _spoilchance, int _epauletterate, int _craftchance, int _mw_craftchance, int _soul_crystalchance, int _weight_limite, int _day, int _cost, boolean _isAcc, String _Nombre, String _Icono, Vector<String> _DataShow, boolean _isEnabled, boolean _buffpremium, int _buffduration, String _PremiumItemChance, boolean _PremiumChanceSystem, boolean _ForTest, boolean _ShowInPplList, boolean _heroStatus, boolean _tele_immediately, int _adenarate, int _droprate, int _spoilrate, boolean _PremiumRateSystem, String _PremiumItemRate, boolean useValueLikeNewRate, int _cpBonus, int _hpBonus, int _mpBonus, boolean cp_OlyRestriction, boolean hp_OlyRestriction, boolean mp_OlyRestriction, boolean _chatPremium, int _idChatPremium, int _reuseChatPremium){
		this.USE_VALUE_LIKE_NEW_RATE = useValueLikeNewRate;
		this.exp = _exp;
		this.sp = _sp;
		this.cp_bonus = _cpBonus;
		this.hp_bonus = _hpBonus;
		this.mp_bonus = _mpBonus;
		this.cp_oly_restriction = cp_OlyRestriction;
		this.hp_oly_restriction = hp_OlyRestriction;
		this.mp_oly_restriction = mp_OlyRestriction;
		this.adena_chance = _adenachance;
		this.adena_rate = _adenarate;
		this.drop_chance = _dropchance;
		this.drop_rate = _droprate;
		this.spoil_chance = _spoilchance;
		this.spoil_rate = _spoilrate;
		this.epaulette_rate = _epauletterate;
		this.craft_chance = _craftchance;
		this.mw_craft_chance = _mw_craftchance;
		this.day = _day;
		this.cost = _cost;
		this.isAccount = _isAcc;
		this.ID = _id;
		this.Nombre = _Nombre;
		this.Icono = _Icono;
		this.InfoShow = _DataShow;
		this.isEnabled = _isEnabled;
		this.UseBuffPremium = _buffpremium;
		this.BuffDuration = _buffduration;
		this.soul_crystal_chance = _soul_crystalchance;
		this.weight_limite = _weight_limite;
		this.haveChancePremium = _PremiumChanceSystem;
		this.haveRatePremium = _PremiumRateSystem;
		this.isForTest = _ForTest;
		this.showInPlayerList = _ShowInPplList;
		this.heroStatus = _heroStatus;
		this.isTeleport_immediately = _tele_immediately;
		this.chatPremium = _chatPremium;
		this.chatIDPremium = _idChatPremium;
		this.chatReusePremium = _reuseChatPremium;
		
		if(_PremiumItemChance!=null){
			if(_PremiumItemChance.length()>0){
				try{
					if(_PremiumItemChance.indexOf(";")>0){
						String P_1[] = _PremiumItemChance.split(";");
						for(String Its : P_1){
							try{
								int idItem = Integer.valueOf(Its.split(",")[0]);
								int chance = Integer.valueOf(Its.split(",")[1]);
								premiumItemChance.put(idItem, chance);
							}catch(Exception e){
								_log.warning("ZeuS error on create Premium Item Chance->" + e);							
							}
						}
					}else{
						int idItem = Integer.valueOf(_PremiumItemChance.split(",")[0]);
						int chance = Integer.valueOf(_PremiumItemChance.split(",")[1]);
						premiumItemChance.put(idItem, chance);						
					}
				}catch(Exception a){
					_log.warning("ZeuS error on create Premium Item Chance->" + a);
				}
			}
		}
		
		if(_PremiumItemRate!=null){
			if(_PremiumItemRate.length()>0){
				try{
					if(_PremiumItemRate.indexOf(";")>0){
						String P_1[] = _PremiumItemRate.split(";");
						for(String Its : P_1){
							try{
								int idItem = Integer.valueOf(Its.split(",")[0]);
								int chance = Integer.valueOf(Its.split(",")[1]);
								premiumItemRate.put(idItem, chance);
							}catch(Exception e){
								_log.warning("ZeuS error on create Premium Item Rate->" + e);							
							}
						}
					}else{
						int idItem = Integer.valueOf(_PremiumItemRate.split(",")[0]);
						int chance = Integer.valueOf(_PremiumItemRate.split(",")[1]);
						premiumItemRate.put(idItem, chance);						
					}
				}catch(Exception a){
					_log.warning("ZeuS error on create Premium Item Rate->" + a);
				}
			}
		}		
		
	}
}


