package environments;

import java.util.TimerTask;

/**
 * handles periodic running of simulation batches when simulating continuously.
 * 
 * @author danpr
 *
 */
public class SimTimer extends TimerTask {

	private final Environment SIM; // environment to simulate after a length of time

	/**
	 * initialise with the target environment.
	 * @param sim
	 */
	public SimTimer(Environment sim) {
		this.SIM = sim;
	}

	/**
	 * action to take once the time is up.
	 */
	@Override
	public void run() {
		SIM.simulateContinuous();
	}
}
