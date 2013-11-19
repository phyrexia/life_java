package vicPackage;

import java.util.Scanner;

public class LifeDriver {

	private static LifeBoard gb;
	private static boolean consoleMode = false;

	public static void main(String[] args) {
		if (args.length != 0)
			consoleMode = true; // if args are present we assume no gui
		if (consoleMode) { // no gui window
			try { // get arts
				gb = new LifeBoard(Integer.parseInt(args[0]),
						Integer.parseInt(args[1]), consoleMode);
			} catch (Exception e) {
				gb = new LifeBoard(76, 18, consoleMode); 
				// looks good with standard console dimensions
			}
			while (true) { // this input section is buggy and was really just
							// for testing
				displayMenu();
				Scanner scanner = new Scanner(System.in);
				String input = "";
				while (input.length() == 0) {
					input = scanner.nextLine();
				}
				switch (input.toUpperCase().charAt(0)) {
				case 'C':
					clearBoard();
					break;
				case 'R':
					gb.randomize();
					break;
				case 'Q':
					System.out.println("Thanks for Playing!");
					System.exit(0);
				case 'S':
					gb.step();
					break;
				case 'X':
					clickCellConsole();
				}
			}
		} else { // swing'n'such
			gb = new LifeBoard(LifeBoard.getDefaultBoardWidth(),
					LifeBoard.getDefaultBoardHeight(), consoleMode);
		}
	}

	private static void clearBoard() {
		gb.clearBoard();
	}

	private static void clickCellConsole() { // no error correction
		int x = -1, y = -1;

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter X Coordinate (1 - " + gb.getWidth() + ") ");
		if (scanner.hasNextInt())
			x = scanner.nextInt() - 1;
		
		System.out.print("Enter Y Coordinate (1 - " + gb.getHeight() + ") ");

		if (scanner.hasNextInt())
			y = scanner.nextInt() - 1;

		if (!gb.click(x, y)) {
			System.out.println("Incorrect input.");
			clickCellConsole();
		}
	}

	private static void displayMenu() {
		clearScreen();
		System.out.println("Conway's 'Life', implemented by Victor Wilson");
		System.out.print(gb);
		System.out.println("Generation: " + gb.getGenerations());
		System.out.print("(S) to Step the Board");
		System.out.print("\t(X) to Click a Cell  ");
		System.out.println("(R) to Randomize the Board");
		System.out.print("(C) to Clear the Board  ");
		System.out.println("(Q) to Quit");
		System.out.print("Command: ");
	}

	private static void clearScreen() {
		String osName = System.getProperty("os.name");
		boolean isMac = osName.matches("(?i).*os x*");

		//this doesn't work
//		if (osName.toLowerCase().contains("windows")) {
//			try { 
//				Runtime.getRuntime().exec("cls");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

		if (isMac) { //this is standard unix clear screen code
			System.out.print("\033[H\033[2J");
		}
	}
}
