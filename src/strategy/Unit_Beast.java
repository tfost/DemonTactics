package strategy;

import java.awt.Color;

public class Unit_Beast extends Unit{
	public Unit_Beast(int x, int y, Board b, Faction f) {
		this(x, y, b, 8, 4, 75, 30, 30, f, "Beast");
	}
	
	public Unit_Beast(int x, int y, Board b, int movementDistance, int spawnTime, int maxHealth, int attack, int defense, Faction faction, String name) {
		super(x, y, b, movementDistance, spawnTime, maxHealth, attack, defense, faction,  name);
	}
	
	public Color charCol() {
		return Color.ORANGE;
	}
}
