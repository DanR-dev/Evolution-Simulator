package frontEnd;

import creatures.Creature;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * display window for graphics generated from creature data.
 * 
 * @author danpr
 *
 */
public class CreatureWindow extends Stage {

	/**
	 * initialise the window with the graphical elements provided by the creature
	 * class.
	 * 
	 * @param creature creature to get graphical elements from
	 */
	public CreatureWindow(Creature creature) {
		VBox stats = creature.getStatsBox();
		Scene scene = new Scene(stats);

		this.setAlwaysOnTop(true);
		this.setMinWidth(300);

		setScene(scene);
		setTitle("Simulation");
	}
}
