package thermostat.thread;

import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import thermostat.bean.Sensor;
import thermostat.bean.Values;
import thermostat.gpio.Rele;

public class CurrentCalendar extends SwingWorker<Void, Void>
{
	Values values;
	Rele rele;
	Sensor sensor;
	JLabel lblTemperature;
	JButton btnOnoff;
	JPanel panelContainerSchedule;
	JLabel listSchedule;
	JPanel panelContainerSetTemperature;
		
	public CurrentCalendar(JPanel panelContainerSetTemperature, JLabel listSchedule, Values values, Rele rele, Sensor sensor, JLabel lblTemperature, JButton btnOnoff,
			JPanel panelContainerSchedule) {
		super();
		this.listSchedule = listSchedule;
		this.values = values;
		this.rele = rele;
		this.sensor = sensor;
		this.lblTemperature = lblTemperature;
		this.btnOnoff = btnOnoff;
		this.panelContainerSchedule = panelContainerSchedule;
		this.panelContainerSetTemperature = panelContainerSetTemperature;
	}

	private CalendarQuickstart cQuickStart;
	
	//cQuickStart = new CalendarQuickstart();

	@Override
	protected Void doInBackground() throws Exception {
		// Simulate doing something useful.
	    while(true) { //per tutto il tempo
	    	Thread.sleep(10000);
	    	
	    	cQuickStart.check(panelContainerSetTemperature, listSchedule, values, rele, sensor, lblTemperature, btnOnoff, panelContainerSchedule);
	    }
	}	
}
