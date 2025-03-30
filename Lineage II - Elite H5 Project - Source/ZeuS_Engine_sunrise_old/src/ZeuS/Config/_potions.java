package ZeuS.Config;

public class _potions {
	private final int CP;
	private final int HP;
	private final int MP;
	private final int OLY_CP;
	private final int OLY_HP;
	private final int OLY_MP;
	public _potions(int _cp, int _hp, int _mp, int _oly_cp, int _oly_hp, int _oly_mp){
		this.CP = _cp;
		this.HP = _hp;
		this.MP = _mp;
		this.OLY_CP = _oly_cp;
		this.OLY_HP = _oly_hp;
		this.OLY_MP = _oly_mp;
	}
	public int getCP(){
		return this.CP;
	}
	public int getHP(){
		return this.HP;
	}
	public int getMP(){
		return this.MP;
	}
	public int getOlyCP(){
		return this.OLY_CP;
	}
	public int getOlyHP(){
		return this.OLY_HP;
	}
	public int getOlyMP(){
		return this.OLY_MP;
	}	
}
