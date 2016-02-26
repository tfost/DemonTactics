package strategy;

import java.awt.Color;

public class Unit_Sorcerer extends Unit{
	
	public Unit_Sorcerer(int x, int y, Board b, Faction f) {
		this(x, y, b, 5, 4, 125, 30, 30, f, "Sorcerer"); 
	}
	
	public Unit_Sorcerer(int x, int y, Board b, int movementDistance, int spawnTime, int maxHealth, int attack, int defense, Faction faction, String name) {
		super(x, y, b, movementDistance, spawnTime, maxHealth, attack, defense, faction,  name);
	}
	
	public Color charCol() {
		return Color.WHITE;
	}
	
		
}
