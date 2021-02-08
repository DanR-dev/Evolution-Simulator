package creatures;

import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import frontEnd.AppRoot;
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
	public static final float STARTING_ENERGY = 0.1F;
	public static final float ENERGY_PER_SIZE = 100;
	public static final int MAX_MUTATION = 10;
	public static final int MAX_SIZE = 100;

	protected final Genome GENOME;
	protected final AppRoot ROOT;
	
	protected Pos posOnTile;
	protected float energy;
	protected int size;
	protected int age;

	public abstract Color getCreatureColor();
	public abstract Creature[] reproduce();
	public abstract boolean survive();
	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile);
	
	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int ageCap, int size, AppRoot root) {
		super();
		this.ROOT = root;
		
		init(size);
		
		GENOME = new Genome();
		GENOME.addGene(GeneType.MUTATION_RATE, new Gene(1, MAX_MUTATION, 1, mutationRate));
		GENOME.addGene(GeneType.REPRODUCE_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, reproduceBehaviour));
		GENOME.addGene(GeneType.GROW_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, growBehaviour));
		GENOME.addGene(GeneType.AGE_CAP, new Gene(100F, 1000F, 100F, ageCap));
	}
	
	public Creature(Genome genome, int size, AppRoot root) {
		super();
		this.ROOT = root;
		
		init(size);
		
		try {
			this.GENOME = (Genome) genome.clone();
			this.size = size;
			this.energy = size * ENERGY_PER_SIZE * STARTING_ENERGY;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("creature cloning error");
		}
	}
	
	private void init(int size) {
		Random rng = new Random();
		
		this.size = size;
		energy = size * ENERGY_PER_SIZE * STARTING_ENERGY;
		age = 0;
		posOnTile = Pos.values()[rng.nextInt(Pos.values().length)];
		
		setFill(getCreatureColor());
		setStrokeWidth(2);
		setStrokeType(StrokeType.OUTSIDE);
	}
	
	public Pos getPos() {
		return posOnTile;
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
	
	public int getSize() {
		return size;
	}
	
	public void refresh() {
		this.setRadius(Math.sqrt(size));
		this.setStroke(Color.BLUE);
	}
}