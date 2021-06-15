package genetics;

import java.util.Random;

/**
 * handles simulation of a single gene describing a single trait of a creature.
 * 
 * @author danpr
 *
 */
public class Gene implements Cloneable {
	public final float MIN_VALUE; // min and max value of the gene
	public final float MAX_VALUE; // ^
	public final float INCREMENTS; // increments by which the gene can change
	private float value;

	/**
	 * initialise with the given values.
	 * 
	 * @param minValue
	 * @param maxValue
	 * @param increments
	 * @param value      initial value of the gene
	 */
	public Gene(float minValue, float maxValue, float increments, float value) {
		this.MIN_VALUE = minValue;
		this.MAX_VALUE = maxValue;
		this.INCREMENTS = increments;

		if (value < MIN_VALUE) {
			this.value = MIN_VALUE;
		} else if (value > MAX_VALUE) {
			this.value = MAX_VALUE;
		} else {
			this.value = value;
		}
	}

	/**
	 * generate a deep copy of this gene.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Gene) super.clone();
	}

	/**
	 * generate a string containing the gene value.
	 */
	@Override
	public String toString() {
		return (value + "+-" + INCREMENTS);
	}

	/**
	 * value getter.
	 * 
	 * @return
	 */
	public float getValue() {
		return value;
	}

	/**
	 * gets the value of the gene normalised to such that the min and max would be 0
	 * and 1.
	 * 
	 * @return
	 */
	public float getMagnitude() {
		return (value - MIN_VALUE) / (MAX_VALUE - MIN_VALUE);
	}

	/**
	 * leave the value of the gene randomly incremented, decremented or unchanged.
	 */
	public void mutate() {
		Random rng = new Random();

		value += (rng.nextInt(3) - 1) * INCREMENTS; // random one of (-1, 0, or 1)
		if (value < MIN_VALUE) {
			value = MIN_VALUE;
		} else if (value > MAX_VALUE) {
			value = MAX_VALUE;
		}
	}
}
