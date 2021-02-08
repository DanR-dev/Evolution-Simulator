package frontEnd;

import creatures.Creature;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppRoot extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage = new SimWindow(15, 7, this);
		stage.show();
	}
	
	public void creatureWindow(Creature creature) {
		CreatureWindow stage = new CreatureWindow(creature);
		stage.show();
	}
	
	@Override
	public void init() {
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
