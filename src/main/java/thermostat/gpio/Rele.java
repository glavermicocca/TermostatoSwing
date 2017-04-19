package thermostat.gpio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import mapper.Mapper;

public class Rele {
	
	public static final String CALDAIA = "23";
	public static final String LEFT = "24";
	public static final String RIGHT = "25";
	
	public static final String ON = "1";
	public static final String OFF = "0";
	
	public Rele() {
		super();
		this.set(CALDAIA, OFF);
	}

	public void set(String pin, String state)
	{		
		//System.err.println("STO SETTANDO CON QUESTI VALORI " + state);
		
		Date dateTime = new Date(System.currentTimeMillis());
		//System.err.println(new SimpleDateFormat("dd.MM.yy - HH:mm.ss").format(dateTime));
		
		try
		{
			//System.err.println(Mapper.ABSOLUTE_PATH_APPLICATION + "resources/rele " + pin + " " + state);
			Process p = Runtime.getRuntime().exec("" + Mapper.ABSOLUTE_PATH_APPLICATION + "resources/rele " + pin + " " + state);
		    p.waitFor();
		
		    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    
		    String line = "";
		    while ((line = reader.readLine())!= null) {
		    	//System.err.println(line);
		    }
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}

