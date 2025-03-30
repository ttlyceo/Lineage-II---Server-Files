package ZeuS.interfase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.Config._custom_drop_system;

public class custom_drop_system {
	private final static Logger _log = Logger.getLogger(custom_drop_system.class.getName());	
	private static Map<Integer, Vector<_custom_drop_system>> DROP_INFO = new HashMap<Integer, Vector<_custom_drop_system>>();
	private static boolean EnabledSystem = false;
	
	public static long getChanceToAddPorcent(int idItem, int idNpc, int Level){
		long retorn=0;
		if(!EnabledSystem) {
			return retorn;
		}
		if(DROP_INFO != null){
			if(DROP_INFO.size()>0){
				if(DROP_INFO.containsKey(idItem)){
					for(_custom_drop_system Dinfo : DROP_INFO.get(idItem)){
						long tt = Dinfo.getChancePorcent(Level, idNpc);
						retorn =( tt > retorn ?  tt : retorn); 
					}
				}
			}
		}
		return retorn;
	}
	
	public static long getRateToAddPorcent(int idItem, int idNpc, int Level){
		long retorn=0;
		if(!EnabledSystem) {
			return retorn;
		}		
		if(DROP_INFO != null){
			if(DROP_INFO.size()>0){
				if(DROP_INFO.containsKey(idItem)){
					for(_custom_drop_system Dinfo : DROP_INFO.get(idItem)){
						long tt = Dinfo.getRatePorcent(Level, idNpc);
						retorn =( tt > retorn ?  tt : retorn); 
					}
				}
			}
		}
		return retorn;
	}	
	
	public static void loadCustomDrop(){
		try{
			DROP_INFO.clear();
		}catch(Exception a){
			
		}
		File dir = new File("./config/zeus/zeus_custom_drop.xml");
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()){
				int _ID_ITEM = 0;
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
					if(d.getNodeName().equalsIgnoreCase("config")) {
						try {
							String tempDataEnabled = d.getAttributes().getNamedItem("value").getNodeValue();
							if(tempDataEnabled.equalsIgnoreCase("enabled")) {
								EnabledSystem = true;
							}else {
								EnabledSystem = false;
							}
						}catch(Exception a) {
							EnabledSystem = false;
						}
						_log.warning("custom drop system: " + ( EnabledSystem ? "Enabled" : "disabled" ));
					}
					if (!"item".equalsIgnoreCase(d.getNodeName())){
						continue;
					}
					try{
						_ID_ITEM  = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
					}catch(Exception a){
						_ID_ITEM = 0;
					}
					for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling())
					{
						long _CHANCE_ADD;
						long _DROP_RATE_ADD;
						String _USE_IN_TYPE;
						if ("option".equalsIgnoreCase(b.getNodeName())){
							_USE_IN_TYPE = b.getAttributes().getNamedItem("UseInType").getNodeValue();
							_CHANCE_ADD = Long.valueOf(b.getAttributes().getNamedItem("chanceAdd").getNodeValue());
							_DROP_RATE_ADD = Long.valueOf(b.getAttributes().getNamedItem("dropRateAdd").getNodeValue());
							_custom_drop_system _tmp = new _custom_drop_system(_ID_ITEM, _USE_IN_TYPE, _CHANCE_ADD, _DROP_RATE_ADD);
							if(DROP_INFO == null){
								DROP_INFO.put(_ID_ITEM, new Vector<_custom_drop_system>());
							}else if(DROP_INFO.size()==0){
								DROP_INFO.put(_ID_ITEM, new Vector<_custom_drop_system>());
							}else if(!DROP_INFO.containsKey(_ID_ITEM)){
								DROP_INFO.put(_ID_ITEM, new Vector<_custom_drop_system>());
							}
							//Vector<_custom_drop_system> tmp1 = new Vector<_custom_drop_system>();
							for (Node _option = b.getFirstChild(); _option != null; _option = _option.getNextSibling())
							{
								if(_USE_IN_TYPE.equalsIgnoreCase("level")){
									if (_option.getNodeName().equalsIgnoreCase("level")){
										int _MIN_LEVEL = 0;
										int _MAX_LEVEL = 0;
										String value = "";
										try{
											value = String.valueOf(_option.getAttributes().getNamedItem("min").getNodeValue().toLowerCase());
											_MIN_LEVEL = Integer.valueOf(value);
										}catch(Exception a){
											
										}
										try{
											value = String.valueOf(_option.getAttributes().getNamedItem("max").getNodeValue().toLowerCase());
											_MAX_LEVEL = Integer.valueOf(value);
										}catch(Exception a){
											
										}
										if(_MIN_LEVEL>0 && _MAX_LEVEL>0){
											_tmp.setLevels(_MIN_LEVEL, _MAX_LEVEL);
										}
									}
								}else if(_USE_IN_TYPE.equalsIgnoreCase("npc_id")){
									if (_option.getNodeName().equalsIgnoreCase("npc")){
										int _NPC_ID_FOR_CHANGE = 0;
										String value = "";
										try{
											value = String.valueOf(_option.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());
											_NPC_ID_FOR_CHANGE = Integer.valueOf(value);
										}catch(Exception a){
											
										}
										_tmp.setIdNpc(_NPC_ID_FOR_CHANGE);
									}									
								}
								DROP_INFO.get(_ID_ITEM).add(_tmp);
							}
							
						}
					}
				}
			}
			if(DROP_INFO != null){
				_log.warning("Custom drop system has loaded " + DROP_INFO.size() + " item data" );
			}
		}
		catch (Exception a)
		{
			_log.warning("	Error loading PVP ZONE ->" + a.getMessage() + "\n");
		}		
	}
}
