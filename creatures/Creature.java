package creatures;

import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/*
 * "size" = volume
 */

public abstract class Creature extends Circle{
	public static final int MAX_MUTATION = 10;
	public static final float ENERGY_PER_SIZE = 100;
	public static final int MAX_SIZE = 100;

	protected final Genome GENOME;
	
	public final Pos POSITION_ON_TILE;

	protected int size;
	protected float energy;

	public abstract Creature[] reproduce();
	public abstract boolean survive();
	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile);
	public abstract Color getCreatureColor();
	
	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int size) {
		super();
		Random rng = new Random();
		
		GENOME = new Genome();
		GENOME.addGene(GeneType.MUTATION_RATE, new Gene(1, MAX_MUTATION, 1, mutationRate));
		GENOME.addGene(GeneType.REPRODUCE_BEHAVIOUR, new Gene(0, 1, 0.1F, reproduceBehaviour));
		GENOME.addGene(GeneType.GROW_BEHAVIOUR, new Gene(0, 1, 0.1F, growBehaviour));

		this.size = size;
		this.energy = size * ENERGY_PER_SIZE;
		
		POSITION_ON_TILE = Pos.values()[rng.nextInt(Pos.values().length)];
		this.setFill(getCreatureColor());
	}

	public Creature(Genome genome, int size) {
		Random rng = new Random();
		try {
			this.GENOME = (Genome) genome.clone();
			this.size = size;
			this.energy = size * ENERGY_PER_SIZE;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("creature cloning error");
		}

		POSITION_ON_TILE = Pos.values()[rng.nextInt(Pos.values().length)];
	}

	public void die() {

	}
	
	public void refresh() {
		this.setRadius(Math.sqrt(size));
	}
	
	public int getSize() {
		return size;
	}
}