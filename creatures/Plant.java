package creatures;

import java.text.DecimalFormat;
import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import frontEnd.AppRoot;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;
import javafx.scene.paint.Color;

public class Plant extends Creature {
	protected static final Color PLANT_COLOR = Color.GREEN;
	protected static final float SUSTAIN_EFFICIENCY = 0.2F;
	protected static final float CLONE_EFFICIENCY = 20F;
	protected static final float PHOTO_EFFICIENCY = 0.2F;
	protected static final float GROW_EFFICIENCY = 200F;
	protected static final float SEED_EFFICIENCY = 2F;
	protected static final int MAX_STARTING_SIZE = 5;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final int MAX_SIZE = 100;

	public static Plant randomPlant(AppRoot root) {
		Random rng = new Random();
		DecimalFormat df = new DecimalFormat("#.#");
		Plant plant;
		int tries = 0;
		
		do {
			plant = new Plant(rng.nextInt(MAX_MUTATION) + 1, // mutation rate
					Float.parseFloat(df.format(rng.nextFloat())), // reproduce behaviour
					Float.parseFloat(df.format(rng.nextFloat())), // grow behaviour
					(rng.nextInt(10) + 1) * 100, // age cap
					rng.nextInt(MAX_SIZE) + 1, // size cap
					rng.nextInt(MAX_STARTING_SIZE) + 1, // starting size
					rng.nextInt(MAX_SEED_RANGE) + 1, // seed spreading range
					root);
			tries++;
		} while (plant.isGeneticDeadEnd() && tries  < 100);
		return plant;
	}

	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int ageCap, int sizeCap,
			int startingSize, int seedRange, AppRoot root) {
		super(mutationRate, reproduceBehaviour, growBehaviour, ageCap, startingSize, root);

		GENOME.addGene(GeneType.SIZE_CAP, new Gene(1, MAX_SIZE, 1, sizeCap));
		GENOME.addGene(GeneType.STARTING_SIZE, new Gene(1, MAX_STARTING_SIZE, 1, startingSize));
		GENOME.addGene(GeneType.SEED_RANGE, new Gene(1, MAX_SEED_RANGE, 1, seedRange));
	}

	public Plant(Genome parentGenome, AppRoot root) {
		super(parentGenome, (int) parentGenome.getGeneValue(GeneType.STARTING_SIZE), root);
	}

	@Override
	public String toString() {
		String result = "Plant - ";
		result += "energy: " + energy + ", size: " + size + ", Genome";
		result += GENOME.toString();
		return result;
	}

	public Plant reproduce() {
		Plant child = new Plant(GENOME, ROOT);
		child.GENOME.mutate();
		this.energy -= reproduceCost();
		return child;
	}

	public Color getCreatureColor() {
		return PLANT_COLOR;
	}

	public float photosynthesise(float availableEnergy) {
		float absorbed = availableEnergy * PHOTO_EFFICIENCY * (size + MAX_SIZE) / (MAX_SIZE + MAX_SIZE);
		energy += absorbed;

		if (energy > size * ENERGY_PER_SIZE) {
			energy = size * ENERGY_PER_SIZE;
		}
		return availableEnergy - absorbed;
	}

	public float reproduceCost() {
		float singleCloneCost = GENOME.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE * CLONE_EFFICIENCY;
		float singleSpreadCost = GENOME.getGeneValue(GeneType.STARTING_SIZE) * GENOME.getGeneValue(GeneType.SEED_RANGE)
				* (1 + GENOME.getGeneValue(GeneType.SEED_RANGE) / MAX_SEED_RANGE) * SEED_EFFICIENCY;
		return (singleCloneCost + singleSpreadCost);
	}

	public float growCost() {
		return (size / MAX_SIZE) * ENERGY_PER_SIZE / GROW_EFFICIENCY;
	}

	public float sustainCost() {
		return size * SUSTAIN_EFFICIENCY * (1 + (GENOME.getGeneValue(GeneType.AGE_CAP) / 1000F));
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

	public boolean survive() {
		sustain();
		if (energy <= 0 || age > GENOME.getGeneValue(GeneType.AGE_CAP)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean shouldGrow() {
		return (energy - growCost()) / (size * ENERGY_PER_SIZE) >= GENOME.getGeneValue(GeneType.GROW_BEHAVIOUR);
	}

	public boolean shouldReproduce() {
		return (energy - reproduceCost()) / (size * ENERGY_PER_SIZE) >= GENOME
				.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR);
	}

	public void chooseBehaviour(Environment environment, EnvironmentTile tile) {
		if (!survive()) {
			tile.removeCreature(this);
		}
		if (size < GENOME.getGeneValue(GeneType.SIZE_CAP) && shouldGrow()) {
			grow();
		} else if (shouldReproduce()) {
			environment.scatter(tile, (int) GENOME.getGeneValue(GeneType.SEED_RANGE), reproduce());
		}
	}

	public void sustain() {
		energy -= sustainCost();
		age++;
	}

	public void grow() {
		size += 1;
		energy -= growCost();
	}

	public void console() {
		System.out.println("livable: " + !isGeneticDeadEnd());
		System.out.println("size: " + size);
		System.out.println("energy: " + energy);
		System.out.println("age: " + age);
		System.out.println("sustain: " + sustainCost());
		System.out.println("grow: " + growCost());
		System.out.println("reproduce: " + reproduceCost());
		System.out.println(GENOME.toString());
	}
	/**
	 * public static void main(String[] args) { Plant parent = randomPlant();
	 * Plant[] children = parent.reproduce(); Plant[][] grandChildren = new
	 * Plant[children.length][];
	 * 
	 * System.out.println("parent:\n" + parent.toString());
	 * System.out.println("children: "); for (int i = 0; i < children.length; i++) {
	 * if (children[i].isGeneticDeadEnd()) { System.out.println("child: " + i + " :
	 * genetic dead end"); grandChildren[i] = new Plant[0]; } else {
	 * System.out.println("child: " + i + " : " + children[i].toString());
	 * grandChildren[i] = children[i].reproduce(); } }
	 * System.out.println("grandchildren: "); for (int i = 0; i < children.length;
	 * i++) { System.out.println("through child: " + i); for (Plant plant :
	 * grandChildren[i]) { if (plant.isGeneticDeadEnd()) {
	 * System.out.println("genetic dead end"); } else {
	 * System.out.println(plant.toString()); } } } }
	 */
}