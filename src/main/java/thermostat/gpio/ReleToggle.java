package thermostat.gpio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import mapper.Mapper;

public class ReleToggle {
	
	public static final String LEFT = "24";
	public static final String RIGHT = "25";
	
	public static final String ON = "1";
	public static final String OFF = "0";
	
	public ReleToggle() {
		super();
	}

	public void set(String pin, String state)
	{		
		//System.err.println("STO SETTANDO CON QUESTI VALORI " + state);
		
		Date dateTime = new Date(System.currentTimeMillis());
		//System.err.println(new SimpleDateFormat("dd.MM.yy - HH:mm.ss").format(dateTime));
		
		try
		{
			//System.err.println(Mapper.ABSOLUTE_PATH_APPLICATION + "resources/rele " + pin + " " + state);
			Process p = Runtime.getRuntime().exec("" + Mapper.ABSOLUTE_PATH_APPLICATION + "resources/releToggle " + pin + " " + state);
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

