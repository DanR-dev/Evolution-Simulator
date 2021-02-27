package frontEnd;

import java.util.ArrayList;

import environments.Environment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimWindow extends Stage {
	private static final int ENVIRONMENT_SPACING = 20;
	private final AppRoot root;

	private ArrayList<Environment> environments = new ArrayList<Environment>();

	public SimWindow(int envWidth, int envHeight, AppRoot root) {
		GridPane environmentGrid = new GridPane();
		Scene scene = new Scene(environmentGrid);
		this.root = root;

		environmentGrid.setHgap(ENVIRONMENT_SPACING);
		environmentGrid.setVgap(ENVIRONMENT_SPACING);
		
		environments.add(new Environment(envWidth, envHeight, 1, 1, 0, root));
		//environments.add(new Environment(envWidth, envHeight, 1, 20, 0, root));

		environmentGrid.add(environments.get(0), 0, 0);
		//environmentGrid.add(environments.get(1), 1, 0);

		root.statWindow(environments.get(0));
		
		setScene(scene);
		setTitle("Simulation");
	}

	public void addEnvironment(Environment environment) {
		environments.add(environment);
	}
}