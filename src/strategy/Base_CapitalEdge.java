package strategy;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Base_CapitalEdge extends Base_Capital{
	private Base parent;//the base to direct all attacks to. 
	public Base_CapitalEdge(int x, int y, BufferedImage img, Faction faction, Base parent) {
		super (x, y, img, faction);
		this.parent = parent;
	}
	
	
	public Color tileCol() {
		switch (this.getFaction()) {
			case RED:
				 return Color.RED;
			case BLUE:
				return Color.BLUE;
			default:
				return Color.WHITE;		
		}
	}
}
