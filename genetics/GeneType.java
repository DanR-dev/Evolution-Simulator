package genetics;

public enum GeneType {
	//creature genes, required
	MUTATION_RATE,
	REPRODUCE_BEHAVIOUR,
	GROW_BEHAVIOUR,
	
	//creature genes, optional
	SIZE_CAP,
	STARTING_SIZE,
	
	//plant genes, required
	GROW_BATCH,
	SEED_BATCH,
	SEED_RANGE,
}