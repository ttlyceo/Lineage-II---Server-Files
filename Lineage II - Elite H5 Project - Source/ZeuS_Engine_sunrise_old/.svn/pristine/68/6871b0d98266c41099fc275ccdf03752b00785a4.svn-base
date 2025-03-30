package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config._buffStoreDataSellInfo;
import ZeuS.Config._buffStoreSkillInfo;
import ZeuS.Config.general;
import ZeuS.Config.enum_skill_path_name;
import ZeuS.ZeuS.ZeuS;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.LoginServerThread;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.PrivateStoreType;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.holders.SkillHolder;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.model.skills.L2SkillType;
import l2r.gameserver.model.skills.targets.L2TargetType;
import l2r.gameserver.network.L2GameClient;
import l2r.gameserver.network.L2GameClient.GameClientState;
import l2r.gameserver.network.serverpackets.ExPrivateStoreSetWholeMsg;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SkillCoolTime;

public class sellBuff {
	
	private static final Logger _log = Logger.getLogger(sellBuff.class.getName());
	
	private static Vector<Integer>BuffBlock = new Vector<Integer>();
	
	private static final String ColorVendedor = "AEB404";
	
	private static String INSERT_ZEUS_BUFFSTORE = "INSERT INTO zeus_buffstore VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static boolean IS_IN_LOAD;
	
	private static final int MAX_SCHEME = 4;
	private static final int MAX_BUFF_SCHEME = 10;
	private static final int MAX_BUFF_SEARCH_LIST = 15;
	private static final int MAX_PLAYER_SEARCH_LIST = 5;
	private static final String []BACK_GROUND_COLOR_FOR_ROWS = {"13270C","0E180B"};
	
	private static Map<Integer, _charbufferStore> CHAR_BUFFSTORE = new HashMap<Integer, _charbufferStore>();
	private static Map<Integer, _buffStoreSkillInfo> BUFFSTORE_FOR_SEARCH = new HashMap<Integer, _buffStoreSkillInfo>();
	private static Map<String, Integer> ACCOUNT_TO_CHAR_ID = new HashMap<String, Integer>();
	
	private static Map<Integer, HashMap<Integer, HashMap<String, buffStore_Scheme>>> SCHEME_PLAYERS = new HashMap<Integer, HashMap<Integer, HashMap<String, buffStore_Scheme>>>(); 
	private static Map<Integer, Vector<Integer>> SCHEME_PLAYERS_LOAD = new HashMap<Integer, Vector<Integer>>();
	private static Map<Integer, String> SCHEME_PLAYER_TEMP = new HashMap<Integer, String>();
	
	private static Map<Integer, _buffSearchVariable> PLAYER_VARIABLE = new HashMap<Integer, _buffSearchVariable>();
	
	public static void setBuffstoreSearch(L2PcInstance player, int idBuff, int levelBuff, int item, long price){
		if(BUFFSTORE_FOR_SEARCH != null){
			if(!BUFFSTORE_FOR_SEARCH.containsKey(idBuff)){
				_buffStoreSkillInfo temp = new _buffStoreSkillInfo(player, idBuff, item, price, levelBuff);
				BUFFSTORE_FOR_SEARCH.put(idBuff, temp);
			}else{
				BUFFSTORE_FOR_SEARCH.get(idBuff).setData(player, idBuff, item, price, levelBuff);
			}
		}else{
			_buffStoreSkillInfo temp = new _buffStoreSkillInfo(player, idBuff, item, price, levelBuff);
			BUFFSTORE_FOR_SEARCH.put(idBuff, temp);			
		}
	}
	
	private static void checkVariable(int idPlayer){
		if(PLAYER_VARIABLE==null){
			_buffSearchVariable temp = new _buffSearchVariable();
			PLAYER_VARIABLE.put(idPlayer, temp);
		}else if(PLAYER_VARIABLE.size()==0){
			_buffSearchVariable temp = new _buffSearchVariable();
			PLAYER_VARIABLE.put(idPlayer, temp);			
		}else if(!PLAYER_VARIABLE.containsKey(idPlayer)){
			_buffSearchVariable temp = new _buffSearchVariable();
			PLAYER_VARIABLE.put(idPlayer, temp);			
		}
	}
	
	public static void setBuffstoreToTeleport(L2PcInstance playerToGo, L2PcInstance playerSeller){
		checkVariable(playerToGo.getObjectId());
		PLAYER_VARIABLE.get(playerToGo.getObjectId()).setLocationFromSeller(playerSeller.getLocation());
	}
	
	public static void setBuffForSearch(L2PcInstance player, int idBuff){
		checkVariable(player.getObjectId());
		PLAYER_VARIABLE.get(player.getObjectId()).setBuffSearching(idBuff);
		searchBuffSystem(player);
	}
	
	public static void setBuffForSearchPagine(L2PcInstance player, int pagine, boolean show){
		checkVariable(player.getObjectId());
		PLAYER_VARIABLE.get(player.getObjectId()).setPagBuffList(pagine);
		if(show){
			searchBuffSystem(player);
		}
	}
	public static void setBuffForSearchPaginePlayer(L2PcInstance player, int pagine, boolean show){
		checkVariable(player.getObjectId());
		PLAYER_VARIABLE.get(player.getObjectId()).setPagPlayerList(pagine);
		if(show){
			searchBuffSystem(player);
		}
	}
	
	public static void setChangeOrder(L2PcInstance player, String NewOrder){
		checkVariable(player.getObjectId());
		_buffSearchVariable temp = PLAYER_VARIABLE.get(player.getObjectId());
		temp.setLastSort(NewOrder);
		setBuffForSearchPaginePlayer(player,0,true);
	}

	public static void setItemFilter(L2PcInstance player, String itemName){
		checkVariable(player.getObjectId());
		PLAYER_VARIABLE.get(player.getObjectId()).setPriceItemSort(itemName);
		searchBuffSystem(player);
	}
	
