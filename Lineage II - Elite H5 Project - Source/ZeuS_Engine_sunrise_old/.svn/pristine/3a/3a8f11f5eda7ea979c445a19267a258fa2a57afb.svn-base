package ZeuS.interfase;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class borrowAccountSystem extends borrowAccount{

	private final Logger _log = Logger.getLogger(borrowAccountSystem.class.getName());
	private Map<Integer, String> TEMP_PASS = new HashMap<Integer, String>();
	
	public void setBorrowPassword(L2PcInstance player, String Pass1, String Pass2) {
		if(!Pass1.trim().equals(Pass2.trim())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_PASSWORD_NOT_EQUALS, player);
			return;
		}
		if(Pass1.trim().length()<5) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_PASSWORD_LENGHT_WROG, player);
			return;
		}
		TEMP_PASS.put(player.getObjectId(), Pass1);
		String strQuestion = language.getInstance().getMsg(player).BORROW_SYSTEM_CONFIRM_PASSWORD_$password.replace("$password", Pass1);
		Dlg.sendDlg(player, strQuestion, IdDialog.ENGINE_BORROW_QUESTION, 60);
	}
	
	
	
	public void setPasswordFromDLG(L2PcInstance player) {
		String PassN = TEMP_PASS.get(player.getObjectId());
		setNewBorrowPassword(player, PassN);
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/borrowSystem-done.html");
		html.replace("%PASSWORD%", PassN);
		central.sendHtml(player, html);		
	}
	
	public void sendBorrowWindows(L2PcInstance player) {
		if(player.isGM()) {
			central.msgbox( language.getInstance().getMsg(player).BORROW_SYSTEM_CAN_NOT_BE_USE_BY_GM_AND_ADMINS, player);
			return;
		}
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/borrowSystem-index.html");
		html.replace("%LINK%", "bypass ZeuS makeBorrowPass");
		central.sendHtml(player, html);
	}
	
	public void sendBorrowWindows_create(L2PcInstance player) {
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/borrowSystem-create-password.html");
		html.replace("%LINK%", "bypass ZeuS CreateBorrowPass $txtPass1 $txtPass2");
		central.sendHtml(player, html);
	}	
	
	public static borrowAccountSystem getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final borrowAccountSystem _instance = new borrowAccountSystem();
	}	
	
}
