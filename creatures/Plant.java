package creatures;

import java.text.DecimalFormat;
import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;

public class Plant extends Creature {
	protected static final int SUSTAIN_COST = 1;
	protected static final int MAX_SIZE = 100;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final int MAX_GROW_BATCH = 10;
	protected static final int MAX_SEED_BATCH = 10;
	protected static final float GROW_EFFICIENCY = 0.5F;
	protected static final float CLONE_EFFICIENCY = 0.5F;
	protected static final float SEED_EFFICIENCY = 0.5F;

	public static void main(String[] args) {
		Plant parent = randomPlant();
		Plant[] children = parent.reproduce();
		Plant[][] grandChildren = new Plant[children.length][];

		System.out.println("parent:\n" + parent.toString());
		System.out.println("children: ");
		for (int i = 0; i < children.length; i++) {
			if (children[i].isGeneticDeadEnd()) {
				System.out.println("child: " + i + " : genetic dead end");
				grandChildren[i] = new Plant[0];
			} else {
				System.out.println("child: " + i + " : " + children[i].toString());
				grandChildren[i] = children[i].reproduce();
			}
		}
		System.out.println("grandchildren: ");
		for (int i = 0; i < children.length; i++) {
			System.out.println("through child: " + i);
			for (Plant plant : grandChildren[i]) {
				if (plant.isGeneticDeadEnd()) {
					System.out.println("genetic dead end");
				} else {
					System.out.println(plant.toString());
				}
			}
		}
	}

	public static Plant randomPlant() {
		Random rng = new Random();
		DecimalFormat df = new DecimalFormat("#.#");
		return new Plant(rng.nextInt(MAX_MUTATION) + 1, Float.parseFloat(df.format(rng.nextFloat())),
				Float.parseFloat(df.format(rng.nextFloat())), rng.nextInt(MAX_SIZE) + 1, 1,
				rng.nextInt(MAX_GROW_BATCH) + 1, rng.nextInt(MAX_SEED_BATCH) + 1, rng.nextInt(MAX_SEED_RANGE) + 1);
	}

	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int sizeCap, int startingSize,
			int growBatch, int seedBatch, int seedRange) {
		super(mutationRate, reproduceBehaviour, growBehaviour, startingSize);
		genome.addGene(GeneType.SIZE_CAP, new Gene(1, MAX_SIZE, 1, sizeCap));
		genome.addGene(GeneType.STARTING_SIZE, new Gene(1, MAX_SIZE, 1, startingSize));
		genome.addGene(GeneType.GROW_BATCH, new Gene(1, MAX_GROW_BATCH, 1, growBatch));
		genome.addGene(GeneType.SEED_BATCH, new Gene(1, MAX_SEED_BATCH, 1, seedBatch));
		genome.addGene(GeneType.SEED_RANGE, new Gene(1, MAX_SEED_RANGE, 1, seedRange));
	}

	public Plant(Genome parentGenome) {
		super(parentGenome, (int) parentGenome.getGeneValue(GeneType.STARTING_SIZE));
	}

	public boolean survive() {
		sustain();
		if (energy <= 0) {
			return false;
		} else {
			return true;
		}
	}

	public void chooseBehaviour(Environment environment, EnvironmentTile tile) {
		if (size < genome.getGeneValue(GeneType.SIZE_CAP)
				&& genome.getGeneValue(GeneType.GROW_BEHAVIOUR) > (energy - growCost()) / (size * ENERGY_PER_SIZE)) {
			grow();
		} else if (genome.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR) > (energy - reproduceCost())
				/ (size * ENERGY_PER_SIZE)) {
			environment.scatter(tile, (int) genome.getGeneValue(GeneType.SEED_RANGE), reproduce());
		}
	}

	public Plant[] reproduce() {
		Plant[] children = new Plant[(int) genome.getGeneValue(GeneType.SEED_BATCH)];
		for (int i = 0; i < children.length; i++) {
			children[i] = new Plant(genome);
			children[i].genome.mutate();
		}
		this.energy -= reproduceCost();
		return children;
	}

	public void sustain() {
		energy -= size * SUSTAIN_COST;
	}

	public void grow() {
		size += genome.getGeneValue(GeneType.GROW_BATCH);
		energy -= growCost();
	}

	public float reproduceCost() {
		float singleCloneCost = CLONE_EFFICIENCY * genome.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE;
		float singleSpreadCost = SEED_EFFICIENCY * genome.getGeneValue(GeneType.STARTING_SIZE)
				* genome.getGeneValue(GeneType.SEED_RANGE) * genome.getGeneValue(GeneType.SEED_RANGE);
		return genome.getGeneValue(GeneType.SEED_BATCH) * (singleCloneCost + singleSpreadCost);
	}

	public float growCost() {
		return genome.getGeneValue(GeneType.GROW_BATCH) * ENERGY_PER_SIZE * GROW_EFFICIENCY;
	}

	public float sustainCost() {
		return size * SUSTAIN_COST;
	}

	public boolean isGeneticDeadEnd() {
		if (growCost() + sustainCost() >= genome.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE
				* (1 - genome.getGeneValue(GeneType.GROW_BEHAVIOUR))) {
			return true;
		}
		if (reproduceCost() + sustainCost() >= genome.getGeneValue(GeneType.SIZE_CAP) * ENERGY_PER_SIZE
				* (1 - genome.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR))) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String result = "Plant - ";
		result += "energy: " + energy + ", size: " + size + ", Genome";
		result += genome.toString();
		return result;
	}
}