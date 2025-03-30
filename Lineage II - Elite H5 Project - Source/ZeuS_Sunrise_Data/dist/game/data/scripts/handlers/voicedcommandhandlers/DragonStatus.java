package handlers.voicedcommandhandlers;



import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import quests.Q00254_LegendaryTales.Q00254_LegendaryTales;
import quests.Q00254_LegendaryTales.Q00254_LegendaryTales.Bosses;

/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public class DragonStatus implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"7rb",

	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance player, String target)
	{
		QuestState qs = player.getQuestState(Q00254_LegendaryTales.class.getSimpleName());
        if (qs == null)
        {
        	player.sendMessage("LegendaryTales: innactive");
            CreatureSay np = new CreatureSay(0, Say2.TELL, "Legendary Tales", "LegendaryTales: innactive");
            player.sendPacket(np);
            return false;
        }
        QuestState st = player.getQuestState(qs.getQuest().getName());
        int var = st.getInt("raids");

        final NpcHtmlMessage html = new NpcHtmlMessage();
		html.setFile(player, player.getHtmlPrefix(), "config/zeus/htm/" + language.getInstance().getFolder(player) + "/7rb_left.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		boolean EMERALD_HORN = false, DUST_RIDER = false, BLEEDING_FLY = false, BLACK_DAGGER = false, SHADOW_SUMMONER = false, SPIKE_SLASHER = false, MUSCLE_BOMBER = false;

		EMERALD_HORN = checkMask(st, Bosses.EMERALD_HORN);
		DUST_RIDER = checkMask(st, Bosses.DUST_RIDER);
		BLEEDING_FLY = checkMask(st, Bosses.BLEEDING_FLY);
		BLACK_DAGGER = checkMask(st, Bosses.BLACK_DAGGER);
		SHADOW_SUMMONER = checkMask(st, Bosses.SHADOW_SUMMONER);
		SPIKE_SLASHER = checkMask(st, Bosses.SPIKE_SLASHER);
		MUSCLE_BOMBER = checkMask(st, Bosses.MUSCLE_BOMBER);
		
		
		html.replace("%EMERALD_HORN%", EMERALD_HORN ? "B40404" : "5FB404");
		html.replace("%DUST_RIDER%", DUST_RIDER ? "B40404" : "5FB404");
		html.replace("%BLEEDING_FLY%", BLEEDING_FLY ? "B40404" : "5FB404");
		html.replace("%BLACK_DAGGER%", BLACK_DAGGER ? "B40404" : "5FB404");
		html.replace("%SHADOW_SUMMONER%", SHADOW_SUMMONER ? "B40404" : "5FB404");
		html.replace("%SPIKE_SLASHER%", SPIKE_SLASHER ? "B40404" : "5FB404");
		html.replace("%MUSCLE_BOMBER%", MUSCLE_BOMBER ? "B40404" : "5FB404");
		player.sendPacket(html);
		
        return true;
	}
	
	private static boolean checkMask(QuestState qs, Bosses boss)
	{
		int pos = boss.getMask();
		return ((qs.getInt("raids") & pos) == pos);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}