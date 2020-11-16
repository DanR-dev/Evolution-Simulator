package main;

import java.util.Random;

public class Plant extends Creature {
	protected static final int MAX_SIZE = 100;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final int SUSTAIN_EFFICIENCY = 1;

	public static Plant randomPlant() {
		Random rng = new Random();
		return new Plant(rng.nextInt(MAX_MUTATION) + 1, rng.nextFloat(), rng.nextFloat(), rng.nextInt(MAX_SIZE) + 1, 1,
				rng.nextInt(MAX_SEED_RANGE) + 1);
	}

	public static void main(String[] args) {
		Plant parent = randomPlant();
		Plant child = parent.reproduce();
		Plant grandChild = child.reproduce();
	}

	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int sizeCap, int startingSize,
			int seedRange) { // grows with genome
		super(mutationRate, reproduceBehaviour, growBehaviour, startingSize);
		genome.addGene(GeneType.SIZE_CAP, new Gene(1, MAX_SIZE, 1, sizeCap));
		genome.addGene(GeneType.STARTING_SIZE, new Gene(1, MAX_SIZE, 1, startingSize));
		genome.addGene(GeneType.SEED_RANGE, new Gene(1, MAX_SEED_RANGE, 1, seedRange));
	}

	public Plant(Genome parentGenome) {
		super(parentGenome, (int) parentGenome.getGeneValue(GeneType.STARTING_SIZE));
	}

	public void chooseBehaviour() {
		sustain();
		if (energy <= 0) {
			die();
		}

		if (size < genome.getGeneValue(GeneType.SIZE_CAP)
				&& genome.getGeneValue(GeneType.GROW_BEHAVIOUR) > energy / (size * ENERGY_PER_SIZE)) {
			grow();
		}

		if (genome.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR) > (energy - reproduceCost()) / (size * ENERGY_PER_SIZE)) {
			reproduce();
		}
	}

	public Plant reproduce() {
		Plant child = new Plant(genome);
		child.genome.mutate();
		this.energy -= reproduceCost();
		return child;
	}

	public void sustain() {
		energy -= size * SUSTAIN_EFFICIENCY;
	}

	public void grow() {
		energy -= growCost();
	}

	public float reproduceCost() {
		return (genome.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE)
				+ (genome.getGeneValue(GeneType.STARTING_SIZE) * genome.getGeneValue(GeneType.SEED_RANGE));
	}

	public float growCost() {
		return ENERGY_PER_SIZE;
	}

	public float sustainCost() {
		return size * SUSTAIN_EFFICIENCY;
	}
}