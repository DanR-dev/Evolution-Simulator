package environments;

import java.util.ArrayList;
import java.util.Collections;

import creatures.Creature;
import creatures.Plant;
import frontEnd.AppRoot;
import javafx.scene.layout.StackPane;

public class EnvironmentTile extends StackPane {
	private static final int TILE_SIZE = 60;
	private final AppRoot ROOT;
	private ArrayList<EnvironmentalCondition> conditions = new ArrayList<EnvironmentalCondition>();
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private int x;
	private int y;

	public EnvironmentTile(int x, int y, AppRoot root) {
		super();
		this.ROOT = root;
		this.x = x;
		this.y = y;

		this.setMinHeight(TILE_SIZE);
		this.setMinWidth(TILE_SIZE);
	}

	public Plant[] getPlants() {
		ArrayList<Plant> plants = new ArrayList<Plant>();
		for (Creature creature : creatures) {
			try {
				plants.add((Plant) creature);
			} catch (ClassCastException e) {
			}
		}
		Collections.sort(plants);
		Collections.reverse(plants);
		return plants.toArray(new Plant[0]);
	}

	public Creature[] getCreatures() {
		Creature[] temp = new Creature[creatures.size()];
		for (int i = 0; i < creatures.size(); i++) {
			temp[i] = creatures.get(i);
		}
		return temp;
	}

	public void refresh() {
		getChildren().clear();
		for (int i = 0; i < Creature.MAX_SIZE; i++) {
			for (Creature creature : creatures) {
				if (creature.getSize() == i) {
					creature.refresh();
					getChildren().add(creature);
					setAlignment(creature, creature.getPos());
				}
			}
		}
	}

	public void simulateCreatures(Environment environment, float sunlight) {
		photosynthesise(sunlight);
		for (int i = 0; i < creatures.size(); i++) {
			creatures.get(i).chooseBehaviour(environment, this);
		}
	}

	public void photosynthesise(float photoEnergy) {
		Plant[] plants = getPlants();
		for (Plant plant : plants) {
			photoEnergy = plant.photosynthesise(photoEnergy);
		}
	}

	public void addCreature(Creature creature) {
		creatures.add(creature);
	}

	public void removeCreature(Creature creature) {
		creatures.remove(creature);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * public static void main(String[] args) { EnvironmentTile tile = new
	 * EnvironmentTile(0, 0); tile.generatePlants(5); for(Creature creature :
	 * tile.getPlants()) { System.out.println(creature.toString()); }
	 * System.out.println(); tile.photosynthesise(100); for(Creature creature :
	 * tile.getPlants()) { System.out.println(creature.toString()); } }
	 */
}