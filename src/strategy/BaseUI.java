package strategy;

import java.awt.Color;
import java.awt.Graphics;

//this class manages player input with the base. 
//it allows them to choose which unit they want to spawn, and
//get other facts about the base.
public class BaseUI {
	private Base base;
	private boolean isActive;
	private Board board;
	private Unit[] spawnables = {new Unit_InfluenceSpreader(this.base.getX(), this.base.getY(), this.board,this.base.getFaction())};
	private int[] spawnZone = {0, StrategyPanel.WIDTH, StrategyPanel.HEIGHT - 50, 50}; //x1,x2, y1, y2
	
	public BaseUI(Board b) {
		this.isActive = false;
		this.base = null;
		this.board = b;
	}
	
	public void setBase(Base b) {
		this.base = b;
	}
	
	public Base getBase() {
		return this.base;
	}
	
	public void setActive(boolean active) {
		this.isActive = active;
	}
	
	public Unit[] getSpawnables() {
		return this.spawnables;
	}
	
	//Handles mouse input into the Ui. 
	//NOTE - there is no offset with the UI. It's location ont the screen is exactly where we want to click.
	public void handleInput(MouseInput m, Camera c) { //TODO - maybe make it a boolean suhc that we don't do input for overlapping objects.
		
	}
	
	public void paint(Graphics g) {
		if (this.isActive) {
			g.setColor(Color.white);
		    g.fillRect(spawnZone[0], spawnZone[1], spawnZone[2], spawnZone[3]);
		}
	}
	
	
}
