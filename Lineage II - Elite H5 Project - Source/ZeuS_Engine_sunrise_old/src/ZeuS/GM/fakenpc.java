package ZeuS.GM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.dressme.dressme;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.FakePc;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;

public class fakenpc{
	private static Logger _log = Logger.getLogger(fakenpc.class.getName());
	private final Map<Integer, fakeNpcVar> _fakesNps = new HashMap<Integer, fakeNpcVar>();
	private final Map<Integer, Integer> Player_IdNpc = new HashMap<Integer, Integer>();
	
	protected fakenpc(){
		loadData();
	}
	
	public FakePc getFakeData(int IdNpc){
		FakePc returnn = null;
		if(_fakesNps != null){
			if(_fakesNps.containsKey(IdNpc)){
				returnn = _fakesNps.get(IdNpc).fakeData;
			}
		}
		
		return returnn;
	}
	
	public L2NpcTemplate getTemplate(int IdNpc){
		L2NpcTemplate returnn = null;
		
		if(_fakesNps != null){
			if(_fakesNps.containsKey(IdNpc)){
				returnn = _fakesNps.get(IdNpc).fakeTemplate;
			}
		}
		
		return returnn;		
	}
	
	public void setHero(int idNpc, boolean _set){
		if(_fakesNps==null){
			return;
		}
		if(!_fakesNps.containsKey(idNpc)){
			return;
		}		
		fakeNpcVar Tem = _fakesNps.get(idNpc);
		Tem.setHero(_set);
	}
	
	public void createFakeClone(L2PcInstance player, L2Npc npcTarget, boolean isFromPlayer){
		fakeNpcVar Temp = new fakeNpcVar(player, npcTarget, isFromPlayer);
		_fakesNps.put(npcTarget.getId(), Temp);
	}
	
	public void createFakeClone(L2PcInstance player, int IdNpc, boolean isFromNpc){
		fakeNpcVar Tem = new fakeNpcVar(player, IdNpc, isFromNpc);
		_fakesNps.put(IdNpc, Tem);
		Player_IdNpc.put(player.getObjectId(), IdNpc);
	}
	
	public void createFakeClone(L2PcInstance player, int IdNpc){
		fakeNpcVar Tem = new fakeNpcVar(player, IdNpc);
		_fakesNps.put(IdNpc, Tem);
		Player_IdNpc.put(player.getObjectId(), IdNpc);
	}
	
	public void createFakeClone(L2PcInstance player, int IdNpc, boolean isForOly, String Place, boolean isFromPlayer){
		fakeNpcVar Tem = new fakeNpcVar(player, IdNpc, isForOly, Place, isFromPlayer);
		_fakesNps.put(IdNpc, Tem);
		Player_IdNpc.put(player.getObjectId(), IdNpc);
	}
	
