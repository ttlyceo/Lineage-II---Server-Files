package ZeuS.dressme;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.L2Armor;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.L2Weapon;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import ZeuS.Config._dressItem;
import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

public class dressme extends newDressme{
	
	private static final Logger _log = Logger.getLogger(dressme.class.getName());
	protected static Map<Integer,dress_player>DRESSME_PLAYER = new HashMap<Integer, dress_player>();

	protected static Map<String, Vector<_dressItem>> DRESSME_ITEMS = new HashMap<String, Vector<_dressItem>>();
	protected static Map<Integer, _dressmeItemConfig> DRESSME_ITEM_CONFIG = new HashMap<Integer, _dressmeItemConfig>();
	public dressme(){
		loadConfig();
	}	
	@SuppressWarnings("unused")
	public void getDressmePart(){
		
		try{
			DRESSME_ITEMS.clear();
			DRESSME_ITEM_CONFIG.clear();
		}catch(Exception a){
			
		}
		File dir = new File("./config/zeus/zeus_dressme.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("armor")){
							if(DRESSME_ITEMS==null){
								DRESSME_ITEMS.put("ARMOR", new Vector<_dressItem>());
							}else if(!DRESSME_ITEMS.containsKey("ARMOR")){
								DRESSME_ITEMS.put("ARMOR", new Vector<_dressItem>());
							}
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String ItemName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ItemId = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									String Price = dd.getAttributes().getNamedItem("cost").getNodeValue().toLowerCase();
									String ClanExclusivePrice = "";
									int ClanDays = 0;
									String CharExclusivePrice = "";
									int CharDays = 0;
									boolean isJustForHero = false;
									boolean isJustForNoble = false;
									boolean isJustForTopPvPPk = false;
									boolean isClanExclusive = false;
									boolean isCharExclusive = false;
									int CharMinLevel = 0;
									int ClanMinLevel = 0;									
									int idHood = -100;
									boolean isEnabled = true;
									try{
										isJustForHero = Boolean.parseBoolean(dd.getAttributes().getNamedItem("forhero").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForNoble = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fornoble").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForTopPvPPk = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fortoppvp").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									String _value = "";
									try{
										_value = dd.getAttributes().getNamedItem("forexclusiveclan").getNodeValue().toLowerCase();
										isClanExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isClanExclusive = false;
									}
									if(isClanExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclandays").getNodeValue();
											ClanDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Clan ("+ ItemId +")");
											ClanDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclanprice").getNodeValue();
											ClanExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Clan ("+ ItemId +")");
											ClanExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusiveclanminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											ClanMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												ClanMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Clan Min. Level for Exlusive Clan ("+ ItemId +")");											
												ClanMinLevel = 0;
												isEnabled = false;
											}
										}
									}
									
									try{
										_value = dd.getAttributes().getNamedItem("forexclusivechar").getNodeValue().toLowerCase();
										isCharExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isCharExclusive = false;
									}
									if(isCharExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivechardays").getNodeValue();
											CharDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Char ("+ ItemId +")");
											CharDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivecharprice").getNodeValue();
											CharExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Char ("+ ItemId +")");
											CharExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusivecharnminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											CharMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												CharMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Char Min Level for Char Exlusive ("+ ItemId +")");											
												CharMinLevel = 0;
												isEnabled = false;
											}
										}										
									}
									
									try {
										_value = dd.getAttributes().getNamedItem("deco").getNodeValue().toLowerCase();
										idHood = Integer.valueOf(_value);
									}catch(Exception a) {
										
									}
									
									Vector<Integer>_ClassRestrictionTemp = new Vector<Integer>();
									try {
										_value = dd.getAttributes().getNamedItem("class_id_restriction").getNodeValue().toLowerCase();
										if(_value!=null) {
											if(_value.length()>0) {
												if(_value.indexOf(",")>0) {
													for(String idClass : _value.split(",")) {
														try {
															int _idClass = Integer.valueOf(idClass);
															_ClassRestrictionTemp.add(_idClass);
														}catch(Exception a) {
															_log.warning("Dressme Class id Restriction cant added this Data: " + idClass);
														}
													}
												}else {
													try {
														int _idClass = Integer.valueOf(_value);
														_ClassRestrictionTemp.add(_idClass);
													}catch(Exception a) {
														_log.warning("Dressme Class id Restriction cant added this Data: " + _value);
													}													
												}
											}
										}
									}catch(Exception a) {
										
									}
									
									_dressItem IT = new _dressItem(Integer.valueOf(ItemId), Price, isJustForHero, isJustForNoble, isJustForTopPvPPk, isClanExclusive, ClanDays, ClanExclusivePrice, isCharExclusive, CharDays, CharExclusivePrice, idHood, true, _ClassRestrictionTemp, CharMinLevel, ClanMinLevel, isEnabled);
									DRESSME_ITEMS.get("ARMOR").add(IT);
									_dressmeItemConfig CT = new _dressmeItemConfig(isJustForNoble, isJustForHero, isJustForTopPvPPk, isClanExclusive, IT._getOwnerClanId(), isCharExclusive, IT._getOwnerCharId(), IT);
									DRESSME_ITEM_CONFIG.put(IT.getIdItem(), CT);
									if(idHood>0){
										_dressItem IT_DECO = new _dressItem(idHood, Price, isJustForHero, isJustForNoble, isJustForTopPvPPk, isClanExclusive, ClanDays, ClanExclusivePrice, isCharExclusive, CharDays, CharExclusivePrice, 0, false, _ClassRestrictionTemp, CharMinLevel, ClanMinLevel, isEnabled);
										DRESSME_ITEMS.get("ARMOR").add(IT_DECO);
										_dressmeItemConfig CT_DECO = new _dressmeItemConfig(isJustForNoble, isJustForHero, isJustForTopPvPPk, isClanExclusive, IT._getOwnerClanId(), isCharExclusive, IT._getOwnerCharId(), IT);
										DRESSME_ITEM_CONFIG.put(IT_DECO.getIdItem(), CT_DECO);										
									}
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("shield")){
							if(DRESSME_ITEMS==null){
								DRESSME_ITEMS.put("SHIELD", new Vector<_dressItem>());
							}else if(!DRESSME_ITEMS.containsKey("SHIELD")){
								DRESSME_ITEMS.put("SHIELD", new Vector<_dressItem>());
							}							
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String ItemName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ItemId = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									String Price = dd.getAttributes().getNamedItem("cost").getNodeValue().toLowerCase();
									int ClanDays = 0;
									String ClanExclusivePrice = "";
									String CharExclusivePrice = "";
									int CharDays = 0;
									boolean isJustForHero = false;
									boolean isJustForNoble = false;
									boolean isJustForTopPvPPk = false;
									boolean isClanExclusive = false;
									boolean isCharExclusive = false;
									int CharMinLevel = 0;
									int ClanMinLevel = 0;
									boolean isEnabled = true;
									try{
										isJustForHero = Boolean.parseBoolean(dd.getAttributes().getNamedItem("forhero").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForNoble = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fornoble").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}
									try{
										isJustForTopPvPPk = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fortoppvp").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}
									String _value = "";
									try{
										_value = dd.getAttributes().getNamedItem("forexclusiveclan").getNodeValue().toLowerCase();
										isClanExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isClanExclusive = false;
									}
									if(isClanExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclandays").getNodeValue();
											ClanDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Clan ("+ ItemId +")");
											ClanDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclanprice").getNodeValue();
											ClanExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Clan ("+ ItemId +")");
											ClanExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusiveclanminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											ClanMinLevel = 0;
											isEnabled = false;
										}
										
										if(!ValTemp.equals("")) {
											try {
												ClanMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Clan Min. Level for Exlusive Clan ("+ ItemId +")");											
												ClanMinLevel = 0;
												isEnabled = false;
											}
										}									
									}
									
									try{
										_value = dd.getAttributes().getNamedItem("forexclusivechar").getNodeValue().toLowerCase();
										isCharExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isCharExclusive = false;
									}
									if(isCharExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivechardays").getNodeValue();
											CharDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Char ("+ ItemId +")");
											CharDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivecharprice").getNodeValue();
											CharExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Char ("+ ItemId +")");
											CharExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusivecharnminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											CharMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												CharMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Char Min Level for Char Exlusive ("+ ItemId +")");											
												CharMinLevel = 0;
												isEnabled = false;
											}
										}										
									}
									
									Vector<Integer>_ClassRestrictionTemp = new Vector<Integer>();
									try {
										_value = dd.getAttributes().getNamedItem("class_id_restriction").getNodeValue().toLowerCase();
										if(_value!=null) {
											if(_value.length()>0) {
												if(_value.indexOf(",")>0) {
													for(String idClass : _value.split(",")) {
														try {
															int _idClass = Integer.valueOf(idClass);
															_ClassRestrictionTemp.add(_idClass);
														}catch(Exception a) {
															_log.warning("Dressme Class id Restriction cant added this Data: " + idClass);
														}
													}
												}else {
													try {
														int _idClass = Integer.valueOf(_value);
														_ClassRestrictionTemp.add(_idClass);
													}catch(Exception a) {
														_log.warning("Dressme Class id Restriction cant added this Data: " + _value);
													}													
												}
											}
										}
									}catch(Exception a) {
										
									}									
									
									_dressItem IT = new _dressItem(Integer.valueOf(ItemId), Price, isJustForHero, isJustForNoble, isJustForTopPvPPk, isClanExclusive, ClanDays, ClanExclusivePrice, isCharExclusive, CharDays, CharExclusivePrice, 0, true, _ClassRestrictionTemp, CharMinLevel, ClanMinLevel, isEnabled);
									DRESSME_ITEMS.get("SHIELD").add(IT);
									_dressmeItemConfig CT = new _dressmeItemConfig(isJustForNoble, isJustForHero, isJustForTopPvPPk, isClanExclusive, IT._getOwnerClanId(), isCharExclusive, IT._getOwnerCharId(), IT);
									DRESSME_ITEM_CONFIG.put(IT.getIdItem(), CT);
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("weapon")){
							if(DRESSME_ITEMS==null){
								DRESSME_ITEMS.put("WEAPON", new Vector<_dressItem>());
							}else if(!DRESSME_ITEMS.containsKey("WEAPON")){
								DRESSME_ITEMS.put("WEAPON", new Vector<_dressItem>());
							}								
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String ItemName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ItemId = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									String Price = dd.getAttributes().getNamedItem("cost").getNodeValue().toLowerCase();
									int ClanDays = 0;
									String ClanExclusivePrice = "";
									String CharExclusivePrice = "";
									int CharDays = 0;
									boolean isJustForHero = false;
									boolean isJustForNoble = false;
									boolean isJustForTopPvPPk = false;
									boolean isClanExclusive = false;
									boolean isCharExclusive = false;
									int CharMinLevel = 0;
									int ClanMinLevel = 0;
									boolean isEnabled = true;
									try{
										isJustForHero = Boolean.parseBoolean(dd.getAttributes().getNamedItem("forhero").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForNoble = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fornoble").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForTopPvPPk = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fortoppvp").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}
									String _value = "";
									try{
										_value = dd.getAttributes().getNamedItem("forexclusiveclan").getNodeValue().toLowerCase();
										isClanExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isClanExclusive = false;
									}
									if(isClanExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclandays").getNodeValue();
											ClanDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Clan ("+ ItemId +")");
											ClanDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclanprice").getNodeValue();
											ClanExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Clan ("+ ItemId +")");
											ClanExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusiveclanminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											ClanMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												ClanMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Clan Min. Level for Exlusive Clan ("+ ItemId +")");											
												ClanMinLevel = 0;
												isEnabled = false;
											}
										}										
									}
									
									try{
										_value = dd.getAttributes().getNamedItem("forexclusivechar").getNodeValue().toLowerCase();
										isCharExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isCharExclusive = false;
									}
									if(isCharExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivechardays").getNodeValue();
											CharDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Char ("+ ItemId +")");
											CharDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivecharprice").getNodeValue();
											CharExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Char ("+ ItemId +")");
											CharExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusivecharnminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											CharMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												CharMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Char Min Level for Char Exlusive ("+ ItemId +")");											
												CharMinLevel = 0;
												isEnabled = false;
											}
										}										
									}
									
									Vector<Integer>_ClassRestrictionTemp = new Vector<Integer>();
									try {
										_value = dd.getAttributes().getNamedItem("class_id_restriction").getNodeValue().toLowerCase();
										if(_value!=null) {
											if(_value.length()>0) {
												if(_value.indexOf(",")>0) {
													for(String idClass : _value.split(",")) {
														try {
															int _idClass = Integer.valueOf(idClass);
															_ClassRestrictionTemp.add(_idClass);
														}catch(Exception a) {
															_log.warning("Dressme Class id Restriction cant added this Data: " + idClass);
														}
													}
												}else {
													try {
														int _idClass = Integer.valueOf(_value);
														_ClassRestrictionTemp.add(_idClass);
													}catch(Exception a) {
														_log.warning("Dressme Class id Restriction cant added this Data: " + _value);
													}													
												}
											}
										}
									}catch(Exception a) {
										
									}									
									
									_dressItem IT = new _dressItem(Integer.valueOf(ItemId), Price, isJustForHero, isJustForNoble, isJustForTopPvPPk, isClanExclusive, ClanDays, ClanExclusivePrice, isCharExclusive, CharDays, CharExclusivePrice, 0, true, _ClassRestrictionTemp, CharMinLevel, ClanMinLevel, isEnabled);
									DRESSME_ITEMS.get("WEAPON").add(IT);
									_dressmeItemConfig CT = new _dressmeItemConfig(isJustForNoble, isJustForHero, isJustForTopPvPPk, isClanExclusive, IT._getOwnerClanId(), isCharExclusive, IT._getOwnerCharId(), IT);
									DRESSME_ITEM_CONFIG.put(IT.getIdItem(), CT);
								}
							}
						}
						else if(d.getNodeName().equalsIgnoreCase("cloak")){
							if(DRESSME_ITEMS==null){
								DRESSME_ITEMS.put("CLOAK", new Vector<_dressItem>());
							}else if(!DRESSME_ITEMS.containsKey("CLOAK")){
								DRESSME_ITEMS.put("CLOAK", new Vector<_dressItem>());
							}							
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String ItemName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ItemId = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									String Price = dd.getAttributes().getNamedItem("cost").getNodeValue().toLowerCase();
									boolean isJustForHero = false;
									boolean isJustForNoble = false;
									boolean isJustForTopPvPPk = false;
									boolean isClanExclusive = false;
									boolean isCharExclusive = false;
									int ClanDays = 0;
									String ClanExclusivePrice = "";
									String CharExclusivePrice = "";
									int CharDays = 0;
									int CharMinLevel = 0;
									int ClanMinLevel = 0;
									boolean isEnabled = true;
									try{
										isJustForHero = Boolean.parseBoolean(dd.getAttributes().getNamedItem("forhero").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForNoble = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fornoble").getNodeValue().toLowerCase());
									}catch(Exception a){
									
									}
									try{
										isJustForTopPvPPk = Boolean.parseBoolean(dd.getAttributes().getNamedItem("fortoppvp").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}	
									String _value = "";
									try{
										_value = dd.getAttributes().getNamedItem("forexclusiveclan").getNodeValue().toLowerCase();
										isClanExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isClanExclusive = false;
									}
									if(isClanExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclandays").getNodeValue();
											ClanDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Clan ("+ ItemId +")");
											ClanDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusiveclanprice").getNodeValue();
											ClanExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Clan ("+ ItemId +")");
											ClanExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusiveclanminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											ClanMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												ClanMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Clan Min. Level for Exlusive Clan ("+ ItemId +")");											
												ClanMinLevel = 0;
												isEnabled = false;
											}
										}									
									}
									
									try{
										_value = dd.getAttributes().getNamedItem("forexclusivechar").getNodeValue().toLowerCase();
										isCharExclusive = Boolean.parseBoolean(_value);
									}catch(Exception a){
										isCharExclusive = false;
									}
									if(isCharExclusive){
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivechardays").getNodeValue();
											CharDays = Integer.valueOf(_value);
										}catch(Exception a){
											_log.warning("Error getting days for Exlusive Char ("+ ItemId +")");
											CharDays = 0;
											isEnabled = false;
										}
										try{
											_value = dd.getAttributes().getNamedItem("forexclusivecharprice").getNodeValue();
											CharExclusivePrice = _value;
										}catch(Exception a){
											_log.warning("Error getting Prices for Exlusive Char ("+ ItemId +")");
											CharExclusivePrice = "";
											isEnabled = false;
										}
										String ValTemp = "";
										try {
											ValTemp = dd.getAttributes().getNamedItem("forexclusivecharnminlevel").getNodeValue();
										}catch(Exception a) {
											ValTemp = "";
											CharMinLevel = 0;
										}
										
										if(!ValTemp.equals("")) {
											try {
												CharMinLevel = Integer.valueOf(ValTemp);
											}catch(Exception a) {
												_log.warning("Error getting Char Min Level for Char Exlusive ("+ ItemId +")");											
												CharMinLevel = 0;
												isEnabled = false;
											}
										}										
									}
									
									Vector<Integer>_ClassRestrictionTemp = new Vector<Integer>();
									try {
										_value = dd.getAttributes().getNamedItem("class_id_restriction").getNodeValue().toLowerCase();
										if(_value!=null) {
											if(_value.length()>0) {
												if(_value.indexOf(",")>0) {
													for(String idClass : _value.split(",")) {
														try {
															int _idClass = Integer.valueOf(idClass);
															_ClassRestrictionTemp.add(_idClass);
														}catch(Exception a) {
															_log.warning("Dressme Class id Restriction cant added this Data: " + idClass);
														}
													}
												}else {
													try {
														int _idClass = Integer.valueOf(_value);
														_ClassRestrictionTemp.add(_idClass);
													}catch(Exception a) {
														_log.warning("Dressme Class id Restriction cant added this Data: " + _value);
													}													
												}
											}
										}
									}catch(Exception a) {
										
									}									
									
									_dressItem IT = new _dressItem(Integer.valueOf(ItemId), Price, isJustForHero, isJustForNoble, isJustForTopPvPPk, isClanExclusive, ClanDays, ClanExclusivePrice, isCharExclusive, CharDays, CharExclusivePrice, 0, true, _ClassRestrictionTemp, CharMinLevel, ClanMinLevel, isEnabled);
									DRESSME_ITEMS.get("CLOAK").add(IT);
									_dressmeItemConfig CT = new _dressmeItemConfig(isJustForNoble, isJustForHero, isJustForTopPvPPk, isClanExclusive, IT._getOwnerClanId(), isCharExclusive, IT._getOwnerCharId(), IT);
									DRESSME_ITEM_CONFIG.put(IT.getIdItem(), CT);
								}
							}
						}						
					}
				}
			}
		}catch(Exception a){
			_log.warning("Dressme load -> " + a.getMessage());
		}
		try{
			Comparator<_dressItem> NameAZ = (p1,p2) -> p1.getItemName() .compareToIgnoreCase(p2.getItemName());
			Collections.sort(DRESSME_ITEMS.get("ARMOR"),NameAZ);
		}catch(Exception a){
			
		}
		try{
			Comparator<_dressItem> NameAZ = (p1,p2) -> p1.getItemName() .compareToIgnoreCase(p2.getItemName());
			Collections.sort(DRESSME_ITEMS.get("WEAPON"),NameAZ);
		}catch(Exception a){
			
		}
		try{
			Comparator<_dressItem> NameAZ = (p1,p2) -> p1.getItemName() .compareToIgnoreCase(p2.getItemName());
			Collections.sort(DRESSME_ITEMS.get("SHIELD"),NameAZ);
		}catch(Exception a){
			
		}
		try{
			Comparator<_dressItem> NameAZ = (p1,p2) -> p1.getItemName() .compareToIgnoreCase(p2.getItemName());
			Collections.sort(DRESSME_ITEMS.get("CLOAK"),NameAZ);
		}catch(Exception a){
			
		}		
	}
	public final dress_player getDressme(int idPlayer) {
		return DRESSME_PLAYER.get(idPlayer);
	}
	public void removeDressmeFromPlayer(int idPlayer){
		try{
			DRESSME_PLAYER.remove(idPlayer);
		}catch(Exception a){
			
		}
	}	

	public void loadDressme(L2PcInstance player){
		loadDressme(player.getObjectId());
	}
	
	public void loadDressme(int Idplayer){
		if(DRESSME_PLAYER == null){
			dress_player DP = new dress_player(Idplayer);
			DRESSME_PLAYER.put(Idplayer, DP);
		}else if(!DRESSME_PLAYER.containsKey(Idplayer)){
			dress_player DP = new dress_player(Idplayer);
			DRESSME_PLAYER.put(Idplayer, DP);
		}
	}
	
	public boolean isTestingDressme(L2PcInstance player){
		if(!general.STATUS_DRESSME){
			return false;
		}else if(!general.DRESSME_CAN_USE_IN_OLYS && (player.isInOlympiad() || player.isInOlympiadMode()) ){
			return false;
		}
		
		if(DRESSME_PLAYER==null){
			return false;
		}else if(DRESSME_PLAYER.size()==0){
			return false;
		}else if(!DRESSME_PLAYER.containsKey(player.getObjectId())){
			return false;
		}else if(DRESSME_PLAYER.get(player.getObjectId()).getDressme()==null){
			return false;
		}
		
		return DRESSME_PLAYER.get(player.getObjectId()).getDressme().isTrySet();
	}
	
	public int getVirtualDress_Armor(int idPlayer, int PPD, boolean isFromCharInfo){
		L2PcInstance player = opera.getPlayerbyID(idPlayer);
		boolean isDressit = true;
		if(player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_GLOVES) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_CHEST) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_LEGS) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_FEET) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_HEAD) == null
		)
		{
			isDressit = false;
		}
		
		int idVirtual = DRESSME_PLAYER.get(idPlayer).getIds(PPD, isFromCharInfo, isDressit);
		return idVirtual;
	}
	
	public int getVirtualDress_Head(int idPlayer, int PPD, boolean isFromCharInfo){
		int idVirtual = DRESSME_PLAYER.get(idPlayer).getIds(PPD, isFromCharInfo, true);
		return idVirtual;		
	}
	
	public int getVirtualDress_Head(L2PcInstance player, int PPD, boolean isFromCharInfo){
		if(!general.STATUS_DRESSME){
			return player.getInventory().getPaperdollItemId(PPD);
		}else if(!general.DRESSME_CAN_USE_IN_OLYS && (player.isInOlympiad() || player.isInOlympiadMode()) ){
			return player.getInventory().getPaperdollItemId(PPD);
		}
		
		int idVirtual = DRESSME_PLAYER.get(player.getObjectId()).getIds(Inventory.PAPERDOLL_HAIR, isFromCharInfo, true);
		
		L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR);
		L2ItemInstance equipedItem2 = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR2);
		
	    if(equipedItem != null || equipedItem2 != null){
	    	return -100;
	    }
	    try{
		   return idVirtual;
	    }catch(Exception a){
	    	  return -100;
	    }		
	}
	
	@SuppressWarnings("unused")
	public int getVirtualDress_Armor(L2PcInstance player, int PPD, boolean isFromCharInfo, boolean IsFullDress){
		
		if(!general.STATUS_DRESSME){
			return player.getInventory().getPaperdollItemId(PPD);
		}else if(!general.DRESSME_CAN_USE_IN_OLYS && (player.isInOlympiad() || player.isInOlympiadMode()) ){
			return player.getInventory().getPaperdollItemId(PPD);
		}
		
		int idVirtual = DRESSME_PLAYER.get(player.getObjectId()).getIds(PPD, isFromCharInfo, IsFullDress);
		
		L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(PPD);
		
		L2Item vItem = ItemData.getInstance().getTemplate(idVirtual);
	    if(equipedItem == null){
	    	return -100;
	    }
	    try{
	    	L2Armor equipedItemArmor = (L2Armor)player.getInventory().getPaperdollItem(PPD).getItem();
		    int equipedItemArmorId = player.getInventory().getPaperdollItemId(PPD);
		    L2Armor virtualGloves = (L2Armor)ItemData.getInstance().getTemplate(idVirtual);
		    if(idVirtual != 0){
		    	return armorMatching(player,virtualGloves.getItemType(), equipedItemArmor.getItemType(),idVirtual, equipedItemArmorId);
		    }else{
		    	return equipedItemArmorId;
		    }
	    }catch(Exception a){
	    	  return -100;
	    }
	}	
	public int getRLHAND(int IdPlayer, int PPD, boolean isFromCharInfo){
		int weaponRHANDId = DRESSME_PLAYER.get(IdPlayer).getIds(PPD, isFromCharInfo, true);
		return weaponRHANDId;
	}
	
	public int getRLHAND(L2PcInstance player, int PPD, boolean isFromCharInfo){
		
		if(!general.STATUS_DRESSME){
			return player.getInventory().getPaperdollItemId(PPD);
		}else if(!general.DRESSME_CAN_USE_IN_OLYS && (player.isInOlympiad() || player.isInOlympiadMode()) ){
			return player.getInventory().getPaperdollItemId(PPD);
		}
		
        int weaponRHANDId = DRESSME_PLAYER.get(player.getObjectId()).getIds(PPD, isFromCharInfo, true);
        L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(PPD);
        int equipedItemId = player.getInventory().getPaperdollItemId(PPD);
        L2Item virtualItem = ItemData.getInstance().getTemplate(weaponRHANDId);
        if((equipedItem == null) || (weaponRHANDId == 0)){
      	  return equipedItemId;
        }

        if((equipedItem.getItem() instanceof L2Weapon) && (virtualItem instanceof L2Weapon))
        {
            L2Weapon weapon = (L2Weapon) equipedItem.getItem();
            L2Weapon virtualweapon = (L2Weapon)virtualItem;

            return weaponMatching(player,virtualweapon.getItemType(), weapon.getItemType(), weaponRHANDId, equipedItemId);
        }
        else if((equipedItem.getItem() instanceof L2Armor) && (virtualItem instanceof L2Armor))
        {
            L2Armor armor = (L2Armor) equipedItem.getItem();
            L2Armor virtualarmor = (L2Armor)virtualItem;

            return armorMatching(player,virtualarmor.getItemType(), armor.getItemType(), weaponRHANDId, equipedItemId);
        }
        return equipedItemId;
    }
	
	public int getVirtualSlot(L2PcInstance player, int idSlot){
		return getVirtualSlot(player, idSlot, false);
	}
	
	public int getVirtualSlot(int idPlayer, int idSlot, boolean isFromCharInfo){
		
		switch(idSlot){
  	  	case Inventory.PAPERDOLL_GLOVES:
  	  	case Inventory.PAPERDOLL_CHEST:
  	  	case Inventory.PAPERDOLL_LEGS:
  	  	case Inventory.PAPERDOLL_FEET:
  	  	case Inventory.PAPERDOLL_CLOAK:
  	  		return getVirtualDress_Armor(idPlayer, idSlot, isFromCharInfo);
  	  	case Inventory.PAPERDOLL_HAIR:
	  	case Inventory.PAPERDOLL_HAIR2:
  			return getVirtualDress_Head(idPlayer, idSlot, isFromCharInfo);
  	  	case Inventory.PAPERDOLL_RHAND:
  	  	case Inventory.PAPERDOLL_LHAND:
  	  		return getRLHAND(idPlayer, idSlot,isFromCharInfo);
  	  }		
		return -100;
	}
	
	public int getVirtualSlot(L2PcInstance player, int idSlot, boolean isFromCharInfo){
		boolean isDressit = true;
		if(player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_GLOVES) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_CHEST) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_LEGS) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_FEET) == null ||
		player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_HEAD) == null
		)
		{
			isDressit = false;
		}
		
	  	  switch(idSlot){
	  	  	case Inventory.PAPERDOLL_GLOVES:
	  	  	case Inventory.PAPERDOLL_CHEST:
	  	  	case Inventory.PAPERDOLL_LEGS:
	  	  	case Inventory.PAPERDOLL_FEET:
	  	  	case Inventory.PAPERDOLL_CLOAK:
	  	  		return getVirtualDress_Armor(player, idSlot, isFromCharInfo, isDressit);
	  	  	case Inventory.PAPERDOLL_HAIR:
	  	  	case Inventory.PAPERDOLL_HAIR2:
	  	  		if(!isUsingDeco(player)) {
	  	  			if(isDressit) {
	  	  				return getVirtualDress_Head(player, idSlot, isFromCharInfo);
	  	  			}else{
	  	  				return -100;
	  	  			}
	  	  		}
	  	  		break;
	  	  	case Inventory.PAPERDOLL_RHAND:
	  	  	case Inventory.PAPERDOLL_LHAND:
	  	  		return getRLHAND(player, idSlot,isFromCharInfo);
	  	  }
	  	  return -100;
	}
	
	private boolean isUsingDeco(L2PcInstance player) {
		L2ItemInstance tmp = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR);
		L2ItemInstance tmp2 = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR2);
		if(tmp==null && tmp2 == null) {
			return false;
		}
		return true;
	}
	
  	public static dressme getInstance(){
	    return SingletonHolder._instance;
	}
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder{
	    protected static final dressme _instance = new dressme();
	}
}
class _dressmeItemConfig{
	private final boolean ONLY_NOBLE;
	private final boolean ONLY_HERO;
	private final boolean ONLY_TOP_PVP_PK;
	private final boolean ONLY_FOR_CLAN;
	private final boolean ONLY_FOR_CHAR;
	private final int ONLYFORCLAN_ID_CLAN;
	private final int ONLYFORCHAR_ID_CHAR;
	private final int ID_ITEM;
	public _dressmeItemConfig(boolean onlyNoble, boolean onlyHero, boolean onlyTopPVPPK, boolean onlyCLan, int clanID, boolean onlyChar, int charID, _dressItem dressItem){
		ONLY_NOBLE = onlyNoble;
		ONLY_HERO = onlyHero;
		ONLY_TOP_PVP_PK = onlyTopPVPPK;
		ONLY_FOR_CLAN = onlyCLan;
		ONLYFORCLAN_ID_CLAN = clanID;
		ONLYFORCHAR_ID_CHAR = charID;
		ONLY_FOR_CHAR = onlyChar;
		ID_ITEM = dressItem.getIdItem();
	}
	
	public boolean isOnlyNoble(){
		return  ONLY_NOBLE;
	}
	public boolean isOnlyHero(){
		return ONLY_HERO;
	}
	public boolean isOnlyTopPvPPK(){
		return ONLY_TOP_PVP_PK;
	}
	public boolean isOnlyForClan(){
		return ONLY_FOR_CLAN;
	}
	public int getIdClanOwner(){
		return ONLYFORCLAN_ID_CLAN;
	}
	public boolean isOnlyForChar(){
		return ONLY_FOR_CHAR;
	}
	public int getIdCharOwner(){
		return ONLYFORCHAR_ID_CHAR;
	}	
	public _dressItem getDressItem(){
		@SuppressWarnings("rawtypes")
		Iterator itr = dressme.DRESSME_ITEMS.entrySet().iterator();
		while(itr.hasNext()){
			@SuppressWarnings("rawtypes")
			Map.Entry InfoItem =(Map.Entry)itr.next();
			String Section = (String)InfoItem.getKey();
			for(_dressItem _data : dressme.DRESSME_ITEMS.get(Section)){
				if(_data.getIdItem() == this.ID_ITEM){
					return _data;
				}
			}
		}
		return null;
	}
}
