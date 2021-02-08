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

public abstract class Creature extends Circle implements Comparable<Creature> {
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

	protected CreatureWindow statWindow = null;
	protected ProgressBar energyOutput = null;
	protected ProgressBar sizeOutput = null;
	protected ProgressBar ageOutput = null;

	public abstract Color getCreatureColor();

	public abstract Creature[] reproduce();

	public abstract boolean survive();

	public abstract void chooseBehaviour(Environment environment, EnvironmentTile tile);

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

	public void refreshOutput() {
		if (statWindow == null || !statWindow.isShowing()) {
			statWindow = null;
			energyOutput = null;
			sizeOutput = null;
			ageOutput = null;
		} else {
			energyOutput.setProgress(energy / (size * ENERGY_PER_SIZE));
			sizeOutput.setProgress(size / GENOME.getGeneValue(GeneType.SIZE_CAP));
			ageOutput.setProgress(age / GENOME.getGeneValue(GeneType.AGE_CAP));
		}
	}

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

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (statWindow == null || !statWindow.isShowing()) {
					statWindow = ROOT.creatureWindow(Creature.this);
				}
			}
		});
	}

	public Pos getPos() {
		return posOnTile;
	}

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

	public int getSize() {
		return size;
	}

	public void refresh() {
		this.setRadius(Math.sqrt(size));
		this.setStroke(Color.BLUE);

		refreshOutput();
	}
}