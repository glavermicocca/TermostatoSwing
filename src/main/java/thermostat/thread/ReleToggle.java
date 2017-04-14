package thermostat.thread;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import thermostat.gpio.Rele;

public class ReleToggle extends SwingWorker<Void, Void>
{
	JButton btnToggle;
	Rele rele;
	int currentPin;
		
	public ReleToggle(JButton btnToggle, Rele rele, int currentPin) {
		super();
		this.btnToggle = btnToggle;
		this.rele = rele;
		this.currentPin = currentPin;
		
		this.btnToggle.setVisible(false);
		
		this.rele.set(currentPin, 1); //uno
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Simulate doing something useful.
	    while(true) { //per tutto il tempo
	    	Thread.sleep(2000);
	    	this.btnToggle.setVisible(true);
	    	
	    	this.rele.set(currentPin, 0);
	    }
	}	
}