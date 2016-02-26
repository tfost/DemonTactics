package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
/**
 * Represents a character entity of the game. 
 * Actors can move throughout the world, attacking both
 * other actors and bases.
 * @author Tyler
 *  
 *
 */
public class Unit {
	private int x;						// coordinates of this character.
	private int y;
	private int strength;
	private int defense;
	private int health;
	private int maxHealth;
	private int turnsToSpawn;
	private int movementDistanceLeft;	
	private int maxMoves; 				//how much a character can move on a given turn.
	
	private String name;
	
	private double attackRange;
	
	private boolean isSelected;
	private boolean inTurn; 			// if the player has run out of moves or not. 
	private boolean isDead;
	
	private UnitState state;
	private Board board;
	private Faction faction;
	private UnitUI ui;
	
	/////////////////constructors////////////////////////
	public Unit() {
		this(0,0, null);
	}
	
	/**
	 * Creates an actor at a specified x, y coordinate on a given Board.
	 * @param x The x position of the character. This
	 * 			should be in terms of tiles 			
	 * @param y The y position of the character. This
	 * 			should be in terms of Tiles.	
	 * @param b The board on which this actor should be placed.
	 */
	public Unit(int x, int y, Board b){
		this(x, y, b, 5, 3, 20, 13, 5, Faction.RED,  "GENERIC_ACTOR");
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param b
	 * @param movementDistance
	 * @param spawnTime
	 * @param maxHealth
	 * @param attack
	 * @param defense
	 * @param name
	 */
	public Unit(int x, int y, Board b, int movementDistance, int spawnTime, int maxHealth, int strength, int defense, Faction faction, String name) {
		this.x = x;
		this.y = y;
		maxMoves = movementDistance;
		movementDistanceLeft = maxMoves;
		turnsToSpawn = spawnTime;
		state = new US_IdleState(this);
		this.board = b;
		inTurn = true;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.strength = strength;
		this.defense = 5;
		this.name = name;
		this.faction = faction;
		this.ui = new UnitUI(this);
		this.attackRange = 1.0;
		//...
	}
	
	/*The 4 types of unit. 
	 * Soldier > Beast > Sorcerer > Soldier
	 * All are strong against pawn. 
	 */
	
	public enum Type {
		SOLDIER, BEAST, SORCERER, PAWN;
	}
	
	/////////////////methods////////////////////////////
	/**
	 * 
	 * @return The board on which this character exists
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * 
	 * @return the tile on which this character is on.
	 */
	public Tile getTile() {
		return board.getTile(this.x, this.y);
	}
	
	/**
	 * 
	 * @param s The new UnitState of this actor.
	 * @see UnitState
	 */
	public void setActorState(UnitState s) {
		state = s;
	}
	
	/**
	 * 
	 * @return the current UnitState of this actor
	 */
	public UnitState getState() {
		return state;
	}
	
	/**
	 * 
	 * @return true if this is a dark character, otherwise false.
	 */
	public boolean isDark() {
		return false;
	}
	
	/**
	 * 
	 * @return If this character is currently selected.
	 */
	public boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * sets the selection status of this character
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	/**
	 * 
	 * @return The color this character should be represented as.
	 */
	public Color charCol() {
		return Color.blue;
	}

	/**
	 * 
	 * @return Standard number of turns this character takes to spawn.
	 */
	public int getTurnsToSpawn() {
		return turnsToSpawn;
	}
	
	/**
	 * 
	 * @return The X value of this actor.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * 
	 * @return The Y value of this actor.
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets this actor's x position to a given y.
	 * @param y The new x position of this Unit.
	 */
	public void moveTo(int x, int y) {
		this.board.moveActorTo(this, x, y);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @return The amount of tiles this Unit can walk before
	 * 			it's turn ends.
	 */
	public int getMovementDistanceLeft() {
		return movementDistanceLeft;
	}
	
	/**
	 * 
	 * @param dist The new amount of actor has left.
	 * @throws IllegalArgumentException if the distance is less than 0 
	 * 			or greater than the maximum moves this actor can move.
	 */
	public void setMovementDistanceLeft(int dist) {
		if (dist < 0 || dist > maxMoves) {
			throw new IllegalArgumentException("given value is out of bounds: " + dist);
		}
		this.movementDistanceLeft = dist;
	}
	
	/**
	 * Resets turn-based elements of a actor for the start of a new turn.
	 * Does not reset dead characters, because, well, they're dead.
	 */
	public void onTurnEnd() {
		if (!this.isDead) { 
			inTurn = true;
			this.movementDistanceLeft = maxMoves;
		}
	}
	
	/**
	 * Called at the start of the turn.
	 * @param turnNum
	 */
	public void onTurnStart(int turnNum) {
		
	}
	
	/**
	 * 
	 * @param inTurn Whether or not this actor is in the middle of it's turn.
	 */
	public void setTurnStatus(boolean inTurn) { // IN FUTURE, DO THIS METHODD AS PART OF ISINTURN
		this.inTurn = inTurn;
	}
	
	/**
	 * 
	 * @return If this actor still has work to do for its turn.
	 */
	public boolean isInTurn() {
		return inTurn;
	}
	
	/**
	 * Draws this actor to the screen.
	 * 
	 * @param c The Camera determining offsets.
	 * @see Camera
	 */
	public void render(Graphics g, Camera c) {
		if (isDead()) {
			g.setColor(Color.red);
		} else {
			g.setColor(this.charCol());
		}
		Point loc = c.translatePointToScreen(new Point (this.x * c.getTileSize(), this.y * c.getTileSize()));
		g.fillOval(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		g.setColor(Color.black);
		g.drawOval(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		g.setColor(Color.white);
		g.drawString(this.health + "/" + this.maxHealth, loc.x, loc.y - 2);
		g.drawString(this.name, loc.x, loc.y - 16);
		
	}
	
		
	public boolean isDead() {
		return isDead;
	}
	
	
	/**
	 * calculates and deals damage to another Unit.
	 * Ends this character's turn upon completion.
	 * @param other
	 */
	public void attack(Unit other) {
		if (this.getFaction() == other.getFaction()) {
			throw new IllegalArgumentException("Error - can't attack an actor of the same faction");
		}
		int raw = this.strength - other.getDef(); //TODO Create a more in-depth damage formula.
		if (raw <= 0) {
			raw = 1;
		} 
		other.setHP(other.getHP() - raw);
		other.onAttack(); 
		this.inTurn = false;
		System.out.println(this.name + " attacked " + other.getName() + " for " + raw + " damage");
		
	}
	
	
	
	//attack and potentially capture a base. 
	public void attackBase(Base b) {
		if (b.getFaction() == this.getFaction()) {
			throw new IllegalArgumentException("You can't attack your own base!");
		}
	}
	
	public int getStr() {
		return this.strength;
	}
	public int getDef() {
		return this.defense;
	}
	
	public int getHP() {
		return this.health;
	}
	
	public void setHP(int hp) {
		this.health = hp;
	}
	
	/**
	 * Sets the deadness of the actor.
	 * @param dead If this characer is dead or not.
	 */
	public void setDead(boolean dead) {
		isDead = dead;
	}
	
	/**
	 * 
	 * @param other
	 * @return Whether another actor is 1 tile away from this character.
	 */
	public boolean isNextTo(Unit other) {
		int xDiff = Math.abs(this.getX() - other.getX());
		int yDiff = Math.abs(this.getY() - other.getY());
		return xDiff <= 1 && yDiff <= 1; // 1,1, 01, 10, and 00 all valid.
	}
	
	/**
	 * 
	 * @param other
	 * @return Whether a given tile is within 1 tile of this character.
	 */
	public boolean isNextTo(Tile other) {
		int xDiff = Math.abs(this.getX() - other.getX());
		int yDiff = Math.abs(this.getY() - other.getY());
		return xDiff <= 1 && yDiff <= 1; // 1,1, 0, 1, 1, 0, and 00 all valid.
	}
	
	/**
	 * Determines if a given character is in the attack range of this character.
	 * @param other the target actor.
	 * @return	whether it can be attacked.
	 */
	public boolean isInAttackRange(Unit other) {
		int xDiff = other.getX() - this.getX();
		int yDiff = other.getY() - this.getY();
		double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
		
		return dist <= this.attackRange; 
		
	}
	
	
	/**
	 * @return the attackRange
	 */
	public double getAttackRange() {
		return attackRange;
	}

	/**
	 * @param attackRange the attackRange to set
	 */
	public void setAttackRange(double attackRange) {
		this.attackRange = attackRange;
	}	
	
	
	public UnitUI getUI() {
		return ui;
	}
	
	public Faction getFaction() {
		return this.faction;
	}

	public void onAttack() {
		if (this.health <= 0) {
			this.setDead(true);
		}
		
	}
	
	public Type getType() {
		return Type.PAWN;
	}
	
	//Soldier > Beast > Sorcerer > Soldier
	public static double getUnitTypeAttackBonus(Unit attacker, Unit victim) {
		if (attacker.getType() == victim.getType()) {
			return 1;
		} else if (attacker.getType() == Type.PAWN) {
			return 0;
		} else if (victim.getType() == Type.PAWN) {
			return 2; //double strength on pawns.
		} else if (attacker.getType() == Type.SOLDIER && victim.getType() == Type.BEAST ||
					attacker.getType() == Type.BEAST && victim.getType() == Type.SORCERER ||
					attacker.getType() == Type.SORCERER && victim.getType() == Type.SOLDIER) {
			return 1.25;
		} else if (attacker.getType() == Type.BEAST && victim.getType() == Type.SOLDIER ||
				attacker.getType() == Type.SORCERER && victim.getType() == Type.BEAST ||
				attacker.getType() == Type.SOLDIER && victim.getType() == Type.SORCERER) {
			return .75;
		} else {
			return 1;
		}
	}

}
