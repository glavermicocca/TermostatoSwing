package thermostat.gpio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import mapper.Mapper;

public class Rele {
	
	public static final int CALDAIA = 23;
	public static final int LEFT = 24;
	public static final int RIGHT = 25;
	
	public Rele() {
		super();
		this.set(23, 0);
	}

	public void set(int pin, int state)
	{		
		//System.err.println("STO SETTANDO CON QUESTI VALORI " + state);
		
		Date dateTime = new Date(System.currentTimeMillis());
		System.err.println(new SimpleDateFormat("dd.MM.yy - HH:mm.ss").format(dateTime));
		
		try
		{			
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

