package strategy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Set;

/**
 * This class draws basic information about a given level to the screen
 * @author Tyler
 *
 */
public class LevelStateUI {
	
	private int screenHeight;
	private int screenWidth;
	private Set<Unit> redActors;
	private Set<Unit> blueActors;
	
	public LevelStateUI(Set<Unit> redActors, Set<Unit> blueActors) {
		this.screenWidth = StrategyPanel.WIDTH + 2;
		this.screenHeight = StrategyPanel.HEIGHT;
		this.redActors = redActors;
		this.blueActors = blueActors;
		
	}
	
	public void render(Graphics g, int turnNum, boolean isRedTurn) {
		if (isRedTurn) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}
		g.fillRect(0, 0, screenWidth, 20);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 0, 12));
		FontMetrics metrics = g.getFontMetrics();
		g.drawString("Turn " + turnNum, 10, 15);		
		if (isRedTurn) {
			g.drawString("It is Red's turn", 200, 15);
		} else {
			g.drawString("It is Blue's turn", 200, 15);
		}
		
		g.drawString("Light Units: " + redActors.size(), 400, 15);
		g.drawString("Dark Units: " + blueActors.size(), 500, 15);
		
	}
	
	//TODO - create Minimap in bottom right.
}
