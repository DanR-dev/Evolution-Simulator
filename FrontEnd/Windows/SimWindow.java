package frontEnd.windows;

import java.util.ArrayList;

import backEnd.environments.Environment;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimWindow extends Stage {
	private static final int ENVIRONMENT_SPACING = 20;
	
	private ArrayList<Environment> environments = new ArrayList<Environment>();

	public SimWindow(int envWidth, int envHeight) {
		GridPane environmentGrid = new GridPane();
		Scene scene = new Scene(environmentGrid);

		environmentGrid.setHgap(ENVIRONMENT_SPACING);
		environmentGrid.setVgap(ENVIRONMENT_SPACING);
		
		environments.add(new Environment(envWidth, envHeight));
		environments.add(new Environment(envWidth, envHeight));

		environmentGrid.add(environments.get(0).getDisplay(), 0, 0);
		environmentGrid.add(environments.get(1).getDisplay(), 1, 0);

		setScene(scene);
		setTitle("Simulation");
	}

	public void addEnvironment(Environment environment) {
		environments.add(environment);
	}
}