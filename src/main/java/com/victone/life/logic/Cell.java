package com.victone.life.logic;

public class Cell {
	private boolean isAlive, nextState;

	public Cell() {
		death(); // every cell starts out dead
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void cycle() {
        if (isAlive) {
			death();
        } else {
			life();
        }
	}

	public void life() {
		isAlive = true;
	}

	public void death() {
		isAlive = false;
	}

    @Override
    public String toString() {
        return ( isAlive ? "Alive" : "Dead");
    }
}
