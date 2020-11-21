package backEnd.creatures;

import backEnd.environments.Environment;
import backEnd.environments.EnvironmentTile;
import backEnd.genetics.Gene;
import backEnd.genetics.GeneType;
import backEnd.genetics.Genome;

/*
 * "size" = volume
 */

public abstract class Creature {
	protected static final int MAX_MUTATION = 10;
	protected static final float ENERGY_PER_SIZE = 100;

	protected final Genome genome;

	protected int size;
	protected float energy;

	public abstract Creature[] reproduce();

	public abstract boolean survive();

	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile);

	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int size) {
		genome = new Genome();
		genome.addGene(GeneType.MUTATION_RATE, new Gene(1, MAX_MUTATION, 1, mutationRate));
		genome.addGene(GeneType.REPRODUCE_BEHAVIOUR, new Gene(0, 1, 0.1F, reproduceBehaviour));
		genome.addGene(GeneType.GROW_BEHAVIOUR, new Gene(0, 1, 0.1F, growBehaviour));

		this.size = size;
		this.energy = size * ENERGY_PER_SIZE;
	}

	public Creature(Genome genome, int size) {
		try {
			this.genome = (Genome) genome.clone();
			this.size = size;
			this.energy = size * ENERGY_PER_SIZE;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("creature cloning error");
		}
	}

	public void die() {

	}
}