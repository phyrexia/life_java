package com.victone.life;

import java.util.Random;
import java.util.Stack;

public class Life {

    private static final int DEFAULT_BOARD_WIDTH = 50;
    private static final int DEFAULT_BOARD_HEIGHT = 20;
    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private Stack<boolean[][]> lifeHistory;

    private int width, height, generation;
    private boolean toroidal;

    public Life() {
        this(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, false);
    }

    public Life(int width, int height) {
        this(width, height, false);
    }

    public Life(boolean toroidal) {
        this(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, toroidal);
    }

    public Life(int width, int height, boolean toroidal) {
        this.width = width;
        this.height = height;
        this.toroidal = toroidal;

        boolean[][] lifeBoard = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                lifeBoard[y][x] = DEAD;
            }
        }

        generation = 0;
        lifeHistory = new Stack<>();
        lifeHistory.push(lifeBoard);
    }

    //expand() and contract() seem to be performance bottlenecks so we will not
    //cleverly combine the two methods at the expense of some if statements
    public void expand() {
        width += 2;
        height += 2;
        boolean[][] bigBoard = new boolean[height][width];
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                bigBoard[y][x] = lifeHistory.peek()[y - 1][x - 1];
            }
        }
        lifeHistory.push(bigBoard);
    }

    public void contract() {
        width -= 2;
        height -= 2;
        boolean[][] littleBoard = new boolean[height][width];
        for (int y = 0; y < height - 2; y++) {
            for (int x = 0; x < width - 2; x++) {
                littleBoard[y][x] = lifeHistory.peek()[y + 1][x + 1];
            }
        }
        lifeHistory.push(littleBoard);
    }

    public void setToroidal(boolean toroidal) {
        this.toroidal = toroidal;
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

    public boolean cycleCell(int xCoord, int yCoord) {
        return lifeHistory.peek()[yCoord][xCoord] = !lifeHistory.peek()[yCoord][xCoord];
    }

    public boolean getCell(int xCoord, int yCoord) {
        if (isCellCoordinateValid(xCoord, yCoord)) {
            return lifeHistory.peek()[yCoord][xCoord];
        } else {
            throw new IllegalArgumentException("Invalid cell coordinate, (" + xCoord + ", " + yCoord + ")!");
        }
    }

    public boolean isCellCoordinateValid(int xCoord, int yCoord) {
        return xCoord >= 0 && yCoord >= 0 && xCoord < width && yCoord < height;
    }

    public void step() {
        boolean[][] newBoard = new boolean[height][width];
        int numberNeighbors;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                numberNeighbors = getNumberNeighbors(x, y);
                if (lifeHistory.peek()[y][x]) { //alive
                    if (numberNeighbors == 3 || numberNeighbors == 2) {
                        newBoard[y][x] = ALIVE;
                    }
                } else { //dead
                    if (numberNeighbors == 3) {
                        newBoard[y][x] = ALIVE; //reproduction
                    }
                }
            }
        }
        lifeHistory.push(newBoard);
        generation++;
    }

    public int getNumberNeighbors(int xCoord, int yCoord) {
        int numNeighbors = 0;
        for (int neighborYpos = yCoord - 1; neighborYpos <= yCoord + 1; neighborYpos++) {
            for (int neighborXpos = xCoord - 1; neighborXpos <= xCoord + 1; neighborXpos++) {
                if (toroidal) {
                    int toroidalYpos = neighborYpos + (neighborYpos < 0 ? height : (neighborYpos >= height ? -height : 0));
                    int toroidalXpos = neighborXpos + (neighborXpos < 0 ? width : (neighborYpos >= width ? -width : 0));

                    if (isCellCoordinateValid(toroidalXpos, toroidalYpos)) {
                        if ((toroidalXpos != xCoord || toroidalYpos != yCoord) && lifeHistory.peek()[toroidalYpos][toroidalXpos])
                            numNeighbors++;
                    }
                } else {
                    if (isCellCoordinateValid(neighborXpos, neighborYpos)) {
                        if ((neighborXpos != xCoord || neighborYpos != yCoord) && lifeHistory.peek()[neighborYpos][neighborXpos]) {
                            numNeighbors++;
                        }
                    }
                }
            }
        }
        return numNeighbors;
    }

    public void extinction() {
        //clears the board and the stack of boards
        lifeHistory = new Stack<>();
        generation = 0;
        boolean[][] newBoard = new boolean[height][width];
        lifeHistory.push(newBoard);
    }

    public void randomize(boolean clearStack) {
        if (clearStack) {
            extinction();
        }
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                lifeHistory.peek()[y][x] = random.nextBoolean();
            }
        }
    }

    public void invert() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                lifeHistory.peek()[y][x] = !lifeHistory.peek()[y][x];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Generation ");
        output.append(lifeHistory.size());
        output.append("\n-1");
        // first line
        for (int q = 0; q < width - 1; q++)
            output.append("-");

        output.append("\n");

        for (int h = 0; h < height; h++) // go down the board
            for (int w = 0; w < width; w++) { // go across the board
                if (w == 0)
                    output.append(h == 0 ? "1" : "|");

                output.append(lifeHistory.peek()[h][w] ? "@" : " " );

                if (w == width - 1)
                    output.append("|");
            }
            output.append("\n");

        for (int q = 0; q <= width + 1; q++)
            output.append("-");

        output.append("\n");
        return output.toString();
    }

    public static void main(String... args) {
        Life life = new Life(true);
        life.randomize(true);
        life.setToroidal(true);
        int fps = 3;
        int frequency = 1000 / fps;

        while (life.getGeneration() < 1000) {
            System.out.println(life);
            try {
                Thread.sleep(frequency);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            life.step();
        }
    }
}