	@SuppressWarnings("rawtypes")
	public static void searchBuffSystem(L2PcInstance player){
		checkVariable(player.getObjectId());		
		int pagina = 0;
		int paginaPlayer = 0;
		_buffSearchVariable VariablePlayer = PLAYER_VARIABLE.get(player.getObjectId()); 
		pagina = VariablePlayer.getPagBuffList();
		paginaPlayer = VariablePlayer.getPagPlayerList();
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffstore-search.htm");
		Iterator itr = BUFFSTORE_FOR_SEARCH.entrySet().iterator();
		String BuffForSearchName = "";
		int BuffForSearch = -1;
		boolean haveNext = false;
		try{
			BuffForSearch = VariablePlayer.getBuffSearching();
			BuffForSearchName = opera.getSkillName(BuffForSearch, 1);
		}catch(Exception a){
			BuffForSearch = -1;
			BuffForSearchName = "";
		}
		
		int MaximBuff = MAX_BUFF_SEARCH_LIST;  //20;
		
		int desde = pagina * MaximBuff;
		int hasta = desde + MaximBuff;
		int cont = 0;
		boolean _endWhile = false;
		String GrillBuff = "";
		while(itr.hasNext() && !_endWhile){
			Map.Entry Entrada = (Map.Entry)itr.next();
			if(cont >= desde && cont < hasta ){
				if((cont%5) == 0){
					if(GrillBuff.length()==0){
						GrillBuff = "<tr>";
					}
					GrillBuff += "</tr><tr>";
				}
				int ID_BUFF = (int)Entrada.getKey();
				String ByPass = "bypass ZeuS buffstoreBuffSearch setIdBuff %ID_BUFF%";
				String icon = "Icon.skill"+opera.getIconSkill(ID_BUFF);
				String ImgIconLink = cbFormato.getBotonForm(icon,ByPass.replace("%ID_BUFF%", String.valueOf(ID_BUFF)));
				GrillBuff += "<td fixwidth=40 align=CENTER height=45>"+ ImgIconLink +"</td>";
			}else if(cont >= hasta){
				_endWhile = true;
				haveNext = true;
			}
			cont++;
		}
		
		if(cont < hasta){
			String icon = ItemData.getInstance().getTemplate(3883).getIcon();
			String ImgIconLink = cbFormato.getBotonForm(icon,"");			
			while(cont < hasta){
				if((cont%5) == 0){
					if(GrillBuff.length()==0){
						GrillBuff = "<tr>";
					}
					GrillBuff += "</tr><tr>";
				}				
				GrillBuff += "<td fixwidth=40 align=CENTER height=45>"+ ImgIconLink +"</td>";
				cont++;
			}
		}
		GrillBuff += "</tr>";
		
		String bypassChangePagg = "bypass ZeuS buffstoreBuffSearch changepag %PAGINE%";
		
		String btnAntes = "<button  action=\""+ bypassChangePagg.replace("%PAGINE%", String.valueOf(pagina - 1 )) +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
		String btnProx = "<button  action=\""+ bypassChangePagg.replace("%PAGINE%", String.valueOf(pagina + 1 )) +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
		
		String BuffPageSystem = "<table width=200 border=0 cellpadding=1><tr>"+
           "<td fixwidth=50 align=RIGHT>"+ (pagina > 0 ? btnAntes : "") +"</td>"+
           "<td fixwidth=100 align=CENTER><font color=04B4AE name=hs12>"+ String.valueOf(pagina + 1) +"</font></td>"+
           "<td fixwidth=50 align=LEFT>"+ (haveNext ? btnProx : "") +"</td></tr></table>";		
		
		String getAllPlayerSeller = "";
		String PlayerControls = "";
		if(BuffForSearch>0){
			if(BUFFSTORE_FOR_SEARCH!=null){
				if(BUFFSTORE_FOR_SEARCH.size()>0){
					if(BUFFSTORE_FOR_SEARCH.containsKey(BuffForSearch)){
						Map<Integer, _buffStoreDataSellInfo> DataOrder = null;
						_buffStoreSkillInfo tempPlayer = BUFFSTORE_FOR_SEARCH.get(BuffForSearch);
						switch (VariablePlayer.getLastSort()) {
							case "NameAZ":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> e1.getValue().getPlayerName() .compareTo(e2.getValue().getPlayerName()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}
								break;
							case "NameZA":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> e2.getValue().getPlayerName() .compareTo(e1.getValue().getPlayerName()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
							case "EnchantAZ":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Integer.compare(e1.getValue().getBuffEnchant(), e2.getValue().getBuffEnchant()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}																
								break;
							case "EnchantZA":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Integer.compare(e2.getValue().getBuffEnchant(), e1.getValue().getBuffEnchant()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
							case "ItemAZ":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Integer.compare(e1.getValue().getIdItem(), e2.getValue().getIdItem()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
							case "ItemZA":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Integer.compare(e2.getValue().getIdItem(), e1.getValue().getIdItem()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
							case "PriceAZ":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Long.compare(e1.getValue().getPrice(), e2.getValue().getPrice()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
							case "PriceZA":
								try{
									Map<Integer, _buffStoreDataSellInfo> newMapSortedByKey = tempPlayer.getAllPlayer().entrySet().stream()
							                .sorted((e1,e2) -> Long.compare(e2.getValue().getPrice(), e1.getValue().getPrice()))
							                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));									
									DataOrder = newMapSortedByKey;
								}catch(Exception a){
											
								}								
								break;
						}						
						int MaximoPourGrid = MAX_PLAYER_SEARCH_LIST;
						int Desde_Player = MaximoPourGrid * paginaPlayer;
						int Hasta_Player = Desde_Player + MaximoPourGrid;
						int Cont_player = 0;
						boolean HaveNextPlayer = false;
						String ByPassGo = "bypass ZeuS buffstoreBuffSearch gotoplayerAsk %IDPLAYER%";
						String GoButton = "<button action=\""+ ByPassGo +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
						String RowF ="<br><table width=530 cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%>"+
						"<tr>"+
						"<td FIXWIDTH=172 align=LEFT><font color=LEVEL>%NAME%<br1>%CLASS_NAME%%CLAN_NAME%</font></td>"+
						"<td FIXWIDTH=112 align=LEFT><font color=LEVEL>%ENCHANT% %BUFF_PATH% </font></td>"+
						"<td FIXWIDTH=112 align=LEFT><font color=LEVEL>%ITEM_PRICE%</font></td>"+
						"<td FIXWIDTH=110 align=LEFT><font color=LEVEL>%PRICE%</font></td>"+
						"<td FIXWIDTH=22 align=LEFT>"+ GoButton +"</td></tr></table>";					
						Iterator itr2 = DataOrder.entrySet().iterator(); //temp.getAllPlayer().entrySet().iterator();
						int IdItemFilter = -1;
						if(!VariablePlayer.GetPriceItemSort().equals("ALL")){
							IdItemFilter = getRequestItemID(VariablePlayer.GetPriceItemSort());
							_log.warning("Id A Filtrar = " + IdItemFilter + "("+ VariablePlayer.GetPriceItemSort() +")");
						}
						while(itr2.hasNext()){
							Map.Entry Entrada2 = (Map.Entry)itr2.next();
							_buffStoreDataSellInfo sellData = (_buffStoreDataSellInfo)Entrada2.getValue();							
							if(IdItemFilter == -1 || ( IdItemFilter == sellData.getIdItem())){
								if(Cont_player >= Desde_Player && Cont_player < Hasta_Player){
									int idPlayer = (int)Entrada2.getKey();
									String ClanName = "";
									if(sellData.getPlayerClanName()!=null){
										if(sellData.getPlayerClanName().length()>0){
											ClanName = " - ("+ sellData.getPlayerClanName() +")";
										}
									}
									getAllPlayerSeller+= RowF.replace("%BGCOLOR%", BACK_GROUND_COLOR_FOR_ROWS[Cont_player % 2]) .replace("%CLAN_NAME%", ClanName).replace("%CLASS_NAME%", sellData.getPlayerClassName()).replace("%IDPLAYER%", String.valueOf(idPlayer)).replace("%BUTTON_GO%", "->") .replace("%PRICE%", opera.getFormatNumbers(String.valueOf(sellData.getPrice() )) ) .replace("%ITEM_PRICE%", central.getNombreITEMbyID(sellData.getIdItem()))  .replace("%BUFF_PATH%", sellData.getBuffPath() ) .replace("%ENCHANT%", String.valueOf(sellData.getBuffEnchant())) .replace("%NAME%",sellData.getPlayerName());						
								}else if(Cont_player > Hasta_Player){
									HaveNextPlayer = true;
									break;
								}
								Cont_player++;
							}
						}

						String bypassChangePaggPlayer = "bypass ZeuS buffstoreBuffSearch changepagPlayer %PAGINE%";
						
						String btnAntesPlayer = "<button  action=\""+ bypassChangePaggPlayer.replace("%PAGINE%", String.valueOf(paginaPlayer - 1 )) +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
						String btnProxPlayer = "<button  action=\""+ bypassChangePaggPlayer.replace("%PAGINE%", String.valueOf(paginaPlayer + 1 )) +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
						
						PlayerControls = "<table width=200 border=0 cellpadding=1><tr>"+
						           "<td fixwidth=50 align=RIGHT>"+ (paginaPlayer > 0 ? btnAntesPlayer : "") +"</td>"+
						           "<td fixwidth=100 align=CENTER><font color=04B4AE name=hs12>"+ String.valueOf(paginaPlayer + 1) +"</font></td>"+
						           "<td fixwidth=50 align=LEFT>"+ (HaveNextPlayer ? btnProxPlayer : "") +"</td></tr></table>";
					}
				}
			}
		}
		
		String UsingSort = VariablePlayer.getLastSort();
		String ByPassNameLink = (UsingSort.equals("NameAZ") ? "bypass ZeuS buffstoreBuffSearch order NameZA" : "bypass ZeuS buffstoreBuffSearch order NameAZ");
		String ByPassEnchant = (UsingSort.equals("EnchantAZ") ? "bypass ZeuS buffstoreBuffSearch order EnchantZA" : "bypass ZeuS buffstoreBuffSearch order EnchantAZ");
		String ByPassItem = (UsingSort.equals("ItemAZ") ? "bypass ZeuS buffstoreBuffSearch order ItemZA" : "bypass ZeuS buffstoreBuffSearch order ItemAZ");
		String ByPassPrice = (UsingSort.equals("PriceAZ") ? "bypass ZeuS buffstoreBuffSearch order PriceZA" : "bypass ZeuS buffstoreBuffSearch order PriceAZ");
		
		String ByPassForSelectItem = "bypass ZeuS buffstoreBuffSearch setitemSearch $cmbItem";
		
		html.replace("%CHANGE_ITEM_PRICE_FOR_SEARCH%", ByPassForSelectItem);
		html.replace("%BUFF_LIST%", GrillBuff);
		html.replace("%SELECTED_BUFF%", BuffForSearchName);
		html.replace("%ITEM_LIST%", getItemForRequest( VariablePlayer.GetPriceItemSort() , "ALL" ) );
		html.replace("%TABLE_BUFF_LIST%", BuffPageSystem);
		html.replace("%SELLER_PLAYERS%", getAllPlayerSeller);
		html.replace("%PLAYERS_CONTROLS%", PlayerControls);
		html.replace("%BYPASS_NAME_LINK%", ByPassNameLink);
		html.replace("%BYPASS_ENCHANT%", ByPassEnchant);
		html.replace("%BYPASS_ITEM_PRICE%", ByPassItem);
		html.replace("%BYPASS_PRICE%", ByPassPrice);
		cbManager.separateAndSend(html.getHtml(), player);
	}
	
	public static void sendBuyerToSeller(L2PcInstance playerBuyer){
		playerBuyer.teleToLocation( PLAYER_VARIABLE.get(playerBuyer.getObjectId()).getLocationFromSeller());
		cbFormato.cerrarCB(playerBuyer);
	}
	
	public static void SetSelectedCharBuffer(int a, L2PcInstance b){
		checkVariable(a);
		PLAYER_VARIABLE.get(a).setSelectedBufferSeller(b);		
	}
	
	public static void checkAccountForBuffStore(L2PcInstance player){
		if(IS_IN_LOAD){
			return;
		}
		if(ACCOUNT_TO_CHAR_ID != null){
			if(ACCOUNT_TO_CHAR_ID.size()>0){
				if(ACCOUNT_TO_CHAR_ID.containsKey(player.getAccountName())){
					int idObject = ACCOUNT_TO_CHAR_ID.get(player.getAccountName());
					CHAR_BUFFSTORE.get(idObject).removeMe();
					removeBDInfo(idObject);
					ACCOUNT_TO_CHAR_ID.remove(player.getAccountName());
				}
			}
		}
	}
	
	private static void loadSchemeFromPlayer(L2PcInstance pplComprador, L2PcInstance pplVendedor){
		boolean cargar = true;
		if(SCHEME_PLAYERS_LOAD!=null){
			if(SCHEME_PLAYERS_LOAD.containsKey(pplComprador.getObjectId())){
				if(SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId())!=null ){
					if(SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId()).contains(pplVendedor.getObjectId())){
						cargar = false;
					}
				}
			}
		}
		
		if(cargar){
			String Consulta = "SELECT id,scheme_name FROM zeus_buffstore_scheme WHERE idplayer=? AND id_ppl_seller=?";
			if(SCHEME_PLAYERS_LOAD==null){
				SCHEME_PLAYERS_LOAD.put(pplComprador.getObjectId(), new Vector<Integer>());
				SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId()).add(pplVendedor.getObjectId());
			}else if(!SCHEME_PLAYERS_LOAD.containsKey(pplComprador.getObjectId())){
				SCHEME_PLAYERS_LOAD.put(pplComprador.getObjectId(), new Vector<Integer>());
				SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId()).add(pplVendedor.getObjectId());
			}else if(!SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId()).contains(pplVendedor.getObjectId()) ){
				SCHEME_PLAYERS_LOAD.get(pplComprador.getObjectId()).add(pplVendedor.getObjectId());
			}
			
