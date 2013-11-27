package com.victone.life.concurrency;

import com.victone.life.logic.LifeBoard;

import java.util.TimerTask;

public class AutoStepTask extends TimerTask {

	private LifeBoard gb;

	public AutoStepTask(LifeBoard gb) {
		this.gb = gb;
	}

	@Override
	public void run() {
			gb.step();
	}
}
