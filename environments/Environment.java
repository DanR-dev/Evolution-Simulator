package environments;

import java.util.ArrayList;
import java.util.Random;

import creatures.Creature;
import creatures.Plant;
import javafx.scene.layout.GridPane;

public class Environment extends GridPane{
	private final static float SUNLIGHT = 200;
	
	private EnvironmentTile[][] tiles;

	/**
	public static void main(String[] args) {
		Environment environment = new Environment(10, 5);
		Plant[] testPlants = new Plant[10];

		System.out.println();

		for (int i = 0; i < 10; i++) {
			testPlants[i] = Plant.randomPlant();
		}

		environment.timeStep();
	}
	*/

	public Environment(int width, int height) {
		super();
		tiles = new EnvironmentTile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new EnvironmentTile(i, j);
				add(tiles[i][j], i, j);
				tiles[i][j].generatePlants(3);
			}
		}
		
		this.setGridLinesVisible(true);
		refresh();
	}

	public Environment(int width, int height, int nClusters, int clusterSize, int clusterWidth) {
		super();
		Random rng = new Random();
		int clusterX;
		int clusterY;
		Creature[] clusterCreatures;
		
		tiles = new EnvironmentTile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new EnvironmentTile(i, j);
				add(tiles[i][j], i, j);
			}
		}

		for(int i = 0; i < nClusters; i++) {
			clusterX = rng.nextInt(tiles.length);
			clusterY = rng.nextInt(tiles[0].length);
			clusterCreatures = new Creature[clusterSize];
			for(int j = 0; j < clusterSize; j++) {
				clusterCreatures[j] = Plant.randomPlant();
			}
			scatter(tiles[clusterX][clusterY], clusterWidth, clusterCreatures);
		}
		
		this.setGridLinesVisible(true);
		refresh();
	}

	public EnvironmentTile getTile(int x, int y) { // wraps
		int trueX;
		int trueY;
		if(x >= 0) {
			trueX = x % tiles.length;
		} else {
			trueX = -(x % tiles.length);
		}
		if(y >= 0) {
			trueY = y % tiles[0].length;
		} else {
			trueY = -(y % tiles[0].length);
		}
		return tiles[trueX][trueY];
	}

	public void timeStep() {
		simulateCreatures();
	}

	public void simulateCreatures() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				getTile(i, j).simulateCreatures(this, SUNLIGHT);
			}
		}
		refresh();
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
			getTile(tile.getX() + rng.nextInt(dist * 2 + 1) - dist, tile.getY() + rng.nextInt(dist * 2 + 1) - dist).addCreature(creature);
		}
	}

	public void refresh() {
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].refresh();
			}
		}
	}
}
