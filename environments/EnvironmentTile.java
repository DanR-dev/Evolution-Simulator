package environments;

import java.util.ArrayList;
import java.util.Collections;

import creatures.Creature;
import creatures.Plant;
import javafx.scene.layout.StackPane;

public class EnvironmentTile extends StackPane{
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<EnvironmentalCondition> conditions = new ArrayList<EnvironmentalCondition>();
	private int x;
	private int y;
	
	private int tileSize = 60;
	
	public EnvironmentTile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		
		this.setMinHeight(tileSize);
		this.setMinWidth(tileSize);
	}
	
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}
	
	public void removeCreature(Creature creature) {
		creatures.remove(creature);
	}
	
	public Creature[] getCreatures() {
		Creature[] temp = new Creature[creatures.size()];
		for(int i = 0; i < creatures.size(); i++) {
			temp[i] = creatures.get(i);
		}
		return temp;
	}
	
	public void generatePlants(int n) {
		for(int i = 0; i < n; i++) {
			this.addCreature(Plant.randomPlant());
		}
	}
	
	public void simulateCreatures(Environment environment, float sunlight) {
		photosynthesise(sunlight);
		for(int i = 0; i < creatures.size(); i++) {
			creatures.get(i).chooseBehaviour(environment, this);
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
	
	public Plant[] getPlants() {
		ArrayList<Plant> plants = new ArrayList<Plant>();
		for(Creature creature : creatures) {
			try {
				plants.add((Plant) creature);
			}catch(ClassCastException e) {}
		}
		Collections.sort(plants);
		Collections.reverse(plants);
		return plants.toArray(new Plant[0]);
	}
	
	public void photosynthesise(float photoEnergy) {
		Plant[] plants = getPlants();
		for(Plant plant : plants) {
			photoEnergy = plant.photosynthesise(photoEnergy);
		}
	}
	
	/**
	public static void main(String[] args) {
		EnvironmentTile tile = new EnvironmentTile(0, 0);
		tile.generatePlants(5);
		for(Creature creature : tile.getPlants()) {
			System.out.println(creature.toString());
		}
		System.out.println();
		tile.photosynthesise(100);
		for(Creature creature : tile.getPlants()) {
			System.out.println(creature.toString());
		}
	}
	*/
}