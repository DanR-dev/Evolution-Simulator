package frontEnd;

import environments.Environment;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * display area for statistical charts generated from an environment.
 * 
 * @author danpr
 *
 */
public class StatWindow extends Stage {

	/**
	 * initialise with charts taken from the given environment.
	 * @param environment
	 */
	public StatWindow(Environment environment) {
		Chart[] basicStats = environment.getBasicCharts();
		Chart[] advancedStats = environment.getGeneHistograms();
		VBox allStats = new VBox();
		HBox basicStatsBox = new HBox();
		HBox advancedStatsBox = new HBox();
		ScrollPane advancedScroll;
		Scene scene = new Scene(allStats);

		for (int i = 0; i < basicStats.length; i++) {
			basicStatsBox.getChildren().add(i, basicStats[i]);
		}

		for (int i = 0; i < advancedStats.length; i++) {
			advancedStatsBox.getChildren().add(i, advancedStats[i]);
		}

		advancedScroll = new ScrollPane(advancedStatsBox);
		allStats.getChildren().add(basicStatsBox);
		allStats.getChildren().add(advancedScroll);

		setScene(scene);
		setTitle("Statistics");
		setWidth(1600);
	}
}