package strategy;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/**
 * The level state is the main action part of the game.
 * LevelStates manage a player's characters, and the enemies characters.
 * 
 */
public class GS_LevelState implements GameState {
	private int turnNumber; 
	private LevelStateUI ui;
	private Board gameBoard;
	private MouseInput input;
	private Set<Unit> redUnits; //Sets for each team's units.
	private Set<Unit> blueUnits;
	private Camera gameCamera; // Camera for this instance of the level.
	private boolean isRedTurn; //determines if light or dark character are in turn.
	private KeyboardInput keyboard;
	
	/**
	 * 
	 * @param m The MouseInput object for the state.
	 * @throws IOException 
	 * @See MouseInput
	 */
	public GS_LevelState(MouseInput m, KeyboardInput keyboard) throws IOException {
		turnNumber = 1;
		gameCamera = new Camera();
		gameBoard = new Board();
		input = m;
		this.keyboard = keyboard;
		redUnits = new HashSet<Unit>();
		blueUnits = new HashSet<Unit>();
		//Spawn initial characters according to the bases.
		for (Base b : gameBoard.getBasesOfFaction(Faction.RED)) {
			redUnits.add(new Unit_InfluenceSpreader(b.getX() + 1, b.getY(), gameBoard, Faction.RED));
			redUnits.add(new Unit_Soldier(b.getX() - 1, b.getY(), gameBoard, Faction.RED));

		}
		
		for (Base b : gameBoard.getBasesOfFaction(Faction.BLUE)) {			
			blueUnits.add(new Unit_InfluenceSpreader(b.getX() - 1, b.getY(), gameBoard, Faction.BLUE)); 
			blueUnits.add(new Unit_Soldier(b.getX(), b.getY() - 1, gameBoard, Faction.BLUE)); 

		}		
		//adds the characters from this state back onto the character board.
		gameBoard.populate(blueUnits);
		gameBoard.populate(redUnits);
		isRedTurn = true;
		ui = new LevelStateUI(redUnits, blueUnits);
		
	}
	
	/**
	 * @return if the levelState should continue to be the active state.
	 */
	public boolean isInState() {
		return true;
	}
	
	/**
	 * 
	 * @return the current gameBoard being acted upon in this levelState.
	 */
	public Board getBoard() {
		return gameBoard;
	}
	
	/**
	 * The main level loop. It updates the faction which is currently doing their turn.
	 */
	public void update() {
		input.poll(); //refresh the mouseListener.
		keyboard.poll();
		gameCamera.update(input, keyboard);
		for (Base b : this.gameBoard.getBases()) {
			b.update(gameBoard);
		}
		if (isRedTurn) { // Red side makes a move.			
			for (Unit unit : redUnits) {
				unit.getState().handleInput(input, gameCamera);
				unit.getState().update();
			}
			
			if (!actorsLeftToUpdate(redUnits)) {
				isRedTurn = false;
				System.out.println("Red's turn is done");
				//reinitialize the red characters for the next turn.
				for (Unit unit : redUnits) {
					unit.onTurnEnd();
				}
				//start blue's turn.
				for (Unit unit : blueUnits) {
					unit.onTurnStart(turnNumber);
				}
			}
			
			
		} else { // blue's characters turn.
			
			// Update all the blue characters.
			for (Unit unit : blueUnits) {
				unit.getState().handleInput(input, gameCamera);
				unit.getState().update();
			}
			if (!actorsLeftToUpdate(blueUnits)) { // switch turn when all charcters have done their stuff.
				isRedTurn = true;
				System.out.println("Blue's turn is done");
				for (Unit unit : blueUnits) {
					unit.onTurnEnd();
				}
				turnNumber++;// a full series of turns has elapsed.
				//start Red's turn.
				for (Unit unit : redUnits) {
					unit.onTurnStart(turnNumber);
				}
				
			}
		}
		
	}
	
	
	private boolean actorsLeftToUpdate(Set<Unit> units) {
		for (Unit unit : units) {
			if (unit.isInTurn()) { // an actor still needs to be updated.
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Draws this levelState to a panel.
	 */
	public void render(Graphics g) {
		gameBoard.render(g, gameCamera);
		for (Unit unit : redUnits) {
			unit.getState().render(g, gameCamera);
		}
		for (Unit unit : blueUnits) {
			unit.getState().render(g, gameCamera);
		}
		ui.render(g, turnNumber, isRedTurn);
		
	}

	
	/**
	 * @return no state should be after this one.
	 */
	public GameState nextState() {
		return null; // we don't want to do anything after this state is over.
	}
		
}



