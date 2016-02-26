package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;

public class US_MovingState implements UnitState{
	private Unit c;
	private int targetX; // goal points
	private int targetY;
	private int pathLength;
	public static final long DELAY = 20; // ms delay between moves c
	private long cycleStartTime; // start of the current wait cycle.
	private long currentTickTime;
	private Stack<Tile> path;
	
	
	
	public US_MovingState(Unit c, Tile target) {
		this.c = c;
		this.targetX = target.getX();
		this.targetY = target.getY();
		boolean characterAtPoint = c.getBoard().getActor(targetX, targetY) != null;
		//If the character could move into this spot, move it. 
		if (c.getBoard().getTile(targetX, targetY).canMoveInto() /*|| c.getBoard().isEmpty(targetX, targetY)*/) {
			this.path = pathfindToPoint(target, characterAtPoint);
			pathLength = path.size();		
			c.setMovementDistanceLeft(c.getMovementDistanceLeft() - pathLength);
			//moves the character for the first tile, to avoid a long delay.
			
			/*if (path.isEmpty()) {
				c.setActorState(new IdleState(c));
			} else {
				Tile next = path.pop();
				c.moveTo(next.getX(), next.getY());
			}
			if (path.isEmpty()) {
				c.setActorState(new IdleState(c));
			}*/
			
			cycleStartTime = System.currentTimeMillis();
		} else {// we can't move. 
			System.out.println("You can't move into " + targetX + "," + targetY);
			c.setActorState(new US_IdleState(c));
			
		}
		
	}
	
	/*
	 * TODO - figure out a way to pathfind from the current point to the next point.
	 * after each delay, move the character to the next point in the list.
	 */
	public void update() {
		currentTickTime = System.currentTimeMillis();
		long timeDiff = currentTickTime - cycleStartTime;
		if (timeDiff > DELAY) { // we've delayed enough. move the character.
			cycleStartTime = System.currentTimeMillis();			
			if (!path.isEmpty()) {
				Tile next = path.pop();	
				c.moveTo(next.getX(), next.getY());
			} else {
				c.setActorState(new US_IdleState(c));
			}
		}
	}
	
	public void handleInput(MouseInput input, Camera camera) {
		//TODO - TEMP.
		/*if (input.buttonDown(1) && ) {
			c.setActorState(new IdleState(c));
		}*/
	}
	
	public void render(Graphics g, Camera camera) {
		g.setColor(Color.BLACK);
		for (Tile t : path) { 
			Point loc = camera.translatePointToScreen(t.getX() * Board.TILE_SIZE,
													t.getY() * Board.TILE_SIZE);
			g.fillRect(loc.x, loc.y, Board.TILE_SIZE, Board.TILE_SIZE);
		}
		c.render(g, camera);
	}

	
	//Returns a stack representing the path the current character of this state
	//would need to take to get to a given target.
	//pre: target should be in the movement distance of the character, determined
	//via in the selectedState.
	private Stack<Tile> pathfindToPoint(Tile target, boolean characterAtPoint) {
		PathNode start = new PathNode(c.getBoard().getTile(c.getX(), c.getY())); // starting square.
		List<PathNode> openList = new ArrayList<PathNode>(); //list of nodes to consider
		openList.add(start);
		List<PathNode> closedList = new ArrayList<PathNode>(); //nodes that have been checked.
		boolean targetFound = false; // whether or not we have added the target to the closed list.
		
		while (!targetFound) {
			PathNode current = openList.get(0);
			for (int i = 1; i < openList.size(); i++) {
				PathNode next = openList.get(i);
				
				if (next.f < current.f) { // this 
					current = next;
				}
			}
			closedList.add(current);
			openList.remove(current);
			
			Tile next = c.getBoard().getTile(current.getTile().getX(), current.getTile().getY() - 1);			
			pathNodeHelper(current, next, target, openList, closedList);
			next = c.getBoard().getTile(current.getTile().getX(), current.getTile().getY() + 1);			
			pathNodeHelper(current, next, target, openList, closedList);
			next = c.getBoard().getTile(current.getTile().getX() - 1, current.getTile().getY());			
			pathNodeHelper(current, next, target, openList, closedList);
			next = c.getBoard().getTile(current.getTile().getX() + 1, current.getTile().getY());			
			pathNodeHelper(current, next, target, openList, closedList);
			if (containsPathNode(closedList, target.getX(), target.getY())) {
				targetFound = true;
			}
			//System.out.println(openList);
		}
		
		Stack<Tile> path = new Stack<Tile>();
		PathNode current = getPathNodeFromTile(closedList, target);
		path.push(current.getTile());
		current = current.parent;
		if (characterAtPoint) {
			path.pop();
		}
		while (current != null && current.parent != null) { // don't want start in final path.
			path.push(current.getTile());
			current = current.parent;
		}
		return path;		
	}	
	
