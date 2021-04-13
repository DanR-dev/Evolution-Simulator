package genetics;

public enum GeneType {
	// creature genes, required
	MUTATION_RATE,
	REPRODUCE_BEHAVIOUR,
	GROW_BEHAVIOUR,
	AGE_CAP,

	// creature genes, optional
	SIZE_CAP,
	STARTING_SIZE,

	// plant genes, required
	SEED_RANGE;

	public String alias() {
		switch (this) {
		case MUTATION_RATE:
			return "Mutations in Offspring";
		case REPRODUCE_BEHAVIOUR:
			return "Reproductive Conservatism";
		case GROW_BEHAVIOUR:
			return "Growth Conservatism";
		case AGE_CAP:
			return "Max Lifespan";
		case SIZE_CAP:
			return "Max Size";
		case STARTING_SIZE:
			return "Seed Size";
		case SEED_RANGE:
			return "Seeding Distance";
		default:
			return "Unknown Gene";
		}
	}
}