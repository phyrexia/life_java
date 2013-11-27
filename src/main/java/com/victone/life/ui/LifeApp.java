package com.victone.life.ui;

import com.victone.life.logic.LifeBoard;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LifeApp extends Application {

    private static final String LABEL = "Jim Conway's The Game of Life (implemented by Victor Wilson)";

    private static final String VERSION = "2.0b1";
    private static final int FRAMEWIDTH = 1000, FRAMEHEIGHT = 680;

    private static final String HELP_STRING = "Jim Conway's Game of Life\nWritten by Victor Wilson Sep 8-13 2012\n\n"
            + "Any live cell with 2 or 3 neighbors lives. Otherwise, it dies.\n"
            + "Any dead cell with exactly three neighbors springs forth.";

    private LifeBoard gameBoard;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle(LABEL + " version " + VERSION);
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, FRAMEWIDTH, FRAMEHEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