			try{
				Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement psqry = conn.prepareStatement(Consulta);
				psqry.setInt(1, pplComprador.getObjectId());
				psqry.setInt(2, pplVendedor.getObjectId());
				ResultSet rss = psqry.executeQuery();
					while (rss.next()){
						try{
							buffStore_Scheme T1 = new buffStore_Scheme(pplVendedor, pplComprador, rss.getString(2) , false);
							T1.loadFromBD(rss.getInt(1));
							
							if(SCHEME_PLAYERS==null){
								SCHEME_PLAYERS.put(pplComprador.getObjectId() ,new HashMap<Integer, HashMap<String, buffStore_Scheme>>());
							}else if(!SCHEME_PLAYERS.containsKey(pplComprador.getObjectId()) ){
								SCHEME_PLAYERS.put(pplComprador.getObjectId() ,new HashMap<Integer, HashMap<String, buffStore_Scheme>>());								
							}
							
							if(SCHEME_PLAYERS.get(pplComprador.getObjectId())==null){
								SCHEME_PLAYERS.get(pplComprador.getObjectId()).put(pplVendedor.getObjectId(), new HashMap<String, buffStore_Scheme>());								
							}else if(!SCHEME_PLAYERS.get(pplComprador.getObjectId()).containsKey(pplVendedor.getObjectId())){
								SCHEME_PLAYERS.get(pplComprador.getObjectId()).put(pplVendedor.getObjectId(), new HashMap<String, buffStore_Scheme>());
							}
							
							SCHEME_PLAYERS.get(pplComprador.getObjectId()).get(pplVendedor.getObjectId()).put(rss.getString(2), T1);
						}catch(Exception e){
							_log.warning("Error loading ZeuS Buffer Scheme Player -> " + e.getMessage());
						}
					}
				conn.close();
			}catch(SQLException e){

			}			
			//TODO Aqui			
		}
		
	}
	
	public static int getNameColorBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return Integer.valueOf(CHAR_BUFFSTORE.get(player.getObjectId()).getOriginalColorName());
			}
		}catch(Exception a){
			
		}
		return player.getAppearance().getNameColor();
	}
	
	public static String getTitleBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return CHAR_BUFFSTORE.get(player.getObjectId()).getOriginalTitle();
			}
		}catch(Exception a){
			
		}
		
		return player.getTitle();
	}
	
	public static int getTitleColorBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return Integer.valueOf(CHAR_BUFFSTORE.get(player.getObjectId()).getOriginalColorTitle());
			}
		}catch(Exception a){
			
		}
		
		return player.getAppearance().getTitleColor();
	}
	
	private static int getRequestItemID(String ItemName){
		if(ItemName.equalsIgnoreCase("adena")){
			return 57;
		}
		
		if(general.BUFFSTORE_ITEMS_REQUEST!=null){
			for(String t : general.BUFFSTORE_ITEMS_REQUEST){
				if(t.split(":")[1].startsWith(ItemName)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}		
		
		return 57;
		
	}
	
	public static void removeBDInfo(int PlayerID){
		if(CHAR_BUFFSTORE!=null){
			if(CHAR_BUFFSTORE.containsKey(PlayerID)){
				CHAR_BUFFSTORE.get(PlayerID).removeMe();
				return;
			}
		}
		
		String DeleteInfo = "DELETE FROM zeus_buffstore WHERE idChar=?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
			{
				statementt.setInt(1, PlayerID);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}		
	}
	
	private static void saveInfoINDB(L2PcInstance player){
		if(!Config.OFFLINE_TRADE_ENABLE){
			return;
		}
		_charbufferStore temp = CHAR_BUFFSTORE.get(player.getObjectId());
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()){
			try (PreparedStatement ps = con.prepareStatement(INSERT_ZEUS_BUFFSTORE))
			{
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, temp.getIdItem());
				ps.setDouble(3, temp.getBuffPrice());
				ps.setString(4, temp.getSellTitle());
				ps.setString(5, String.valueOf(temp.getFreeClan()) );
				ps.setString(6, String.valueOf(temp.getFreeWan()));
				ps.setString(7, String.valueOf(temp.getFreeFriend()));
				ps.setString(8, String.valueOf(temp.getFreeEmail()));
				ps.setInt(9, player.getLocation().getX());
				ps.setInt(10, player.getLocation().getY());
				ps.setInt(11, player.getLocation().getZ());
				ps.setDouble(12, opera.getUnixTimeL2JServer());
				ps.setString(13, ZeuS.getIp_Wan(player));
				ps.execute();
			} catch(Exception a){
				}
		}catch(Exception b){
			
		}
	}
	
	public static void removeFromOffline(L2PcInstance player){
		removeBDInfo(player.getObjectId());
		resetBuffSeller(player);
		try{
			ACCOUNT_TO_CHAR_ID.remove(player.getAccountName());
		}catch(Exception a){
			
		}
	}
	
	public static void restoreOffline(){
		if(!general.ALLOW_BUFFSTORE){
			return;
		}
		IS_IN_LOAD = true;
		if(!Config.OFFLINE_TRADE_ENABLE){
			IS_IN_LOAD = false;
			return;
		}		
		String Consulta = "SELECT * FROM zeus_buffstore";
		int contador = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(Consulta))
			{
			
			while (rs.next())
			{
				
				long time = rs.getLong("time");
				if (Config.OFFLINE_MAX_DAYS > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);
					cal.add(Calendar.DAY_OF_YEAR, Config.OFFLINE_MAX_DAYS);
					if (cal.getTimeInMillis() <= System.currentTimeMillis())
					{
						removeBDInfo(rs.getInt("idChar"));
						continue;

					}
				}
				try{
					L2GameClient client = new L2GameClient(null);				
					L2PcInstance player = null;
					client.setDetached(true);
					player = L2PcInstance.load(rs.getInt("idChar"));
					player.loadRecommendations();
					client.setActiveChar(player);
					player.setOnlineStatus(true, false);
					client.setAccountName(player.getAccountNamePlayer());
					L2World.getInstance().addPlayerToWorld(player);
					client.setState(GameClientState.IN_GAME);
					player.setClient(client);
					player.spawnMe(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
					LoginServerThread.getInstance().addGameServerLogin(player.getAccountName(), client);				
					if (Config.OFFLINE_SET_NAME_COLOR)
					{
						player.getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
					}
					player.setOnlineStatus(true, true);
					player.restoreEffects();
					player.broadcastUserInfo();
					String WAN_IP = rs.getString("ipwan");
					//startBuff $cmbItem $cmbBuffClan $cmbFreeConecc $cmbBuffFriend $txtPrice $txtTitulo
					String cmbItemRequest = central.getNombreITEMbyID(rs.getInt("idRequest")).replace(" ", "_");
					String ByClan = rs.getString("clan").equals("true") ? "Yes" : "No";
					String ByWan = rs.getString("wan").equals("true") ? "Yes" : "No";
					String ByEmail = rs.getString("email").equals("true") ? "Yes" : "No";
					String ByFriend = rs.getString("friend").equals("true") ? "Yes" : "No";
					String ByPrice = String.valueOf(rs.getLong("price")).replace(".0", "");
					String SELLTITLE = rs.getString("title");

					String byPassEnviar = "startBuff "+ cmbItemRequest + " "+ ByClan + " "+ ByWan + " "+ ByFriend +" "+ ByEmail + " " + ByPrice + " " + SELLTITLE;
					showMainWindows(player, false, byPassEnviar, WAN_IP, SELLTITLE);
					contador++;
				}catch(Exception e){
					_log.warning("Error on load player buffstore information ("+ String.valueOf(rs.getInt("idChar")) +"), error->" + e.getMessage());
				}
			}
			
		}catch(Exception a){
			_log.warning("Error loading Buffstore players->" + a.getMessage());
		}
		if(contador>0){
			_log.info("ZeuS -> Loading " + String.valueOf(contador) + " offline buff store");
		}
		
		IS_IN_LOAD = false;
	}
	
	public static boolean IsInLoadProcess(){
		return IS_IN_LOAD;
	}
	
	private static String getItemForRequest(){
		return getItemForRequest("","");
	}	
	
	private static String getItemForRequest(String blockItem, String defaultItemName){
		String retorno = "";
		if(blockItem.trim().length()>0 || defaultItemName.trim().length()>0){
			if(blockItem.equals(defaultItemName)){
				retorno = defaultItemName;
			}else if(blockItem.trim().length()>0 && defaultItemName.trim().length()>0){
				retorno = blockItem + ";" + defaultItemName;
			}
		}
	
		if(general.BUFFSTORE_ITEMS_REQUEST!=null){
			for(String t : general.BUFFSTORE_ITEMS_REQUEST){
				if(retorno.trim().length()>0){
					retorno += ";";
				}
				String NomItem = t.split(":")[1];
				if(blockItem.equals(NomItem)){
					continue;
				}
				retorno += NomItem.length()>15 ? NomItem.substring(0,15) : NomItem;
			}
		}
		
		if(retorno.length()==0){
			retorno = "Adena";
		}
		return retorno;
	}
	
	@SuppressWarnings("unused")
	private static void setBlockbuff(){
		if(BuffBlock!=null){
			BuffBlock.clear();
		}
		BuffBlock.add(970);
		BuffBlock.add(357);
		BuffBlock.add(1323);
		BuffBlock.add(327);
		BuffBlock.add(1325);
		BuffBlock.add(1326);
		BuffBlock.add(1327);
	}
	
