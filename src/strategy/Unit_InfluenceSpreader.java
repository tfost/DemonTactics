package strategy;

import java.awt.Color;

/**
 * This unit spreads the demon world, by setting tiles to be dark or not every time it moves.
 * It creates a portable. region of darkness that allows demon units to leave a base's
 * area of influence.
 * @author Tyler
 *
 */
public class Unit_InfluenceSpreader extends Unit{
	private int spreadRadius;
	private int spreadStrength;
	public Unit_InfluenceSpreader(int x, int y, Board board, Faction f) {
		super(x, y, board, 6, 3, 10, 2, 5, f, "Influencer");
		spreadRadius = 4;
		this.spreadStrength = 1;
		influence(x, y, spreadRadius,  spreadStrength);
	}
	
	/**
	 * Moves this character to a given x and y value.
	 * Updates tiles surrounding it to be in the dark, and resets those not in the dark accordingly.
	 */
	public void moveTo(int x, int y) {
		//update old tiles to not be in the dark
		influence(this.getX(), this.getY(), spreadRadius, -spreadStrength); //uninfluence tiles.
		super.moveTo(x,  y);
		influence(x, y, spreadRadius, spreadStrength); 
	}
	
	public Color charCol() {
		return Color.DARK_GRAY;
	}
	
	//TODO - use floats with the radius in order to do circles. 
	private void influence(int x, int y, int radius, int amount) {
		if (radius > 0) {
			Tile t = this.getBoard().getTile(x,y);
			if (t != null) {
				t.modifyInfluence(getFaction(), amount);
				
				influence(x - 1, y, radius - 1, amount);
				influence(x + 1, y, radius - 1, amount);
				influence(x, y - 1, radius - 1, amount);
				influence(x, y + 1, radius - 1, amount);
			}
			
		}
	}
		
}
