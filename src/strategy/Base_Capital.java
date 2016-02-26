package strategy;

import java.awt.Color;
import java.awt.image.BufferedImage;

//TODO - make this support multi-sized bases.
public class Base_Capital extends Base {
	public Base_Capital(int x, int y, BufferedImage img, Faction faction) {
		super (x, y, img, 50, 10, 7, faction);
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
