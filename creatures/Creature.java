package creatures;

import java.util.Random;

import environments.Environment;
import environments.EnvironmentTile;
import frontEnd.AppRoot;
import frontEnd.CreatureWindow;
import genetics.Gene;
import genetics.GeneType;
import genetics.Genome;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/*
 * "size" = volume
 */

/**
 * Handles common creature data and processes for simulation and graphical
 * output.
 * 
 * @author danpr
 *
 */
public abstract class Creature extends Circle implements Comparable<Creature> {
	public static final float STARTING_ENERGY = 0.1F; // fraction of maximum energy that creatures should start with
	public static final float ENERGY_PER_SIZE = 100; // maximum energy capacity per size
	public static final int MAX_MUTATION = 10; // max mutations to occur in an offspring
	public static final int MAX_SIZE = 100;

	public final Genome GENOME;
	public final AppRoot ROOT; // reference for program-wide access

	protected Pos posOnTile; // creatures position when rendered on a tile (aesthetic only)
	protected float energy;
	protected int size;
	protected int age;

	protected CreatureWindow creatureStats = null; // reference for updating of graphical output
	protected ProgressBar energyOutput = null; // ^
	protected ProgressBar sizeOutput = null; // ^
	protected ProgressBar ageOutput = null; // ^

	public abstract Color getCreatureColor();

	public abstract Creature reproduce(); // produce a genetically similar offspring (at appropriate energy cost)

	public abstract boolean survive(); // check if the creature survives a timestep

	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile); // process a single timestep
																							// for this creature
																							// possibly including
																							// creature actions

	/**
	 * Generate a new creature.
	 * 
	 * @param mutationRate       number of mutations to occur during reproduction
	 * @param reproduceBehaviour proportion of energy to retain during reproduction
	 * @param growBehaviour      proportion of energy to retain during growth
	 * @param ageCap             age at which the creature will naturally die
	 * @param size               initial size of the creature
	 * @param root               program-wide action reference
	 */
	public Creature(int mutationRate, float reproduceBehaviour, float growBehaviour, int ageCap, int size,
			AppRoot root) {
		super();
		this.ROOT = root;

		init(size);

		GENOME = new Genome();
		GENOME.addGene(GeneType.MUTATION_RATE, new Gene(1, MAX_MUTATION, 1, mutationRate));
		GENOME.addGene(GeneType.REPRODUCE_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, reproduceBehaviour));
		GENOME.addGene(GeneType.GROW_BEHAVIOUR, new Gene(0F, 0.9F, 0.1F, growBehaviour));
		GENOME.addGene(GeneType.AGE_CAP, new Gene(100F, 1000F, 100F, ageCap));
	}

	/**
	 * Generate a genetic clone of the given creature.
	 * 
	 * @param genome genome of parent
	 * @param size   size of clone
	 * @param root   program-wide action reference
	 */
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

	/**
	 * generate a graphical item to display creature outputs and local references to
	 * allow live updating.
	 * 
	 * @return VBox containing creature data outputs (energy, size, age)
	 */
	public VBox getStatsBox() {
		VBox box = new VBox();
		VBox[] stats = new VBox[3];
		VBox genetics = GENOME.getOutput();

		energyOutput = new ProgressBar();
		sizeOutput = new ProgressBar();
		ageOutput = new ProgressBar();

		for (int i = 0; i < 3; i++) {
			stats[i] = new VBox();
			box.getChildren().add(stats[i]);
		}

		stats[0].getChildren().add(new Label("Energy:"));
		stats[0].getChildren().add(energyOutput);
		stats[1].getChildren().add(new Label("Size:"));
		stats[1].getChildren().add(sizeOutput);
		stats[2].getChildren().add(new Label("Age:"));
		stats[2].getChildren().add(ageOutput);

		box.getChildren().add(genetics);

		return box;
	}

	/**
	 * bumper-function for this creature's Genome.
	 */
	public float[] getGeneMagnitudes() {
		return GENOME.getMagnitudes();
	}

	/**
	 * posOnTile getter.
	 * 
	 * @return
	 */
	public Pos getPos() {
		return posOnTile;
	}

	/**
	 * compares this creature with another based on size for the Comparable
	 * interface.
	 */
	@Override
	public int compareTo(Creature creature) {
		if (size < creature.getSize()) {
			return -1;
		} else if (size > creature.getSize()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * size getter.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * refresh graphical output of creature and creature data (creature size is
	 * proportional to circle area)
	 */
	public void refresh() {
		setRadius(Math.sqrt(size));

		refreshOutput();
	}

	/**
	 * update graphical output of creature data (if exists).
	 */
	public void refreshOutput() {
		if (creatureStats == null || !creatureStats.isShowing()) {
			creatureStats = null;
			energyOutput = null;
			sizeOutput = null;
			ageOutput = null;
		} else {
			energyOutput.setProgress(energy / (size * ENERGY_PER_SIZE));
			sizeOutput.setProgress(size / GENOME.getGeneValue(GeneType.SIZE_CAP));
			ageOutput.setProgress(age / GENOME.getGeneValue(GeneType.AGE_CAP));
		}
	}

	/**
	 * initialise creature data, graphical appearance, and interactivity.
	 * 
	 * @param size
	 */
	private void init(int size) {
		Random rng = new Random();

		this.size = size;
		energy = size * ENERGY_PER_SIZE * STARTING_ENERGY;
		age = 0;
		posOnTile = Pos.values()[rng.nextInt(9)]; // first 9 Pos values are in square, last 3 are outside square

		setFill(getCreatureColor());
		setStrokeWidth(2);
		setStrokeType(StrokeType.OUTSIDE);
		setStroke(Color.BLACK);

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			/**
			 * on click, open a new CreatureWindow with this creature's data (if not already
			 * opened)
			 */
			@Override
			public void handle(MouseEvent event) {
				if (creatureStats == null || !creatureStats.isShowing()) {
					creatureStats = ROOT.creatureWindow(Creature.this);
				}
			}
		});
	}
}