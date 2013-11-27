package com.victone.life.concurrency;

import javafx.scene.control.Button;

import java.util.TimerTask;

@SuppressWarnings("Unused")
public class AutoStepTask extends TimerTask {

    private Button stepButton;

    public AutoStepTask(Button stepButton) {
        this.stepButton = stepButton;
    }

    @Override
    public void run() {
        stepButton.fire();
    }
}
