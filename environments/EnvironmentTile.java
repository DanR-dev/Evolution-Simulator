package environments;

import java.util.ArrayList;

import creatures.Creature;
import creatures.Plant;
import javafx.scene.layout.StackPane;

public class EnvironmentTile extends StackPane{
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<EnvironmentalCondition> conditions = new ArrayList<EnvironmentalCondition>();
	private int x;
	private int y;
	
	Creature[] creatureBuffer;
	
	public EnvironmentTile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		
		this.setMinHeight(40);
		this.setMinWidth(40);
		
		this.addCreature(Plant.randomPlant()); //
		this.addCreature(Plant.randomPlant()); //
		this.addCreature(Plant.randomPlant()); //
		this.addCreature(Plant.randomPlant()); //
		this.addCreature(Plant.randomPlant()); //
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
	
	public void refresh() {
		getChildren().clear();
		for(int i = 0; i < Creature.MAX_SIZE; i++) {
			for(Creature creature : creatures) {
				if(creature.getSize() == i) {
					creature.refresh();
					getChildren().add(creature);
					setAlignment(creature, creature.POSITION_ON_TILE);
				}
			}
		}
	}
}