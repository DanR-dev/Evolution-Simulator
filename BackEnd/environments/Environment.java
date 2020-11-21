package backEnd.environments;

import java.util.ArrayList;
import java.util.Random;

import backEnd.creatures.Creature;
import backEnd.creatures.Plant;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

public class Environment {
	private EnvironmentTile[][] tiles;

	public static void main(String[] args) {
		Environment environment = new Environment(10, 5);
		Plant[] testPlants = new Plant[10];

		System.out.println();

		for (int i = 0; i < 10; i++) {
			testPlants[i] = Plant.randomPlant();
		}

		environment.timeStep();
	}

	public Environment(int width, int height) {
		tiles = new EnvironmentTile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new EnvironmentTile(i, j);
			}
		}
	}

	public EnvironmentTile getTile(int x, int y) { // wraps
		return tiles[x % tiles.length][y % tiles[0].length];
	}

	public void timeStep() {
		simulateCreatures();
	}

	public void simulateCreatures() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				getTile(i, j).bufferCreatures();
			}
		}
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				getTile(i, j).simulateCreatures(this);
			}
		}
	}

	public Creature[] getCreatures() {
		ArrayList<Creature> creatures = new ArrayList<Creature>();

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				for (Creature creature : getTile(i, j).getCreatures()) {
					creatures.add(creature);
				}
			}
		}

		return (Creature[]) creatures.toArray();
	}

	public void scatter(EnvironmentTile tile, int dist, Creature[] creatures) {
		Random rng = new Random();

		for (Creature creature : creatures) {
			getTile(tile.getX() + rng.nextInt(dist * 2 + 1) - dist, tile.getY() + rng.nextInt(dist * 2 + 1) - dist)
					.addCreature(creature);
		}
	}

	public TilePane getDisplay() {
		TilePane tileGrid = new TilePane();
		
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tileGrid(tiles[i][j].getDisplay(), i, j);
			}
		}
		
		return tileGrid;
	}
}