	public void recallNpc(int INSTANCE_ID, int idNpc){
		if(INSTANCE_ID==0){
			for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(idNpc)){
				L2Object npcTe = SpawnLo.getLastSpawn();
				if(npcTe instanceof L2Npc){
					L2Npc target = (L2Npc) npcTe;
					updateNpc(target, INSTANCE_ID);
				}
			}
		}else{
			Instance Instancia = InstanceManager.getInstance().getInstance(INSTANCE_ID);
			for(L2Npc Nnpc : Instancia.getNpcs()){
				if(Nnpc.getId() == idNpc){
					updateNpc(Nnpc, INSTANCE_ID);
				}
			}
		}
	}
	
	private void updateNpc(L2Npc Fnpc, int INSTANCE_ID){
		int TX = -100374, TY = -260284, TZ = -15537;		
		Location locNpc = Fnpc.getLocation();
		Fnpc.broadcastInfo();
		Fnpc.broadcastStatusUpdate();
		Fnpc.teleToLocation(TX, TY, TZ);
		Fnpc.broadcastInfo();
		Fnpc.teleToLocation(locNpc);
		Fnpc.setInstanceId(INSTANCE_ID);
		Fnpc.broadcastInfo();		
	}	
	
	public void resetFake(L2PcInstance player, int idNpc){
		int IdInstance = player.getInstanceId();		
		resetFake(IdInstance, idNpc);
	}

	public void resetFake(L2PcInstance player, L2Npc _npc){
		if(_fakesNps==null){
			return;
		}
		if(!_fakesNps.containsKey(_npc.getId())){
			return;
		}
		fakeNpcVar Tem = _fakesNps.get(_npc.getId());
		Tem.removeMe();
		_fakesNps.remove(_npc.getId());
		recallNpc(player.getInstanceId(),_npc.getId());		
	}
	
	public void resetFake(int IdInstance, int idNpc){
		if(_fakesNps==null){
			return;
		}
		if(!_fakesNps.containsKey(idNpc)){
			return;
		}
		fakeNpcVar Tem = _fakesNps.get(idNpc);
		Tem.removeMe();
		_fakesNps.remove(idNpc);
		recallNpc(IdInstance,idNpc);	
	}
	
	public void loadData(){
		_fakesNps.clear();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement st = con.createStatement();
				ResultSet rset = st.executeQuery("SELECT * FROM zeus_fake_heroes")){
				fakeNpcVar fpc = null;
				while (rset.next()){
					int npcId = rset.getInt("id");
					int idPlayer = rset.getInt("idPlayer");
					fpc = new fakeNpcVar(false, true, npcId, idPlayer, rset.getInt("race"), rset.getInt("sex"), rset.getInt("plClass"), rset.getString("plTitle"), rset.getString("plTitleColor"), rset.getString("plName"), rset.getString("plNameColor"), rset.getInt("hairStyle"), rset.getInt("hairColor"), rset.getInt("face"), rset.getInt("RHand"), rset.getInt("LHand"), rset.getInt("Gloves"), rset.getInt("Chest"), rset.getInt("Legs"), rset.getInt("Feet"), rset.getInt("LRHand"), rset.getInt("Hair1"), rset.getInt("Hair2"), rset.getInt("enchantEffect"), rset.getByte("hero"), rset.getInt("cloak"), rset.getInt("idClan"), rset.getInt("idCrestClan"), rset.getInt("idAlly"), rset.getInt("idCrestAlly"));           
					_fakesNps.put(npcId, fpc);
					Player_IdNpc.put(idPlayer, npcId);
				}
				rset.close();
		}catch (Exception e){
				_log.warning("FakePcsTable: Error while creating fake pc table: " + e.getMessage());
		}
		
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement st = con.createStatement();
				ResultSet rset = st.executeQuery("SELECT * FROM zeus_fake_npc")){
				fakeNpcVar fpc = null;
				while (rset.next()){
					int npcId = rset.getInt("id");
					int idPlayer = -1;
					fpc = new fakeNpcVar(true, true, npcId, idPlayer, rset.getInt("race"), rset.getInt("sex"), rset.getInt("plClass"), rset.getString("plTitle"), rset.getString("plTitleColor"), rset.getString("plName"), rset.getString("plNameColor"), rset.getInt("hairStyle"), rset.getInt("hairColor"), rset.getInt("face"), rset.getInt("RHand"), rset.getInt("LHand"), rset.getInt("Gloves"), rset.getInt("Chest"), rset.getInt("Legs"), rset.getInt("Feet"), rset.getInt("LRHand"), rset.getInt("Hair1"), rset.getInt("Hair2"), rset.getInt("enchantEffect"), rset.getByte("hero"), rset.getInt("cloak"), rset.getInt("idClan"), rset.getInt("idCrestClan"), rset.getInt("idAlly"), rset.getInt("idCrestAlly"));           
					_fakesNps.put(npcId, fpc);
					Player_IdNpc.put(idPlayer, npcId);
				}
				rset.close();
		}catch (Exception e){
				_log.warning("FakePcsTable: Error while creating fake pc table: " + e.getMessage());
		}		
		
		
		
		_log.info(getClass().getSimpleName() + ": Loaded " + _fakesNps.size() + " Fake's NPC from Engine.");
	}
	
	public fakeNpcVar getFakePc(int npcId)
	{
		return _fakesNps.get(npcId);
	}
	
	public static fakenpc getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final fakenpc _instance = new fakenpc();
	}	
}

class fakeNpcVar {
	private static Logger _log = Logger.getLogger(fakenpc.class.getName());
	public int idNpc;
	public int idPlayer;
	public int locx;
	public int locy;
	public int locz;
	public int heading;
	private int INSTANCE_ID;
	public FakePc fakeData = new FakePc();
	public L2NpcTemplate fakeTemplate = null;
	private boolean _IS_NPC = false;
	
	public final int getInstanceId(){
		return this.INSTANCE_ID;
	}
	
	public void setHero(boolean setHero){
		this.fakeData.hero = (byte) (setHero ? 1 : 0) ;
		this.saveInDB(this.idNpc);
		fakenpc.getInstance().recallNpc(this.INSTANCE_ID, this.idNpc);
	}
	
