package frontEnd.components;

import backEnd.environments.Environment;
import javafx.scene.layout.TilePane;

public class EnvDisplay extends TilePane{
	private Environment environment;
	
	public EnvDisplay() {
		
	}
	
	public void assign(Environment environment) {
		this.environment = environment;
		refresh();
	}
	
	public void refresh() {
		this.setPrefColumns(environment.getWidth();
		this.setPrefRows(environment.getHeight();
	}
}
