package vicPackage;

import java.util.Random;

import javax.swing.JButton;

public class LifeBoard {

	private static int defaultBoardWidth = 100, defaultBoardHeight = 60;
	//this fits the window size perfectly on lion/java6; is framed in the center in win7

	private LifeCell cellArray[][];
	private LifeMainGUI gui;

	private int width, height, generations, frequency = 3;
	private boolean automatic, consoleMode;

	public LifeBoard(int x, int y, boolean useConsole) {
		width = x;
		height = y;
		consoleMode = useConsole;

		automatic = false;

		initBoard();
		if (!consoleMode) {
			// @SuppressWarnings("unused")
			gui = new LifeMainGUI(this);
		}
	}

	private void initBoard() { 
		// initialize board with new Cells and JButtons (if (!consoleMode))
		cellArray = new LifeCell[height][width];
		
		generations = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.cellArray[i][j] = new LifeCell(j, i, this, (consoleMode ? null
						: new JButton()));
			}
		}
	}

	public LifeCell getCell(int xCoord, int yCoord) {
		// returns the [y][x]th Cell, or null if the cell does not exist
		if (xCoord < 0 || yCoord < 0 || xCoord >= width || yCoord >= height) {
			return null;
		} else
			return cellArray[yCoord][xCoord];
	}

	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int hertz) { //unused
		frequency = hertz;
	}
	
	public void incrementFrequency() {
		if (frequency <= 50)
			frequency++;
	}
	
	public void decrementFrequency() {
		if (frequency > 1)
			frequency--;
	}

	public static int getDefaultBoardWidth() { //kinda unnecessary
		return defaultBoardWidth;
	}
	
	public static int getDefaultBoardHeight() { //this one too
		return defaultBoardHeight;
	}

	public int getGenerations() {
		return generations;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean getAuto() {
		return automatic;
	}

	public void auto() {
		if (!automatic) {
			automatic = !automatic;
			(new LifeAutoThread(this, gui)).start();
		} else {
			automatic = !automatic;
		}
	}

	public boolean click(int xCoord, int yCoord) {
		// changes state of [y][x]th cell (if it exists)
		LifeCell c = getCell(xCoord, yCoord);
		if (c != null) {
			
//****following lines make unclick permanent
			
			if (c.isAlive()) {
				//following lines are a hack!  just like plague.
				JButton tempJB = cellArray[yCoord][xCoord].getButton();
				cellArray[yCoord][xCoord] = new LifeCell(xCoord, yCoord, this, tempJB);
			}
			else 
			
//*****end note
			
			{
			c.click();
			
			}
			return true;
		} else
			return false; // only if cell does not exist
	}

	public void step() {
		// one iteration. meat & potatoes of logic
		// of interest is that we need an evaluation run and an action run

		// eval run
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				cellArray[i][j].findNextState();
			}
		}

		// action run
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				cellArray[i][j].takeNextAction();
				//System.out.println(cellBoard[i][j] + ": " + cellBoard[i][j].neighbors.size());
			}
		}
		generations++;
	}

	public void step(int numSteps) { // unused
		// perform <numSteps> steps
		for (int i = 1; i <= numSteps; i++) {
			step();
		}
	}

	public void clearBoard() {
		generations = 0;
		// different approach. Works!  still a fucking hack.
		//
		// old method that doesn't work: for each cell that is alive, click it
		// (to make it dead).  
		// not sure why it doesn't work.  it unclicks the cell, but upon the next
		// generation of the simulation, all unclicked cells regenerate.  
		// damndest thing.
		// similarly, you can't unclick a cell that has survived through a step()
		// you must obliterate it and create a new cell

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				JButton tempJB = cellArray[i][j].getButton();
				cellArray[i][j] = new LifeCell(j, i, this, tempJB);
			}
		}
	}

	public void randomize() {
		// randomly clicks or doesn't click each cell
		generations = 0;
		Random random = new Random();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (random.nextBoolean()) {
					LifeCell cell = getCell(j, i);
					if (cell != null)
						cell.click();
				}
			}
		}
	}
	
	public void invert() {
		//flip dead cells to alive and alive cells to dead
	}

	@Override
	public String toString() { //only for console mode & debugging
		StringBuffer output = new StringBuffer("-1");
		// first line
		for (int q = 0; q < width; q++)
			output.append("-");
		output.append("\n");

		for (int h = 0; h < height; h++) { // go down the board
			for (int w = 0; w < width; w++) { // go across the board
				if (w == 0) {
					if (h == 0) // first line only
						output.append("1");
					else
						output.append("|");
				}
				if (cellArray[h][w].isAlive()) {
					output.append("X");
//					if (w == width - 1)
//						output.append("|");
				} else {
					output.append(" ");
				}
				if (w == width - 1)
					output.append("|");
			}
			output.append("\n");
		}
		for (int q = 0; q <= width + 1; q++) {
			output.append("-");
		}
		output.append("\n");
		return output.toString();
	}

}