	public fakeNpcVar(L2PcInstance _player, L2Npc _NpcTarget, boolean isFromPlayer){
		this.idNpc = _NpcTarget.getId();
		this._IS_NPC = true;
		this.saveTemplate(_player, false, "", true, _NpcTarget, isFromPlayer);
	}
	
	public fakeNpcVar(L2PcInstance _player, int _IdNpc){
		this.idNpc = _IdNpc;
		this.saveTemplate(_player, false, "", true);
	}
	public fakeNpcVar(L2PcInstance _player, int _IdNpc, boolean _isNpc){
		this.idNpc = _IdNpc;
		L2Npc tmp = null;
		for(L2Spawn _t : SpawnTable.getInstance().getSpawns(_IdNpc)) {
			if(_t.getLastSpawn() != null) {
				tmp = _t.getLastSpawn();
				break;
			}
		}
		if(tmp==null) {
			return;
		}
		this.saveTemplate(_player, false, "", _isNpc, tmp, _isNpc);
		this._IS_NPC = true;
	}	
	
	public fakeNpcVar(L2PcInstance _player, int _IdNpc, boolean _ForOly, String _Place, boolean isFromPlayer){
		this.idNpc = _IdNpc;
		this.saveTemplate(_player, _ForOly, _Place, isFromPlayer);
	}
	public fakeNpcVar(boolean _isNpc, boolean isFromLoad, int _idNpc, int _idPlayer, int _race, int _sex, int _plClasss, String _title, String _titlecolor, String _name, String _nameColor, int _hairStyle, int _hairColor, int _face, int _pdRHand, int _pdLHand,int _pdGloves,int _pdChest, int _pdLegs, int _pdFeet, int _pdLRHand, int _pdHair, int _pdHair2, int _enchantEffect, byte _hero, int _cloak, int _idClan, int _idClanCrest, int _idAlly, int _idAllyCrest){
		this.idNpc = _idNpc;
		this.idPlayer = _idPlayer;
		this.fakeData.race = _race;
		this.fakeData.sex = _sex;
		this.fakeData.clazz = _plClasss;
		this.fakeData.title = _title;
		this.fakeData.titleColor = Integer.valueOf(_titlecolor);
		this.fakeData.name = _name;
		this.fakeData.nameColor = Integer.valueOf(_nameColor);
		this.fakeData.hairStyle = _hairStyle;
		this.fakeData.hairColor = _hairColor;
		this.fakeData.face = _face;
		this.fakeData.pdRHand = _pdRHand;
		this.fakeData.pdLHand = _pdLHand;
		this.fakeData.pdGloves = _pdGloves;
		this.fakeData.pdChest = _pdChest;
		this.fakeData.pdLegs = _pdLegs;
		this.fakeData.pdFeet = _pdFeet;
		this.fakeData.pdLRHand = _pdLRHand;
		this.fakeData.pdHair = _pdHair;
		this.fakeData.pdHair2 = _pdHair2;
		this.fakeData.enchantEffect = _enchantEffect;
		this.fakeData.hero = _hero;
		this.fakeData.pdBack = _cloak;
		this._IS_NPC = _isNpc;
		this.fakeData.clanID = _idClan;
		this.fakeData.clanCrestID = _idClanCrest;
		this.fakeData.allyID = _idAlly;
		this.fakeData.allyCrestID = _idAllyCrest;
		if(!isFromLoad){
			this.saveInDB(this.idNpc);
			fakenpc.getInstance().recallNpc(this.INSTANCE_ID, this.idNpc);
		}
	}
	
	protected void saveTemplate(L2PcInstance player, boolean forOly, String Place, boolean isFromPlayer){
		this.saveTemplate(player, forOly, Place, false, null, isFromPlayer);
	}
	
