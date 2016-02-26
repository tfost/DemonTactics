package strategy;

import java.awt.Graphics;
/**
 * The Interface for all possible states an actor can be in.
 * @author Tyler
 *
 */
public interface UnitState {
	/**
	 * Updates an actor.
	 */
	public void update();
	
	/**
	 * Draws an actor to the screen.
	 * @param g
	 * @param c The Camera to determine offset. 
	 * @see Camera
	 */
	public void render(Graphics g, Camera c);
	/**
	 * Determines how an actor should behave according to given MouseInput. 
	 * @param input The MouseInput object used.
	 * @param c
	 * @see MouseInput
	 */
	public void handleInput(MouseInput input, Camera c);
}
