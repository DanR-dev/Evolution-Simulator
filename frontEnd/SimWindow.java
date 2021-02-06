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

	private ArrayList<Environment> environments = new ArrayList<Environment>();

	public SimWindow(int envWidth, int envHeight) {
		GridPane environmentGrid = new GridPane();
		Scene scene = new Scene(environmentGrid);
		

		environmentGrid.setHgap(ENVIRONMENT_SPACING);
		environmentGrid.setVgap(ENVIRONMENT_SPACING);
		
		environments.add(new Environment(envWidth, envHeight, 1, 20, 0));
		//environments.add(new Environment(envWidth, envHeight));

		environmentGrid.add(environments.get(0), 0, 0);
		//environmentGrid.add(environments.get(1), 1, 0);

		
		Button testButton = new Button();
		testButton.setText("step");
		testButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	for(int i = 0; i < 10; i++) {
                    environments.get(0).simulateCreatures();
            	}
                System.out.println("step");
            }
        });
		environmentGrid.add(testButton, 0, 0);

		
		setScene(scene);
		setTitle("Simulation");
	}

	public void addEnvironment(Environment environment) {
		environments.add(environment);
	}
}