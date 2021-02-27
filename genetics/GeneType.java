package genetics;

public enum GeneType {
	//creature genes, required
	MUTATION_RATE,
	REPRODUCE_BEHAVIOUR,
	GROW_BEHAVIOUR,
	AGE_CAP,
	
	//creature genes, optional
	SIZE_CAP,
	STARTING_SIZE,
	
	//plant genes, required
	SEED_RANGE,
}