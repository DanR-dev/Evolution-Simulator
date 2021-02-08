package genetics;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class Genome implements Cloneable {

	protected ArrayList<Gene> genes;

	public Genome() {
		genes = new ArrayList<Gene>();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Genome shallowCopy = (Genome) super.clone();
		shallowCopy.genes = new ArrayList<Gene>();
		for(Gene gene : this.genes){
			shallowCopy.genes.add((Gene) gene.clone());
		}
		return shallowCopy;
	}
	
	@Override
	public String toString() {
		GeneType[] types = GeneType.values();
		String result = "(";
		for(GeneType type : types) {
			if(getGene(type) != null) {
				result += type + ": " + genes.get(type.ordinal()) + "\n";
			}
		}
		result += ")";
		return result;
	}
	
	public VBox getOutput() {
		VBox output = new VBox();
		VBox[] geneOutputs = new VBox[genes.size()];

		int n = 0;
		GeneType[] types = GeneType.values();
		
		for(GeneType type : types) {
			if(getGene(type) != null) {
				geneOutputs[n] = new VBox();
				geneOutputs[n].getChildren().add(new Label(type + ":"));
				geneOutputs[n].getChildren().add(new ProgressBar(getGene(type).getMagnitude()));
				output.getChildren().add(geneOutputs[n]);
				n++;
			}
		}
		
		return output;
	}

	public Gene getGene(GeneType type) {
		return genes.get(type.ordinal());
	}

	public float getGeneValue(GeneType type) {
		return genes.get(type.ordinal()).getValue();
	}
	
	public int getNumGenes() {
		return genes.size();
	}

	public void mutate() {
		Random rng = new Random();
		int nMutations = (int) getGeneValue(GeneType.MUTATION_RATE);

		getGene(GeneType.MUTATION_RATE).mutate();

		while (nMutations > 0) {
			int randomI = rng.nextInt(genes.size());
			if (randomI != GeneType.MUTATION_RATE.ordinal()) {
				genes.get(randomI).mutate();
				nMutations--;
			}
		}
	}

	public void addGene(GeneType type, Gene gene) {
		genes.add(type.ordinal(), gene);
	}
}