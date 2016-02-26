package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class US_IdleState implements UnitState{
	private Unit c;
	public US_IdleState(Unit c) {
		this.c = c;
		if (c.getMovementDistanceLeft() <= 0) {
			c.setTurnStatus(false); // player is done with turn. 
		}

	}
	
	
	public void update() {

	}

	public void render(Graphics g, Camera camera) {
		c.render(g, camera);
	}

	/**
	 * handles click input for the given character. If this character is 
	 * clicked on, mark it as selected.
	 */
	public void handleInput(MouseInput input, Camera camera) {
		//if left clicked on this character.
		
		if (input.buttonDownOnce(1) && Board.pixelsToTile(camera.translateScreenToPoint(
						input.getPosition()), c.getBoard().getWidth(), c.getBoard().getHeight())
						.equals(new Point(c.getX(), c.getY()))) { 
			if (c.isInTurn()) {
				c.setSelected(true);
				c.setActorState(new US_SelectedState(c));
			}
		}
	}

}
