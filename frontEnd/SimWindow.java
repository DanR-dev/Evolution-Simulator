package frontEnd;

import java.util.ArrayList;

import environments.Environment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * display area for one or more environments.
 * @author danpr
 *
 */
public class SimWindow extends Stage {
	private static final int ENVIRONMENT_SPACING = 20; // display element spacing between environments
	private final AppRoot root; // reference for program-wide access

	private ArrayList<Environment> environments = new ArrayList<Environment>(); // environments being displayed

	/**
	 * initialise with a new environment generated with the given parameters.
	 * @param envWidth height and width of environment to generate
	 * @param envHeight ^
	 * @param root reference for program-wide access
	 */
	public SimWindow(int envWidth, int envHeight, AppRoot root) {
		GridPane environmentGrid = new GridPane();
		Scene scene = new Scene(environmentGrid);
		this.root = root;

		environmentGrid.setHgap(ENVIRONMENT_SPACING);
		environmentGrid.setVgap(ENVIRONMENT_SPACING);
		
		environments.add(new Environment(envWidth, envHeight, root));

		environmentGrid.add(environments.get(0), 0, 0);

		root.statWindow(environments.get(0));
		
		setScene(scene);
		setTitle("Simulation");
	}
}