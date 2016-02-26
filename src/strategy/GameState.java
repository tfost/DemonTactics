package strategy;

import java.awt.Graphics;

public interface GameState {
	//updates all parts of this gameState.
	public void update();
	
	//draws this gameState to the screen.
	public void render(Graphics g);
	
	// returns if we should keep updating the state.
	public boolean isInState(); 
	
	//returns the next gamestate we should go to after the current one is complete
	public GameState nextState();
}
