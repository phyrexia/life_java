package com.victone.life.logic;

import java.util.ArrayList;
import java.util.Random;

public class LifeBoard {

	private static int defaultBoardWidth = 100, defaultBoardHeight = 60;
	//this fits the window size perfectly on lion/java6; is framed in the center in win7

	private Cell[][] lifeBoard;

	private int width, height, generation;

    public LifeBoard() {
        this(defaultBoardWidth, defaultBoardHeight);
    }

	public LifeBoard(int width, int height) {
        this.width = width;
        this.height = height;

    	lifeBoard = new Cell[height][width];
		
		generation = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.lifeBoard[i][j] = new Cell();
            }
        }
	}

	public Cell getCell(int xCoord, int yCoord) { //v2
		// returns the [y][x]th Cell,
		if (xCoord < 0 || yCoord < 0 || xCoord >= width || yCoord >= height) {
			throw new IllegalArgumentException("Invalid cell coordinate!");
		} else
			return lifeBoard[yCoord][xCoord];
	}

	public int getGeneration() {
		return generation;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public void step() {
		// one iteration. meat & potatoes of logic
		// of interest is that we need an evaluation run and an action run
        // [later] but we don't actually, if we use a new LB.


        /*
		// eval run
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				findNextState();
			}
		}

		// action run
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				lifeBoard[i][j].takeNextAction();
				//System.out.println(cellBoard[i][j] + ": " + cellBoard[i][j].neighbors.size());
			}
		}
		*/
		generation++;
	}

    private Cell[][] getNextBoard(Cell[][] oldBoard) {

        return null;
    }


    public void step(int numSteps) {
		// perform <numSteps> steps
		for (int i = 1; i <= numSteps; i++) {
			step();
		}
	}

    private void findNextState() {
       /* int numNeighbors = 0;
        Cell cell;
        for (int neighborYpos = getY() - 1; neighborYpos <= getY() + 1; neighborYpos++) {
            for (int neighborXpos = getX() - 1; neighborXpos <= getX() + 1; neighborXpos++) {
                cell = myBoard.getCell(neighborXpos, neighborYpos);
                if (cell != null) { // so we don't go off board and throw a null pointer
                    if (neighborXpos == xCoord && neighborYpos == yCoord)
                        continue; // we skip this cell since it's not a neighbor of itself
                    else if (cell.isAlive())
                        numNeighbors++;
                }
            }
        } */
        // System.out.println(this + " : " + neighbors); //DEBUG

    }

	public void clearBoard() {   //v2.0b1
		generation = 0;
		for (int y = 0; y < defaultBoardHeight; y++) {
            for (int x = 0; x < defaultBoardWidth; x++) {
                lifeBoard[y][x].death();
            }
        }
	}

	public void randomize(boolean freshStart) {
        if (freshStart) {
            clearBoard();
            generation = 0;
        }
        // randomly clicks or doesn't cycle each cell
		Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (random.nextBoolean()) {
					lifeBoard[y][x].cycle();
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
				if (lifeBoard[h][w].isAlive()) {
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