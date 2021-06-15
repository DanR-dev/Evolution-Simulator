package genetics;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * handles all of the genes of a single creature.
 * @author danpr
 *
 */
public class Genome implements Cloneable {

	protected ArrayList<Gene> genes;

	/**
	 * initialise an empty genome.
	 */
	public Genome() {
		genes = new ArrayList<Gene>();
	}

	/**
	 * generate a deep copy of this genome.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Genome clone = (Genome) super.clone();
		clone.genes = new ArrayList<Gene>();
		for(Gene gene : this.genes){
			clone.genes.add((Gene) gene.clone());
		}
		return clone;
	}

	/**
	 * generate a string containing the data of the each gene is this genome.
	 */
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
	
	/**
	 * generates graphical elements displaying the values of each gene in this genome.
	 * @return
	 */
	public VBox getOutput() {
		VBox output = new VBox();
		VBox[] geneOutputs = new VBox[genes.size()];

		int n = 0;
		GeneType[] types = GeneType.values();
		
		for(GeneType type : types) {
			if(getGene(type) != null) {
				geneOutputs[n] = new VBox();
				geneOutputs[n].getChildren().add(new Label(type.alias() + ":"));
				geneOutputs[n].getChildren().add(new ProgressBar(getGene(type).getMagnitude()));
				output.getChildren().add(geneOutputs[n]);
				n++;
			}
		}
		
		return output;
	}

	/**
	 * gene getter by gene type.
	 * @param type
	 * @return
	 */
	public Gene getGene(GeneType type) {
		return genes.get(type.ordinal());
	}
	
	/**
	 * get the normalised values of all genes in this genome.
	 * @return
	 */
	public float[] getMagnitudes() {
		float[] magnitudes = new float[GeneType.values().length];
		for(int i = 0; i < GeneType.values().length; i++) {
			magnitudes[i] = getGene(GeneType.values()[i]).getMagnitude();
		}
		return magnitudes;
	}

	/**
	 * bumper function for the specified gene.
	 * @param type
	 * @return
	 */
	public float getGeneValue(GeneType type) {
		return genes.get(type.ordinal()).getValue();
	}
	
	/**
	 * counts the number of genes in this genome.
	 * @return
	 */
	public int getNumGenes() {
		return genes.size();
	}

	/**
	 * apply a number of random mutations to genes in this genome according to its mutation rate gene.
	 */
	public void mutate() {
		Random rng = new Random();
		int nMutations = (int) getGeneValue(GeneType.MUTATION_RATE);

		while (nMutations > 0) {
			int randomI = rng.nextInt(genes.size());
			genes.get(randomI).mutate();
			nMutations--;
		}
	}

	/**
	 * add the given gene to this genome.
	 * @param type
	 * @param gene
	 */
	public void addGene(GeneType type, Gene gene) {
		genes.add(type.ordinal(), gene);
	}
}