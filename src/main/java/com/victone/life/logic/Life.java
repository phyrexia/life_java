package com.victone.life.logic;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class Life {

    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private static final int defaultBoardWidth = 50;
    private static int defaultBoardHeight = 20;

    private Stack<boolean[][]> stack;

    private int width, height;
    private boolean toroidal;
    private int generation;

    public static boolean[][] copyOf(boolean[][] source) {
        boolean[][] dest = new boolean[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return dest;
    }

    public Life() {
        this(defaultBoardWidth, defaultBoardHeight, false);
    }

    public Life(int width, int height) {
        this(width, height, false);
    }

    public Life(boolean toroidal) {
        this(defaultBoardWidth, defaultBoardHeight, toroidal);
    }

    public Life(int width, int height, boolean toroidal) { //v2
        this.width = width;
        this.height = height;
        this.toroidal = toroidal;

        stack = new Stack<>();
        generation = 0;

        boolean[][] lifeBoard = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                lifeBoard[y][x] = DEAD;
            }
        }
        stack.push(lifeBoard);
    }

    public boolean isToroidal() {
        return toroidal;
    }

    public void setToroidal(boolean toroidal) {
        this.toroidal = toroidal;
    }

    public Stack<boolean[][]> getStack() {
        return stack;
    }

    private int getGeneration() {
        return generation;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean cycleCell(int xCoord, int yCoord) {  //v2b1
        //boolean[][] newBoard = copyOf(stack.peek());
        //stack.push(newBoard);
        return stack.peek()[yCoord][xCoord] = !stack.peek()[yCoord][xCoord];
    }

    public boolean getCell(int xCoord, int yCoord) { //v2
        if (isCellCoordinateValid(xCoord, yCoord)) {
            return stack.peek()[yCoord][xCoord];
        } else {
            throw new IllegalArgumentException("Invalid cell coordinate, (" + xCoord + ", " + yCoord + ")!");
        }
    }

    private boolean isCellCoordinateValid(int xCoord, int yCoord) {  //v2b1 creation
        return xCoord >= 0 && yCoord >= 0 && xCoord < width && yCoord < height;
    }

    public void step() { //v2b1
        boolean[][] newBoard = new boolean[height][width];

        int numberNeighbors;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                numberNeighbors = getNumberNeighbors(x, y);
                if (stack.peek()[y][x]) { //alive
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
        stack.push(newBoard);
        generation++;
    }

    public void step(int numSteps) { //v2 unchanged
        for (int i = 1; i <= numSteps; i++) {
            step();
        }
    }

    public int getNumberNeighbors(int xCoord, int yCoord) {  //v2 approved (unfinished)
        int numNeighbors = 0;
        if (toroidal) {
            for (int neighborYpos = yCoord - 1; neighborYpos <= yCoord + 1; neighborYpos++) {
                for (int neighborXpos = xCoord - 1; neighborXpos <= xCoord + 1; neighborXpos++) {

                    //todo: toroidal neighbor algorithm

                    if (neighborXpos == xCoord && neighborYpos == yCoord)
                        continue; // we skip this cell since it's not a neighbor of itself
                    else if (stack.peek()[neighborYpos][neighborXpos]) {
                        numNeighbors++;
                    }
                }
            }
        } else {
            for (int neighborYpos = yCoord - 1; neighborYpos <= yCoord + 1; neighborYpos++) {
                for (int neighborXpos = xCoord - 1; neighborXpos <= xCoord + 1; neighborXpos++) {
                    if (isCellCoordinateValid(neighborXpos, neighborYpos)) {
                        if (neighborXpos == xCoord && neighborYpos == yCoord)
                            continue; // we skip this cell since it's not a neighbor of itself
                        else if (stack.peek()[neighborYpos][neighborXpos])
                            numNeighbors++;
                    }
                }
            }
        }
        return numNeighbors;
    }

    public void clearStack() {   //v2.0b1
        stack = new Stack<>();
        generation = 0;

        boolean[][] newBoard = new boolean[height][width];
        for (int y = 0; y < defaultBoardHeight; y++) {
            for (int x = 0; x < defaultBoardWidth; x++) {
                newBoard[y][x] = false;
            }
        }
        stack.push(newBoard);
    }

    public void randomize(boolean clearStack) { //v2
        if (clearStack) {
            clearStack();
        }
        // randomly cycles cells
        Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (random.nextBoolean()) {
                    stack.peek()[y][x] = !stack.peek()[y][x];
                }
            }
        }
    }

    public void invert() {  //v2
        //flip dead cells to alive and alive cells to dead
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                stack.peek()[y][x] = !stack.peek()[y][x];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Generation ");
        output.append(stack.size());
        output.append("\n-1");
        // first line
        for (int q = 0; q < width - 1; q++) {
            output.append("-");
        }
        output.append("\n");

        for (int h = 0; h < height; h++) { // go down the board
            for (int w = 0; w < width; w++) { // go across the board
                if (w == 0) {
                    if (h == 0) // first line only
                        output.append("1");
                    else
                        output.append("|");
                }
                if (stack.peek()[h][w]) {
                    output.append("@");
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

    public static void main(String... args) {
        Life life = new Life();
        life.randomize(true);
        while (life.getGeneration() < 1000) {
            System.out.println(life);
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            life.step();
        }
    }

    private static void testGlider(Life life) {
        life.cycleCell(6, 3);
        life.cycleCell(7, 4);
        life.cycleCell(5, 5);
        life.cycleCell(6, 5);
        life.cycleCell(7, 5);
    }
}