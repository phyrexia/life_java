package vicPackage;

public class LifeAutoThread extends Thread {

	// this entire class could be implemented with a Timer and TierActivity or
	// wtfever it's called

	private LifeBoard gb;
	private LifeMainGUI gui;

	public LifeAutoThread(LifeBoard gb, LifeMainGUI gui) {
		this.gb = gb;
		this.gui = gui;
	}

	@Override
	public void run() {

		while (gb.getAuto()) { // while on automode
			gb.step(); // step
			if (gui != null) // in case we're consoling
				gui.updateCounter();
			try {
				Thread.sleep(1000 / gb.getFrequency());
				// gb.getFrequency() updates per second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
