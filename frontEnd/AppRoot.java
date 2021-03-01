package frontEnd;

import creatures.Creature;
import environments.Environment;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppRoot extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage = new SimWindow(20, 10, this);
		stage.show();
	}
	
	public CreatureWindow creatureWindow(Creature creature) {
		CreatureWindow creatureWindow = new CreatureWindow(creature);
		creatureWindow.show();
		return creatureWindow;
	}
	
	public StatWindow statWindow(Environment environment) {
		StatWindow statWindow = new StatWindow(environment);
		statWindow.show();
		return statWindow;
	}
	
	@Override
	public void init() {
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
