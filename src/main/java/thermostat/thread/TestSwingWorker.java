package thermostat.thread;

import java.awt.Label;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class TestSwingWorker extends SwingWorker<Boolean, Integer> {
	
	Label statusLabel;
	Label countLabel1;

	public TestSwingWorker(Label statusLabel, Label countLabel1) {
		super();
		this.statusLabel = statusLabel;
		this.countLabel1 = countLabel1;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		// Simulate doing something useful.
	    for (int i = 0; i <= 10; i++) {
	     Thread.sleep(1000);
	     
	     // The type we pass to publish() is determined
	     // by the second template parameter.
	     publish(i);
	    }

	    // Here we can return some object of whatever type
	    // we specified for the first template parameter.
	    // (in this case we're auto-boxing 'true').
	    return true;
	}

	// Can safely update the GUI from this method.
	@Override
	protected void done() {
	    
	    boolean status;
	    try {
	     // Retrieve the return value of doInBackground.
	     status = get();
	     statusLabel.setText("Completed with status: " + status);
	    } catch (InterruptedException e) {
	     // This is thrown if the thread's interrupted.
	    } catch (ExecutionException e) {
	     // This is thrown if we throw an exception
	     // from doInBackground.
	    }
	}

	@Override
	// Can safely update the GUI from this method.
	protected void process(List<Integer> chunks) {
	 // Here we receive the values that we publish().
	 // They may come grouped in chunks.
	 int mostRecentValue = chunks.get(chunks.size()-1);
	 
	 countLabel1.setText(Integer.toString(mostRecentValue));
	}
}