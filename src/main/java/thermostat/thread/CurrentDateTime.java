package thermostat.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class CurrentDateTime extends SwingWorker<Void, Void>
{
	JLabel timeLabel;
	JLabel dateLabel;

	public CurrentDateTime(JLabel timeLabel, JLabel dateLabel) {
		super();
		this.timeLabel = timeLabel;
		this.dateLabel = dateLabel;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// Simulate doing something useful.
	    while(true) { //per tutto il tempo
	    	Thread.sleep(1000);
	    	
			Date dateTime = new Date(System.currentTimeMillis());
			timeLabel.setText(new SimpleDateFormat("HH:mm.ss").format(dateTime));
			dateLabel.setText(new SimpleDateFormat("dd.MM.yy").format(dateTime));
	    }
	}	
}