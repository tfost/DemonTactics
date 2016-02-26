package strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*
 * This class provides functions loading numbers from a
 * text file into an array, and then a function to access said values.
 */

public class LevelImporter {

	private int[][] testArray;
	private int numRows;
	private int numCols;
	
	public LevelImporter() {
		this.scan();
	}
	public void scan(){			
		Scanner sc = null;
		try {
			File directory = new File("LevelTest.txt");
			Path file = Paths.get(directory.getAbsolutePath());
			sc = new Scanner(file);
		} catch(IOException e){
			e.printStackTrace();// print the error
		}
		
		numRows = 0;
		numCols = 0;
		
		String cols = sc.nextLine();
		numCols = cols.length() / 2; // figure out how long the array is
		//sc.reset();
		while(sc.hasNextLine()){ // figure out the height of the array
			numRows++;
			sc.nextLine();
		}
		//sc.reset();
		numRows += 1; // add one to create an array of proper size.
		numCols += 1;
		
		testArray = new int[numCols][numRows];
		sc.close(); // close, then reopen the scanner		
		try {
			File directory = new File("LevelTest.txt");
			Path file = Paths.get(directory.getAbsolutePath());
			sc = new Scanner(file);
		} catch(IOException e){
			e.printStackTrace();// print the error
		}		
		
		System.out.println(" number of collumns (x) " + numCols);
		System.out.println(" number of collumns (y) " + numRows);
		//System.out.println("Here's the integer representation of the map:");
		for(int r = 0; r < numRows; r++){ // debug
			for(int c = 0; c < numCols; c++){
				//System.out.println(c + " , " + r);
				testArray[c][r] = sc.nextInt();
				
			}					
		}	
		
	}
	
	//returns the tile at a given X/Y coordinate
	public int getTile(int x, int y){
		return testArray[x][y];
	}
	
	//returns the number of rows (y) in the boards.
	public int getRows(){
		return numRows;
	}
	
	//returns the number of collumns(x) in the current board.
	public int getCols(){
		return numCols;
	}
	
	
	
}
