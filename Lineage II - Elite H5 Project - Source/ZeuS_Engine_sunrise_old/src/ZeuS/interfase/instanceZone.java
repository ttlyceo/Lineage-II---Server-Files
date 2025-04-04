package ZeuS.interfase;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class instanceZone {
	public static final Logger _log = Logger.getLogger(instanceZone.class.getName());
	private static Map<Integer, Integer> ID_INSTANCE_4_PLAYER = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer>TemporalData = new HashMap<Integer, Integer>(); 
	public static void getMain(L2PcInstance player){
		//instancezone
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/instancezone.html");
		boolean haveInstance = false;
		Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player.getObjectId());
		
		String Table = "<table width=280><tr><td width=150><center>Instance</center></td><td width=60><center>Time(HH:MM)</center></td><td width=60><center></center></td></tr></table><table>";
		String Insert = "<tr><td fixwidth=150><center>%NAME%</center></td><td fixwidth=60><center>%TIME%</center></td><td width=60><center>%BTN%</center></td></tr>";

		String btnRemove = "<button value=\"Clear\" action=\"bypass -h ZeuS instancezone RemoveAsk %IDINSTANCE%\" width=60 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		for (int id : instanceTimes.keySet())
		{
			haveInstance=true;
			int hours = 0;
			int minutes = 0;
			long remainingTime = (instanceTimes.get(id) - System.currentTimeMillis()) / 1000;
			if (remainingTime > 0)
			{
				hours = (int) (remainingTime / 3600);
				minutes = (int) ((remainingTime % 3600) / 60);
				String Name = InstanceManager.getInstance().getInstanceIdName(id);
				Table += Insert.replace("%NAME%", Name).replace("%TIME%", String.valueOf(hours) + ":" + String.valueOf(minutes)).replace("%BTN%", btnRemove.replace("%IDINSTANCE%", String.valueOf(id)) );
			}
		}
		if(haveInstance){
			String Cost = "";
			for(String Data : general.INSTANCE_ZONE_COST.split(";")){
				int ItemID = Integer.valueOf(Data.split(",")[0]);
				int ItemMount = Integer.valueOf(Data.split(",")[1]);
				if(Cost.length()>0){
					Cost += ", ";
				}
				Cost += opera.getFormatNumbers(ItemMount) + " " + central.getNombreITEMbyID(ItemID);
			}			
			Table += "</table>";
			html.replace("%TABLE%", Table);
			html.replace("%COST%", Cost) ;
			central.sendHtml(player, opera.htmlSearchLogo(player, html.getHtml()), false);
		}else{
			central.msgbox("You dont have any Instance Penalty.", player);
		}
	}
	public static void bypass(L2PcInstance player, String Command){
		
		String ccv = Command.split(" ")[2];
		String idInstancia = Command.split(" ")[3];
		if(ccv.equals("Remove")){
			if(ID_INSTANCE_4_PLAYER!=null){
				if(ID_INSTANCE_4_PLAYER.containsKey(player.getObjectId())){
					removeInstance(player, Integer.valueOf(idInstancia));
				}
			}
		}else if(ccv.equals("RemoveAsk")){
			ID_INSTANCE_4_PLAYER.put(player.getObjectId(), Integer.valueOf(idInstancia));
			TemporalData.put(player.getObjectId(), Integer.valueOf(idInstancia));
			//null instancezone Remove null
			int hours = 0;
			Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player.getObjectId());
			long remainingTime = (instanceTimes.get(Integer.valueOf(idInstancia)) - System.currentTimeMillis()) / 1000;
			String Reward = "";
			if (remainingTime > 0)
			{
//				hours = (int) (remainingTime / 3600);
				for(String Data : general.INSTANCE_ZONE_COST.split(";")){
					int ItemID = Integer.valueOf(Data.split(",")[0]);
					Long ItemMount = Long.valueOf(Data.split(",")[1]);
					if(Reward.length()>0){
						Reward += ", ";
					}
//					if(hours > 0){
//						ItemMount = ItemMount * hours;
//					}

					Reward += central.getNombreITEMbyID(ItemID)  + " " + opera.getFormatNumbers(ItemMount);
				}
			}
			try{
				Dlg.sendDlg(player, "You need to pay " + Reward + ". Do you want it?", IdDialog.INSTANCE_REMOVE_BY_PAY);
			}catch(Exception a){
				_log.warning("Error->" + a.getMessage());
			}
		}
		
	}
	@SuppressWarnings("unused")
	private static void removeInstance(L2PcInstance player, int instanceId){
		
		if(!general.INSTANCE_ZONE_CLEAR){
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return;
		}
		
		Map<Integer, Long> instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(player.getObjectId());
//		int hours = 0;
//		int minutes = 0;
		long remainingTime = (instanceTimes.get(instanceId) - System.currentTimeMillis()) / 1000;
		String Reward = "";
		if (remainingTime > 0)
		{
//			hours = (int) (remainingTime / 3600);
//			minutes = (int) ((remainingTime % 3600) / 60);
			
			for(String Data : general.INSTANCE_ZONE_COST.split(";")){
				int ItemID = Integer.valueOf(Data.split(",")[0]);
				Long ItemMount = Long.valueOf(Data.split(",")[1]);
				if(Reward.length()>0){
					Reward += ";";
				}
//				if(hours > 0){
//					ItemMount = ItemMount * hours;
//				}
				Reward += String.valueOf(ItemID) + "," + ItemMount;
			}
		}
		
		
		if(!opera.haveItem(player, Reward.length()==0 ? general.INSTANCE_ZONE_COST : Reward)){
			central.msgbox("====>Cost of Service<====", player);
			for(String Data : (Reward.length()==0 ? general.INSTANCE_ZONE_COST : Reward).split(";")){
				String ItemName = central.getNombreITEMbyID( Integer.valueOf(Data.split(",")[0]) );
				String ItemCantidad = opera.getFormatNumbers(Data.split(",")[1]);
				central.msgbox( ItemCantidad + " " + ItemName , player);
			}
			central.msgbox("====>Cost of Service<====", player);
			return;
		}
		
		opera.removeItem(Reward.length()==0 ? general.INSTANCE_ZONE_COST : Reward, player);
		final String name = InstanceManager.getInstance().getInstanceIdName(instanceId);
		InstanceManager.getInstance().deleteInstanceTime(player.getObjectId(), instanceId);
		player.sendMessage("Instance zone " + name + " are Clear");		
	}
}

