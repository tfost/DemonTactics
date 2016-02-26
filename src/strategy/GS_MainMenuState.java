package strategy;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Scanner;

public class GS_MainMenuState implements GameState{
	Scanner console;
	MouseInput input;
	KeyboardInput keyboard;
	boolean isInState; // whether or not we should continually update this state.
	
	public GS_MainMenuState(MouseInput input, KeyboardInput keyboard) {
		console = new Scanner(System.in);
		isInState = true;
		this.input = input;
		this.keyboard = keyboard;
	}
	
	
	public void update() {
		System.out.println("Press e to begin!   : ");
		String next = console.next();
		if (next.equals("e")) {
			isInState = false; // time to leave the state.
		
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Press e to begin!", 50, 50);
		g.drawRect(10,10,50,50);
	}

	
	public boolean isInState() {
		return isInState;
	}

	
	//should either go to the world map or end the game
	//however for dev purposes, goes to a new test level.
	public GameState nextState() {
		try {
			return new GS_LevelState(input, keyboard);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
