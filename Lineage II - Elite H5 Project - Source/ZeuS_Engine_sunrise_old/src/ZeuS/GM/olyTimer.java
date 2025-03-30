package ZeuS.GM;

import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.procedimientos.opera;
import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;

public class olyTimer {
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(olyTimer.class.getName());
	private boolean isRun = true;
	private L2PcInstance Player;
	private int unixStart;
	private int errorCount = 0;
	private int MeDmg = 0;
	private int OtherDmg = 0;
	private int MY_ID = 0;
	private int MILISECOND_TO_UPDATE = 320;
	public olyTimer(L2PcInstance player, int IdOly){
		this.Player = player;
		this.unixStart = opera.getUnixTimeNow();
		this.MY_ID = IdOly;
		this.MeDmg = 0;
		this.OtherDmg = 0;		
		this.runClock();
	}
	
	public void addDmg(int Dmg, int IdOly){
		if(IdOly == this.MY_ID){
			this.MeDmg += Dmg;
		}else{
			this.OtherDmg += Dmg;
		}
	}
	
	public void stopClock(boolean setOff){
		if(setOff){
			this.isRun = false;
		}
	}
	private void runClock(){
		if(!this.isRun){
			return;
		}
		try{
			int TotalSecondOnOly = ((int) (Config.ALT_OLY_BATTLE / 1000));
			int ActualUnix = opera.getUnixTimeNow();
			int TimeLeft = TotalSecondOnOly - ( ActualUnix - this.unixStart );
			if(general.ZEUS_OLY_COUNTER){
				String TimeToShow  = "Oly time Left:\n" + opera.getTimeFromUnix(TimeLeft);
				String Damage = general.ZEUS_OLY_SHOW_DMG ? "\nMy Dmg to Opp.: " + opera.getFormatNumbers(this.MeDmg) + "\nOpponent's Dmg in me : " + opera.getFormatNumbers(this.OtherDmg) : "";
				ExShowScreenMessage Mnsje = new ExShowScreenMessage(1, -1,  ExShowScreenMessage.TOP_LEFT , 1, /*Size*/1, 0, 0, false, this.MILISECOND_TO_UPDATE + 20, false, TimeToShow + Damage);				
				this.Player.sendPacket(Mnsje);
			}
			if(TimeLeft<=0){
				this.isRun = false;
			}
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
    			@Override
    			public void run(){
    				runClock();
    			}
    		}, this.MILISECOND_TO_UPDATE);				
			
		}catch(Exception a){
			errorCount++;
			if(errorCount>=6){
				this.isRun = false;
			}
		}
	}
}
