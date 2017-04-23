package thermostat.thread;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import thermostat.gpio.Rele;
import thermostat.gpio.ReleToggle;

public class ReleThread extends SwingWorker<Void, Void>
{
	JButton btnToggle;
	ReleToggle releToggle;
	String currentPin;
		
	public ReleThread(JButton btnToggle, ReleToggle releToggle, String currentPin) {
		super();
		this.btnToggle = btnToggle;
		this.releToggle = releToggle;
		this.currentPin = currentPin;
		
		this.btnToggle.setVisible(false);
		
		this.releToggle.set(currentPin, ReleToggle.ON); //uno
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Simulate doing something useful.
	    while(true) { //per tutto il tempo
	    	Thread.sleep(15000); //aspetto 15 secondi prima di permettere un altro click (il cancello si sta aprendo)
	    	this.btnToggle.setVisible(true);
	    }
	}	
}
