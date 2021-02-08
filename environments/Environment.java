package environments;

import java.util.ArrayList;
import java.util.Random;

import creatures.Creature;
import creatures.Plant;
import frontEnd.AppRoot;
import javafx.scene.layout.GridPane;

public class Environment extends GridPane {
	private final static float SUNLIGHT = 200;
	private final AppRoot ROOT;

	private EnvironmentTile[][] tiles;

	public Environment(int width, int height, AppRoot root) {
		super();
		this.ROOT = root;
		init(width, height, root);
		refresh();
	}

	public Environment(int width, int height, int nClusters, int clusterSize, int clusterWidth, AppRoot root) {
		super();
		Random rng = new Random();
		int clusterX;
		int clusterY;
		Creature[] clusterCreatures;

		this.ROOT = root;
		init(width, height, root);

		for (int i = 0; i < nClusters; i++) {
			clusterX = rng.nextInt(tiles.length);
			clusterY = rng.nextInt(tiles[0].length);
			clusterCreatures = new Creature[clusterSize];
			for (int j = 0; j < clusterSize; j++) {
				clusterCreatures[j] = Plant.randomPlant(root);
			}
			scatter(tiles[clusterX][clusterY], clusterWidth, clusterCreatures);
		}

		refresh();
	}

	private void init(int width, int height, AppRoot root) {
		tiles = new EnvironmentTile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new EnvironmentTile(i, j, root);
				add(tiles[i][j], i, j);
			}
		}
		this.setGridLinesVisible(true);
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

	public EnvironmentTile getTile(int x, int y) { // wraps
		int trueX;
		int trueY;

		if (x >= 0) {
			trueX = x % tiles.length;
		} else {
			trueX = -(x % tiles.length);
		}
		if (y >= 0) {
			trueY = y % tiles[0].length;
		} else {
			trueY = -(y % tiles[0].length);
		}

		return tiles[trueX][trueY];
	}

	public void scatter(EnvironmentTile tile, int dist, Creature[] creatures) {
		Random rng = new Random();

		for (Creature creature : creatures) {
			getTile(tile.getX() + rng.nextInt(dist * 2 + 1) - dist, tile.getY() + rng.nextInt(dist * 2 + 1) - dist)
					.addCreature(creature);
		}
	}

	public void simulateCreatures() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				getTile(i, j).simulateCreatures(this, SUNLIGHT);
			}
		}
		refresh();
	}

	public void refresh() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].refresh();
			}
		}
	}

	public void timeStep() {
		simulateCreatures();
	}

	/**
	 * public static void main(String[] args) { Environment environment = new
	 * Environment(10, 5); Plant[] testPlants = new Plant[10];
	 * 
	 * System.out.println();
	 * 
	 * for (int i = 0; i < 10; i++) { testPlants[i] = Plant.randomPlant(); }
	 * 
	 * environment.timeStep(); }
	 */
}
