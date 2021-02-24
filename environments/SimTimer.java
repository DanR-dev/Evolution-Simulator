package environments;

import java.util.Timer;
import java.util.TimerTask;

public class SimTimer extends TimerTask {
	
	private final Environment SIM;

	public SimTimer(Environment sim) {
		this.SIM = sim;
	}
	
	@Override
	public void run() {
		SIM.simulateContinuous();
	}
}
