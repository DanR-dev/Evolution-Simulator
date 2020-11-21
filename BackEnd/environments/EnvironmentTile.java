package backEnd.environments;

import java.util.ArrayList;

import backEnd.creatures.Creature;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class EnvironmentTile {
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<EnvironmentalCondition> conditions = new ArrayList<EnvironmentalCondition>();
	private int x;
	private int y;
	
	Creature[] creatureBuffer;
	
	public EnvironmentTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}
	
	public Creature[] getCreatures() {
		if(creatures.size() > 0) {
			return (Creature[]) creatures.toArray();
		} else {
			return new Creature[0];
		}
	}
	
	public void bufferCreatures() {
		creatureBuffer = getCreatures();
	}
	
	public void simulateCreatures(Environment environment) {
		for(Creature creature : creatureBuffer) {
			creature.chooseBehaviour(environment, this);
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Group getDisplay() {
		Group tileDisplay = new Group();
		
		tileDisplay.getChildren().add(new Text("Tile: " + x + ", " + y));
		
		return tileDisplay;
	}
}