public static void removeBuffFromScheme(L2PcInstance player, String EsquemaName, int idBuff){
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(player.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(!isSellBuff(playerVendedor,true)){
				return;
			}
		}
		
		if(SCHEME_PLAYERS!=null){
			if(SCHEME_PLAYERS.containsKey(player.getObjectId())){
				if(SCHEME_PLAYERS.get(player.getObjectId()).containsKey(playerVendedor.getObjectId())){
					Map<String, buffStore_Scheme> T1 = SCHEME_PLAYERS.get(player.getObjectId()).get(playerVendedor.getObjectId()) ;
					if(T1.containsKey(EsquemaName)){
						T1.get(EsquemaName).removeBuffFromScheme(idBuff);
						if(T1.get(EsquemaName).getCountBuff()>0){
							showSellBufferMacroEditBuff(player,EsquemaName);
						}else{
							ShowMainWIndows(player);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void showSellBufferMacroEditBuff(L2PcInstance player, String EsquemaName){
		String ByPass_Remove = "bypass -h ZeuS buffstore RemoveBuff %IDBUFF%";
		checkVariable(player.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(player.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(!isSellBuff(playerVendedor,true)){
				return;
			}
		}
		
		String BuffData = "";
		
		
		if(SCHEME_PLAYERS!=null){
			if(SCHEME_PLAYERS.containsKey(player.getObjectId())){
				if(SCHEME_PLAYERS.get(player.getObjectId()).containsKey(playerVendedor.getObjectId())){
					Map<String, buffStore_Scheme> T1 = SCHEME_PLAYERS.get(player.getObjectId()).get(playerVendedor.getObjectId()) ;
					if(T1.containsKey(EsquemaName)){
						BuffData = T1.get(EsquemaName).getAllBuffEdit();
					}
				}
			}
		}
		
		
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"buffstore_seller_editBuff.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%CHAR_NAME%", playerVendedor.getName() );
		html.replace("%BUFF_TABLE%", BuffData);
		central.sendHtml(player, html.getHtml(), false);		
	}	
			
				
	
	public static void showSellBuffer_main(L2PcInstance playerComprador, L2PcInstance playerVendedor){
		
		if(!playerComprador.isInsideRadius(playerVendedor.getLocation(), general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
			return;
		}
		
		setSchemeTemporal(playerComprador,"");
		String ByPass_Single = "bypass -h ZeuS buffstore S_Single";
		String ByPass_Scheme = "bypass -h ZeuS buffstore S_Scheme_Show";
		
		loadSchemeFromPlayer(playerComprador, playerVendedor);		
		
		final NpcHtmlMessage html = comun.htmlMaker(playerComprador, general.DIR_HTML + "/" + language.getInstance().getFolder(playerComprador) + "/" +"buffstore_seller_main.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(playerComprador));
		html.replace("%CHAR_NAME%", playerVendedor.getName() );
		html.replace("%BYPASS_SINGLE%", ByPass_Single);
		html.replace("%BYPASS_MACRO%", ByPass_Scheme);
		html.replace("%BUYER_MACROS%", getMacroFromBuyer(playerComprador));
		central.sendHtml(playerComprador, html.getHtml(), false);		
	}
	
	private static void showInfoWindows(L2PcInstance player, String ByPass){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"buffstore_main.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%BYPASS%", ByPass);
		central.sendHtml(player, html.getHtml(), false);		
	}
	
	private static void showConfigWindows(L2PcInstance player, String ByPass){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"buffstore_config.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%ITEM_LIST%", getItemForRequest());
		html.replace("%MY_IP%", ZeuS.getIp_Wan(player));
		html.replace("%BYPASS_START%", ByPass);
		central.sendHtml(player, html.getHtml(), false);		
	}
	
	public static void showMainWindows(L2PcInstance player, boolean getMainWindows,String Params){
		showMainWindows(player, getMainWindows, Params, "", "");
	}
	
	public static void showMainWindows(L2PcInstance player, boolean getMainWindows,String Params, String loadWAN, String loadSellTitle){
		checkVariable(player.getObjectId());
		_buffSearchVariable CharVariable = PLAYER_VARIABLE.get(player.getObjectId());		
		if(!player.getClient().isDetached()){
			if(!player.isInsideZone(ZoneIdType.PEACEBUFF) && !player.isInsideZone(ZoneIdType.CLAN_HALL)){
				central.msgbox( language.getInstance().getMsg(player).BUFFER_BUFFSTORE_WROG_ZONE_TO_CREATE_SPOT, player);
				return;
			}
		}
		String ByPassBtn1 = "bypass -h ZeuS buffstoreSet configBuff 0 0 0 0";
		String ByPassBtn2 = "bypass -h ZeuS buffstoreSet startBuff $cmbItem $cmbBuffClan $cmbFreeConecc $cmbBuffFriend $cmbBuffEmail $txtPrice $txtTitulo";
		
		String retorno = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		retorno += central.LineaDivisora(2) + central.headFormat("Buff Store Manager") + central.LineaDivisora(2);
		retorno += "<center><br>";
		
		String Grilla = "";
		
		boolean enviar = false;
		
		if(getMainWindows){
			showInfoWindows(player,ByPassBtn1);
			return;
		}else{
			if(Params.split(" ")[0].equals("configBuff")){
				showConfigWindows(player,ByPassBtn2);
				return;				
			}else if(Params.split(" ")[0].equals("startBuff")){
				boolean canBuffClan = false;
				boolean canBuffIPLan = false;
				boolean canBuffFriend = false;
				boolean canBuffEmail = false;
				String ItemRequest = "";
				String Price = "100";
				String Titulo = "";
				if(Params.split(" ")[0].equals("startBuff")){
					if(Params.split(" ").length<7){
						central.msgbox_Lado(language.getInstance().getMsg(player).NEED_ENTER_THE_REQUESTED_DATA, player, "BUFF STORE");
						showMainWindows(player, false, "configBuff 0 0 0 0");
						return;
					}
					ItemRequest = Params.split(" ")[1];
					canBuffClan = Params.split(" ")[2].equals("Yes");
					canBuffIPLan = Params.split(" ")[3].equals("Yes");
					canBuffFriend = Params.split(" ")[4].equals("Yes");
					canBuffEmail = Params.split(" ")[5].equals("Yes");
					Price = Params.split(" ")[6];
					long BuffPrice = Long.parseLong(Price);
					
					if(BuffPrice <= 0){
						central.msgbox_Lado(language.getInstance().getMsg(player).BUFFER_BUFFSTORE_ENTER_PRICE_CORRECTLY, player, "BUFF STORE");
						showMainWindows(player, false, "configBuff 0 0 0 0");
						return;						
					}
					for(int i=7;i<Params.split(" ").length;i++){
						Titulo += Params.split(" ")[i]+" ";
					}
					int LargoTitulo = Titulo.trim().length();
					Titulo = Titulo.substring(0, (LargoTitulo>=24 ? 23 : LargoTitulo) ).trim();
					
					if(CHAR_BUFFSTORE != null){
						if(CHAR_BUFFSTORE.containsKey(player.getObjectId())){
							CHAR_BUFFSTORE.remove(player.getObjectId());
						}
					}
					
					_charbufferStore temp = new _charbufferStore(player, Titulo, canBuffClan, canBuffIPLan, canBuffFriend, canBuffEmail, getRequestItemID(ItemRequest) , BuffPrice);
					if(loadWAN.length()==0){
						temp.setDataLoad(loadWAN, Titulo);
					}
					CHAR_BUFFSTORE.put(player.getObjectId(), temp);
					ACCOUNT_TO_CHAR_ID.put(player.getAccountName(), player.getObjectId());
					setBuffSell(player,Titulo);
					retorno = "";
				}
			}else if(Params.split(" ")[0].equals("changeToBuff")){
				CharVariable.changeBuffOnPlayer();
				int Pagina = Integer.valueOf(Params.split(" ")[1]);
				L2PcInstance playerBuffer = CharVariable.getSelectedBufferSeller();
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,Pagina);
					//SelectedCharBuffer.put(playerComprador.getObjectId(), player);
				}				
			}else if(Params.split(" ")[0].equals("showBuffer")){
				int Pagina = Integer.valueOf(Params.split(" ")[1]);
				L2PcInstance playerBuffer = CharVariable.getSelectedBufferSeller();
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,Pagina);
					//SelectedCharBuffer.put(playerComprador.getObjectId(), player);
				}				
			}else if(Params.split(" ")[0].equals("GiveBuff")){
				//player.doCast(GIFT_OF_VITALITY.getSkill()); player.doSimultaneousCast(JOY_OF_VITALITY.getSkill());
				int IDSKILL = 0;
				int IDLEVEL = 0;
				int PAGINA = 0;
				IDSKILL = Integer.valueOf(Params.split(" ")[1]);
				IDLEVEL = Integer.valueOf(Params.split(" ")[2]);
				PAGINA = Integer.valueOf(Params.split(" ")[3]);
				
				L2PcInstance playerBuffer = CharVariable.getSelectedBufferSeller();
				giveBuff(IDSKILL,IDLEVEL,playerBuffer,player);
				
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,PAGINA);
				}
				
				return;
			}
		}
		
		retorno += central.LineaDivisora(1) + central.headFormat(Grilla) + central.LineaDivisora(1);
		retorno += central.getPieHTML(false) + "</center></body></html>";
		if(enviar){
			central.sendHtml(player, retorno);
		}
	}
	
	private static boolean AreSameClan(L2PcInstance ppl1, L2PcInstance ppl2){
		if(ppl1.getClan()!=null && ppl2.getClan()!=null){
			if(ppl1.getClan() == ppl2.getClan()){
				return true;
			}
		}
		return false;
	}
	
	private static boolean AreFriend(L2PcInstance ppl1, L2PcInstance ppl2){
		List<Integer> FriendsPpl = ppl1.getFriendList();
		
		for(Integer Amigos : FriendsPpl){
			if(Amigos.equals(ppl2.getObjectId())){
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean isSameEmail(L2PcInstance Vendedor, L2PcInstance Comprador){
		boolean _return = false;
		
		String VendorEmail = opera.getUserMail(Vendedor.getAccountName());
		String CompraEmail = opera.getUserMail(Comprador.getAccountName());
		
		if(VendorEmail!=null && CompraEmail!=null){
			if(VendorEmail.length()>0 && CompraEmail.length()>0){
				if(VendorEmail.equalsIgnoreCase(CompraEmail)){
					return true;
				}
			}
		}
		
		return _return;
	}
	
	private static void giveBuff(int IdSkill, int IdLevel, L2PcInstance Vendedor, L2PcInstance Comprador){
		if(!Comprador.isInsideRadius(Vendedor.getLocation(), general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
			return;
		}	
		
		
		_charbufferStore Info = CHAR_BUFFSTORE.get(Vendedor.getObjectId());
		
		boolean ForFree = false;
		
		boolean AreSameWan = ZeuS.getIp_Wan(Comprador).equalsIgnoreCase(Info.getWAN());
		
		int IDItemToQuest = Info.getIdItem();
		
		if(Info.getFreeClan() && AreSameClan(Vendedor, Comprador)){
			ForFree = true;
		}else if(Info.getFreeWan() && AreSameWan){
			ForFree = true;
		}else if(Info.getFreeFriend() && AreFriend(Vendedor,Comprador)){
			ForFree = true;
		}else if(Info.getFreeEmail() && isSameEmail(Vendedor,Comprador)){
			ForFree = true;
		}
		if(!ForFree){
			if(!opera.haveItem(Comprador,IDItemToQuest, Info.getBuffPrice(),true)){
				return;
			}else{
				opera.removeItem(IDItemToQuest, Info.getBuffPrice(), Comprador);
				opera.giveReward(Vendedor,IDItemToQuest,Info.getBuffPrice());
			}
		}
		
		_buffSearchVariable charVar = PLAYER_VARIABLE.get(Comprador.getObjectId());
		
		if(!charVar.getUseBuffOnPlayer() && Comprador.getSummon()==null){
			central.msgbox(language.getInstance().getMsg(Comprador).BUFFERCHAR_YOU_NOT_HAVE_PET, Comprador);
			charVar.setUseBuffOnPlayer(true);
			return;
		}else if(!charVar.getUseBuffOnPlayer() && Comprador.getSummon().isDead()){
			central.msgbox(language.getInstance().getMsg(Comprador).BUFFERCHAR_YOU_NOT_HAVE_PET, Comprador);
			return;
		}
		
		if(Vendedor.isInOfflineMode()) {
			Vendedor.resetTimeStamps();
			Vendedor.resetDisabledSkills();
			if (Vendedor.isPlayer())
			{
				Vendedor.sendPacket(new SkillCoolTime(Vendedor.getActingPlayer()));
			}
		}		
		
		SkillHolder BUFF_SELECTED_USE = new SkillHolder(IdSkill, IdLevel);
		L2Skill BuffDar = SkillData.getInstance().getInfo(IdSkill, IdLevel);
		if(charVar.getUseBuffOnPlayer()){
			if (Comprador.isAffectedBySkill(IdSkill))
			{
				Comprador.stopSkillEffects(IdSkill);
			}
			Vendedor.setTarget(Comprador);
			SkillData.getInstance().getInfo(IdSkill, IdLevel).getEffects(Vendedor, Comprador);
		}else{
			if (Comprador.getSummon().isAffectedBySkill(IdSkill))
			{
				Comprador.getSummon().stopSkillEffects(IdSkill);
			}			
			Vendedor.setTarget(Comprador.getSummon());
			SkillData.getInstance().getInfo(IdSkill, IdLevel).getEffects(Vendedor, Comprador.getSummon());
		}
		Vendedor.doCast(BuffDar);
		Vendedor.doCast(BUFF_SELECTED_USE.getSkill());


		
	}
	
	private static void resetBuffSeller(L2PcInstance player){
		if(isBuffSeller(player)){
			CHAR_BUFFSTORE.remove(player.getObjectId());
		}
	}
	
	public static boolean isBuffSeller(L2PcInstance player){
		if(player!=null){
			if(CHAR_BUFFSTORE!=null){
				if(CHAR_BUFFSTORE.size()>0){
					if(CHAR_BUFFSTORE.containsKey(player.getObjectId())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void setBuffSell(L2PcInstance player,String Titulo) {
		player.sitDown();
		//player.setTitle(Titulo);
		player.getAppearance().setTitleColor(Integer.decode("0x" + ColorVendedor));
		if(!player.getClient().isDetached()){
			player.getAppearance().setNameColor(Integer.decode("0x" + ColorVendedor));
		}

		player.getSellList().setTitle(Titulo);
		player.setPrivateStoreType(PrivateStoreType.PACKAGE_SELL);
		player.broadcastPacket(new ExPrivateStoreSetWholeMsg(player));
		//player.sendPacket(new PrivateStoreManageListBuy(player));	
		
		player.broadcastInfo();
		player.broadcastUserInfo();
		player.broadcastTitleInfo();
		if(!player.getClient().isDetached()){
			saveInfoINDB(player);
		}
		opera.setBlockSaveInDB(player);
	}
	
	@SuppressWarnings("rawtypes")
	private static void checkAllBuffStoreForSearch(int idPlayer){
		Iterator itr = BUFFSTORE_FOR_SEARCH.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idBuff = (int)Entrada.getKey();
			if(idPlayer>0){
				BUFFSTORE_FOR_SEARCH.get(idBuff).checkIsInBuffStore(idPlayer);
			}else{
				BUFFSTORE_FOR_SEARCH.get(idBuff).checkIsInBuffStore();
			}
		}		
	}
	
	public static void setStopSellBuff(L2PcInstance player){
		if(CHAR_BUFFSTORE!=null){
			if(CHAR_BUFFSTORE.containsKey(player.getObjectId())){
				_charbufferStore info = CHAR_BUFFSTORE.get(player.getObjectId());
				try{
					player.setTitle( info.getOriginalTitle() );
					player.getAppearance().setTitleColor(Integer.decode( info.getOriginalColorTitle() ));
					player.setPrivateStoreType(PrivateStoreType.NONE);
					player.getAppearance().setNameColor(Integer.decode( info.getOriginalColorName()));
					CHAR_BUFFSTORE.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				player.standUp();
				player.broadcastInfo();
				player.broadcastUserInfo();
				opera.setUnBlockSaveInDB(player);	
				removeBDInfo(player.getObjectId());
				checkAllBuffStoreForSearch(player.getObjectId());
				try{
					ACCOUNT_TO_CHAR_ID.remove(player.getAccountName());
				}catch(Exception a){
					
				}				
			}
		}
	}
	
	private static String getEnchantSkill(String enchant, boolean showMessage){
		if(enchant.length()>1){
			return enchant.substring(1, enchant.length()) + "+";
		}
		if(showMessage){
			return "No Have";
		}
		return "";
	}
	
	private static boolean isForUse(L2PcInstance playerComprador){
		try{
			String NameScheme = SCHEME_PLAYER_TEMP.get(playerComprador.getObjectId());
			if(NameScheme==null){
				return true;
			}else if(NameScheme.length()==0){
				return true;
			}else{
				return false;
			}
		}catch(Exception a){
			
		}
		return true;
	}
	
	public static void getBuffForSell(L2PcInstance playerBuffer, L2PcInstance playerCompra, int Pagina){
		
		if(!playerCompra.isInsideRadius(playerBuffer, general.RADIO_PLAYER_NPC_MAXIMO , true, true)){
			return;
		}
		
		boolean isBuffForUse = isForUse(playerCompra);
		
		
		if(playerBuffer==null){
			return;
		}
		
		if(playerBuffer == playerCompra){
			return;
		}
		
		boolean BuffPlayer = PLAYER_VARIABLE.get(playerCompra.getObjectId()).getUseBuffOnPlayer();
		boolean haveSummon = ( playerCompra.getSummon() == null? false : true );
		
		
		if(!BuffPlayer && !haveSummon){
			PLAYER_VARIABLE.get(playerCompra.getObjectId()).setUseBuffOnPlayer(true);
		}
		_charbufferStore info = CHAR_BUFFSTORE.get(playerBuffer.getObjectId());
		
		String btnBuffPlayer = "<button value=\"Buff Char\" width=95 height=20 action=\"bypass -h ZeuS buffstoreSet changeToBuff " + String.valueOf(Pagina) + " 0 0 0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
		String btnBuffSummon = "<button value=\"Buff Summon\" width=95 height=20 action=\"bypass -h ZeuS buffstoreSet changeToBuff " + String.valueOf(Pagina) + " 0 0 0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
		
		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		
		html += central.LineaDivisora(2) + central.headFormat(playerBuffer.getName() + " - Buff Store") + central.LineaDivisora(2);
		html += "<center>";
		
		if(isBuffForUse){
			html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER>"+
	               "Class: <font color=\"DD9B22\">"+ opera.getClassName(playerBuffer.getClassId().getId()) +"</font>  Level: <font color=\"DD9B22\">"+ String.valueOf(playerBuffer.getLevel()) +"</font>"+
	       "</td></tr><tr>"+
	       "<td align=CENTER>"+
	       "Buff Price: <font color=\"CCFFCC\">"+ opera.getFormatNumbers(String.valueOf(info.getBuffPrice()).replace(".0", "")) +"</font><br1>"+
	       "Item Request: <font color=\"CCFFCC\">"+ info.getItemRequestedName() +"</font>"+
	       ( isBuffForUse ? ( haveSummon ?  ( BuffPlayer ? btnBuffPlayer : btnBuffSummon ) + "<br1>"   : "<br1>" ) : "") +
	       "</td></tr></table>";
		}else{
			String NombreScheme = SCHEME_PLAYER_TEMP.get(playerCompra.getObjectId());
			html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER>"+
		               "Scheme Name: <font color=\"DD9B22\">"+ NombreScheme +"</font>"+
		       "</td></tr><tr>"+
		       "<td align=CENTER>"+
		       "Buff Price: <font color=\"CCFFCC\">"+ opera.getFormatNumbers(String.valueOf(info.getBuffPrice()).replace(".0", "")) +"</font><br1>"+
		       "Item Request: <font color=\"CCFFCC\">"+ info.getItemRequestedName() +"</font>"+
		       ( isBuffForUse ? ( haveSummon ?  ( BuffPlayer ? btnBuffPlayer : btnBuffSummon ) + "<br1>"   : "<br1>" ) : "") +
		       "</td></tr></table>";			
		}
		
		int Contador = 0;
		int Maximo = 6;
		
		int Desde = Maximo * Pagina;
		int Hasta = Desde + Maximo;
		
		boolean haveNext=false;

		String ByPassForSaveInScheme = "bypass -h ZeuS buffstore setBuff %IDBUFF% %PAGE%";
		
		Vector<Integer>BuffFromScheme = new Vector<Integer>();
		
		if(!isBuffForUse){
			String NameScheme = SCHEME_PLAYER_TEMP.get(playerCompra.getObjectId());
			try{
				BuffFromScheme = SCHEME_PLAYERS.get(playerCompra.getObjectId()).get(playerBuffer.getObjectId()).get(NameScheme).getBuffs();
			}catch(Exception a){
				
			}
		}
		
	    for(L2Skill p : info.getBuffs()){
	    	String Imagen = "Icon.skill"+ opera.getIconSkill(p.getId());

	    	//"bypass -h ZeuS buffstoreSet GiveBuff %IDBUFF% %IDLEVEL% "
	    	String ByPass = (isBuffForUse ? "bypass -h ZeuS buffstore GiveBuff %IDBUFF% %IDLEVEL% " + String.valueOf(Pagina) : ByPassForSaveInScheme.replace("%PAGE%", String.valueOf(Pagina)));

	    	if(!isBuffForUse){
	    		if(BuffFromScheme!=null){
	    			if(BuffFromScheme.contains(p.getId())){
	    				continue;
	    			}
	    		}
	    	}
	    	
	    	if(!general.BUFF_STORE_BUFFPROHIBITED.contains(p.getId())){
	    		if(Contador>=Desde && Contador < Hasta){
	    			String PathName = "";
	    			try{
	    				PathName = enum_skill_path_name.getPathName(p.getId(), p.getLevel());
	    			}catch(Exception a){
	    				
	    			}
		    		html += "<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><table width=280><tr><td align=CENTER fixwidth=32>"+
	                opera.getBotonForm(Imagen, ByPass.replace("%IDLEVEL%",String.valueOf(p.getLevel())).replace("%IDBUFF%", String.valueOf(p.getId()))) + "</td><td fixwidth=248>"+
	                "<font color=FAAC58>"+p.getName() + " - <font color=936028>Level</font> <font color=A9E2F3>" + String.valueOf(/*p.getLevel()*/p.getAbnormalLvl()) + "</font> <br1>"+
	                "<font color=97F776>Enchant: </font>"+ getEnchantSkill(String.valueOf(p.getLevel()), true) +
	                (PathName.trim().length()>0 && p.getLevel()>100 ? "<br1><font color=97F776>Buff Type: </font>" + PathName : "")+
	                "</td></tr></table>";
		    	}else if(Contador>Hasta){
		    		haveNext = true;
		    	}
		    	Contador++;	    		
	    	}
	    }
	    
	    if(haveNext || Pagina>0){
	    	String ByPass_Prev = "bypass -h ZeuS buffstoreSet showBuffer " + String.valueOf(Pagina - 1) + " 0 0 0";
	    	String ByPass_Next = "bypass -h ZeuS buffstoreSet showBuffer " + String.valueOf(Pagina + 1) + " 0 0 0";
	    	
	    	String BtnPrev = "<button action=\""+ ByPass_Prev +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
	    	String BtnNext = "<button action=\""+ ByPass_Next +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
	    	
	    	String Controles = "<table width=280><tr>"+
            "<td fixwidth=93 align=RIGHT>" + ( Pagina>0 ? BtnPrev : "" ) +"</td><td width=93 align=CENTER >"+
            "<font name=\"hs12\" color=76F7D1>"+ String.valueOf(Pagina + 1) +"</font></td>"+
            "<td fixwidth=93 align=LEFT>" + (haveNext ? BtnNext : "") + "</td></tr></table>";
	    	html += Controles;
	    }
	    
	    html += central.getPieHTML(false) + "</center></body></html>";
		central.sendHtml(playerCompra, html);
	}
	
	public static void setBuffToScheme(L2PcInstance playerComprador, int IdBuff, int Pagina){
		checkVariable(playerComprador.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				String NameScheme = SCHEME_PLAYER_TEMP.get(playerComprador.getObjectId());
				SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).get(NameScheme).setBuff(IdBuff, true);
				getBuffForSell(playerVendedor,playerComprador,Pagina);
			}
		}
	}

	public static void removeScheme(L2PcInstance playerComprador, String NameScheme){
		checkVariable(playerComprador.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).get(NameScheme).removeMe();
				SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).remove(NameScheme);
			}
		}		
	}
	
	public static void UseScheme(L2PcInstance playerComprador, boolean useInChar, String NameScheme){
		checkVariable(playerComprador.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				_charbufferStore info = CHAR_BUFFSTORE.get(playerVendedor.getObjectId());
				boolean isWanSame = ZeuS.getIp_Wan(playerComprador).equalsIgnoreCase(info.getWAN());
				boolean isEmailSame = isSameEmail(playerVendedor, playerComprador);
				SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).get(NameScheme).useMe(useInChar, info, info.getBuffPrice(), info.getIdItem() , isWanSame, isEmailSame );
			}
		}
	}
	
	
	public static boolean isSellBuff(L2PcInstance playerBuyer){
		return isSellBuff(playerBuyer,false);
	}
	
	public static boolean isSellBuff(L2PcInstance playerBuyer, boolean isVendor){
		
		L2PcInstance playerSell = null;
		if(!isVendor){
			if(playerBuyer.getTarget()==null){
				return false;
			}
			
			L2Object TargetObject = playerBuyer.getTarget();
			
			if(!(TargetObject instanceof L2PcInstance)){
				return false;
			}
			playerSell = (L2PcInstance) TargetObject;
		}else{
			playerSell = playerBuyer;
		}
		
		if(CHAR_BUFFSTORE!=null){
			if(CHAR_BUFFSTORE.containsKey(playerSell.getObjectId())){
				return true;
			}
		}
		return false;
	}

	public static void setSchemeTemporal(L2PcInstance playerComprador, String schemeName){
		SCHEME_PLAYER_TEMP.put(playerComprador.getObjectId(), schemeName);
	}
	
	
	@SuppressWarnings("rawtypes")
	private static String getMacroFromBuyer(L2PcInstance playerComprador){
		String retorno = "";

		String ByPassAddBuff = "bypass -h ZeuS buffstore addBuff %NAME_SCHEME_98%";
		String ByPassRemoveScheme = "bypass -h ZeuS buffstore removeSch %NAME_SCHEME_98%";
		String ByPassBuffMe = "bypass -h ZeuS buffstore buffMe %NAME_SCHEME_98%";
		String ByPassBuffPet = "bypass -h ZeuS buffstore buffPet %NAME_SCHEME_98%";
		String ByPassBuffEdit = "bypass -h ZeuS buffstore buffEdit %NAME_SCHEME_98%";
		
		String ImagenLogo = "icon.skill6152_";
		
		String Botonera = "<table width=240><tr>"+
						"<td fixwidth=120><button value=\"Add Buff\" width=95 height=18 action=\""+ ByPassAddBuff +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>"+
						"<td fixwidth=120><button value=\"Remove Sche\" width=95 height=18 action=\""+ ByPassRemoveScheme +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>"+				
				"</tr><tr>"+
					"<td fixwidth=120><button value=\"Buff Me\" width=95 height=18 action=\""+ ByPassBuffMe +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>"+
					"<td fixwidth=120><button value=\"Buff Pet\" width=95 height=18 action=\""+ ByPassBuffPet +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>"+						
				"</tr></table>";
		
		String GrillaScheme = "<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><table width=280 bgcolor=\"2E2E2E\"><tr><td align=CENTER fixwidth=32>"+
				"<img src=\"%IMAGEN%\" width=32 height=32><button value=\"Edit\" width=33 height=18 action=\""+ ByPassBuffEdit +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator><br><br></td><td fixwidth=248>"+
               "<font color=FAAC58>%SCHEME_NAME%</font> - <font color=936028>%BUFF_COUNT% (buffs / Dances)</font><br1>"+
               "%BTN_ACTION%</td></tr></table><img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><br>";
		

		String Grilla = "";
		
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				if(SCHEME_PLAYERS!=null){
					if(SCHEME_PLAYERS.containsKey(playerComprador.getObjectId())){
						if(SCHEME_PLAYERS.get(playerComprador.getObjectId()).containsKey(playerVendedor.getObjectId())){
							Map<String, buffStore_Scheme> T1 = SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()) ;
							Iterator itr = T1.entrySet().iterator();
							int cont=1;
							while(itr.hasNext()){
								Map.Entry Entrada = (Map.Entry)itr.next();
								String Nombre = (String)Entrada.getKey();
								buffStore_Scheme BuffInfo = (buffStore_Scheme)Entrada.getValue();
								BuffInfo.checkComprador();
								Grilla += GrillaScheme.replace("%BTN_ACTION%", Botonera) .replace("%BUFF_COUNT%", String.valueOf(BuffInfo.getCountBuff()) ) .replace("%SCHEME_NAME%", Nombre) .replace("%IMAGEN%", ImagenLogo + String.valueOf(cont)).replace("%NAME_SCHEME_98%", Nombre) ;
								cont++;
							}
							retorno = Grilla;
						}
					}
				}
			}
		}
		
		return retorno;
	}
	
	public static void showNewMacroWindows(L2PcInstance playerComprador){
		String ByPass_Scheme = "bypass -h ZeuS buffstore S_Scheme_Create $txtName";
		String ByPass_Cancel = "bypass -h ZeuS buffstore S_Scheme_Cancel ";
		final NpcHtmlMessage html = comun.htmlMaker(playerComprador,general.DIR_HTML + "/" + language.getInstance().getFolder(playerComprador) + "/" +"buffstore_seller_macro.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(playerComprador));
		html.replace("%BYPASS_MACRO%", ByPass_Scheme);
		html.replace("%BYPASS_CANCEL%", ByPass_Cancel);
		
		central.sendHtml(playerComprador, html.getHtml(), false);		
	}
	
	public static void createScheme(L2PcInstance playerComprador, String NameScheme){
		checkVariable(playerComprador.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();		
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				//getBuffForSell(playerVendedor,playerComprador,0);
				if(SCHEME_PLAYERS==null){
					buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
					SCHEME_PLAYERS.put(playerComprador.getObjectId(), new HashMap<Integer, HashMap<String, buffStore_Scheme>>());
					SCHEME_PLAYERS.get(playerComprador.getObjectId()).put(playerVendedor.getObjectId() , new HashMap<String, buffStore_Scheme>());
					SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
				}else if(SCHEME_PLAYERS.containsKey(playerComprador.getObjectId())){
					if(SCHEME_PLAYERS.get(playerComprador.getObjectId()) == null ){
						buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
						SCHEME_PLAYERS.get(playerComprador.getObjectId()).put(playerVendedor.getObjectId() , new HashMap<String, buffStore_Scheme>());
						SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
					}else if(SCHEME_PLAYERS.get(playerComprador.getObjectId()).containsKey(playerVendedor.getObjectId())){
						int SchemeHave = SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).size();
						if(SchemeHave >= MAX_SCHEME){
							central.msgbox(language.getInstance().getMsg(playerComprador).BUFFER_BUFFSTORE_MAX_SCHEME.replace("$scheme", String.valueOf(MAX_SCHEME)), playerComprador);
							return;
						}
						if(SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId())==null){
							buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
							SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
						}else if(!SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).containsKey(NameScheme)){
							buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
							SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
						}else{
							central.msgbox(language.getInstance().getMsg(playerComprador).BUFFER_BUFFSTORE_YOU_HAVE_THIS_SCHEME_NAME_ON_THIS_BUFFSTORE_SELLER , playerComprador);	
						}
					}else{
						buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
						SCHEME_PLAYERS.get(playerComprador.getObjectId()).put(playerVendedor.getObjectId() , new HashMap<String, buffStore_Scheme>());
						SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
					}
				}else{
					buffStore_Scheme T1 = new buffStore_Scheme(playerVendedor, playerComprador, NameScheme, true);
					SCHEME_PLAYERS.put(playerComprador.getObjectId(), new HashMap<Integer, HashMap<String, buffStore_Scheme>>());
					SCHEME_PLAYERS.get(playerComprador.getObjectId()).put(playerVendedor.getObjectId() , new HashMap<String, buffStore_Scheme>());
					SCHEME_PLAYERS.get(playerComprador.getObjectId()).get(playerVendedor.getObjectId()).put(NameScheme, T1);
				}
			}
		}		
	}
	
	public static void showBuffWindows(L2PcInstance playerComprador){
		checkVariable(playerComprador.getObjectId());
		L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();		
		if(playerVendedor != null){
			if(isSellBuff(playerVendedor,true)){
				getBuffForSell(playerVendedor,playerComprador,0);
			}
		}
	}
	
	public static void ShowMainWIndows(L2PcInstance playerComprador){
		checkVariable(playerComprador.getObjectId());
		L2Object targetO = playerComprador.getTarget();
		if(targetO instanceof L2PcInstance){
			L2PcInstance plTarget = (L2PcInstance)targetO;
			L2PcInstance playerVendedor = PLAYER_VARIABLE.get(playerComprador.getObjectId()).getSelectedBufferSeller();
			if(plTarget != playerVendedor){
				return;
			}

			if(!playerComprador.isInsideRadius(playerVendedor.getLocation(), general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
					return;
				}

			if(playerVendedor != null){
				if(isSellBuff(playerVendedor,true)){
					showSellBuffer_main(playerComprador,playerVendedor);
				}
			}			
			
		}
	}
	
	public static void ByPass(L2PcInstance player, String Params, L2PcInstance playerComprador){
		checkVariable(playerComprador.getObjectId());
		if(isSellBuff(player)){
			PLAYER_VARIABLE.get(playerComprador.getObjectId()).setSelectedBufferSeller(player);
			showSellBuffer_main(playerComprador,player);
		}
	}
	
	public static class buffStore_Scheme{
		private L2PcInstance Vendedor;
		private L2PcInstance Comprador;
		private String SchemeName;
		private Vector<Integer> Buffs = new Vector<Integer>();
		private int idMacro;
		public buffStore_Scheme(L2PcInstance playerSeller, L2PcInstance playerBuyer, String _SchemeName, boolean saveInBD){
			this.Vendedor = playerSeller;
			this.Comprador = playerBuyer;
			this.SchemeName = _SchemeName;
			if(saveInBD){
				saveMacroinBD();
			}
		}
		
		public void checkComprador(){
				int idComprador = Comprador.getObjectId();
				L2PcInstance tPla = opera.getPlayerbyID(idComprador);
				if(Comprador==null){
					this.Comprador = tPla;
				}else if(this.Comprador !=tPla ){
					this.Comprador = tPla;
				}
			}		
		
		public int getCountBuff(){
			try{
				return Buffs.size();
			}catch(Exception a){
				
			}
			return 0;
		}
		
	public void removeBuffFromScheme(int idBuff){
			this.Buffs.removeElement(idBuff);
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buffstore_scheme_buff WHERE idscheme=? AND idbuff=?");
				ins.setInt(1, this.idMacro);
				ins.setInt(2, idBuff);
				try{
					ins.executeUpdate();
				}catch(SQLException e){
					_log.warning("Error removing Buff from buffstore scheme->" + e.getMessage());
				}
			}catch(Exception a){
				
			}			
		}
		
		public String getAllBuffEdit(){
			String retorno = "";
			String BtnRemove = "<button value=\"Remove It\" width=95 height=20 action=\"bypass -h ZeuS buffstore removebuff "+ SchemeName +" %ID_BUFF%\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
			Collection<L2Skill> skills = Vendedor.getAllSkills();
		    for(L2Skill s : skills){
		    	if(Buffs.contains(s.getId())){
		    		int IdSkill = s.getId();
		    		int IdLevel = s.getLevel();
		    		if(retorno.length()>0){
		    			retorno += "<br>";
		    		}
		    		String ImagenLogo = "Icon.skill" + opera.getIconSkill(IdSkill);
		    		String GrillaScheme = "<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><table width=280 bgcolor=\"2E2E2E\"><tr><td align=CENTER fixwidth=32>"+
		                "<img src=\""+ ImagenLogo +"\" width=32 height=32><br><br></td><td fixwidth=248>"+
		                "<font color=FAAC58>"+ opera.getSkillName(IdSkill, IdLevel) + " " + getEnchantSkill(String.valueOf(IdLevel), false) + "</font><br1>"+
		                BtnRemove.replace("%ID_BUFF%", String.valueOf(IdSkill)) + "</td></tr></table><img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><br1>";
		    		retorno += GrillaScheme;
		    	}
		    }
		    return retorno;
		}		
		
		public Vector<Integer> getBuffs(){
			return Buffs;
		}
		
		public void loadFromBD(int idScheme){
			this.idMacro = idScheme;
			loadBuffFromScheme();
		}
		
		private void loadBuffFromScheme(){
			try{
				String Consulta = "select idbuff from zeus_buffstore_scheme_buff where idscheme=?";
				Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement psqry = conn.prepareStatement(Consulta);
				psqry.setInt(1, this.idMacro);
				ResultSet rss = psqry.executeQuery();
					while (rss.next()){
						try{
							Buffs.add(rss.getInt(1));
						}catch(Exception e){
							_log.warning("Error loading ZeuS buffstore scheme buff Player("+ Comprador.getName() +") -> " + e.getMessage());
						}
					}
				conn.close();
			}catch(SQLException e){

			}			
		}
		
		public void useMe(boolean onChar, _charbufferStore info, Long Price, int idItemRequest, boolean AreSameWan, boolean AreSameEmail){
			if(!Comprador.isInsideRadius(Vendedor.getLocation(), general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
				return;
			}			
			if(Buffs==null){
				return;
			}else if(Buffs.size()==0){
				return;
			}
			
			boolean FreeBuffClan = info.getFreeClan();
			boolean FreeWAN = info.getFreeWan();
			boolean FreeFriend = info.getFreeFriend();
			boolean FreeEmail = info.getFreeEmail();
			Long Precio = Price * Buffs.size();
			boolean ForFree = false;
			if(FreeBuffClan && AreSameClan(this.Vendedor, this.Comprador)){
				ForFree = true;
			}else if(FreeWAN && AreSameWan){
				ForFree = true;
			}else if(FreeFriend && AreFriend(this.Vendedor,this.Comprador)){
				ForFree = true;
			}else if(FreeEmail && AreSameEmail){
				ForFree = true;
			}
			
			
			if(!onChar && Comprador.getSummon()==null){
				central.msgbox(language.getInstance().getMsg(Comprador).BUFFERCHAR_YOU_NOT_HAVE_PET, Comprador);
				return;
			}else if(!onChar && Comprador.getSummon().isDead()){
				central.msgbox(language.getInstance().getMsg(Comprador).BUFFERCHAR_YOU_NOT_HAVE_PET, Comprador);
				return;
			}
			
			if(!ForFree){
				if(!opera.haveItem(Comprador,idItemRequest, Precio,true)){
					return;
				}else{
					opera.removeItem(idItemRequest, Precio, Comprador);
					opera.giveReward(Vendedor,idItemRequest,Precio);
				}
			}
			Collection<L2Skill> skills = Vendedor.getAllSkills();
		    for(L2Skill s : skills){
		    	if(Buffs.contains(s.getId())){
		    		int IdSkill = s.getId();
		    		int IdLevel = s.getLevel();
		    		
		    		SkillHolder BUFF_SELECTED_USE = new SkillHolder(IdSkill, IdLevel);
					L2Skill BuffDar = SkillData.getInstance().getInfo(IdSkill, IdLevel);
					if(onChar){
						Vendedor.setTarget(Comprador);
						SkillData.getInstance().getInfo(IdSkill, IdLevel).getEffects(Vendedor, Comprador);
					}else{
						Vendedor.setTarget(Comprador.getSummon());
						SkillData.getInstance().getInfo(IdSkill, IdLevel).getEffects(Vendedor, Comprador.getSummon());
					}
					Vendedor.doCast(BuffDar);
					Vendedor.doCast(BUFF_SELECTED_USE.getSkill());		    		
		    		
		    	}
		    }
		}
		
		public void setBuff(int idBuff, boolean saveInBD){
			
			if(Buffs!=null){
				if(Buffs.size() >= MAX_BUFF_SCHEME ){
					central.msgbox( language.getInstance().getMsg(Comprador).BUFFER_BUFFSTORE_MAX_BUFF_POUR_SCHEME.replace("$max_buff", String.valueOf(MAX_BUFF_SCHEME)), Comprador);
					return;
				}
			}
			
			Buffs.add(idBuff);
			if(saveInBD){
				saveBuffinBD(idBuff);
			}
		}
		public void removeBuff(int idBuff){
			try{
				Buffs.remove(idBuff);
			}catch(Exception a){
				
			}
			try{
				removeBuffFromBD(idBuff);
			}catch(Exception a){
				
			}
		}
		
		public void removeMe(){
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buffstore_scheme WHERE id=?");
				ins.setInt(1, this.idMacro);
				try{
					ins.executeUpdate();
				}catch(SQLException e){

				}
				
			}catch(Exception a){
				
			}			
			
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buffstore_scheme_buff WHERE idscheme=?");
				ins.setInt(1, this.idMacro);
				try{
					ins.executeUpdate();
				}catch(SQLException e){

				}
				
			}catch(Exception a){
				
			}			
		}
		
		public boolean haveBuff(int idBuff){
			try{
				return Buffs.contains(idBuff);
			}catch(Exception a){
				
			}
			return false;
		}
		
		private void saveMacroinBD(){
			String consulta = "INSERT INTO zeus_buffstore_scheme(idplayer,id_ppl_seller, scheme_name) VALUES(?,?,?)";
			Connection con = null;
			PreparedStatement statement = null;
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement(consulta,Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, this.Comprador.getObjectId());
				statement.setInt(2, this.Vendedor.getObjectId());
				statement.setString(3, this.SchemeName );
				statement.execute();
				try (ResultSet rset = statement.getGeneratedKeys())
				{
					if (rset.next())
					{
						int _id = rset.getInt(1);
						this.idMacro = _id;
					}
				}			
			}catch(Exception a){
				_log.warning("Error on Save Buffstore scheme->" + a.getMessage());
			}			
		}			

		private void saveBuffinBD(int idSkill){
			String consulta = "INSERT INTO zeus_buffstore_scheme_buff values (?,?)";
			Connection con = null;
			PreparedStatement statement = null;
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement(consulta);
				statement.setInt(1, this.idMacro);
				statement.setInt(2, idSkill);
				statement.execute();
			}catch(Exception a){
				_log.warning("Error on Save Buff on buffstore Scheme from "+ this.Comprador.getName() +"->" + a.getMessage());
			}			
		}
		
		private void removeBuffFromBD(int idSkill){
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buffstore_scheme_buff WHERE idscheme=? AND idbuff=?");
				ins.setInt(1, this.idMacro);
				ins.setInt(2, idSkill);
				try{
					ins.executeUpdate();
				}catch(SQLException e){

				}
				
			}catch(Exception a){
				
			}			
		}
		
	}
}
class _charbufferStore{
	private long BUFF_PRICE;
	private String ORIGINAL_TITLE;
	private String ORIGINAL_COLOR_TITLE;	
	private String SELL_TITLE;
	private String ORIGINAL_NAME_COLOR;
	private String WANIP;
	private boolean BUFF_FREE_CLAN;
	private boolean BUFF_FREE_WAN;
	private boolean BUFF_FREE_FRIEND;
	private boolean BUFF_FREE_EMAIL;
	private int ITEM_REQUEST_ID;
	private List<L2Skill> BUFF = new ArrayList<L2Skill>();
	private int CHAR_ID;
	private Vector<Integer> BuffBlock = new Vector<Integer>();
	private boolean IS_SELLING_BUFF;
	private String ACCOUNT_NAME;
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(_charbufferStore.class.getName());
	public final String getItemRequestedName(){
		return central.getNombreITEMbyID(this.ITEM_REQUEST_ID);
	}
	
