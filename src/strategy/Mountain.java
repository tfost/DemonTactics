package strategy;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Mountain extends Tile{

	public Mountain(int x, int y, BufferedImage img) {
		super(x, y, 32, 0, img);
		// TODO Auto-generated constructor stub
	}
	
	public Color tileCol() {
		return Color.LIGHT_GRAY; 
	}
	
}
