package ZeuS.Comunidad.EngineForm;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.classmaster;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_profession {
	
	private static final boolean validateClassId(ClassId oldCID, ClassId newCID)
	{
		if ((newCID == null) || (newCID.getRace() == null))
		{
			return false;
		}

		if (oldCID.equals(newCID.getParent()))
		{
			return true;
		}

		if (Config.ALLOW_ENTIRE_TREE && newCID.childOf(oldCID))
		{
			return true;
		}

		return false;
	}
	
	private static final int getMinLevel(int level)
	{
		switch (level)
		{
			case 0:
				return 20;
			case 1:
				return 40;
			case 2:
				return 76;
			default:
				return Integer.MAX_VALUE;
		}
	}
	
	private static NpcHtmlMessage getProfesiones(L2PcInstance player, NpcHtmlMessage html){
		int level = player.getClassId().level() + 1;

		String bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Profession.name() +";add;%IDCLASS%;"+String.valueOf(level)+";0;0;0";
		String DataGrid = "";
		String botonera = opera.getGridFormatFromHtml(html, 1, "%GRID_DATA%").replace("%BYPASS_CLASS%", bypass);
		String NoMoreClass = opera.getGridFormatFromHtml(html, 2, "%NO_MORE_PROFESSION%");
		final ClassId currentClassId = player.getClassId();
		final int minLevel = getMinLevel(currentClassId.level());
		int jobLevel=player.getClassId().level();
		
		boolean haveClass = false;

		if ((player.getLevel() >= minLevel) || Config.ALLOW_ENTIRE_TREE)
		{
			for (ClassId cid : ClassId.values())
			{
				if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2))
				{
					continue;
				}
				if (validateClassId(currentClassId, cid) && (cid.level() == level))
				{
					haveClass = true;
					String Imagen = general.getClassInfo(cid.getId()).getLogo();
					String Nombre = general.getClassInfo(cid.getId()).getName();
					DataGrid += botonera.replace("%IDCLASS%",String.valueOf(cid.getId())).replace("%IMAGEN%", Imagen).replace("%CLASS%", central.getClassName(player, cid.getId())).replace("%RAZA%", Nombre);
				}
			}
		}
		
		if(!haveClass){
			String Mensaje = "";
			if((jobLevel ==0) && (level < 20)) {
				Mensaje = language.getInstance().getMsg(player).PRO_COME_BACK_LV20;
			} else if((jobLevel <=1) && (level < 40)) {
				Mensaje = language.getInstance().getMsg(player).PRO_COME_BACK_LV40;
			} else if((jobLevel <=2) && (level < 76)) {
				Mensaje = language.getInstance().getMsg(player).PRO_COME_BACK_LV76;
			} else {
				Mensaje = "No more Class Changes Available";
			}
			html.replace("%GRID_DATA%", "");
			html.replace("%NO_MORE_PROFESSION%", NoMoreClass.replace("%MESAGE_NO_PROFESION%", Mensaje));
		}else{
			html.replace("%GRID_DATA%", DataGrid);
			html.replace("%NO_MORE_PROFESSION%", "");
		}
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-profession.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html= getProfesiones(player, html); 
		
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_CLASS_TRANSFER) {
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
			return mainHtml(player,params);
		}else if(parm1.equals("add")){
			if(classmaster.AddProfesion(player, Integer.valueOf(parm2), Integer.valueOf(parm3))){
				return mainHtml(player,"0");	
			}
		}
		return "";
	}
}
