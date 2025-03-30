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

import ZeuS.Config._potions;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.holders.SkillHolder;
import l2r.gameserver.model.items.L2Item;

public class potionSystem {
	private static final Logger _log = Logger.getLogger(potionSystem.class.getName());
	private static Map<Integer, _potions> POTIONS_CLASS = new HashMap<Integer, _potions>();
	private static Vector<Integer> CP_POTIONS_SKILL = new Vector<Integer>();
	private static Vector<Integer> HP_POTIONS_SKILL = new Vector<Integer>();
	private static Vector<Integer> MP_POTIONS_SKILL = new Vector<Integer>();
	private static boolean IS_ENABLED = false;
	private potionSystem _instance;
	public potionSystem getInstance(){
		return _instance;
	}
	private static void CleanVar(){
		try{
			POTIONS_CLASS.clear();
		}catch(Exception a){
			
		}
		try{
			CP_POTIONS_SKILL.clear();
		}catch(Exception a){
			
		}
		try{
			HP_POTIONS_SKILL.clear();
		}catch(Exception a){
			
		}
		try{
			MP_POTIONS_SKILL.clear();
		}catch(Exception a){
			
		}
	}
	public static void loadPotions(){
		CleanVar();
		File dir = new File("./config/zeus/zeus_potions_system.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("config")){
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String NameConfig = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String Value = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									switch(NameConfig.toLowerCase()){
										case "enable":
											IS_ENABLED = Boolean.valueOf(Value);
											break;
										case "healing_cp_potion_ids":
											if(Value!=null){
												if(Value.indexOf(",") > 0){
													for(String _id : Value.split(",")){
														L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(_id));
														if(_item == null){
															_log.warning("  Potion System Cant load Item with ID ->" + _id);
															continue;
														}
														if(_item.getSkills()==null){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}else if(_item.getSkills().size()==0){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}
														for(SkillHolder _skill : _item.getSkills()){
															CP_POTIONS_SKILL.add( _skill.getSkillId() );
														}
													}
												}else{
													L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(Integer.valueOf(Value)));
													if(_item == null){
														_log.warning("  Potion System Cant load Item with ID ->" + Value);
														continue;
													}
													if(_item.getSkills()==null){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}else if(_item.getSkills().size()==0){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}													
													for(SkillHolder _skill : _item.getSkills()){
														CP_POTIONS_SKILL.add( _skill.getSkillId() );
													}													
												}
											}
											break;
										case "healing_hp_potion_ids":
											if(Value!=null){
												if(Value.indexOf(",") > 0){
													for(String _id : Value.split(",")){
														L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(_id));
														if(_item == null){
															_log.warning("  Potion System Cant load Item with ID ->" + _id);
															continue;
														}
														if(_item.getSkills()==null){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}else if(_item.getSkills().size()==0){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}														
														for(SkillHolder _skill : _item.getSkills()){
															HP_POTIONS_SKILL.add( _skill.getSkillId() );
														}
													}
												}else{
													L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(Value));
													if(_item == null){
														_log.warning("  Potion System Cant load Item with ID ->" + Value);
														continue;
													}
													if(_item.getSkills()==null){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}else if(_item.getSkills().size()==0){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}													
													for(SkillHolder _skill : _item.getSkills()){
														HP_POTIONS_SKILL.add( _skill.getSkillId() );
													}
												}
											}
											break;
										case "healing_mp_potion_ids":
											if(Value!=null){
												if(Value.indexOf(",") > 0){
													for(String _id : Value.split(",")){
														L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(_id));
														if(_item == null){
															_log.warning("  Potion System Cant load Item with ID ->" + _id);
															continue;
														}
														if(_item.getSkills()==null){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}else if(_item.getSkills().size()==0){
															_log.warning("  Potion System Cant load Skill from item with ID ->" + _id);
															continue;															
														}														
														for(SkillHolder _skill : _item.getSkills()){
															MP_POTIONS_SKILL.add( _skill.getSkillId() );
														}
													}
												}else{
													L2Item _item = ItemData.getInstance().getTemplate(Integer.valueOf(Value));
													if(_item == null){
														_log.warning("  Potion System Cant load Item with ID ->" + Value);
														continue;
													}
													if(_item.getSkills()==null){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}else if(_item.getSkills().size()==0){
														_log.warning("  Potion System Cant load Skill from item with ID ->" + Value);
														continue;															
													}													
													for(SkillHolder _skill : _item.getSkills()){
														MP_POTIONS_SKILL.add( _skill.getSkillId() );
													}
												}
											}
											break;
									}									
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("potions")){
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("potion")){
									String class_ids = dd.getAttributes().getNamedItem("class_id").getNodeValue().toLowerCase();
									int cp_value = 0;
									int hp_value = 0;
									int mp_value = 0;
									int oly_cp_value = 0;
									int oly_hp_value = 0;
									int oly_mp_value = 0;									
									if(class_ids==null){
										continue;
									}
									if(class_ids.length()==0){
										continue;
									}
									String _value;
									_value = dd.getAttributes().getNamedItem("cp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										cp_value = Integer.valueOf(_value);										
									}
									_value = dd.getAttributes().getNamedItem("hp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										hp_value = Integer.valueOf(_value);
									}
									_value = dd.getAttributes().getNamedItem("mp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										mp_value = Integer.valueOf(_value);										
									}
									_value = dd.getAttributes().getNamedItem("oly_cp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										oly_cp_value = Integer.valueOf(_value);										
									}
									_value = dd.getAttributes().getNamedItem("oly_hp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										oly_hp_value = Integer.valueOf(_value);
									}
									_value = dd.getAttributes().getNamedItem("oly_mp_value").getNodeValue().toLowerCase();
									if(_value==null || _value.length()>0){
										oly_mp_value = Integer.valueOf(_value);										
									}
									_potions _tmp = new _potions(cp_value, hp_value, mp_value, oly_cp_value, oly_hp_value, oly_mp_value);
									if(class_ids.indexOf(",")>0){
										for(String _ids : class_ids.split(",")){
											POTIONS_CLASS.put(Integer.valueOf(_ids), _tmp);											
										}
									}else{
										POTIONS_CLASS.put(Integer.valueOf(class_ids), _tmp);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception a){
			
		}
		if(POTIONS_CLASS==null){
			_log.warning("ZeuS Potion System has Load 0 Properties");
		}else if(POTIONS_CLASS.size()==0){
			_log.warning("ZeuS Potion System has Load 0 Properties");
		}else{
			_log.warning("ZeuS Potion System has Load "+ String.valueOf(POTIONS_CLASS.size()) +" Properties");
		}
	}
	public static final boolean isEnabled(){
		return IS_ENABLED;
	}
	
	private static boolean isSkillInCP(int _idSkill){
		if(CP_POTIONS_SKILL!=null){
			if(CP_POTIONS_SKILL.size()>0){
				return CP_POTIONS_SKILL.contains(_idSkill);
			}
		}
		return false;
	}
	private static boolean isSkillInHP(int _idSkill){
		if(HP_POTIONS_SKILL!=null){
			if(HP_POTIONS_SKILL.size()>0){
				return HP_POTIONS_SKILL.contains(_idSkill);
			}
		}
		return false;
	}
	private static boolean isSkillInMP(int _idSkill){
		if(MP_POTIONS_SKILL!=null){
			if(MP_POTIONS_SKILL.size()>0){
				return MP_POTIONS_SKILL.contains(_idSkill);
			}
		}
		return false;
	}	
	
	public static final int getCP(int idClass, int _idskill, L2PcInstance player){
		if(IS_ENABLED && isSkillInCP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getCP();
					}
				}
			}
		}
		return 0;
	}
	public static final int getHP(int idClass, int _idskill, L2PcInstance player){
		if(IS_ENABLED && isSkillInHP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getHP();
					}
				}
			}
		}
		return 0;
	}
	public static final int getMP(int idClass, int _idskill, L2PcInstance player){
		if(IS_ENABLED && isSkillInMP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getMP();
					}
				}
			}
		}
		return 0;
	}
	public static final int getOlyCP(int idClass, int _idskill){
		if(IS_ENABLED && isSkillInCP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getOlyCP();
					}
				}
			}
		}
		return 0;
	}
	public static final int getOlyHP(int idClass, int _idskill){
		if(IS_ENABLED && isSkillInHP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getOlyHP();
					}
				}
			}
		}
		return 0;
	}
	public static final int getOlyMP(int idClass, int _idskill){
		if(IS_ENABLED && isSkillInMP(_idskill)){
			if(POTIONS_CLASS!=null){
				if(POTIONS_CLASS.size()>0){
					if(POTIONS_CLASS.containsKey(idClass)){
						return POTIONS_CLASS.get(idClass).getOlyMP();
					}
				}
			}
		}
		return 0;
	}	
}
