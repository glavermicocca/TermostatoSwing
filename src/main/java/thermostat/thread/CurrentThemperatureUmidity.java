package thermostat.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import mapper.Mapper;
import thermostat.bean.Sensor;
import thermostat.bean.Values;
import thermostat.gpio.Rele;

public class CurrentThemperatureUmidity extends SwingWorker<Void, Void>
{
	private JLabel labelTemperature;
	private JLabel labelUmidity;
	private Sensor sensor;
	private Rele rele;
	private Values values;
	
	private int counter;
	private int WAITING_TIME = 5;
	
	public CurrentThemperatureUmidity(Values values, Rele rele, Sensor sensor, JLabel labelTemperature, JLabel labelUmidity) {
		super();
		this.values = values;
		this.rele = rele;
		this.sensor = sensor;
		this.labelTemperature = labelTemperature;
		this.labelUmidity = labelUmidity;
	}
	
	private void readFromExecution()
	{
		try
		{
		    this.labelTemperature.setText(""+this.sensor.getCurrentTemperature());
		    this.labelUmidity.setText(""+this.sensor.getCurrentUmidity());
			
		    //System.out.println(Mapper.ABSOLUTE_PATH_USER_HOME + "Adafruit_Python_DHT/examples/AdafruitDHT.py 2302 12");
		    
			Process p = Runtime.getRuntime().exec(Mapper.ABSOLUTE_PATH_USER_HOME + "Adafruit_Python_DHT/examples/AdafruitDHT.py 2302 12");
		    p.waitFor();
	
		    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    
		    String line = "";
		    while ((line = reader.readLine())!= null) {
		    	//System.err.println(line);
		    	
		    	//System.err.println(" QUI ->> " + line.indexOf("*"));
		    	this.sensor.setCurrentTemperature(line.substring(0,line.indexOf("*")));
		    	this.sensor.setCurrentUmidity(line.substring(line.indexOf("*") + 1));
		    	
		    	if(this.values.isToggleButton())
		    	{
		    		if(this.sensor.isHigher())
			    	{
		    			if(counter % WAITING_TIME == 0)
		    			{
		    				rele.set(Rele.CALDAIA, Rele.ON);	
		    			}
			    	}
			    	else
			    	{
		    			if(counter % WAITING_TIME == 0)
		    			{
		    				rele.set(Rele.CALDAIA, Rele.OFF);
		    			}
			    	}	
		    	}
		    	
		    	counter++;
		    }
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}



	@Override
	protected Void doInBackground() throws Exception {

		while(true)
		{
			Thread.sleep(5000);
			readFromExecution();
		}
	}
	
}