package frontEnd;

import creatures.Creature;
import environments.Environment;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * handles opening of different windows.
 * 
 * @author danpr
 *
 */
public class AppRoot extends Application {

	/**
	 * opens a simulation window when the program is run.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage = new SimWindow(22, 10, this);
		stage.show();
	}

	/**
	 * opens a creature window with the data of the given creature displayed.
	 * 
	 * @param creature
	 * @return
	 */
	public CreatureWindow creatureWindow(Creature creature) {
		CreatureWindow creatureWindow = new CreatureWindow(creature);
		creatureWindow.show();
		return creatureWindow;
	}

	/**
	 * opens a statistics window with the data of the given environment.
	 * 
	 * @param environment
	 * @return
	 */
	public StatWindow statWindow(Environment environment) {
		StatWindow statWindow = new StatWindow(environment);
		statWindow.show();
		return statWindow;
	}

	/**
	 * launch the graphical interface when the program is run.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}
