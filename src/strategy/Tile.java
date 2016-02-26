package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Tile {
	private int x, y; //x/y coordinates of the tile on a board.
	private int tilex, tiley; // image tile coordinates.
	private boolean isDarkTile;	//this tile is in the dark world.
								// marks different properties about the tile.
	private Faction faction; // marks who owns the tile. owned by a player/
	private boolean isSelected; // this tile is selected 
	private BufferedImage texture;
	//references to tiles all around for searching and selecting.
	private Tile westTile; 
	private Tile northTile;
	private Tile eastTile;
	private Tile southTile;
	private Base owner;
	int redInfluence;
	int blueInfluence;
	
	public Tile(int x, int y, BufferedImage img) {
		this(x, y, 0, 0, img);
	}

	public Tile(int x, int y, int tilex, int tiley, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.tilex = tilex;
		this.tiley = tiley;
		this.texture = img;
		this.faction = Faction.HUMAN;
		this.owner = null;
	}
	
	public int getTilex() {
		return tilex;
	}
	
	public int getTiley() {
		return tiley;
	}
	
	public boolean isBase() {
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setSelected(boolean s) {
		this.isSelected = s;
	}
	
	public boolean isSelected() {
		return this.isSelected;
	}
	
	public boolean isDarkTile() {
		return this.isDarkTile;
	}
	
	public void setDarkTile(boolean b) {
		this.isDarkTile = b;
	}
	
	public void modifyInfluence(Faction f, int amount) {
		if (f == Faction.RED) {
			this.redInfluence += amount;
		} else if (f == Faction.BLUE) {
			this.blueInfluence += amount;
		}
		this.updateFaction();
	}
	
	
	public Faction getFaction() {
		return faction;
	}
	
	
	public void updateFaction() {
		if (this.redInfluence == 0 && this.blueInfluence == 0) {
			this.faction = Faction.HUMAN; //no influence.
		} else if (this.redInfluence != 0 && this.blueInfluence != 0) {
			this.faction = Faction.CONTESTED;
		} else if (this.redInfluence != 0 && this.blueInfluence == 0) {
			this.faction = Faction.RED;
		} else if (this.redInfluence == 0 && this.blueInfluence != 0){
			this.faction = Faction.BLUE;
		} else {
			throw new IllegalStateException("Illegal Values of Red and Blue Influence:" + this.redInfluence + ", " + this.blueInfluence);
		}
	}
	
	/**
	 * @return the owner of the tile.
	 */
	public Base getOwner() {
		return owner;
	}

	/**
	 * @param owner the base to set as the owner
	 */
	public void setOwner(Base owner) {
		this.owner = owner;
	}
	
	public Color tileCol() {
		return Color.MAGENTA; // a weird color that will stand out.
	}
	
	public boolean canMoveInto() {
		return false;
	}

	public Tile getSouthTile() {
		return southTile;
	}

	public void setSouthTile(Tile southTile) {
		this.southTile = southTile;
	}

	public Tile getEastTile() {
		return eastTile;
	}

	public void setEastTile(Tile eastTile) {
		this.eastTile = eastTile;
	}

	public Tile getNorthTile() {
		return northTile;
	}

	public void setNorthTile(Tile northTile) {
		this.northTile = northTile;
	}

	public Tile getWestTile() {
		return westTile;
	}

	public void setWestTile(Tile westTile) {
		this.westTile = westTile;
	}
	
	public void renderTile(Graphics g, Camera c) { // each tile will render like this.
		//do selection stuff on the charBoard.
		
		g.setColor(this.tileCol());//draw main tile.
		Point loc = new Point(x * c.getTileSize(), y * c.getTileSize());
		loc = c.translatePointToScreen(loc);
		g.fillRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		g.setColor(Color.BLACK);//draw outline
		g.drawRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
	
		if (this.faction == Faction.RED) {
			g.setColor(new Color(255, 0, 0, 100));		
			g.fillRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		} else if (this.faction == Faction.BLUE) {
			g.setColor(new Color(0, 0, 255, 160));
			g.fillRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		} else if (this.faction == Faction.CONTESTED) {
			g.setColor(new Color(200, 0, 200, 100));
			g.fillRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
		}
		if (this.isSelected) {
			g.setColor(new Color(51, 153, 255, 160));
			g.fillRect(loc.x, loc.y, c.getTileSize(), c.getTileSize());
			
		} 
		
	}
	
	public String toString() {
		String result = "[";
		result += "coordinates = " + x + ", " + y;
		result += "| Type = " + this.getClass().getSimpleName();
		return result + "]";
	}

	
}


