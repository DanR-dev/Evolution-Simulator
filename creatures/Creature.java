package creatures;

import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/*
 * "size" = volume
 */

public abstract class Creature extends Circle implements Comparable<Creature>{
	public static final int MAX_MUTATION = 10;
	public static final float ENERGY_PER_SIZE = 100;
	public static final int MAX_SIZE = 100;
	public static final float STARTING_ENERGY = 0.1F;

	protected final Genome GENOME;
	
	public final Pos POSITION_ON_TILE;

	protected int size;
	protected float energy;
	protected int age;

	public abstract Creature[] reproduce();
	public abstract boolean survive();
	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile);
	public abstract Color getCreatureColor();
	
	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int ageCap, int size) {
		super();
		Random rng = new Random();
		
		GENOME = new Genome();
		GENOME.addGene(GeneType.MUTATION_RATE, new Gene(1, MAX_MUTATION, 1, mutationRate));
		GENOME.addGene(GeneType.REPRODUCE_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, reproduceBehaviour));
		GENOME.addGene(GeneType.GROW_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, growBehaviour));
		GENOME.addGene(GeneType.AGE_CAP, new Gene(100F, 1000F, 100F, ageCap));

		this.size = size;
		this.energy = size * ENERGY_PER_SIZE * STARTING_ENERGY;
		this.age = 0;
		
		POSITION_ON_TILE = Pos.values()[rng.nextInt(Pos.values().length)];
		
		this.setFill(getCreatureColor());
		this.setStrokeWidth(2);
		this.setStrokeType(StrokeType.OUTSIDE);
	}

	public Creature(Genome genome, int size) {
		Random rng = new Random();
		try {
			this.GENOME = (Genome) genome.clone();
			this.size = size;
			this.energy = size * ENERGY_PER_SIZE * STARTING_ENERGY;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("creature cloning error");
		}
		this.age = 0;

		POSITION_ON_TILE = Pos.values()[rng.nextInt(Pos.values().length)];
		
		this.setFill(getCreatureColor());
		this.setStrokeWidth(2);
		this.setStrokeType(StrokeType.OUTSIDE);
	}
	
	public void refresh() {
		this.setRadius(Math.sqrt(size));
		this.setStroke(Color.BLUE);
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public int compareTo(Creature creature) {
		if(size < creature.getSize()) {
			return -1;
		} else if(size > creature.getSize()) {
			return 1;
		} else {
			return 0;
		}
	}
}