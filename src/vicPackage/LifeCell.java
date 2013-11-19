package vicPackage;

import java.awt.Color;

import javax.swing.JButton;

public class LifeCell {

	private static LifeBoard myBoard;
	private JButton myButton;

	private static final boolean ALIVE = true;
	private static final boolean DEAD = false;

	private boolean state, nextState;
	private int numNeighbors, xCoord, yCoord;

	public LifeCell(int xcoord, int ycoord, LifeBoard gb, JButton jb) {
		// all method invocations involving x and y coordinates occur
		// in the order (x,y)
		// however all array accessing occurs in the order [y][x]
		
		// a lifecell in console mode is constructed with a null jbutton.

		xCoord = xcoord;
		yCoord = ycoord;

		myBoard = gb;

		myButton = jb; // might be null if we're in console mode
		
		if (myButton != null) {
			myButton.setOpaque(true);
			myButton.setBorderPainted(false);
			myButton.setText(toString());
		}

		death(); // every cell starts out dead
	}

	public int getX() {
		return xCoord;
	}

	public int getY() {
		return yCoord;
	}

	public void setButton(JButton jb) { // associates a jbutton with this cell
		myButton = jb;
	}

	public JButton getButton() { // returns this cell's jbutton (or null)
		return myButton;
	}

	private void updateNeighbors() {
		// x x x the xs are the neighbors of o
		// x o x
		// x x x
		numNeighbors = 0;
		LifeCell cell;
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
		}
		// System.out.println(this + " : " + neighbors); //DEBUG
	}

	public void findNextState() { // determine next state
		updateNeighbors();
		if (state) {
			if (numNeighbors < 2 || numNeighbors > 3)
				nextState = DEAD;
				// Any live cell with fewer than two live neighbors dies by
				// under-population.

			if (numNeighbors == 2 || numNeighbors == 3)
				nextState = ALIVE;
				// Any live cell with two or three live neighbors lives on to
				// the next generation.
		} else {
			
			if (numNeighbors == 3) 
				nextState = ALIVE;
				// Any dead cell with exactly three live neighbors becomes a
				// live cell by reproduction.
		}
	}

	public void takeNextAction() {
		if (nextState)
			life();
		else
			death();
	}

	public boolean isAlive() {
		return state;
	}

	public void click() {
		if (state)
			death();
		else
			life();
	}

	private void life() {
		state = ALIVE;

		if (myButton != null) {
			myButton.setForeground(Color.BLACK);
			myButton.setBackground(Color.BLACK);
		}
	}

	protected void death() {
		state = DEAD;
		
		if (myButton != null) {
			myButton.setForeground(Color.WHITE);
			myButton.setBackground(Color.WHITE);
		}
	}

	public String toString() {
		return new String("" + xCoord + "." + yCoord);
	}
}
