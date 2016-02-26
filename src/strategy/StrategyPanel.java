package strategy;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class StrategyPanel extends JPanel{
	public static final int WIDTH = 768;
	public static final int HEIGHT = 768;
	
	Stack<GameState> stateMachine;//state stack.
	MouseInput input; // mouseListener for the game.
	KeyboardInput keyboard;
	
	public StrategyPanel() throws IOException {
		//add mouse listeners
		input = new MouseInput();
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
				
		keyboard = new KeyboardInput();
		
		stateMachine = new Stack<GameState>(); // state stack.
		//push the main menu to the stack immediately so we don't run into
		//empty stack exceptions later on.
		this.stateMachine.push(new GS_LevelState(input, keyboard)); 
		// DEBUG - Start off right in the battle. in future, start in mainMenu.
		frameInit();
	}
	
	//initializes the game. Takes a strategy main to activate 
	//keyListeners and the like.
	public void frameInit() {
		JFrame frame = new JFrame("Strategy Prototype");
		frame.pack();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setBackground(Color.BLACK);
		Insets insets = frame.getInsets();
		Dimension d = new Dimension(WIDTH + insets.left , HEIGHT + insets.top);		
		frame.setSize(d);
		frame.setResizable(false);
		frame.setVisible(true); // set frame visible and render first pass.
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(this.keyboard);
		System.out.println(frame.getInsets());
		
		//setup the rendering stuff for the frame.
		Graphics g = this.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		this.setOpaque(true);
		
		
	}
		
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		this.stateMachine.peek().render(g2d); // render the current element in the stateStack.
		
		// draw functions go here.
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException{
		System.out.println("Loading. . . ");
		
		//graphics/window initialization
		StrategyPanel panel = new StrategyPanel();		
		
		while (!panel.stateMachine.isEmpty()) { // keep playing while there are still states to manage.
			if (panel.stateMachine.peek().isInState()) { // this state is still running
				panel.stateMachine.peek().update();
				panel.repaint();
				Thread.sleep(15);
				
			} else {  //either we need to add a new state, or get rid of this one.	
				if (panel.stateMachine.peek().nextState() == null) {
					panel.stateMachine.pop(); // done with this state. do the one beneath it now.
				} else {
					panel.stateMachine.push(panel.stateMachine.peek().nextState());
				}
							
			}
		}
		
		
	}
	
}
