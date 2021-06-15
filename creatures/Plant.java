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

/**
 * Handles processes specific to plants for simulation and graphical output.
 * 
 * @author danpr
 *
 */
public class Plant extends Creature {
	protected static final Color PLANT_COLOR = Color.GREEN; // visual identifier of creature type
	protected static final float SUSTAIN_EFFICIENCY = 0.2F; // multiplier for cost of surviving the passing of time
	protected static final float CLONE_EFFICIENCY = 20F; // multiplier for cost of creating offspring
	protected static final float PHOTO_EFFICIENCY = 0.2F; // fraction of light capturable during photosynthesis
	protected static final float GROW_EFFICIENCY = 0.005F; // multiplier for cost of growing
	protected static final float SEED_EFFICIENCY = 2F; // multiplier for cost of spreading seeds
	protected static final int MAX_STARTING_SIZE = 5;
	protected static final int MAX_SEED_RANGE = 10;
	protected static final int MIN_AGE = 100;
	protected static final int MAX_AGE = 1000;

	/**
	 * Generate a new plant.
	 * 
	 * @param mutationRate       number of mutations to occur during reproduction
	 * @param reproduceBehaviour proportion of energy to retain during reproduction
	 * @param growBehaviour      proportion of energy to retain during growth
	 * @param ageCap             age at which the creature will naturally die
	 * @param sizeCap            maximum size to grow to
	 * @param startingSize       initial size of offspring
	 * @param seedRange          maximum distribution range of offspring
	 * @param root               program-wide action reference
	 */
	public Plant(int mutationRate, float reproduceBehaviour, float growBehaviour, int ageCap, int sizeCap,
			int startingSize, int seedRange, AppRoot root) {
		super(mutationRate, reproduceBehaviour, growBehaviour, ageCap, startingSize, root);

		GENOME.addGene(GeneType.SIZE_CAP, new Gene(1, MAX_SIZE, 1, sizeCap));
		GENOME.addGene(GeneType.STARTING_SIZE, new Gene(1, MAX_STARTING_SIZE, 1, startingSize));
		GENOME.addGene(GeneType.SEED_RANGE, new Gene(1, MAX_SEED_RANGE, 1, seedRange));
	}

	/**
	 * Generate a genetic clone of the plant with the given genome.
	 * 
	 * @param parentGenome genome to clone
	 * @param root         program-wide action reference
	 */
	public Plant(Genome parentGenome, AppRoot root) {
		super(parentGenome, (int) parentGenome.getGeneValue(GeneType.STARTING_SIZE), root);
	}

	/**
	 * Generate a genetically random plant that is theoretically capable of
	 * survival.
	 * 
	 * @param root program-wide action reference
	 * @return generated plant
	 */
	public static Plant randomPlant(AppRoot root) {
		Random rng = new Random();
		DecimalFormat df = new DecimalFormat("#.#");
		Plant plant;
		int tries = 0;

		do {
			plant = new Plant(rng.nextInt(MAX_MUTATION) + 1, // mutation rate
					Float.parseFloat(df.format(rng.nextFloat())), // reproduce behaviour
					Float.parseFloat(df.format(rng.nextFloat())), // grow behaviour
					(rng.nextInt(10) + 1) * MIN_AGE, // age cap
					rng.nextInt(MAX_SIZE) + 1, // size cap
					rng.nextInt(MAX_STARTING_SIZE) + 1, // starting size
					rng.nextInt(MAX_SEED_RANGE) + 1, // seed spreading range
					root);
			tries++;
		} while (plant.isGeneticDeadEnd() && tries < 100);
		return plant;
	}

	/**
	 * Generate a viable example of a plant.
	 * 
	 * @param root program-wide action reference
	 * @return generated plant
	 */
	public static Plant demoPlant(AppRoot root) {
		return new Plant(3, // mutation rate
				0.1F, // reproduce behaviour
				0.1F, // grow behaviour
				500, // age cap
				50, // size cap
				1, // starting size
				5, // seed spreading range
				root);
	}

	/**
	 * generate a string containing the plant data.
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		String result = "Plant - ";
		result += "energy: " + energy + ", size: " + size + "age: " + age + ", Genome";
		result += GENOME.toString();
		return result;
	}

	/**
	 * generate and mutate an offspring to this plant.
	 * 
	 * @return a genetically similar offspring
	 */
	public Plant reproduce() {
		Plant child = new Plant(GENOME, ROOT);
		child.GENOME.mutate();
		this.energy -= reproduceCost();
		return child;
	}

