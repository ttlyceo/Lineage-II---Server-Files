package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import l2r.L2DatabaseFactory;
import ZeuS.Config.general;

public class GrandBossInfo {
	@SuppressWarnings("unused")
	public static String getInfo()
	{
		String MAIN = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN = central.LineaDivisora(2) + central.headFormat("Grand Boss Info") + central.LineaDivisora(2);
		MAIN += central.LineaDivisora(2) + central.headFormat("Grand Boss Info","LEVEL") + central.LineaDivisora(2);
		Connection con = null;
		int pos = 0;

		String HtmlBoss ="";

		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT boss_id, status, respawn_time FROM grandboss_data");
			ResultSet result = statement.executeQuery();
			final Calendar time = Calendar.getInstance();
			//time.setTimeInMillis(respawnTime);
			String RespawnHora = time.getTime().toString();

			nextnpc:
			while (result.next())
			{
				int npcid = result.getInt("boss_id");
				int status = result.getInt("status");
				Long RespawnData = result.getLong("respawn_time");
				if ((npcid == 29066) || (npcid == 29067) || (npcid == 29068) || (npcid == 29118)) {
					continue nextnpc;
				}

				PreparedStatement statement2 = con.prepareStatement("SELECT name FROM npc WHERE id=" + npcid);
				ResultSet result2 = statement2.executeQuery();

				while (result2.next())
				{
					pos++;
					boolean rstatus = false;

					if (status == 0) {
						HtmlBoss += central.LineaDivisora(2) + central.headFormat("Raid: " + result2.getString("name")+"<br1><font color=99FF00>ALIVE</font>","LEVEL") + central.LineaDivisora(2);
					}else{
						time.setTimeInMillis(RespawnData);
						RespawnHora = time.getTime().toString();
						HtmlBoss += central.LineaDivisora(2) + central.headFormat("Raid: " + result2.getString("name")+"<br1><font color=CC0000>DEAD</font><br1>" + RespawnHora,"LEVEL") + central.LineaDivisora(2);
					}
					String npcname = result2.getString("name");
				}
				result2.close();
				statement2.close();
			}

			result.close();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}
		try{
			con.close();
		}catch(Exception a){
			
		}
		MAIN += HtmlBoss+central.getPieHTML()+"</body></html>";
		return MAIN;
	}
}
