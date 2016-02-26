package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * provides a menu/ui for an actor.
 * displays a menu of actions the actor can take, drawn to the edge of the screen.
 * on click of the menu, the actor will take that action
 * otherwise, click options are the same.
 * similar to the CIV V ui.
 * should only be drawn when in selected state.
 * @author Tyler
 *
 */
public class UnitUI {
	private Unit unit;
	private boolean selected;
	private Set<Tile> tilesInRange;
	
	public UnitUI(Unit unit) {
		this.unit = unit;
		tilesInRange = new HashSet<Tile>();
	}
	
	
	public void update() {
		if (unit.isSelected()) { // only want ot show stuff about characters that are selected.
			
		}
	}
	
	public void selectTiles(int movementDistance, int x, int y, boolean selected) {
		if (movementDistance >= 0 && unit.getBoard().getTile(x, y) != null && unit.getBoard().getTile(x, y).canMoveInto()) {
			tilesInRange.add(unit.getBoard().getTile(x,y));
			
			selectionHelper(movementDistance - 1, x - 1 , y, selected);		
			selectionHelper(movementDistance - 1, x + 1 , y, selected);	
			selectionHelper(movementDistance - 1, x , y - 1, selected);			
			selectionHelper(movementDistance - 1, x , y + 1, selected);
		}
	}
	
	//sets all tiles in the movement range of a character to a given boolean value.
	private void selectionHelper(int movementDistance, int x, int y, boolean selected) {
		if (movementDistance >= 0 && unit.getBoard().getTile(x, y) != null && 
				unit.getBoard().getTile(x, y).canMoveInto() /*&& board.isEmpty(x, y)*/) { 
			
			//draw the tranparent selection squares
			//g.setColor(new Color(51, 153, 255, 128));
			//g.fillRect(x * camera.getTileSize(), y * camera.getTileSize(), 
			//		camera.getTileSize(), camera.getTileSize());
			//a.getBoard().getTile(x, y).setSelected(selected);				
				
			
			selectionHelper(movementDistance - 1, x - 1 , y, selected);		
			selectionHelper(movementDistance - 1, x + 1 , y, selected);		
			selectionHelper(movementDistance - 1, x , y - 1, selected);		
			selectionHelper(movementDistance - 1, x , y + 1, selected);
		}
	}
	/**
	 * Draws important information about the actor to the screen, including
	 * 		tiles which are in range of that actor
	 * 		its health
	 * 		number of moves left
	 * 		name?
	 * Also, displays any possible other actions an actor can do
	 * 
	 * @param g Graphics object
	 * @param camera the camera on which drawing locations are based.
	 */
	public void draw(Graphics g, Camera camera) {
		
		if (unit.isSelected()) {
			//draw the tranparent selection squares
			g.setColor(new Color(51, 153, 255, 128));
			for (Tile t : tilesInRange) {
				g.fillRect(t.getX() * camera.getTileSize(), t.getY() * camera.getTileSize(), 
					camera.getTileSize(), camera.getTileSize());
				//a.getBoard().getTile(x, y).setSelected(selected);	
			}
		}
			
	}
}
