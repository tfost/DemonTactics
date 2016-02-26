package strategy;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Grassland extends Tile{
	Color grassCol;
	public Grassland(int x, int y, BufferedImage img) {
		super(x, y, 0, 0, img);
		grassCol = new Color(44, 176, 55);
	}
	
	public Color tileCol() {
		return grassCol; // a weird color that will stand out.
	}
	
	public boolean canMoveInto() {
		return true;
	}

}
