package environments;

import java.util.ArrayList;
import java.util.Random;

import creatures.Creature;
import creatures.Plant;
import frontEnd.AppRoot;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Environment extends VBox {
	private static final int HISTORY_LENGTH = 1000;
	private final static float SUNLIGHT = 200;
	private final AppRoot ROOT;

	private BarChart<String, Number> sizeHistogram;
	private LineChart<Number, Number> biomassGraph;

	private HBox controls;
	private GridPane simArea;
	private EnvironmentTile[][] tiles;

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
		Button testButton = new Button();
		testButton.setText("step");
		testButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               Environment.this.simulateCreatures(100);
                System.out.println("step");
            }
        });
		controls.getChildren().add(testButton);
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
			scatter(tiles[clusterX][clusterY], clusterWidth, clusterCreatures);
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
		
		biomassGraph.getData().add(series);
		refreshSizeHistogram();
		return biomassGraph;
	}

	private void refreshBiomassGraph(int step) {
		if (biomassGraph != null) {
			Series<Number, Number> series =biomassGraph.getData().get(0);
			ObservableList<Data<Number, Number>> data = series.getData();
			Creature[] creatures = getCreatures();
			int totalMass = 0;
			int nextTime;
			
			for(int i = 0; i < data.size(); i++) {
				nextTime = (int) data.get(i).getXValue() - step;
				if(nextTime > -HISTORY_LENGTH) {
					data.get(i).setXValue(nextTime);
				} else {
					data.remove(i);
				}
			}

			for (Creature creature : creatures) {
				totalMass += creature.getSize();
			}
			data.add(new Data<Number, Number>(0, totalMass));
		}
	}

	public Chart[] getCharts() {
		Chart[] charts = new Chart[2];

		charts[0] = sizeHistogram();
		charts[1] = biomassGraph();

		return charts;
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
			trueX = -(x % tiles.length);
		}
		if (y >= 0) {
			trueY = y % tiles[0].length;
		} else {
			trueY = -(y % tiles[0].length);
		}

		return tiles[trueX][trueY];
	}

	public void scatter(EnvironmentTile tile, int dist, Creature[] creatures) {
		Random rng = new Random();

		for (Creature creature : creatures) {
			getTile(tile.getX() + rng.nextInt(dist * 2 + 1) - dist, tile.getY() + rng.nextInt(dist * 2 + 1) - dist)
					.addCreature(creature);
		}
	}

	public void simulateCreatures(int step) {
		for(int i = 0; i < step; i++) {
			for (int j = 0; j < tiles.length; j++) {
				for (int k = 0; k < tiles[0].length; k++) {
					getTile(j, k).simulateCreatures(this, SUNLIGHT);
				}
			}
		}
		refresh(step);
	}

	public void refresh(int step) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].refresh();
			}
		}

		refreshSizeHistogram();
		refreshBiomassGraph(step);
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
