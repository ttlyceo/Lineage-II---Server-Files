/*
 * Copyright (C) 2004-2013 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ZeuS.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import l2r.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author zairus
 */
public class httpResp
{
	private static Logger _log = Logger.getLogger(httpResp.class.getName());

	public static boolean isConect = true;
	private static int VOTOSHOPZONE = 0;
	private static int VOTOSTOPZONE = 0;

	public static int getTopZoneVote(){
		return VOTOSTOPZONE;
	}
	public static int getHopZoneVote(){
		return VOTOSHOPZONE;
	}
	
	private static int getIntVoteTopZone(){
		int retorno = -1;
		
		try{
			
			String topzoneUrl = general.WEB_TOP_ZONE_SERVER;
			
			if(!topzoneUrl.endsWith(".htm")){
				topzoneUrl+=".html";
			}else if(topzoneUrl.endsWith(".htm")){
				topzoneUrl+="l";
			}			
			
	        InputStreamReader isr = null;
	        BufferedReader br = null;
            URLConnection con = new URL(topzoneUrl).openConnection();
            con.addRequestProperty("User-Agent", "L2TopZone");
            isr = new InputStreamReader(con.getInputStream());
            br = new BufferedReader(isr);
            boolean got = false;
            String line;
            String SearchText = "fa fa-fw fa-lg fa-thumbs-up";
            while ((line = br.readLine()) != null)
            {
                if (line.indexOf(SearchText)>=0 && !got)
                {
                	int Start = line.indexOf(SearchText) + SearchText.length();
                	int Stop = Start + 80;
                	String Fragment = line.substring(Start, Stop).replace("</i>", "").split(">")[1].replace("</span", "").trim();
                    got = true;
                    retorno = Integer.valueOf(Fragment);
                }
            }
            if(got){
            	return retorno;
            }
		}catch(Exception e){
			_log.warning("Error getting topzone votes->" + e.getMessage());
		}
		return retorno;
	}
			
	private static int getIntVoteHopZone(){
		int retorno = -1;
		try
		{
			InputStreamReader isr = null;
			BufferedReader br = null;
			
			String hopzoneUrl = general.WEB_HOP_ZONE_SERVER;
			
			if(!hopzoneUrl.endsWith(".htm")){
				hopzoneUrl+=".html";
			}else if(hopzoneUrl.endsWith(".htm")){
				hopzoneUrl+="l";
			}
			
			boolean got = false;
			
			if(!hopzoneUrl.endsWith(".html"))
				hopzoneUrl+=".html";
			
			URLConnection con = new URL(hopzoneUrl).openConnection();
			con.addRequestProperty("User-L2Hopzone", "Mozilla/4.76");
			isr = new InputStreamReader(con.getInputStream());
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null){
				if (line.indexOf("Total Votes")>=0 || line.indexOf("no steal make love")>=0 || line.indexOf("no votes here")>=0 || line.indexOf("bang, you don't have votes")>=0 || line.indexOf("la vita e bella")>= 0 ){
					String Parte[] = line.split(">");
					String Voto = Parte[2].replaceAll("</span", "");
					retorno = Integer.valueOf(Voto);
				}
			}
			br.close();
			isr.close();			
			if(got){
				return retorno;
			}
		}
		catch (Exception e)
		{
			_log.warning("Error getting hopzone votes->" + e.getMessage());
		}
		return retorno;		
	}
				
	
	

	public static int getVoteTopZoneNow(){
		return getIntVoteTopZone();
	}	
	
	
	public static boolean haveVoteTopZone(){
		if(!general.VOTO_REWARD_ACTIVO_TOPZONE){
			VOTOSTOPZONE = -1;
			return false;
		}
		VOTOSTOPZONE = getIntVoteTopZone();
		return VOTOSTOPZONE >=0 ? true : false;
	}
	
	public static int getHopZoneVoteNow(){
		return getIntVoteHopZone();
	}

	public static boolean haveVoteHopZone(){
		
		if(!general.VOTO_REWARD_ACTIVO_HOPZONE){
			VOTOSHOPZONE = -1;
			return false;
		}
		
		VOTOSHOPZONE = getIntVoteHopZone();
		return VOTOSHOPZONE>=0 ? true :  false;		
	}

	public static boolean _sendEmailDonation(L2PcInstance player, String IDDonacionFromBD){
		int idServer = opera.getServerID(player);
		String WebPage = general.GET_NAME_VAR_DIR_WEB + "?" + general.GET_NAME_VAR_TYPE + "=SEND_EMAIL_DONATION&"+ general.GET_NAME_VAR_IDDONACION + "=" + IDDonacionFromBD + "&" + general.GET_NAME_VAR_SERVER_ID + "=" + String.valueOf(idServer);
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning(WebPage);
		}
		try
		{
			URL oracle = new URL(WebPage);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String Respuesta = "";
			while ((inputLine = in.readLine()) != null)
			{
				Respuesta += inputLine.trim();
			}
			in.close();
			Respuesta = Respuesta.trim();
			if(Respuesta.indexOf("COR;DONATION")>0){
				return true;
			}
		}
		catch (IOException e)
		{
			_log.warning("httpResponce error: " + e + "  Web:" + WebPage);
			return false;
		}
		return true;
	}

	public static boolean sendCodeForRegistration(String Email, String Code, L2PcInstance player){
		central.msgbox(language.getInstance().getMsg(player).SENDING_EMAIL_TO.replace("$email", Email), player);
		jMail.sendRegisterWindows(player, Email, Code);
		return true;
	}
	
	public static boolean sendCodeForChangePassword(String Email, String Code, L2PcInstance player){
		central.msgbox(language.getInstance().getMsg(player).SENDING_EMAIL_TO.replace("$email", Email), player);		
		jMail.sendCodeForChangePasswordWindows(player, Email, Code);
		return true;
	}	

	public static final boolean SendToWeb(String WebPage)
	{
		try
		{
			URL oracle = new URL(WebPage);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String Respuesta = "";
			while ((inputLine = in.readLine()) != null)
			{
				Respuesta += inputLine.trim();
			}
			in.close();
			Respuesta = Respuesta.trim();
			return Boolean.valueOf(Respuesta);
		}
		catch (IOException e)
		{
			_log.warning("httpResponce error: " + e + "  Web:" + WebPage);
		}
		return false;
	}

	public static httpResp getInstance()
	{
	    return SingletonHolder._instance;
	}


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
	    protected static final httpResp _instance = new httpResp();
	}
}
