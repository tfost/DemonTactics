package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * A Base is a Tile that can spawn Actors. 
 * each base also contains an influence radius, in which demons can live 
 * if they are of the correct faction.
 * @author Tyler
 *
 */
public class Base extends Tile{
	private int health; 
	private int maxHealth;
	private int def;
	private int influenceRadius;
	private int spawnTimer;
	private int influenceStrength;
	
	/**
	 *  Constructs a base at a given x and y tile. Bases
	 *  Must specify their faction - light or dark.
	 * @param x X location of this base.
	 * @param y Y location of this base.
	 */
	public Base(int x, int y, BufferedImage img, Faction faction) { 
		this(x, y, img, 50, 10, 6, faction);
	}
	
	public Base(int x, int y, BufferedImage img, int maxHealth, int def, int influenceRadius, Faction faction) {
		super(x, y, 64, 0, img );
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.def = def;
		this.influenceRadius = influenceRadius;
		this.influenceStrength = 70;
		this.modifyInfluence(faction, influenceStrength);
		
	}
	
	public void initInfluenceRange(Board b) {
		if (this.influenceRadius > 0) {
			initHelper(this.getX(), this.getY(), this.influenceRadius, b);
		}
	}
	
	private void initHelper(int x, int y, int radius, Board b) {
		if (radius > 0) {
			Tile t = b.getTile(x,y);
			if (t != null ) {
				t.modifyInfluence(this.getFaction(), this.influenceStrength);
				t.setOwner(this);
				initHelper(x - 1, y, radius - 1, b);
				initHelper(x + 1, y, radius - 1, b);
				initHelper(x, y - 1, radius - 1, b);
				initHelper(x, y + 1, radius - 1, b);
			}
		}
	}
	
	public void onTurnStart() {
		if (this.health < this.maxHealth) {
			this.health += 5;
			if (health > maxHealth) {
				this.health = maxHealth;
			}
		}
	}
	
	public void update(Board b) {		
		/*if (this.influenceRadius > 0) {
			spreadInfluence(this.getX(), this.getY(), this.getInfluenceRadius(), b);
		}*/
	}	
	
	public Color tileCol() {
		return Color.MAGENTA;
	}
	
	public void spawnUnits() {
		
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		// TODO Auto-generated method stub
		this.health = health;
	}	
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	//todo - if a character is here, have stronger defense.
	public int getDefense() {
		return def;
	}
	
	
	public int getInfluenceRadius() {
		return influenceRadius;
	}
	
	public void setInfluenceRadius(int i) {
		this.influenceRadius = i;
	}
	
	//returns if a given x/y coordinate falls within the influence radius of this base.
	public boolean inAreaOfInfluence(int tempx, int tempy) {
		if (this.influenceRadius == -1) { // base doesn't generate influence
			return false;
		} else {
			return Point.distance(this.getX(), this.getY(), tempx, tempy) < influenceRadius;
		}
	}		
	
	public boolean isBase() {
		return true;
	}
	
	public void renderTile(Graphics g, Camera c) {
		super.renderTile(g, c);
		g.setColor(Color.RED);
		Point loc = c.translatePointToScreen(new Point (this.getX() * c.getTileSize(), this.getY() * c.getTileSize()));
		g.drawString(this.health + "/" + this.maxHealth, loc.x, loc.y - 2);

	}
	
	public boolean canMoveInto() {
		return true;
	}
}
