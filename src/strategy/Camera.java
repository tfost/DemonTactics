package strategy;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class Camera {
	
	private int xOffset;
	private int yOffset;
	private int lastX;
	private int lastY;
	private int tileSize;
	public final static int DEFAULT_TILE_SIZE = 32;
	public static final int MIN_TILE_SIZE = 8;
	public static final int MAX_TILE_SIZE = 64;
	
	
	public Camera() {
		this.xOffset = 0;
		this.yOffset = 0;
		this.lastX = 0;
		this.lastY = 0;
		this.tileSize = DEFAULT_TILE_SIZE;
	}
	
	public void update(MouseInput input, KeyboardInput keyboard) {
		if (input.buttonDownOnce(1)) { // set up the initial startingPoint of the dragging
			Point p = translateScreenToPoint(input.getPosition().x, input.getPosition().y);
			this.lastX = p.x;
			this.lastY = p.y;
			//System.out.println("Offset = " + this.xOffset + ", " + this.yOffset);
		} else if (input.buttonDown(1)) {
			//move the camera.
			this.xOffset = lastX - input.getPosition().x;
			this.yOffset = lastY - input.getPosition().y;
			//
		} 		
		//zoom control
		//TODO - implement clicking with zooming.
		if (keyboard.keyDownOnce(KeyEvent.VK_EQUALS)) {//zoom in
			if (this.tileSize < MAX_TILE_SIZE) {//max size = 32;
				this.tileSize *= 2;
			}
		} else if (keyboard.keyDownOnce(KeyEvent.VK_MINUS)) {
			if (this.tileSize >= MIN_TILE_SIZE) { //minimum size of a tile
				this.tileSize /= 2;
			}
		}
	}
	
	/*returns a new point corresponding to the offset x and y positions
	of a point, for drawing things to the screen.
	*/
	public Point translatePointToScreen(Point p) {
		return new Point(p.x - xOffset, p.y - yOffset);
	}
	
	public Point translatePointToScreen(int x, int y) {
		return translatePointToScreen(new Point(x, y));
	}
	
	//returns a new point corresponding to the non-offset x and y positions
	public Point translateScreenToPoint(int x, int y) {
		return new Point(x + xOffset, y + yOffset);
	}

	public Point translateScreenToPoint(Point position) {
		return new Point(position.x + xOffset, position.y + yOffset);
	}
	
	public int getTileSize() {
		return this.tileSize;
	}
	
	
	
	
	
}
