package creatures;

import java.text.DecimalFormat;
import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Plant extends Creature {
	protected static final int SUSTAIN_COST = 1;
	protected static final int MAX_SIZE = 100;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final int MAX_GROW_BATCH = 10;
	protected static final int MAX_SEED_BATCH = 10;
	protected static final float GROW_EFFICIENCY = 0.5F;
	protected static final float CLONE_EFFICIENCY = 0.5F;
	protected static final float SEED_EFFICIENCY = 0.5F;
	protected static final float PHOTO_EFFICIENCY = 0.5F;
	
	protected static final Color PLANT_COLOR = Color.GREEN;

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
		int sizeCap = rng.nextInt(MAX_SIZE) + 1;
		
		return new Plant(rng.nextInt(MAX_MUTATION) + 1, Float.parseFloat(df.format(rng.nextFloat())),
				Float.parseFloat(df.format(rng.nextFloat())), sizeCap, rng.nextInt(sizeCap) + 1,
				rng.nextInt(MAX_GROW_BATCH) + 1, rng.nextInt(MAX_SEED_BATCH) + 1, rng.nextInt(MAX_SEED_RANGE) + 1);
	}

	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int sizeCap, int startingSize,
			int growBatch, int seedBatch, int seedRange) {
		super(mutationRate, reproduceBehaviour, growBehaviour, startingSize);
		GENOME.addGene(GeneType.SIZE_CAP, new Gene(1, MAX_SIZE, 1, sizeCap));
		GENOME.addGene(GeneType.STARTING_SIZE, new Gene(1, MAX_SIZE, 1, startingSize));
		GENOME.addGene(GeneType.GROW_BATCH, new Gene(1, MAX_GROW_BATCH, 1, growBatch));
		GENOME.addGene(GeneType.SEED_BATCH, new Gene(1, MAX_SEED_BATCH, 1, seedBatch));
		GENOME.addGene(GeneType.SEED_RANGE, new Gene(1, MAX_SEED_RANGE, 1, seedRange));
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
		if (size < GENOME.getGeneValue(GeneType.SIZE_CAP)
				&& GENOME.getGeneValue(GeneType.GROW_BEHAVIOUR) > (energy - growCost()) / (size * ENERGY_PER_SIZE)) {
			grow();
		} else if (GENOME.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR) > (energy - reproduceCost())
				/ (size * ENERGY_PER_SIZE)) {
			environment.scatter(tile, (int) GENOME.getGeneValue(GeneType.SEED_RANGE), reproduce());
		}
	}

	public Plant[] reproduce() {
		Plant[] children = new Plant[(int) GENOME.getGeneValue(GeneType.SEED_BATCH)];
		for (int i = 0; i < children.length; i++) {
			children[i] = new Plant(GENOME);
			children[i].GENOME.mutate();
		}
		this.energy -= reproduceCost();
		return children;
	}

	public void sustain() {
		energy -= size * SUSTAIN_COST;
	}

	public void grow() {
		size += GENOME.getGeneValue(GeneType.GROW_BATCH);
		energy -= growCost();
	}

	public float reproduceCost() {
		float singleCloneCost = CLONE_EFFICIENCY * GENOME.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE;
		float singleSpreadCost = SEED_EFFICIENCY * GENOME.getGeneValue(GeneType.STARTING_SIZE)
				* GENOME.getGeneValue(GeneType.SEED_RANGE) * GENOME.getGeneValue(GeneType.SEED_RANGE);
		return GENOME.getGeneValue(GeneType.SEED_BATCH) * (singleCloneCost + singleSpreadCost);
	}

	public float growCost() {
		return GENOME.getGeneValue(GeneType.GROW_BATCH) * ENERGY_PER_SIZE * GROW_EFFICIENCY;
	}

	public float sustainCost() {
		return size * SUSTAIN_COST;
	}

	public boolean isGeneticDeadEnd() {
		if (growCost() + sustainCost() >= GENOME.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE
				* (1 - GENOME.getGeneValue(GeneType.GROW_BEHAVIOUR))) {
			return true;
		}
		if (reproduceCost() + sustainCost() >= GENOME.getGeneValue(GeneType.SIZE_CAP) * ENERGY_PER_SIZE
				* (1 - GENOME.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR))) {
			return true;
		}

		return false;
	}
	
	public Color getCreatureColor() {
		return PLANT_COLOR;
	}
	
	public float photosynthesise(float availableEnergy) {
		energy += availableEnergy * PHOTO_EFFICIENCY;

		if(energy > size * ENERGY_PER_SIZE) {
			energy = size * ENERGY_PER_SIZE;
		}
		return availableEnergy * (1 - PHOTO_EFFICIENCY);
	}

	@Override
	public String toString() {
		String result = "Plant - ";
		result += "energy: " + energy + ", size: " + size + ", Genome";
		result += GENOME.toString();
		return result;
	}
}