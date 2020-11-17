package main;

import java.util.ArrayList;

public class EnvironmentTile {
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<EnvironmentalCondition> conditions = new ArrayList<EnvironmentalCondition>();
	private int x;
	private int y;
	
	Creature[] creatureBuffer;
	
	public EnvironmentTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}
	
	public Creature[] getCreatures() {
		return (Creature[]) creatures.toArray();
	}
	
	public void bufferCreatures() {
		creatureBuffer = getCreatures();
	}
	
	public void simulateCreatures(Environment environment) {
		for(Creature creature : creatureBuffer) {
			creature.chooseBehaviour(environment, this);
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}