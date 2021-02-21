package frontEnd;

import creatures.Creature;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreatureWindow extends Stage {
	
	public CreatureWindow(Creature creature) {
		VBox stats = creature.getStatsBox();
		Scene scene = new Scene(stats);
		
		this.setAlwaysOnTop(true);
		this.setMinWidth(300);
		
		setScene(scene);
		setTitle("Simulation");
	}
}