	public final String getAccount(){
		return this.ACCOUNT_NAME;
	}
	
	public final int getCharID(){
		return this.CHAR_ID;
	}
	public final long getBuffPrice(){
		return this.BUFF_PRICE;
	} 
	public final String getOriginalTitle(){
		return this.ORIGINAL_TITLE;
	}
	public final String getOriginalColorTitle(){
		return this.ORIGINAL_COLOR_TITLE;
	}
	public final String getOriginalColorName(){
		return this.ORIGINAL_NAME_COLOR;
	}
	public final String getSellTitle(){
		return this.SELL_TITLE;
	}
	public final String getWAN(){
		return this.WANIP;
	}
	public final boolean getFreeClan(){
		return this.BUFF_FREE_CLAN;
	}
	public final boolean getFreeWan(){
		return this.BUFF_FREE_WAN;
	}
	public final boolean getFreeFriend(){
		return this.BUFF_FREE_FRIEND;
	}
	public final boolean getFreeEmail(){
		return this.BUFF_FREE_EMAIL;
	}
	public final int getIdItem(){
		return this.ITEM_REQUEST_ID;
	}
	public final List<L2Skill> getBuffs(){
		return this.BUFF;
	}
	public final boolean isSellingBuff(){
		return this.IS_SELLING_BUFF;
	}
	
