package strategy;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
/**
 * This class handles all of the tiles that make up a working level.
 * 
 * @author Tyler
 */
public class Board {
	public static final int TILE_SIZE = 32;
	private Tile[][] tileBoard;
	private Unit[][] unitBoard; // stores locations of all characters.
	private Set<Base> bases;
	private int width, height;
	
	/**
	 * Constructs a Board using a LevelImporter. 
	 * @throws IOException if a given image is not found.
	 */
 	public Board() throws IOException {
		LevelImporter importer = new LevelImporter();
		BufferedImage img = ImageIO.read(getClass().getResource("/tiles.png"));
		width = importer.getCols();
		height = importer.getRows();
		tileBoard = new Tile[width][height];
		unitBoard = new Unit[width][height];
		//lightBases = new HashSet<LightBase>();
		//darkBases = new HashSet<DarkBase>();		
		bases = new HashSet<Base>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int next = importer.getTile(x, y);
				if (next == 0) {
					tileBoard[x][y] = new Grassland(x,y, img);
				} else if (next == 1) {
					tileBoard[x][y] = new Mountain(x,y, img);
				} else if (next == 2) {
					Base b = new Base_Capital(x,y,img, Faction.BLUE);
					tileBoard[x][y] = b;
					bases.add(b);
				} else if (next == 3) {
					Base b = new Base_Capital(x,y,img, Faction.RED);
					tileBoard[x][y] = b;
					bases.add(b);
				}
				else {
					tileBoard[x][y] = new Tile(x,y, img);
				}
			}
		}
		
		//set all tile references. if out of bounds of array, set it to null.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Tile t = getTile(x,y);
				try {
					t.setEastTile(getTile(x - 1, y));
				} catch (ArrayIndexOutOfBoundsException e){
					t.setEastTile(null);
				}
				try {
					t.setWestTile(getTile(x + 1, y));
				} catch (ArrayIndexOutOfBoundsException e){
					t.setWestTile(null);
				}
				try {
					t.setNorthTile(getTile(x, y - 1));
				} catch (ArrayIndexOutOfBoundsException e){
					t.setNorthTile(null);
				}
				try {
					t.setSouthTile(getTile(x, y + 1));
				} catch (ArrayIndexOutOfBoundsException e){
					t.setSouthTile(null);
				}
			}
		}
		
		for (Base b : this.bases) {
			b.initInfluenceRange(this);
		}
		/*for(Base b : this.getBasesOfFaction(Faction.BLUE)) {
			DarkBase d = (DarkBase) b;
			initBaseTiles(d, d.getInfluenceRadius(), Faction.BLUE);
		}
		
		for (Base b : this.getBasesOfFaction(Faction.RED)) {
			LightBase l = (LightBase) b;
			initBaseTiles(l, l.getInfluenceRadius(),  Faction.RED);
		}*/
	}

 	
 	public void update() {
 		for (Base b : this.bases) {
 			b.update(this);
 		}
 	}
 	
 	public Tile getTile(int x, int y) {
 		if (x >= 0 && x < width && y >= 0 && y < height) {
 			return tileBoard[x][y];
 		} else {
 			return null;
 		}
 	}
 	
 
	public Set<Base> getBasesOfFaction(Faction f) {
		Set<Base> result = new HashSet<Base>();
		for (Base b : bases) {
			if (b.getFaction() == f) {
				result.add(b);
			}
		}
		
		return result;
	}
	
	public Set<Base> getBases() {
		return this.bases;
	}
	
	
 	
 	public void render(Graphics g, Camera gameCamera) {
 		for (int x = 0; x < width; x++) {
 			for (int y = 0; y < height; y++) {
 				tileBoard[x][y].renderTile(g, gameCamera); 				
 			}
 		}
 		
 	}
 	
 	/**
 	 * Adds each set of characters onto the board.
 	 * @param units The set of actors needed to be added to this board.
 	 */
	public void populate(Set<Unit> units) {
 		for (Unit a : units) {
 			unitBoard[a.getX()][a.getY()] = a;
 		}
 	}
	
 	/**
 	 * @param x The x coordinate to inspect
 	 * @param y The y coordinate to inspect
 	 * @return false if a character exists at given coordinates. otherwise returns true.
 	 */
 	public boolean isEmpty(int x, int y) {
 		return unitBoard[x][y] == null;
 	}
 	
 	public Unit getActor(int x, int y) {
 		return unitBoard[x][y];
 	}
 	
 	public void moveActorTo(Unit unit, int x, int y) {
 		this.unitBoard[unit.getX()][unit.getY()] = null;
 		this.unitBoard[x][y] = unit;
 	}
 	
 	public int getWidth() {
 		return width;
 	}
 	
 	public int getHeight() {
 		return this.height;
 	}
 	//takes a point , a number of x tiles, and number of y tiles, and converts
 	//the points' x and y values to match what index tile they refer to.
 	public static Point pixelsToTile(Point p, int numXTiles, int numYTiles) {
 		Point result = new Point(p.x * numXTiles / StrategyPanel.WIDTH, 
 								p.y * numYTiles / StrategyPanel.HEIGHT);
 		return result;
	}
}


