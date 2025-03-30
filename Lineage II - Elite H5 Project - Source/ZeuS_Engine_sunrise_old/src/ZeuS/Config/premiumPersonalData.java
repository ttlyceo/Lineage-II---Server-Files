package ZeuS.Config;

import l2r.gameserver.data.sql.ClanTable;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.L2ClanMember;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class premiumPersonalData {
	private String ACCOUNT_OR_CLANID;
	private int BEGIN_DATE;
	private int END_DATE;
	protected boolean ISACTIVE;
	protected boolean ISACCOUNT;
	private int ID_PREMIUM;
	private premiumsystem PREMIUM_SYSTEM;
	
	public boolean isActive(){
		if(this.ISACTIVE){
			if(this.END_DATE <= opera.getUnixTimeNow()){
				this.ISACTIVE=false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	public int getBegin(){
		return this.BEGIN_DATE;
	}
	
	public int getEnd(){
		return this.END_DATE;
	}
	
	public int getIdPremiumUse(){
		return this.ID_PREMIUM;
	}
	
	public premiumsystem getPremiumData(){
		return this.PREMIUM_SYSTEM;
	}
	
	public premiumPersonalData(String _Account_or_ClanID, int _BeginDate, int _EndDate, boolean _isAccount, int _idPremium, premiumsystem _PremiumData){
		this.ACCOUNT_OR_CLANID = _Account_or_ClanID;
		this.BEGIN_DATE = _BeginDate;
		this.END_DATE = _EndDate;
		if(_EndDate > opera.getUnixTimeNow()){
			this.ISACTIVE = true;
		}else{
			this.ISACTIVE = false;
		}
		this.ISACCOUNT = _isAccount;
		this.ID_PREMIUM = _idPremium;
		this.PREMIUM_SYSTEM = _PremiumData;
	}
	
	public class EndPremiumClock implements Runnable{
		public EndPremiumClock(){
		}
		@Override
		public void run(){
			try{
				if(END_DATE <= opera.getUnixTimeNow()){
					ISACTIVE = false;
					if(ISACCOUNT){
						if(opera.isCharInGame(Integer.valueOf(ACCOUNT_OR_CLANID))){
							L2PcInstance ppl = opera.getPlayerbyID(Integer.valueOf(ACCOUNT_OR_CLANID));
							central.msgbox_Lado(language.getInstance().getMsg(ppl).DONATION_PREMIUM_ACCOUNT_IS_OVER, ppl, "PREMIUM");
						}
					}else{
						L2Clan clan = ClanTable.getInstance().getClan(Integer.valueOf(ACCOUNT_OR_CLANID));
						for(L2ClanMember ppl : clan.getMembers()){
							if(!ppl.isOnline()){
								continue;
							}
							L2PcInstance ppl2 = ppl.getPlayerInstance();
							central.msgbox_Lado(language.getInstance().getMsg(ppl2).DONATION_PREMIUM_CLAN_IS_OVER, ppl2, "PREMIUM_CLAN");
						}
					}
				}
			}catch(Exception a){

			}

		}
	}
}
