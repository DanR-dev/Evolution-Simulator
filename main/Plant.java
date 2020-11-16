package main;

import java.util.ArrayList;
import java.util.Random;

public class Plant extends Creature {
	protected static final int MAX_SIZE = 100;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final float SUSTAIN_EFFICIENCY = 1;

	public static Plant randomPlant() {
		Random rng = new Random();
		return new Plant(rng.nextInt(MAX_MUTATION) + 1, rng.nextFloat(), rng.nextFloat(), rng.nextInt(MAX_SIZE) + 1, 1,
				rng.nextInt(MAX_SEED_RANGE) + 1);
	}

	public static void main(String[] args) {
		int test = 1;
		Plant parent = randomPlant();
		Plant child = parent.reproduce();
	}

	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int sizeCap, int startingSize,
			int seedRange) { // grows with genome
		super(mutationRate, reproduceBehaviour, growBehaviour, startingSize);
		genome.add(GeneType.SIZE_CAP.ordinal(), new Gene(1, MAX_SIZE, 1, sizeCap));
		genome.add(GeneType.STARTING_SIZE.ordinal(), new Gene(1, MAX_SIZE, 1, startingSize));
		genome.add(GeneType.SEED_RANGE.ordinal(), new Gene(1, MAX_SEED_RANGE, 1, seedRange));
	}

	public Plant(ArrayList<Gene> parentGenome) {
		super(parentGenome, (int) parentGenome.get(GeneType.STARTING_SIZE.ordinal()).getValue());
	}

	public void chooseBehaviour() {
		sustain();
		if (energy <= 0) {
			die();
		}

		if (size < genome.get(GeneType.SIZE_CAP.ordinal()).getValue()
				&& genome.get(GeneType.GROW_BEHAVIOUR.ordinal()).getValue() > energy / (size * ENERGY_PER_SIZE)) {
			grow();
		}

		if (genome.get(GeneType.REPRODUCE_BEHAVIOUR.ordinal()).getValue() > (energy - reproduceCost())
				/ (size * ENERGY_PER_SIZE)) {
			reproduce();
		}
	}

	public Plant reproduce() {
		Plant child = new Plant(genome);
		child.genome.mutate();
		this.energy -= reproduceCost();
		return child;
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
}