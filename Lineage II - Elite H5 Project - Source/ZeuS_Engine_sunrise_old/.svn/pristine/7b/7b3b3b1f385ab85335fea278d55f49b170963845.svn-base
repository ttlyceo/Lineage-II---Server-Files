package ZeuS.interfase;

import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.xml.impl.TransformData;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;

import java.util.HashMap;
import java.util.Map;

import ZeuS.Config.general;

import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class transform {

	private static Map<Integer, HashMap<String, Integer>> TransforPlayerVar = new HashMap<Integer, HashMap<String, Integer>>();
	private static Map<Integer, Integer> TransformTimeReuse = new HashMap<Integer, Integer>();
	
	protected static String[] Transformaciones_especiales = {"Enchant,257","Healer,255","Knight,252","Rogue,254","Summon,258","Warrior,253","Wizard,256"};
	protected static String[] Transformaciones_RaidBoss = {"Anakim,306","Benom,307","D Prince,311","Zaken,305","Ranku,309","Kiyachi,310","Gordon,308","Akamanah,302","Zariche,301"};
	protected static String[] Transformaciones_Varias = {"Kamael,251","Native,101","Golem,259","WLK Fuse,219","ES Fuse,220","PS Fuse,221","Rabbit,105","Pig,104","Pixy,304","Yeti,102","Onyx Be,1","Buffalo,103",
			"Doom Wr,2","D Blader,7","Zombie,303","Heretic,3","OlMahum,6","Saber,5","Vale Mas,4","Lava Gol,322","Timitran,321","Frog,111","Grizzly,320","Treykan,126","Dragon Bom,218",	"Golem Guard,212",
			"Grail Apost,203","Infern Drake,215","Lilim Knight,209","Unicorn,206","Ghost,108","Snow King,114","Scarecrow,115","Tin Golem,116","Gatekeeper1,107","Gatekeeper2,319","Red Elf,121","Blue Elf,122",
			"Big Blue Elf,127","Purple Tow,117","Yellow Tow,120","Pink Tow,118","Grey Tow,119","Child,112","Native Dawn,124","Kadomas,20000","Aqua Elf,125","Game Man.,22","DM Lee,20005","DM Karin,20006",
			"Human,10",	"Dark Elf,12","Light Elf,11","Dwarf,14","Orc,13","Kamael,15","Archer C.,17","Fortress C.,21","Kamael C.,19","Guard C.,16","Magic L.,18","Dragon Lee,20005","G. of Dawn,113","Dragon Karin,20006",
			"K. of Dawn,20"};
	
	
	
	
	
	private static boolean canUseTransformTime(L2PcInstance player){
		if(TransformTimeReuse!=null){
			if(TransformTimeReuse.containsKey(player.getObjectId())){
				int TiempoReuso = TransformTimeReuse.get(player.getObjectId());
				int TiempoActual = opera.getUnixTimeNow();
				int SegundosReuso = general.TRANSFORM_REUSE_TIME_MINUTES * 60;
				if(TiempoActual <= (TiempoReuso + SegundosReuso)){
					int Falta = (TiempoReuso + SegundosReuso) - TiempoActual;
					central.msgbox("You need to wait " + String.valueOf(Falta) + " to use this option again.", player);					
					return false;
				}
			}
		}
		return true;
	}
	
	public static void untransform(L2PcInstance player){
		L2Character ch = player;
		if(ch.isTransformed()){
			ch.stopTransformation(true);
		}
	}
	
	
	private static int getIdTransformTime(L2PcInstance player){
		int retorno = -1;
		if(TransforPlayerVar!=null){
			if(TransforPlayerVar.containsKey(player.getObjectId())){
				return TransforPlayerVar.get(player.getObjectId()).get("ID") ; 
			}
		}
		
		return retorno;
	}
	
	private static void cleanTimeTransformation(L2PcInstance player){
		int idPlayer = player.getObjectId();
		if(TransforPlayerVar!=null){
			if(TransforPlayerVar.containsKey(idPlayer)){
				TransforPlayerVar.remove(idPlayer);
			}
		}
	}
	
	private static void setTime(L2PcInstance player){
		int idPlayer = player.getObjectId();
		int time = opera.getUnixTimeNow();
		if(TransforPlayerVar==null){
			TransforPlayerVar.put(idPlayer, new HashMap<String, Integer>());
			TransforPlayerVar.get(idPlayer).put("ID", time);
			TransforPlayerVar.get(idPlayer).put("TIME", time);
		}else{
			if(TransforPlayerVar.containsKey(idPlayer)){
				TransforPlayerVar.get(idPlayer).put("ID", time);
				TransforPlayerVar.get(idPlayer).put("TIME", time);				
			}else{
				TransforPlayerVar.put(idPlayer, new HashMap<String, Integer>());
				TransforPlayerVar.get(idPlayer).put("ID", time);
				TransforPlayerVar.get(idPlayer).put("TIME", time);				
			}
		}
	}
	

	protected static boolean transformPlayer(L2PcInstance player, int idTrans){
		untransform(player);
		L2Character cha = player;
		if (player.isSitting())
		{
			player.sendMessage("Cannot transform while Sitting");
			return false;
		}else if (cha.isTransformed() || player.isIn7sDungeon())
		{
			player.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
			return false;
		}

		else if (player.isInWater())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER);
			return false;
		}

		else if (player.isFlyingMounted() || player.isMounted())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET);
			return false;
		}
		
		if(general.TRANSFORM_TIME){
			if(!canUseTransformTime(player)){
				central.msgbox("You can not use this option right now. You need to wait", player);
				return false;
			}
		}

		if (!TransformData.getInstance().transformPlayer(idTrans, player))
		{
			cha.sendMessage("Unknown transformation Id: " + String.valueOf(idTrans));
			return true;
		}else{
			if(general.TRANSFORM_TIME){
				TransformTimeReuse.put(player.getObjectId(), opera.getUnixTimeNow());
				setTime(player);
				int tiempoDuracion = (general.TRANSFORM_TIME_MINUTES  * 60) * 1000;
				ThreadPoolManager.getInstance().scheduleGeneral(new TransformTime(opera.getUnixTimeNow(),player) , tiempoDuracion);
			}
		}
		return true;
	}

	public static boolean setTransformar(L2PcInstance player, int idTrans, int IdTipo){
		if(general.TRANSFORMATION_NOBLE && !player.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
			return false;
		}

		if(general.TRANSFORMATION_LVL > player.getLevel()){
			central.msgbox( language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.TRANSFORMATION_LVL)), player);
			return false;
		}

		if((IdTipo==1) && !opera.haveItem(player, general.TRANSFORMATION_ESPECIALES_PRICE)){
			return false;
		}

		if((IdTipo==2) && !opera.haveItem(player, general.TRANSFORMATION_RAIDBOSS_PRICE)){
			return false;
		}

		if((IdTipo==3) && !opera.haveItem(player, general.TRANSFORMATION_PRICE)){
			return false;
		}

		return transformPlayer(player, idTrans);
	}

	public static String mainTransform(L2PcInstance player,int Seccion){
		String MAIN_HTML ="";
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Transformations") + central.LineaDivisora(2);

		if(Seccion < 0){
			untransform(player);
			String BotonBack = "<button value=\"back\" action=\"bypass -h ZeuSNPC transform 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("You took off the transformation, <br1> now you can choose your new Transformation"+BotonBack,"LEVEL") + central.LineaDivisora(2);
			MAIN_HTML += central.getPieHTML() + "</body></html>";
			return MAIN_HTML;
		}

		


		L2Character cha = player;

		if(cha.isTransformed()){
			String btn = "<button value=\"Destransformarme\" action=\"bypass -h ZeuSNPC transform 4 0 0\" width=160 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("You must take off your transformation before using my services" + btn,"LEVEL") + central.LineaDivisora(2);
			MAIN_HTML += central.getPieHTML() + "</body></html>";
			return MAIN_HTML;
		}

		String BotonEspeciales = general.TRANSFORMATION_ESPECIALES ? "<button value=\"Special\" action=\"bypass -h ZeuSNPC transform 0 1 0\" width=86 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">" : "";
		String BotonRaidboss = general.TRANSFORMATION_RAIDBOSS ? "<button value=\"RaidBoss\" action=\"bypass -h ZeuSNPC transform 0 2 0\" width=86 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">" : "";
		String BotonGeneral = "<button value=\"Normal\" action=\"bypass -h ZeuSNPC transform 0 3 0\" width=86 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";;

		String BotonesEleccion ="<center><table><tr>";
		if(BotonEspeciales.length()>0){
			BotonesEleccion+= "<td width=85>"+BotonEspeciales+"</td>";
		}

		if(BotonRaidboss.length()>0){
			BotonesEleccion += "<td width=85>"+BotonRaidboss+"</td>";
		}

		BotonesEleccion += "<td width=85>"+BotonGeneral+"</td></tr></table></center>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(BotonesEleccion) + central.LineaDivisora(1);
		
		if(general.TRANSFORM_TIME){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Transformation Time: " + String.valueOf(general.TRANSFORM_TIME_MINUTES) + " Minutes <br1> Transformation Reuse: " + String.valueOf(general.TRANSFORM_REUSE_TIME_MINUTES) + " Minutes") + central.LineaDivisora(1);			
		}
		
		String strBotones = "";


		MAIN_HTML += central.LineaDivisora(1);
		if(Seccion == 0){
			if(general.TRANSFORMATION_ESPECIALES){
				MAIN_HTML += central.ItemNeedShowBox(general.TRANSFORMATION_ESPECIALES_PRICE, "Item needed to use the Special Transformations");
				MAIN_HTML += central.LineaDivisora(1);
			}
			if(general.TRANSFORMATION_RAIDBOSS){
				MAIN_HTML += central.ItemNeedShowBox(general.TRANSFORMATION_RAIDBOSS_PRICE, "Item needed to use the RaidBoss Transformations");
				MAIN_HTML += central.LineaDivisora(1);
			}
			MAIN_HTML += central.ItemNeedShowBox(general.TRANSFORMATION_PRICE,"Item needed to use the Normal Transformations");
			MAIN_HTML += central.LineaDivisora(1);
		}


		if(Seccion != 0){
			int Cont = 0;
			if((Seccion == 1) && general.TRANSFORMATION_ESPECIALES){
				strBotones = "<table width=260><tr>";
				if(general.TRANSFORMATION_ESPECIALES){
					for(String _formPart : Transformaciones_especiales){
						String[] _TransForm = _formPart.split(",");
						strBotones+= "<td width=85><button value=\""+_TransForm[0]+"\" action=\"bypass -h ZeuSNPC transform 1 "+_TransForm[1]+" 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>";
						Cont++;
						if((Cont%3)==0){
							strBotones+="</tr><tr>";
						}
					}

					if(strBotones.endsWith("<tr>") || strBotones.endsWith("</td>")){
						strBotones+="</tr></table>";
					}else{
						strBotones+="</table>";
					}

					MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Special Transformations") + central.LineaDivisora(1);
					MAIN_HTML += central.headFormat(strBotones);
				}
			}
			Cont = 0;
			if((Seccion == 2) && general.TRANSFORMATION_RAIDBOSS){
				strBotones = "<table width=260><tr>";
				if(general.TRANSFORMATION_RAIDBOSS){
					for(String _formPart : Transformaciones_RaidBoss){
						String[] _TransForm = _formPart.split(",");
						strBotones+= "<td width=85><button value=\""+_TransForm[0]+"\" action=\"bypass -h ZeuSNPC transform 2 "+_TransForm[1]+" 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>";
						Cont++;
						if((Cont%3)==0){
							strBotones+="</tr><tr>";
						}
					}

					if(strBotones.endsWith("<tr>") || strBotones.endsWith("</td>")){
						strBotones+="</tr></table>";
					}else{
						strBotones+="</table>";
					}

					MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Raidboss Transformations") + central.LineaDivisora(1);
					MAIN_HTML += central.headFormat(strBotones);
				}
			}
			Cont = 0;
			if(Seccion == 3){
				strBotones = "<table width=260><tr>";
				for(String _formPart : Transformaciones_Varias){
					String[] _TransForm = _formPart.split(",");
					strBotones+= "<td width=85><button value=\""+_TransForm[0]+"\" action=\"bypass -h ZeuSNPC transform 3 "+_TransForm[1]+" 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td>";
					Cont++;
					if((Cont%3)==0){
						strBotones+="</tr><tr>";
					}
				}

				if(strBotones.endsWith("<tr>") || strBotones.endsWith("</td>")){
					strBotones+="</tr></table>";
				}else{
					strBotones+="</table>";
				}
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Normal Transformations") + central.LineaDivisora(1);
				MAIN_HTML += central.headFormat(strBotones);
			}
		}

		MAIN_HTML += central.getPieHTML() + "</body></html>";

		return MAIN_HTML;
	}
	
	//ThreadPoolManager.getInstance().scheduleGeneral(new AutoReward(), ( general.VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK * 60 ) * 1000);
	
	
	private static class TransformTime implements Runnable{
		private int _IdUnix;
		L2PcInstance _player;

		public TransformTime (int IdUnix, L2PcInstance player){
			_player = player;
			_IdUnix = IdUnix;
		}
		@Override
		public void run(){
			if(general.TRANSFORM_TIME){
				int TranformationID = getIdTransformTime(_player);
				if(TranformationID == _IdUnix){
					try{
						untransform(_player);
					}catch(Exception a){
						
					}
					central.msgbox("Transformation time is Over", _player);
					cleanTimeTransformation(_player);
				}
			}
		}
	}
	
	

}
