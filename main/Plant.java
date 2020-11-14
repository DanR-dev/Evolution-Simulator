package main;

import java.util.Random;

public class Plant extends Creature {
	protected static final int MAX_SIZE = 100;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final float SUSTAIN_EFFICIENCY = 1;

	// full genome:
	protected final int MUTATION_RATE;
	protected final int SIZE_CAP;
	protected final float REPRODUCE_BEHAVIOUR;
	protected final float GROW_BEHAVIOUR;
	protected final int STARTING_SIZE;
	protected final int SEED_RANGE;

	public static Plant randomPlant() {
		Random rng = new Random();
		return new Plant(rng.nextInt(MAX_MUTATION) + 1, rng.nextInt(MAX_SIZE) + 1, rng.nextFloat(), rng.nextFloat(), 1,
				rng.nextInt(MAX_SEED_RANGE) + 1);
	}

	public Plant(int mutationRate, int sizeCap, float reproduceBehaviour, float growBehaviour, int startingSize,
			int seedRange) {
		super();
		MUTATION_RATE = mutationRate;
		SIZE_CAP = sizeCap;
		REPRODUCE_BEHAVIOUR = reproduceBehaviour;
		GROW_BEHAVIOUR = growBehaviour;
		STARTING_SIZE = startingSize;
		SEED_RANGE = seedRange;
	}

	public void chooseBehaviour() {
		sustain();
		if (energy <= 0) {
			die();
		}

		if (size < SIZE_CAP && GROW_BEHAVIOUR > energy / (size * ENERGY_PER_SIZE)) {
			grow();
		}

		if (REPRODUCE_BEHAVIOUR > (energy - reproduceCost()) / (size * ENERGY_PER_SIZE)) {
			reproduce();
		}
	}

	public Plant reproduce() {
		Plant child;

		try {
			child = (Plant) this.clone();

			child.size = this.STARTING_SIZE;
			child.energy = child.size * ENERGY_PER_SIZE;

			child.mutate();

			this.energy -= reproduceCost();

			return child;
		} catch (Exception e) {
			throw new RuntimeException("plant cloning error");
		}
	}

	public float reproduceCost() {
		return (STARTING_SIZE * ENERGY_PER_SIZE) + (STARTING_SIZE * SEED_RANGE);
	}

	public float growCost() {
		return ENERGY_PER_SIZE;
	}

	public void sustain() {
		energy -= size * SUSTAIN_EFFICIENCY;
	}

	public void grow() {
		energy -= growCost();
	}

	public void mutate() {

	}
}