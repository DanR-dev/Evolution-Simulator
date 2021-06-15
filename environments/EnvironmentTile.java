package environments;

import java.util.ArrayList;
import java.util.Collections;

import creatures.Creature;
import creatures.Plant;
import frontEnd.AppRoot;
import javafx.scene.layout.StackPane;

/**
 * handles simulation and graphical appearance of a single tile of an
 * environment.
 * 
 * @author danpr
 *
 */
public class EnvironmentTile extends StackPane {
	private static final int TILE_SIZE = 60; // graphical size of tile
	private final AppRoot ROOT; // reference for program-wide access

	private ArrayList<Creature> creatures = new ArrayList<Creature>(); // population of this tile
	private int x; // position in an environment
	private int y; // ^

	/**
	 * initialise as an empty tile.
	 * 
	 * @param x    position in an environment
	 * @param y    ^
	 * @param root reference for program-wide access
	 */
	public EnvironmentTile(int x, int y, AppRoot root) {
		super();
		this.ROOT = root;
		this.x = x;
		this.y = y;

		this.setMinHeight(TILE_SIZE);
		this.setMinWidth(TILE_SIZE);
	}

	/**
	 * get a reference to each plant on this tile on order of size (largest to
	 * shortest).
	 * 
	 * @return
	 */
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

	/**
	 * get a reference to each creature on this tile.
	 * 
	 * @return
	 */
	public Creature[] getCreatures() {
		Creature[] temp = new Creature[creatures.size()];
		for (int i = 0; i < creatures.size(); i++) {
			temp[i] = creatures.get(i);
		}
		return temp;
	}

	/**
	 * refresh the graphical appearance of creatures on this tile.
	 */
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

	/**
	 * simulate each creature on this tile.
	 * 
	 * @param environment environment this tile is part of
	 * @param sunlight    amount of energy available to plants on this tile
	 */
	public void simulateCreatures(Environment environment, float sunlight) {
		photosynthesise(sunlight);
		for (int i = 0; i < creatures.size(); i++) {
			creatures.get(i).chooseBehaviour(environment, this);
		}
	}

	/**
	 * kill all creatures on this tile.
	 */
	public void killCreatures() {
		creatures.clear();
	}

	/**
	 * allow each plant in order of size to absorb a portion of the available
	 * sunlight as energy, with smaller plants having access to less because of the
	 * larger plants absorbing some first.
	 * 
	 * @param photoEnergy total available sunlight hitting this tile
	 */
	public void photosynthesise(float photoEnergy) {
		Plant[] plants = getPlants(); // getPlants() returns in descending order of size
		for (Plant plant : plants) {
			photoEnergy = plant.photosynthesise(photoEnergy);
		}
	}

	/**
	 * add the given creature to this tile.
	 * 
	 * @param creature
	 */
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}

	/**
	 * remove the given creature from this tile.
	 * 
	 * @param creature
	 */
	public void removeCreature(Creature creature) {
		creatures.remove(creature);
	}

	/**
	 * x getter.
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * y getter.
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}
}