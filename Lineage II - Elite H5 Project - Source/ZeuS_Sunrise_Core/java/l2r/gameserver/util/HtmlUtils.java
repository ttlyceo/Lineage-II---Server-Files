package l2r.gameserver.util;

import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.SystemMessageId;

/**
 * @author VISTALL
 * @date 17:17/21.04.2011
 */
public class HtmlUtils
{
	public static final String PREV_BUTTON = "<button value=\"&$1037;\" action=\"bypass %prev_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	public static final String NEXT_BUTTON = "<button value=\"&$1038;\" action=\"bypass %next_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	
	public static String htmlResidenceName(int id)
	{
		return "&%" + id + ";";
	}
	
	public static String htmlNpcName(int npcId)
	{
		return "&@" + npcId + ";";
	}
	
	public static String htmlSysString(SystemMessageId sysString)
	{
		return htmlSysString(sysString.getId());
	}
	
	public static String htmlSysString(int id)
	{
		return "&$" + id + ";";
	}
	
	public static String htmlItemName(int itemId)
	{
		return "&#" + itemId + ";";
	}
	
	public static String htmlClassName(int classId)
	{
		return "<ClassId>" + classId + "</ClassId>";
	}
	
	public static String htmlNpcString(NpcStringId id, Object... params)
	{
		return htmlNpcString(id.getId(), params);
	}
	
	public static String htmlNpcString(int id, Object... params)
	{
		String replace = "<fstring";
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; i++)
			{
				replace += " p" + (i + 1) + "=\"" + String.valueOf(params[i]) + "\"";
			}
		}
		replace += ">" + id + "</fstring>";
		return replace;
	}
	
	public static String htmlButton(String value, String action, int width)
	{
		return htmlButton(value, action, width, 22);
	}
	
	public static String htmlButton(String value, String action, int width, int height)
	{
		return String.format("<button value=\"%s\" action=\"%s\" back=\"L2UI_CT1.Button_DF_Small_Down\" width=%d height=%d fore=\"L2UI_CT1.Button_DF_Small\">", value, action, width, height);
	}

    public static String getHpGauge(int width, long current, long max, boolean displayAsPercentage)
    {
        return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_HP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_HP_Center", 17, -13);
    }
    public static String getMpGauge(int width, long current, long max, boolean displayAsPercentage)
    {
        return getGauge(width, current, max, displayAsPercentage, "L2UI_CT1.Gauges.Gauge_DF_Large_MP_bg_Center", "L2UI_CT1.Gauges.Gauge_DF_Large_MP_Center", 17, -13);
    }

    private static String getGauge(int width, long current, long max, boolean displayAsPercentage, String backgroundImage, String image, long imageHeight, long top)
    {
        current = Math.min(current, max);
        final StringBuilder sb = new StringBuilder();
        sb.append("<table width=" + String.valueOf(width) + " cellpadding=0 cellspacing=0><tr><td background=\"" + backgroundImage + "\">");
        sb.append("<img src=\"" + image + "\" width=" + String.valueOf((long) (((double) current / max) * width)) + " height=" + String.valueOf(imageHeight) + ">");
        sb.append("</td></tr><tr><td align=center><table cellpadding=0 cellspacing=" + String.valueOf(top) + "><tr><td>");
        if (displayAsPercentage)
        {
            sb.append("<table cellpadding=0 cellspacing=2><tr><td>" + String.format("%.2f%%", ((double) current / max) * 100) + "</td></tr></table>");
        }
        else
        {
            final String tdWidth = String.valueOf((width - 10) / 2);
            sb.append("<table cellpadding=0 cellspacing=0><tr><td width=" + tdWidth + " align=right>" + String.valueOf(current) + "</td>");
            sb.append("<td width=10 align=center>/</td><td width=" + tdWidth + ">" + String.valueOf(max) + "</td></tr></table>");
        }
        sb.append("</td></tr></table></td></tr></table>");
        return sb.toString();
    }
}
