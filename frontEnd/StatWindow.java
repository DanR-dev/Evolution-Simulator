package frontEnd;

import environments.Environment;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StatWindow extends Stage {
	
	public StatWindow(Environment environment) {
		Chart[] stats = environment.getCharts();
		HBox statGrid = new HBox();
		Scene scene = new Scene(statGrid);

		for(Chart stat : stats) {
		}
		statGrid.getChildren().add(0, stats[0]);
		statGrid.getChildren().add(1, stats[1]);
		
		setScene(scene);
		setTitle("Statistics");
	}
}
