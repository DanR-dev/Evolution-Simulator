package main;

import java.util.ArrayList;

/*
 * "size" = volume
 */

public abstract class Creature {
	protected static final int MAX_MUTATION = 10;
	protected static final float ENERGY_PER_SIZE = 100;
	
	protected final ArrayList<Gene> genome;
	
	protected int size;
	protected float energy;

	public abstract Creature reproduce();
	public abstract void chooseBehaviour();
	
	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int size) {
		genome = new ArrayList<Gene>();
		genome.add(GeneType.MUTATION_RATE.ordinal(), new Gene(1, MAX_MUTATION, 1, mutationRate));
		genome.add(GeneType.REPRODUCE_BEHAVIOUR.ordinal(), new Gene(0, 1, 0.1F, mutationRate));
		genome.add(GeneType.GROW_BEHAVIOUR.ordinal(), new Gene(0, 1, 0.1F, mutationRate));
		
		this.size = size;
		this.energy = size * ENERGY_PER_SIZE;
	}
	
	public Creature(ArrayList<Gene> genome, int size) {
		this.genome = (ArrayList<Gene>) genome.clone();
		
		this.size = size;
		this.energy = size * ENERGY_PER_SIZE;
	}
	
	public void die() {
		
	}
}