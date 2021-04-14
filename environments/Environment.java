package environments;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import creatures.Creature;
import creatures.Plant;
import frontEnd.AppRoot;
import genetics.GeneType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Environment extends VBox {
	private static final Timer TIMER = new Timer();

	private static final float SUNLIGHT = 200;

	private static final int HISTORY_LENGTH = 5000;
	private static final int CONTROL_SPACING = 10;
	private static final int MIN_KILL_PERIOD = 1;
	private static final int MAX_KILL_PERIOD = 10000;
	private static final int MIN_KILL_RADIUS = 0;
	private static final int MAX_KILL_RADIUS = 10;

	private static final int FRAME_TIME = 1000;

	private final AppRoot ROOT;

	private BarChart<String, Number> sizeHistogram;
	private LineChart<Number, Number> biomassGraph;
	private LineChart<Number, Number> geneGraph;
	private BarChart<String, Number>[] geneHistograms = new BarChart[GeneType.values().length];

	private HBox controls;
	private GridPane simArea;
	private EnvironmentTile[][] tiles;

	private SimTimer simTimer = new SimTimer(this);
	
	private CheckBox killPeriodicCheck;
	private TextField killPeriodInput;
	private TextField killAreaInput;

	private int simTime = 0;
	private int simSpeed = 0;

	public Environment(int width, int height, AppRoot root) {
		super();
		this.ROOT = root;
		initGrid(width, height, root);
		refresh(0);
	}

	public Environment(int width, int height, int nClusters, int clusterSize, int clusterWidth, AppRoot root) {
		super();

		controls = new HBox();
		simArea = new GridPane();

		this.getChildren().add(controls);
		this.getChildren().add(simArea);

		this.ROOT = root;
		initControls();
		initGrid(width, height, root);
		initCreatures(nClusters, clusterSize, clusterWidth);

		refresh(0);
	}

	private void initControls() {
		HBox stepControls = new HBox();
		Button step1 = new Button();
		Button step10 = new Button();
		Button step100 = new Button();
		Button step1000 = new Button();
		HBox timeControls = new HBox();
		Button timeStop = new Button();
		Button time1 = new Button();
		Button time10 = new Button();
		Button time100 = new Button();
		Button time1000 = new Button();
		HBox killControls = new HBox();
		Button killAll = new Button();
		Button seedLife = new Button();
		VBox killPeriodBox = new VBox();
		VBox killAreaBox = new VBox();
		Label killPeriodLabel = new Label();
		Label killAreaLabel = new Label();
		killPeriodicCheck = new CheckBox();
		killPeriodInput = new TextField();
		killAreaInput = new TextField();

		step1.setText("Step 1");
		step1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.simulateSingle(1);
			}
		});

		step10.setText("Step 10");
		step10.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.simulateSingle(10);
			}
		});

		step100.setText("Step 100");
		step100.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.simulateSingle(100);
			}
		});

		step1000.setText("Step 1000");
		step1000.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.simulateSingle(1000);
			}
		});

		timeStop.setText("Sim Stop");
		timeStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.simSpeed = 0;
			}
		});

		time1.setText("Sim Speed x1");
		time1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Environment.this.simSpeed == 0) {
					Environment.this.simSpeed = 1;
					simulateContinuous();
				} else {
					Environment.this.simSpeed = 1;
				}
			}
		});

		time10.setText("Sim Speed x10");
		time10.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Environment.this.simSpeed == 0) {
					Environment.this.simSpeed = 10;
					simulateContinuous();
				} else {
					Environment.this.simSpeed = 10;
				}
			}
		});

		time100.setText("Sim Speed x100");
		time100.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Environment.this.simSpeed == 0) {
					Environment.this.simSpeed = 100;
					simulateContinuous();
				} else {
					Environment.this.simSpeed = 100;
				}
			}
		});

		time1000.setText("Sim Speed x1000");
		time1000.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (Environment.this.simSpeed == 0) {
					Environment.this.simSpeed = 1000;
					simulateContinuous();
				} else {
					Environment.this.simSpeed = 1000;
				}
			}
		});

		killAll.setText("Clear");
		killAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(int i = 0; i < Environment.this.tiles.length; i++) {
					for(int j = 0; j < Environment.this.tiles[0].length; j++) {
						Environment.this.tiles[i][j].killCreatures();
					}
				}
				Environment.this.refresh(0);
			}
		});

		seedLife.setText("New Plant");
		seedLife.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Environment.this.initCreatures(1, 1, 1);;
				Environment.this.refresh(0);
			}
		});
		
		killPeriodicCheck.setText("Periodic Cull");
		killPeriodLabel.setText("Cull Period");
		killAreaLabel.setText("Cull Radius");
		
		killPeriodBox.getChildren().add(killPeriodLabel);
		killPeriodBox.getChildren().add(killPeriodInput);
		killPeriodBox.setAlignment(Pos.CENTER);
		killAreaBox.getChildren().add(killAreaLabel);
		killAreaBox.getChildren().add(killAreaInput);
		killAreaBox.setAlignment(Pos.CENTER);

		stepControls.getChildren().add(step1);
		stepControls.getChildren().add(step10);
		stepControls.getChildren().add(step100);
		stepControls.getChildren().add(step1000);
		timeControls.getChildren().add(timeStop);
		timeControls.getChildren().add(time1);
		timeControls.getChildren().add(time10);
		timeControls.getChildren().add(time100);
		timeControls.getChildren().add(time1000);
		killControls.getChildren().add(killAll);
		killControls.getChildren().add(seedLife);
		killControls.getChildren().add(killPeriodicCheck);
		killControls.getChildren().add(killPeriodBox);
		killControls.getChildren().add(killAreaBox);
		controls.getChildren().add(stepControls);
		controls.getChildren().add(timeControls);
		controls.getChildren().add(killControls);
		controls.setSpacing(CONTROL_SPACING);
	}

	private void initGrid(int width, int height, AppRoot root) {
		tiles = new EnvironmentTile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new EnvironmentTile(i, j, root);
				simArea.add(tiles[i][j], i, j);
			}
		}
		simArea.setGridLinesVisible(true);
	}

	private void initCreatures(int nClusters, int clusterSize, int clusterWidth) {
		Random rng = new Random();
		int clusterX;
		int clusterY;
		Creature[] clusterCreatures;
		for (int i = 0; i < nClusters; i++) {
			clusterX = rng.nextInt(tiles.length);
			clusterY = rng.nextInt(tiles[0].length);
			clusterCreatures = new Creature[clusterSize];
			for (int j = 0; j < clusterSize; j++) {
				clusterCreatures[j] = Plant.randomPlant(ROOT);
			}
			scatterAll(tiles[clusterX][clusterY], clusterWidth, clusterCreatures);
		}
	}

	private Chart sizeHistogram() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		Series<String, Number> series = new Series<String, Number>();

		xAxis.setLabel("Size");
		yAxis.setLabel("Number");
		sizeHistogram = new BarChart<String, Number>(xAxis, yAxis);
		sizeHistogram.setTitle("Creature size");
		series.setName("Size");
		sizeHistogram.getData().add(series);

		refreshSizeHistogram();
		return sizeHistogram;
	}

	private void refreshSizeHistogram() {
		if (sizeHistogram != null) {
			Series<String, Number> series;
			ObservableList<Data<String, Number>> data;
			Creature[] creatures = getCreatures();
			int numSize;

			series = sizeHistogram.getData().get(0);

			data = series.getData();

			for (int i = 1; i < Creature.MAX_SIZE; i++) {
				numSize = 0;
				for (Creature creature : creatures) {
					if (creature.getSize() == i) {
						numSize++;
					}
				}
				data.add(new Data<String, Number>("" + i, numSize));
			}
		}
	}

	private Chart biomassGraph() {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		Series<Number, Number> series = new Series<Number, Number>();

		xAxis.setLabel("Time");
		yAxis.setLabel("Total Biomass");
		biomassGraph = new LineChart<Number, Number>(xAxis, yAxis);
		biomassGraph.setTitle("Biomass Over Time");
		series.setName("Total Biomass");

		biomassGraph.getData().add(series);
		refreshBiomassGraph(0);
		return biomassGraph;
	}

	private void refreshBiomassGraph(int step) {
		if (biomassGraph != null && getCreatures().length != 0) {
			Series<Number, Number> series = biomassGraph.getData().get(0);
			ObservableList<Data<Number, Number>> data = series.getData();
			Creature[] creatures = getCreatures();
			int totalMass = 0;
			int nextTime;

			for (int i = 0; i < data.size(); i++) {
				nextTime = (int) data.get(i).getXValue() - step;
				if (nextTime >= -HISTORY_LENGTH) {
					data.get(i).setXValue(nextTime);
				} else {
					data.get(i).setXValue(nextTime);
					data.remove(i);
				}
			}

			for (Creature creature : creatures) {
				totalMass += creature.getSize();
			}
			data.add(new Data<Number, Number>(0, totalMass));
		}
	}

	private Chart geneGraph() {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		Series<Number, Number>[] seriesSet = new Series[GeneType.values().length];

		for (int i = 0; i < seriesSet.length; i++) {
			seriesSet[i] = new Series<Number, Number>();
			seriesSet[i].setName(GeneType.values()[i].alias());
		}

		xAxis.setLabel("Time");
		yAxis.setLabel("Gene Values (proportional)");
		geneGraph = new LineChart<Number, Number>(xAxis, yAxis);
		geneGraph.setTitle("Gene Values Over Time");

		geneGraph.getData().addAll(seriesSet);
		refreshGeneGraph(0);
		return geneGraph;
	}

	private void refreshGeneGraph(int step) {
		if (geneGraph != null && getCreatures().length != 0) {
			Creature[] creatures = getCreatures();
			Series<Number, Number> currentSeries;
			ObservableList<Data<Number, Number>> currentData;
			int nextDatapointTime;
			float[] averageGeneMags = new float[GeneType.values().length];
			float[] currentGeneMags;

			for (Creature creature : creatures) {
				currentGeneMags = creature.getGeneMagnitudes();
				for (int i = 0; i < GeneType.values().length; i++) {
					averageGeneMags[i] += currentGeneMags[i];
				}
			}
			for (int i = 0; i < GeneType.values().length; i++) {
				averageGeneMags[i] = averageGeneMags[i] / creatures.length;
			}

			for (GeneType type : GeneType.values()) {
				currentSeries = geneGraph.getData().get(type.ordinal());
				currentData = currentSeries.getData();

				for (int i = 0; i < currentData.size(); i++) {
					nextDatapointTime = (int) currentData.get(i).getXValue() - step;
					if (nextDatapointTime >= -HISTORY_LENGTH) {
						currentData.get(i).setXValue(nextDatapointTime);
					} else {
						currentData.get(i).setXValue(nextDatapointTime);
						currentData.remove(i);
					}
				}
				currentData.add(new Data<Number, Number>(0, averageGeneMags[type.ordinal()]));
			}
		}
	}

	private Chart[] geneHistograms() {
		CategoryAxis xAxis[] = new CategoryAxis[GeneType.values().length];
		NumberAxis yAxis[] = new NumberAxis[GeneType.values().length];
		Series<String, Number> series[] = new Series[GeneType.values().length];

		for (int i = 0; i < GeneType.values().length; i++) {
			xAxis[i] = new CategoryAxis();
			yAxis[i] = new NumberAxis();
			xAxis[i].setLabel("Gene Value");
			yAxis[i].setLabel("Number");

			geneHistograms[i] = new BarChart<String, Number>(xAxis[i], yAxis[i]);
			geneHistograms[i].setTitle("Distribution of " + GeneType.values()[i].alias());

			series[i] = new Series<String, Number>();
			series[i].setName(GeneType.values()[i].alias());
			geneHistograms[i].getData().add(series[i]);
		}

		refreshGeneHistograms();
		return geneHistograms;
	}

	private void refreshGeneHistograms() {
		Creature[] creatures = getCreatures();

		for (int i = 0; i < GeneType.values().length; i++) {
			if (geneHistograms[i] != null) {
				Series<String, Number> series = geneHistograms[i].getData().get(0);
				ObservableList<Data<String, Number>> data = series.getData();
				int numMag;

				if (creatures.length != 0) {
					for (float j = creatures[0].GENOME.getGene(GeneType.values()[i]).MIN_VALUE; j <= creatures[0].GENOME
							.getGene(GeneType.values()[i]).MAX_VALUE; j += creatures[0].GENOME
									.getGene(GeneType.values()[i]).INCREMENTS) {
						numMag = 0;
						for (Creature creature : creatures) {
							if (creature.GENOME.getGene(GeneType.values()[i]).getValue() == j) {
								numMag++;
							}
						}
						data.add(new Data<String, Number>("" + j, numMag));
					}
				}
			}
		}
	}

	public Chart[] getBasicCharts() {
		Chart[] charts = new Chart[3];

		charts[0] = sizeHistogram();
		charts[1] = biomassGraph();
		charts[2] = geneGraph();

		return charts;
	}

	public Chart[] getGeneHistograms() {
		return geneHistograms();
	}

	public Creature[] getCreatures() {
		ArrayList<Creature> creatures = new ArrayList<Creature>();

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				for (Creature creature : getTile(i, j).getCreatures()) {
					creatures.add(creature);
				}
			}
		}

		return creatures.toArray(new Creature[0]);
	}

	public EnvironmentTile getTile(int x, int y) { // wraps
		int trueX;
		int trueY;

		if (x >= 0) {
			trueX = x % tiles.length;
		} else {
			trueX = x % tiles.length;
			if(trueX < 0) {
				trueX += tiles.length;
			}
		}
		if (y >= 0) {
			trueY = y % tiles[0].length;
		} else {
			trueY = y % tiles[0].length;
			if(trueY < 0) {
				trueY += tiles[0].length;
			}
		}

		return tiles[trueX][trueY];
	}

	public void killRandomAreas(int nClusters, int clusterRadius) {
		Random rng = new Random();
		int clusterX;
		int clusterY;
		for (int i = 0; i < nClusters; i++) {
			clusterX = rng.nextInt(tiles.length);
			clusterY = rng.nextInt(tiles[0].length);
			for (int x = clusterX - clusterRadius; x <= clusterX + clusterRadius; x++) {
				for (int y = clusterY - clusterRadius; y <= clusterY + clusterRadius; y++) {
					getTile(x, y).killCreatures();
				}
			}
		}
	}

	public void scatterAll(EnvironmentTile tile, int dist, Creature[] creatures) {
		for (Creature creature : creatures) {
			scatter(tile, dist, creature);
		}
	}

	public void scatter(EnvironmentTile tile, int dist, Creature creature) {
		Random rng = new Random();

		getTile(tile.getX() + rng.nextInt(dist * 2 + 1) - dist, tile.getY() + rng.nextInt(dist * 2 + 1) - dist)
				.addCreature(creature);
	}
	
	public void correctInput() {
		int periodInput;
		int areaInput;
		
		try {
			periodInput = Integer.parseInt(killPeriodInput.getText());
			areaInput = Integer.parseInt(killAreaInput.getText());

			if(periodInput < MIN_KILL_PERIOD) {
				periodInput = MIN_KILL_PERIOD;
			}
			if(periodInput > MAX_KILL_PERIOD) {
				periodInput = MAX_KILL_PERIOD;
			}
			if(areaInput < MIN_KILL_RADIUS) {
				areaInput = MIN_KILL_RADIUS;
			}
			if(areaInput > MAX_KILL_RADIUS) {
				areaInput = MAX_KILL_RADIUS;
			}
		}catch(NumberFormatException e) {
			periodInput = MIN_KILL_PERIOD;
			areaInput = MIN_KILL_RADIUS;
		}
		
		killPeriodInput.setText("" + periodInput);
		killAreaInput.setText("" + areaInput);
	}

	public void simulateSingle(int step) {
		correctInput();
		for (int i = 0; i < step; i++) {
			if (simTime < Integer.MAX_VALUE) {
				simTime++;
			} else {
				simTime = 0;
			}

			if (killPeriodicCheck.isSelected() && simTime % Integer.parseInt(killPeriodInput.getText()) == 0) {
				killRandomAreas(1, (int) Integer.parseInt(killAreaInput.getText()));
			}

			for (int j = 0; j < tiles.length; j++) {
				for (int k = 0; k < tiles[0].length; k++) {
					getTile(j, k).simulateCreatures(this, SUNLIGHT);
				}
			}
		}
		
		refresh(step);
	}

	public void simulateContinuous() {
		if (simSpeed > 0) {
			Platform.runLater(() -> simulateSingle(simSpeed));
			simTimer = new SimTimer(this);
			TIMER.schedule(simTimer, FRAME_TIME);
		}
	}

	public void refresh(int step) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].refresh();
			}
		}

		refreshSizeHistogram();
		refreshGeneHistograms();
		
		if(step != 0) {
			refreshBiomassGraph(step);
			refreshGeneGraph(step);
		}
	}

	/**
	 * public static void main(String[] args) { Environment environment = new
	 * Environment(10, 5); Plant[] testPlants = new Plant[10];
	 * 
	 * System.out.println();
	 * 
	 * for (int i = 0; i < 10; i++) { testPlants[i] = Plant.randomPlant(); }
	 * 
	 * environment.timeStep(); }
	 */
}
