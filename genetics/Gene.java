package genetics;

import java.util.Random;

public class Gene implements Cloneable {
	private final float INCREMENTS;
	private final float MIN_VALUE;
	private final float MAX_VALUE;
	private float value;
	
	public Gene(float minValue, float maxValue, float increments, float value) {
		this.MIN_VALUE = minValue;
		this.MAX_VALUE = maxValue;
		this.INCREMENTS = increments;
		
		if(value < MIN_VALUE) {
			this.value = MIN_VALUE;
		} else if(value > MAX_VALUE) {
			this.value = MAX_VALUE;
		} else {
			this.value = value;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (Gene) super.clone();
	}
	
	@Override
	public String toString() {
		return (value + "+-" + INCREMENTS);
	}
	
	public float getValue() {
		return value;
	}
	
	public void mutate() {
		Random rng = new Random();
		
		value += (rng.nextInt(3) - 1) * INCREMENTS;
		if(value < MIN_VALUE) {
			value = MIN_VALUE;
		} else if(value > MAX_VALUE) {
			value = MAX_VALUE;
		}
	}
}