	public void setSellingBuff(boolean value){
		this.IS_SELLING_BUFF = value;
	}
	
	private void setBlockbuff(){
		BuffBlock.add(970);
		BuffBlock.add(357);
		BuffBlock.add(1323);
		BuffBlock.add(327);
		BuffBlock.add(1325);
		BuffBlock.add(1326);
		BuffBlock.add(1327);
	}	
	
	public _charbufferStore(L2PcInstance player, String SellTitle, boolean free_clan, boolean free_wan, boolean free_friend, boolean free_email, int itemRequestID, long buffPrice){
		this.setBlockbuff();
		this.CHAR_ID = player.getObjectId();
		this.BUFF_PRICE = buffPrice;
		this.BUFF_FREE_CLAN = free_clan;
		this.BUFF_FREE_WAN = free_wan;
		this.BUFF_FREE_FRIEND = free_friend;
		this.BUFF_FREE_EMAIL = free_email;
		this.ITEM_REQUEST_ID = itemRequestID;
		this.ORIGINAL_TITLE = player.getTitle();
		this.ORIGINAL_COLOR_TITLE=String.valueOf(player.getAppearance().getTitleColor());	
		this.SELL_TITLE = SellTitle;
		this.ORIGINAL_NAME_COLOR = String.valueOf(player.getAppearance().getNameColor());
		try{
			this.WANIP = ZeuS.getIp_Wan(player);
		}catch(Exception a){
			this.WANIP = "";
		}
		this.IS_SELLING_BUFF = true;
		this.ACCOUNT_NAME = player.getAccountName();
		this.getBuff(player);
	}
	
