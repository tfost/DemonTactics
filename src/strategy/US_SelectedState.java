package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * This class determines possible courses of action for a character.
 * When a character is selected, all empty, walkable tiles will be
 * marked as such. An additional ring of tiles one past the range of the
 * character will be demarcated as extra areas the character could attack.
 * Upon click, the class then determines on the proper course of action.
 * If the clicked upon tile is empty, the character will simply move towards
 * this tile and update its movement accordingly, in the MovementState.
 * If the tile clicked on is empty, the player will move to the closest
 * tile within range of the character, and proceed to attack it. This
 * will end its turn.
 * @author Tyler
 * @see UnitState
 *
 */
public class US_SelectedState implements UnitState{
	private Unit c;
	private Board board;
	private Set<Tile> possibleActionTiles;
	
	public US_SelectedState(Unit c) {
		this.c = c;
		System.out.println("Selected " + c.getName() + " at " + c.getX() + ", " + c.getY());		
		this.board = c.getBoard();
		//c.setSelected(true);
		//c.getUI().assembleSelectedLists();
		possibleActionTiles = new HashSet<Tile>();
		selectTiles(c.getMovementDistanceLeft(), c.getX(), c.getY(), true, possibleActionTiles);
		
	}
	
	public void render(Graphics g, Camera camera) {
		this.c.render(g, camera);	
		this.c.getUI().draw(g, camera);
		
	}
	
	//marks all tiles within the movementRange as selected.
	//TODO - do this via the UnitUI class!
	public void selectTiles(int movementDistance, int x, int y, boolean selected, Set<Tile> tiles) {
		if (movementDistance >= 0 && board.getTile(x, y) != null && board.getTile(x, y).canMoveInto()) {
			Tile t = board.getTile(x, y);
			t.setSelected(selected);
			tiles.add(t);
			selectionHelper(movementDistance - 1, x - 1 , y, selected, tiles);		
			selectionHelper(movementDistance - 1, x + 1 , y, selected, tiles);		
			selectionHelper(movementDistance - 1, x , y - 1, selected, tiles);			
			selectionHelper(movementDistance - 1, x , y + 1, selected, tiles);
		}
	}
	
	//sets all tiles in the movement range of a character to a given boolean value.
	private void selectionHelper(int movementDistance, int x, int y, boolean selected, Set<Tile> tiles) {
		Tile t = board.getTile(x, y);
		if (movementDistance >= 0 && t != null && t.canMoveInto()) { 
			
			t.setSelected(selected);				
			tiles.add(t);
			
			selectionHelper(movementDistance - 1, x - 1 , y, selected, tiles);		
			selectionHelper(movementDistance - 1, x + 1 , y, selected, tiles);		
			selectionHelper(movementDistance - 1, x , y - 1, selected, tiles);			
			selectionHelper(movementDistance - 1, x , y + 1, selected, tiles);
		}
	}
	
		
	//left click off the character deselects the character. 
	//right click moves the character to a given tile if 
	//it is in its movement radius and the character can move into it.
	public void handleInput(MouseInput input, Camera camera) {
		//if left clicked off the character, deselect the character.
		if (input.buttonDownOnce(1) && 	!Board.pixelsToTile(camera.translateScreenToPoint(
						input.getPosition()), c.getBoard().getHeight(), c.getBoard().getHeight())
						.equals(new Point(c.getX(), c.getY()))) {
			
			selectTiles(c.getMovementDistanceLeft(), c.getX(), //clicked off character, move it off.
						c.getY(), false, possibleActionTiles);
			c.setSelected(false);
			c.setActorState(new US_IdleState(c));
		}
		
		//right mouse button indicates move to this point. move if it's in range.
		// movement clicked on a new tile to move to.
		else if (input.buttonDownOnce(3) || input.buttonDownOnce(2)) { 
			Point pos = camera.translateScreenToPoint(input.getPosition());
			pos = Board.pixelsToTile(pos, c.getBoard().getWidth(), c.getBoard().getHeight()); 
			Tile target = c.getBoard().getTile((int) pos.getX(), (int) pos.getY());
			
			if (possibleActionTiles.contains(target)) { //if this tile is in range. 
				selectTiles(c.getMovementDistanceLeft(), c.getX(), c.getY(), false, possibleActionTiles); // deselect the tiles.
				if (target.isBase() && this.c.isNextTo(target) &&
						(this.c.getFaction() != ((Base) (target)).getFaction()) ) {
						//if clicked on base of opposite faction next to the character.
					//c.attackBase((Base) target);
					System.out.println("Attacking base!");
				} else { //didn't click on a base. 
					Unit other = board.getActor(pos.x, pos.y);
					if (this.c == other) {//if we clicked on ourself.
						c.setTurnStatus(false);
					}
					//TODO-change to if c.attackRangeContains(other); //attack if it's in range.
					else if (other != null && c.isInAttackRange(other)) { // this character is next to us and we have moves left! attack it!
						c.attack(other);
						c.setActorState(new US_IdleState(c));
					} else { //clicked on a tile we can move to.
						c.setActorState(new US_MovingState(c, target));
					}
				}
				
			} else { // clicked off character. 
				c.setActorState(new US_IdleState(c)); 
			}
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
}
