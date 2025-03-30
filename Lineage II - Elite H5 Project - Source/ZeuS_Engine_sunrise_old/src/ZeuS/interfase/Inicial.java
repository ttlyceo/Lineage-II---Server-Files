package ZeuS.interfase;

public class Inicial {
	
	public static void start()
	{
		
		try
		{
			getClass("ZeuS").getMethod("ZeuSStart").invoke(null);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static Class<?> getClass(String name)
	{
		Class<?> c = null;
		try
		{
			c = Class.forName("ZeuS.interfase." + name);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return c;
	}		
	

}