	public void setDataLoad(String WanIp, String SellTitle){
		this.WANIP = WanIp;
		this.SELL_TITLE = SellTitle;
	}
	
	private void getBuff(L2PcInstance player){
		Collection<L2Skill> skills = player.getAllSkills();
        List<L2Skill> ba = new ArrayList<L2Skill>();
    	Vector<L2TargetType> NoPermitidos = new Vector<L2TargetType>();
    	NoPermitidos.add(L2TargetType.SELF );
    	NoPermitidos.add(L2TargetType.OWNER_PET);
	    for(L2Skill s : skills){
	    	if(s == null){
	    		continue;
	    	}
	    	if(NoPermitidos.contains(s.getTargetType())){
	    		continue;
	    	}
	    	if(general.BUFF_STORE_BUFFPROHIBITED!=null){
	    		if(general.BUFF_STORE_BUFFPROHIBITED.size()>0){
	    			if(general.BUFF_STORE_BUFFPROHIBITED.contains(s.getId())){
	    				continue;
	    			}
	    		}
	    	}
	    	if(s.getSkillType() == L2SkillType.BUFF && s.isActive() &&  !BuffBlock.contains(s.getId())){
	    		ba.add(s);
	    		sellBuff.setBuffstoreSearch(player, s.getId(), s.getLevel(), this.ITEM_REQUEST_ID, this.BUFF_PRICE);
	    	}
	    }
	    try{
			Comparator<L2Skill> NameAZ = (p1,p2) -> p1.getName() .compareToIgnoreCase(p2.getName());
			Collections.sort(ba,NameAZ);
		}catch(Exception a){
			
		}
	    this.BUFF = ba;		
	}
	
