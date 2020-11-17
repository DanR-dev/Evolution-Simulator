package main;

import java.util.ArrayList;
import java.util.Random;

public class Environment {
	private EnvironmentTile[][] tiles;
	
	public static void main(String[] args) {
		Environment environment = new Environment(10, 5);
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
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].bufferCreatures();
			}
		}
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].simulateCreatures(this);
			}
		}
	}

	public Creature[] getCreatures() {
		ArrayList<Creature> creatures = new ArrayList<Creature>();

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				for (Creature creature : tiles[i][j].getCreatures()) {
					creatures.add(creature);
				}
			}
		}

		return (Creature[]) creatures.toArray();
	}

	public void scatter(EnvironmentTile tile, int dist, Creature[] creatures) {
		Random rng = new Random();

		for (Creature creature : creatures) {
			tiles[tile.getX() + rng.nextInt(dist * 2 + 1) - dist][tile.getY() + rng.nextInt(dist * 2 + 1) - dist]
					.addCreature(creature);
		}
	}
}