	protected void saveTemplate(L2PcInstance player, boolean forOly, String Place, boolean _isNpc, L2Npc _npc, boolean isFromPlayer){
		dressme.getInstance().loadDressme(player);
		if(_IS_NPC){
			this.idNpc = _npc.getId();
		}
		this._IS_NPC = _isNpc;
		this.idPlayer = player.getObjectId();
		this.INSTANCE_ID = player.getInstanceId();
		int SlotIn = Inventory.PAPERDOLL_RHAND;
		int td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.race = player.getRace().ordinal();
		this.fakeData.sex = player.getAppearance().getSex() ? 1 : 0;
		this.fakeData.clazz = player.getClassId().getId();
		this.fakeData.title = (!_IS_NPC ? (forOly ? Place : player.getTitle()) : _npc.getTemplate().getTitle());
		this.fakeData.titleColor = (!_IS_NPC ? player.getAppearance().getTitleColor() : 0x9CE8A9);
		this.fakeData.name = (_IS_NPC ? _npc.getName() : player.getName());
		this.fakeData.nameColor = (!_IS_NPC ? player.getAppearance().getNameColor() : 0xFFFFFF) ;
		this.fakeData.hairStyle = player.getAppearance().getHairStyle();
		this.fakeData.hairColor = player.getAppearance().getHairColor();
		this.fakeData.face = player.getAppearance().getFace();
		this.fakeData.pdRHand = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
		SlotIn = Inventory.PAPERDOLL_LHAND;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdLHand = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
		SlotIn = Inventory.PAPERDOLL_GLOVES;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdGloves = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
		SlotIn = Inventory.PAPERDOLL_CHEST;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdChest = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;		
		SlotIn = Inventory.PAPERDOLL_LEGS;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdLegs = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;		
		SlotIn = Inventory.PAPERDOLL_FEET;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdFeet = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
		SlotIn = Inventory.PAPERDOLL_CLOAK;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		this.fakeData.pdBack = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
		SlotIn = Inventory.PAPERDOLL_HAIR;
		td = dressme.getInstance().getVirtualSlot(player, SlotIn);
		if(td <= 0) {
			try{
				L2ItemInstance ItemTemp = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR);
				if(ItemTemp!=null){
					this.fakeData.pdHair = ItemTemp.getItem().getId();
				}
			}catch(Exception a){
				
			}
			try{
				L2ItemInstance ItemTemp2 = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_HAIR2);
				if(ItemTemp2!=null){
					this.fakeData.pdHair2 = ItemTemp2.getItem().getId();
				}
			}catch(Exception a){
				
			}
		}else {
			this.fakeData.pdHair = td;
			this.fakeData.pdHair2 = td;
		}
		try{
			this.fakeData.hero = (byte) (forOly ? 1 : 0);
		}catch(Exception a){
			
		}
		try{
			this.fakeData.enchantEffect = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).getEnchantLevel();
		}catch(Exception a){
			this.fakeData.enchantEffect = 0;
		}

		this.fakeData.clanID = 0;
		this.fakeData.allyID = 0;
		
		if(forOly || isFromPlayer) {
			if(player.getClan()!=null) {
				this.fakeData.clanID = player.getClanId();
				this.fakeData.clanCrestID = player.getClanCrestId();
				if(player.getClan().getAllyId()>0) {
					this.fakeData.allyID = player.getAllyId();
					this.fakeData.allyCrestID = player.getAllyCrestId();
				}				
			}
		}
		try{
			SlotIn = Inventory.PAPERDOLL_RHAND;
			td = dressme.getInstance().getVirtualSlot(player, SlotIn);
			int idUsing = (td == -100) ? player.getInventory().getPaperdollItemDisplayId(SlotIn) : td;
			L2Item _item = ItemData.getInstance().getTemplate(idUsing);
			int BodyPart = _item.getBodyPart();
			if(BodyPart == L2Item.SLOT_LR_HAND){
								
				this.fakeData.pdLHand = idUsing;
				this.fakeData.pdRHand = idUsing;
				this.fakeData.pdLRHand = idUsing;
			}
		}catch(Exception a){
			
		}
		
		this.locx = player.getLocation().getX();
		this.locy = player.getLocation().getY();
		this.locz = player.getLocation().getZ();
		this.heading = player.getLocation().getHeading();
		this.saveInDB(this.idNpc);
		fakenpc.getInstance().recallNpc(this.INSTANCE_ID, this.idNpc);
	}
	
	protected void saveTemplate(L2PcInstance player, boolean isFromPlayer){
		this.saveTemplate(player, false, "", isFromPlayer);	
	}
	
	public void removeMe(){
		String Query = "DELETE FROM " + (this._IS_NPC ? "zeus_fake_npc" : "zeus_fake_heroes") + " WHERE id=?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(Query))
			{
				statementt.setInt(1, this.idNpc);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}			
	}
	
	private void saveInDB(int idNpc){
		String InsertQuery = "INSERT INTO " + ( this._IS_NPC ? "zeus_fake_npc" : "zeus_fake_heroes" ) + " (id, idPlayer, race, sex, plClass, plTitle, plName, plTitleColor, plNameColor, hairStyle, hairColor, face, RHand, LHand, Gloves, Chest, Legs, Feet, LRHand, Hair1, Hair2, enchantEffect, hero, cloak, idClan, idAlly, idCrestClan, idCrestAlly) "+
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE race=?, sex=?, plClass=?, plTitle=?, plName=?, plTitleColor=?, plNameColor=?, hairStyle=?, hairColor=?, face=?, RHand=?, LHand=?, Gloves=?, Chest=?, Legs=?, Feet=?, LRHand=?, Hair1=?, Hair2=?, enchantEffect=?, hero=?, cloak=?, idClan=?, idAlly=?, idCrestClan=?, idCrestAlly=?";
		Connection con = null;
		PreparedStatement ins = null;
		int Fila = 1;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(InsertQuery);
			ins.setInt(Fila++, idNpc);
			ins.setInt(Fila++, this.idPlayer);
			ins.setInt(Fila++, this.fakeData.race);
			ins.setInt(Fila++, this.fakeData.sex);
			ins.setInt(Fila++, this.fakeData.clazz);
			ins.setString(Fila++, this.fakeData.title);
			ins.setString(Fila++, this.fakeData.name);
			ins.setString(Fila++, String.valueOf(this.fakeData.titleColor));
			ins.setString(Fila++, String.valueOf(this.fakeData.nameColor));
			ins.setInt(Fila++, this.fakeData.hairStyle);
			ins.setInt(Fila++, this.fakeData.hairColor);
			ins.setInt(Fila++, this.fakeData.face);
			ins.setInt(Fila++, this.fakeData.pdRHand);
			ins.setInt(Fila++, this.fakeData.pdLHand);
			ins.setInt(Fila++, this.fakeData.pdGloves);
			ins.setInt(Fila++, this.fakeData.pdChest);
			ins.setInt(Fila++, this.fakeData.pdLegs);
			ins.setInt(Fila++, this.fakeData.pdFeet);
			ins.setInt(Fila++, this.fakeData.pdLRHand);
			ins.setInt(Fila++, this.fakeData.pdHair);
			ins.setInt(Fila++, this.fakeData.pdHair2);
			ins.setInt(Fila++, this.fakeData.enchantEffect);
			ins.setInt(Fila++, this.fakeData.hero);
			ins.setInt(Fila++, this.fakeData.pdBack);
			ins.setInt(Fila++, this.fakeData.clanID);
			ins.setInt(Fila++, this.fakeData.allyID);
			ins.setInt(Fila++, this.fakeData.clanCrestID);
			ins.setInt(Fila++, this.fakeData.allyCrestID);
			
			
			ins.setInt(Fila++, this.fakeData.race);
			ins.setInt(Fila++, this.fakeData.sex);
			ins.setInt(Fila++, this.fakeData.clazz);
			ins.setString(Fila++, this.fakeData.title);
			ins.setString(Fila++, this.fakeData.name);
			ins.setString(Fila++, String.valueOf(this.fakeData.titleColor));
			ins.setString(Fila++, String.valueOf(this.fakeData.nameColor));
			ins.setInt(Fila++, this.fakeData.hairStyle);
			ins.setInt(Fila++, this.fakeData.hairColor);
			ins.setInt(Fila++, this.fakeData.face);
			ins.setInt(Fila++, this.fakeData.pdRHand);
			ins.setInt(Fila++, this.fakeData.pdLHand);
			ins.setInt(Fila++, this.fakeData.pdGloves);
			ins.setInt(Fila++, this.fakeData.pdChest);
			ins.setInt(Fila++, this.fakeData.pdLegs);
			ins.setInt(Fila++, this.fakeData.pdFeet);
			ins.setInt(Fila++, this.fakeData.pdLRHand);
			ins.setInt(Fila++, this.fakeData.pdHair);
			ins.setInt(Fila++, this.fakeData.pdHair2);
			ins.setInt(Fila++, this.fakeData.enchantEffect);
			ins.setInt(Fila++, this.fakeData.hero);
			ins.setInt(Fila++, this.fakeData.pdBack);
			ins.setInt(Fila++, this.fakeData.clanID);
			ins.setInt(Fila++, this.fakeData.allyID);
			ins.setInt(Fila++, this.fakeData.clanCrestID);
			ins.setInt(Fila++, this.fakeData.allyCrestID);			
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error 1 FakeNPC->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error 2 FakeNPC->" + a.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){

		}
	}
}