	/**
	 * creature colour getter.
	 */
	public Color getCreatureColor() {
		return PLANT_COLOR;
	}

	/**
	 * absorbs a portion of given light as energy.
	 * 
	 * @param availableEnergy total available light
	 * @return un-absorbed light
	 */
	public float photosynthesise(float availableEnergy) {
		float absorbed = availableEnergy * PHOTO_EFFICIENCY * (size + MAX_SIZE) / (MAX_SIZE + MAX_SIZE);
		energy += absorbed;

		if (energy > size * ENERGY_PER_SIZE) {
			energy = size * ENERGY_PER_SIZE;
		}
		return availableEnergy - absorbed;
	}

	/**
	 * calculate the total energy cost for this plant to reproduce.
	 * 
	 * @return
	 */
	public float reproduceCost() {
		float singleCloneCost = GENOME.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE * CLONE_EFFICIENCY;
		float singleSpreadCost = (GENOME.getGeneValue(GeneType.STARTING_SIZE) * GENOME.getGeneValue(GeneType.SEED_RANGE)
				* SEED_EFFICIENCY) / MAX_SEED_RANGE;
		return (singleCloneCost + singleSpreadCost);
	}

	/**
	 * calculates the total energy cost for this plant to grow.
	 * 
	 * @return
	 */
	public float growCost() {
		return (size / MAX_SIZE) * ENERGY_PER_SIZE * GROW_EFFICIENCY;
	}

	/**
	 * calculates the total energy cost for this plant to survive a single
	 * time-step.
	 * 
	 * @return
	 */
	public float sustainCost() {
		return size * SUSTAIN_EFFICIENCY * (1 + (GENOME.getGeneValue(GeneType.AGE_CAP) / MAX_AGE));
	}

	/**
	 * calculates if this plants' genes make it unable to eventually reproduce.
	 * 
	 * @return true if dead-end, false otherwise
	 */
	public boolean isGeneticDeadEnd() {
		if (growCost() + sustainCost() >= GENOME.getGeneValue(GeneType.STARTING_SIZE) * ENERGY_PER_SIZE
				* (1 - GENOME.getGeneValue(GeneType.GROW_BEHAVIOUR))) {
			return true;
		} // Plant will never grow from seeding size

		if (reproduceCost() + sustainCost() >= GENOME.getGeneValue(GeneType.SIZE_CAP) * ENERGY_PER_SIZE
				* (1 - GENOME.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR))) {
			return true;
		} // Plant will never reproduce from mature size

		return false;
	}

	/**
	 * subtracts the energy required to survive 1 time-step and calculates whether
	 * the plant survives.
	 * 
	 * @return true if survived, false if died
	 */
	public boolean survive() {
		sustain();
		if (energy <= 0 || age > GENOME.getGeneValue(GeneType.AGE_CAP)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * calculates if this plant should grow this time-step according to its genetic
	 * behaviour.
	 * 
	 * @return true if should grow, false otherwise.
	 */
	public boolean shouldGrow() {
		return (energy - growCost()) / (size * ENERGY_PER_SIZE) >= GENOME.getGeneValue(GeneType.GROW_BEHAVIOUR);
	}

	/**
	 * calculates if this plant should reproduce this time-step according to its
	 * genetic behaviour.
	 * 
	 * @return true if should reproduce, false otherwise.
	 */
	public boolean shouldReproduce() {
		return (energy - reproduceCost()) / (size * ENERGY_PER_SIZE) >= GENOME
				.getGeneValue(GeneType.REPRODUCE_BEHAVIOUR);
	}

	/**
	 * decide the actions of this plant for a single timestep.
	 */
	public void chooseBehaviour(Environment environment, EnvironmentTile tile) {
		if (!survive()) {
			tile.removeCreature(this); // die
		} else if (size < GENOME.getGeneValue(GeneType.SIZE_CAP) && shouldGrow()) {
			grow(); // or grow
		} else if (shouldReproduce()) {
			environment.scatter(tile, (int) GENOME.getGeneValue(GeneType.SEED_RANGE), reproduce()); // or reproduce
		}
	}

	/**
	 * subtract the energy required for this plant to survive a single time-step.
	 */
	public void sustain() {
		energy -= sustainCost();
		age++;
	}

	/**
	 * grow by 1 and subtract the energy cost of growth.
	 */
	public void grow() {
		size += 1;
		energy -= growCost();
	}
}