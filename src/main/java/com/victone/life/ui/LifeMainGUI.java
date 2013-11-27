package com.victone.life.ui;

import com.victone.life.logic.LifeBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionListener;

@Deprecated
public class LifeMainGUI extends JFrame implements ActionListener {

	private static final String LABEL = "Jim Conway's The Game of Life (implemented by Victor Wilson)";

	private static final String VERSION = "2.0b1";
	private static final int FRAMEWIDTH = 1000, FRAMEHEIGHT = 680;

	private static final String HELP_STRING = "Jim Conway's Game of Life\nWritten by Victor Wilson Sep 8-13 2012\n\n"
			+ "Any live cell with 2 or 3 neighbors lives. Otherwise, it dies.\n"
			+ "Any dead cell with exactly three neighbors springs forth.";

	private LifeBoard gameBoard;

	private JLabel generationsLabel, frequencyLabel;

    private JButton stepButton, clearButton, autoButton, randomButton,
			fastButton, slowButton, helpButton, newButton;

    private JPanel gamePanel, controlPanel;
    private int frequencyInHertz;

    public LifeMainGUI(LifeBoard gb) {
		super(LABEL + " version " + VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(FRAMEWIDTH, FRAMEHEIGHT);

		gameBoard = gb;

		buildGUI();
		setVisible(true);
	}

	public void buildGUI() {
		gamePanel = new JPanel(new GridLayout(gameBoard.getHeight(),
				gameBoard.getWidth()));
		controlPanel = new JPanel(new FlowLayout());

		generationsLabel = new JLabel("Generation: 0");
		frequencyLabel = new JLabel("Frequency: " + frequencyInHertz + " per second.");

		initButtonBoard();

		populateGamePanel();

		initControlButtons();

		addControlListeners();

		addGameBoardListeners();

		populateControlPanel();

		// this is last
		add(gamePanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
	}

	private void populateGamePanel() {
		// add buttonBoard to the panel
		for (int i = 0; i < gameBoard.getHeight(); i++) {
			for (int j = 0; j < gameBoard.getWidth(); j++) {
			//	gamePanel.add(buttonBoard[i][j]);
			}
		}
	}

	private void initControlButtons() {
		// newButton = new JButton("New");
		// newButton
		// .setToolTipText("Create a new Game Board with different dimensions");
		// newButton.setEnabled(false); // enable this eventually

		helpButton = new JButton("Help");
		helpButton.setToolTipText("Instructions & Information");

		clearButton = new JButton("Clear");
		clearButton
				.setToolTipText("Clear the simulation and reset the generation count");

		stepButton = new JButton("Step");
		stepButton.setToolTipText("Advance the simulation by one step");

		autoButton = new JButton("Auto");
		autoButton.setToolTipText("Automatically advance the simulation");

		randomButton = new JButton("Randomize");
		randomButton.setToolTipText("Randomly populate the simulation and reset the generation count");

		fastButton = new JButton("Faster");
		fastButton.setToolTipText("Increase the speed of the simulation");
		//fastButton.setEnabled(false);

		slowButton = new JButton("Slower");
		slowButton.setToolTipText("Decrease the speed of the simulation");
		//slowButton.setEnabled(false);
	}

	private void addControlListeners() {
		// newButton.addActionListener(this);
		helpButton.	 addActionListener(this);
		clearButton. addActionListener(this);
		stepButton.  addActionListener(this);
		autoButton.  addActionListener(this);
		randomButton.addActionListener(this);
		fastButton.  addActionListener(this);
		slowButton.  addActionListener(this);
	}

	private void populateControlPanel() {
		// controlPanel.add(newButton);
		controlPanel.add(helpButton);
		controlPanel.add(clearButton);
		controlPanel.add(stepButton);
		controlPanel.add(randomButton);
		controlPanel.add(autoButton);
		controlPanel.add(generationsLabel);
		controlPanel.add(slowButton);
		controlPanel.add(frequencyLabel);
		controlPanel.add(fastButton);
	}

	private void addGameBoardListeners() {
		for (int i = 0; i < gameBoard.getHeight(); i++) {
			for (int j = 0; j < gameBoard.getWidth(); j++) {
				//buttonBoard[i][j].addActionListener(this);
				//buttonBoard[i][j].addMouseMotionListener(this);
			}
		}
	}
	
	private void initButtonBoard() {
		int height = gameBoard.getHeight(), width = gameBoard.getWidth();
		//buttonBoard = new JButton[height][width];
		for (int i = 0; i < height; i++) {
		//	for (int j = 0; j < width; j++) //take button from cell
				//buttonBoard[i][j] = gameBoard.getCell(j, i).getButton();
		}
	}

	public void updateCounter() {
		//generationsLabel.setText("Generation: " + gameBoard.getGenerations());
	}
	
	public void updateFrequency() {
		//frequencyLabel.setText("Frequency: " + gameBoard.getFrequency());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		if (command.equals("New")) {
			// implement me
		} else if (command.equals("Help")) {
			help();
		} else if (command.equals("Clear")) {
			clear();
		} else if (command.equals("Step")) {
			step();
		} else if (command.equals("Randomize")) {
			randomize();
		} else if (command.equals("Auto") || command.equals("Stop")) {
			auto();
		} else if (command.equals("Faster")) {
			faster();
		} else if (command.equals("Slower")) {
			slower();
		} else {
			// parse button name into x and y coordinates
			String split[] = command.split("[.]");
			//gameBoard.click(Integer.parseInt(split[0]),
			//		Integer.parseInt(split[1]));
		}
	}

	private void help() {
		JOptionPane.showMessageDialog(null, HELP_STRING, "Help",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void clear() {
		//if (gameBoard.getAuto()) {
		//	auto();
		//}
		gameBoard.clearBoard();
		updateCounter();
	}

	private void step() {
		gameBoard.step();
		updateCounter();
	}

	private void randomize() {
		//gameBoard.randomize();
		updateCounter();
	}

	private void auto() { // this required threading to work
		//if (!gameBoard.getAuto()) {
			autoButton.setText("Stop");
			autoButton
			.setToolTipText("Stop automatic advancing of the simulation");
			stepButton.setEnabled(false);
			//fastButton.setEnabled(true);
			//slowButton.setEnabled(true);
	//		gameBoard.auto();
	//	} else {
			autoButton.setText("Auto");
			autoButton.setToolTipText("Automatically advance the simulation");
			stepButton.setEnabled(true);
			//fastButton.setEnabled(false);
			//slowButton.setEnabled(false);
	//		gameBoard.auto();
		}
	//}

	private void faster() {
		//gameBoard.incrementFrequency();
		//updateFrequency();
	}

	private void slower() {
		//gameBoard.decrementFrequency();
		//updateFrequency();
	}

//		@SuppressWarnings("unused")
//	private void cycle(JButton jb) { // not used so far
//		String split[] = jb.getActionCommand().split("[.]");
//		gameBoard.cycle(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
//	}

//	@Override
//	public void mouseMoved(MouseEvent arg0) { // not used
//		if (arg0.getComponent() instanceof JButton) {
//
//		}
//	}

//	@Override
//	public void mouseDragged(MouseEvent arg0) { // doesn't work
//		/*
//		 * // if (arg0.getComponent() instanceof JButton) { // if
//		 * (previousMouseEvent == null) { //initial case // previousMouseEvent =
//		 * arg0; // JButton jb = (JButton) previousMouseEvent.getComponent(); //
//		 * String[] s = jb.getActionCommand().split("[.]"); //
//		 * gameBoard.cycle(Integer.parseInt(s[0]), Integer.parseInt(s[1])); // }
//		 * else { // subsequent cases // JButton jb = (JButton)
//		 * previousMouseEvent.getComponent(); // JButton argjb = (JButton)
//		 * arg0.getComponent(); // if(
//		 * !jb.getActionCommand().equals(argjb.getActionCommand())) { //
//		 * String[] s = jb.getActionCommand().split("[.]"); //
//		 * gameBoard.cycle(Integer.parseInt(s[0]), Integer.parseInt(s[1])); // }
//		 * else { // // } // // // } // previousMouseEvent = arg0; // JButton jb
//		 * = (JButton) arg0.getComponent(); // String[] s =
//		 * jb.getActionCommand().split("[.]"); //
//		 * gameBoard.cycle(Integer.parseInt(s[0]), Integer.parseInt(s[1])); // }
//		 * 
//		 * // String origin = (JButton) arg0.getComponent().getActionCommand();
//		 * // System.out.println("bloop"); // String command = (JButton)
//		 * arg0.getComponent() // if
//		 */
//	}

    public static void main(String... args) {

    }
}