	//does the work of adding a node to either the open list or closed list, reordering things.
	private void pathNodeHelper(PathNode current, Tile next, Tile target, List<PathNode> openList, 
																	List<PathNode> closedList) {
		if (next.canMoveInto() /*&& this.c.getBoard().isEmpty(next.getX(), next.getY()) */&& !closedList.contains(next)) {
			PathNode nextNode = new PathNode(current, next, target.getX(), target.getY());
			
			//if the open list doesn't contain a path to this point, add the current path to this point.
			if (!containsPathNode(openList, nextNode.getTile().getX(), nextNode.getTile().getY())) {
				openList.add(nextNode);					
			} else { // there is a pathnode to this tile. do comparisons for g scores.
					//if there's already a pathnode to this same tile, compare g scores from that 
					//parent with g scores for if we approached it from this tile.
				PathNode inList = getPathNodeFromTile(openList, nextNode.getTile());
				if (inList.g > nextNode.g) { // 
					 inList.setParent(current);
					 inList.calculateF();
				}
			}
			
		}
	}
	
	private PathNode getPathNodeFromTile(List<PathNode> openList, Tile t) {
		for (int i = 0; i < openList.size(); i++) {
			PathNode next = openList.get(i);
			if (next.getTile() == t) {
				return next;
			}
		}
		throw new IllegalStateException("Tile not found in open list. Check References.");
	}
	
	//determines if a given list contains a pathnode to a given x/y position
	private boolean containsPathNode(List<PathNode> openList, int x, int y) {
		for (int i = 0; i < openList.size(); i++) {
			PathNode curr = openList.get(i);
			if (curr.getTile().getX() == x && curr.getTile().getY() == y) {
				return true; // we found it
			}
		}
		return false;
	}
	
	
	private class PathNode {
		private PathNode parent;
		private Tile location;
		public int f; // total cost of paht
		public int g; //movement cost into this tile.
		public int h; // estimated movement cost to move from this to target
		int tx; // target x pos
		int ty; // target y pos
		
		public PathNode(Tile location) { // for initial node
			this.parent = null;
			this.location = location;
			f = g = h = 0;
			
		}
		
		//takes a parent node, location of this tile, and target x/y values
		public PathNode(PathNode p, Tile location, int tx, int ty) {
			this.tx = tx;
			this.ty = ty;
			this.parent = p;
			this.location = location;
			f = getMovementCost() + calculateH(tx, ty);
		}
		
		public Tile getTile() {
			return location;
		}
		
		public PathNode getParent() {
			return this.parent;
		}
		
		public void setParent(PathNode p) {
			this.parent = p;
		}
		
		public void calculateF() {
			f = getMovementCost() + calculateH(tx, ty);

		}
		
		public int getMovementCost() {
			if (parent == null) {
				return 0;
			} else {
				return 1 + parent.getMovementCost();
			}
		}
		
		public int calculateH(int targetX, int targetY) {
			int xDiff = Math.abs(location.getX() - targetX);
			int yDiff = Math.abs(location.getY() - targetY);
			return xDiff + yDiff;
		}
		
		public String toString() {
			return "" + location.getX() + "," + 
							location.getY() + location.getClass().getSimpleName();
		}
		
	}

}