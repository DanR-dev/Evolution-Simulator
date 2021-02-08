package genetics;

import java.util.ArrayList;
import java.util.Random;

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
			if(genes.get(type.ordinal()) != null) {
				result += type + ": " + genes.get(type.ordinal()) + "\n";
			}
		}
		result += ")";
		return result;
	}

	public Gene getGene(GeneType type) {
		return genes.get(type.ordinal());
	}

	public float getGeneValue(GeneType type) {
		return genes.get(type.ordinal()).getValue();
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