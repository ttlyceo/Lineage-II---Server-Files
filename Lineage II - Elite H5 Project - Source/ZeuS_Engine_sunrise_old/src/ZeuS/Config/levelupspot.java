package ZeuS.Config;

import java.util.logging.Logger;


public class levelupspot {
	
	public final Logger _log = Logger.getLogger(levelupspot.class.getName());
	
	@SuppressWarnings("unused")
	private int LEVEL;
	private String NOMBRE;
	private int IMAGEN;
	private int LOC_X;
	private int LOC_Y;
	private int LOC_Z;
	
	public int getX(){
		return LOC_X;
	}
	
	public int getY(){
		return LOC_Y;
	}
	
	public int getZ(){
		return LOC_Z;
	}
	
	public String getName(){
		return NOMBRE;
	}
	
	public int getImagen(){
		return IMAGEN;
	}
	
	
	public levelupspot(){
		
	}
	
	public levelupspot(int _Level, String _Nombre, int _Imagen, int _X, int _Y, int _Z){
		this.LEVEL = _Level;
		this.NOMBRE = _Nombre;
		this.IMAGEN = _Imagen;
		this.LOC_X = _X;
		this.LOC_Y = _Y;
		this.LOC_Z = _Z;
	}
}
