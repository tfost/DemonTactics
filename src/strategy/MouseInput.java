package strategy;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MouseInput implements MouseListener, MouseMotionListener{
	
	private static final int BUTTON_COUNT = 3;
	
	//polled position of the mouse cursor.
	private Point mousePos;
	
	//current position of the mosue cursor
	private Point currentPos;
	
	//current state of the mouse buttons
	private boolean[] state = null;
	
	//polled mouse buttons
	private MouseState[] poll = null;
	
	private enum MouseState {
		RELEASED, 	//not down
		PRESSED, 	//down but not for the first time
		ONCE 		//down for the first time 
					//eg, if it wasn't down the last frame,
					//but now it is
	}
	
	
	public MouseInput() {
		//create default mouse positions
		mousePos = new Point(0, 0);
		currentPos = new Point (0, 0);
		
		//setup initial button states
		state = new boolean[BUTTON_COUNT];
		poll = new MouseState[BUTTON_COUNT];
		
		for (int i = 0; i < BUTTON_COUNT; ++i) { 
			poll[i] = MouseState.RELEASED;
		}
		
	}
	
	//figures out the states of the buttons at any given time.
	public synchronized void poll() {
		
		mousePos = new Point(currentPos); //save current location
		
		for (int i = 0; i < BUTTON_COUNT; ++i) {//check each mouse button
			//if down for first time, it's once, otherwise it's pressed.
			if (state[i]) {
				if (poll[i] == MouseState.RELEASED) {
					poll[i] = MouseState.ONCE;
				} else {
					poll[i] = MouseState.PRESSED;
				}
			} else {
				poll[i] = MouseState.RELEASED;
			}
		}
	}
	
	//returns the position of the mouse.
	public Point getPosition() {
		return mousePos;
	}
	
	//returns if this button newly down for this frame
	public boolean buttonDownOnce(int button) {
		return poll[button - 1] == MouseState.ONCE;
	}
	
	//returns if this button is currently being pressed.
	//button ranges from 1 - 3
	public boolean buttonDown(int button) {
		return poll[button - 1] == MouseState.ONCE ||
				poll[button - 1] == MouseState.PRESSED;
	}
	
	
	public synchronized void mousePressed(MouseEvent e) {
		state[e.getButton() - 1] = true;
	}
	
	
	public synchronized void mouseReleased(MouseEvent e) {
		state[e.getButton()-1] = false;
	}
	
	
	public synchronized void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}
	
	public synchronized void mouseExited(MouseEvent e) {
		mouseMoved(e);
	}
	
	public synchronized void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	public synchronized void mouseMoved(MouseEvent e) {
		currentPos = e.getPoint();
	}
	
	public void mouseClicked(MouseEvent e) {
		//not needed
	}
	
}















