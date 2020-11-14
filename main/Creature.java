package main;

/*
 * "size" = volume
 */

public abstract class Creature {
	protected static final int MIN_MUTATION = 1;
	protected static final int MAX_MUTATION = 10;
	protected static final float ENERGY_PER_SIZE = 100;
	
	
	protected int size;
	protected float energy;

	public abstract void mutate();
	public abstract Creature reproduce();
	public abstract void chooseBehaviour();
	
	public Creature() {}

	
	public void die() {
		
	}
}