	public void removeMe(){
		String DeleteInfo = "DELETE FROM zeus_buffstore WHERE idChar=?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
			{
				statementt.setInt(1, this.CHAR_ID);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}		
	}
	
}
class _buffSearchVariable{
	private int BUFF_FOR_SEARCH_ID_BUFF;
	private int BUFF_FOR_SEARCH_BUFF_LIST_PAG;
	private int BUFF_FOR_SEARCH_PLAYER_PAG;
	private Location BUFF_FOR_SEARCH_TELEPORT;
	private String BUFF_FOR_SEARCH_LAST_SORT;
	private String ITEM_PRICE_SORT;
	private L2PcInstance SELECTED_CHAR_BUFFER_SELLER;
	private boolean USE_BUFF_ON_PLAYER;
	//SelectedCharBuffer
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(_buffSearchVariable.class.getName());

	public _buffSearchVariable(){
		this.BUFF_FOR_SEARCH_ID_BUFF = 0;
		this.BUFF_FOR_SEARCH_BUFF_LIST_PAG = 0;
		this.BUFF_FOR_SEARCH_PLAYER_PAG = 0;
		this.BUFF_FOR_SEARCH_TELEPORT = null;
		this.BUFF_FOR_SEARCH_LAST_SORT = "NameAZ";
		this.SELECTED_CHAR_BUFFER_SELLER = null;
		this.USE_BUFF_ON_PLAYER = true;
		this.ITEM_PRICE_SORT = "ALL";
	}
	
	public void setPriceItemSort(String item){
		this.ITEM_PRICE_SORT = item;
	}
	public String GetPriceItemSort(){
		return this.ITEM_PRICE_SORT;
	}	
	
	public void setUseBuffOnPlayer(boolean _value){
		this.USE_BUFF_ON_PLAYER = _value;
	}
	public boolean getUseBuffOnPlayer(){
		return this.USE_BUFF_ON_PLAYER;
	}
	public void changeBuffOnPlayer(){
		this.USE_BUFF_ON_PLAYER = !this.USE_BUFF_ON_PLAYER; 
	}
	
	public void setSelectedBufferSeller(L2PcInstance player){
		this.SELECTED_CHAR_BUFFER_SELLER = player;
	}
	public L2PcInstance getSelectedBufferSeller(){
		return this.SELECTED_CHAR_BUFFER_SELLER;
	}	
	
	public void setBuffSearching(int idBuff){
		this.BUFF_FOR_SEARCH_ID_BUFF = idBuff;
	}
	public int getBuffSearching(){
		return this.BUFF_FOR_SEARCH_ID_BUFF;
	}
	
	public void setPagBuffList(int Pagine){
		this.BUFF_FOR_SEARCH_BUFF_LIST_PAG = Pagine;
	}
	public int getPagBuffList(){
		return this.BUFF_FOR_SEARCH_BUFF_LIST_PAG;
	}
	
	public void setPagPlayerList(int Pagine){
		this.BUFF_FOR_SEARCH_PLAYER_PAG = Pagine; 
	}
	public int getPagPlayerList(){
		return this.BUFF_FOR_SEARCH_PLAYER_PAG;
	}
	
	public void setLocationFromSeller(Location loc){
		this.BUFF_FOR_SEARCH_TELEPORT = loc;
	}
	public Location getLocationFromSeller(){
		return this.BUFF_FOR_SEARCH_TELEPORT;
	}
	
	public void setLastSort(String sort){
		this.BUFF_FOR_SEARCH_LAST_SORT = sort;
	}
	public String getLastSort(){
		return this.BUFF_FOR_SEARCH_LAST_SORT;
	}
}
