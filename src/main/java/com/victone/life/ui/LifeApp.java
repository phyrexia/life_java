package com.victone.life.ui;

import com.victone.life.logic.Life;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LifeApp extends Application {

    private static final String LABEL = "The Game of Life (implemented by Victor Wilson)";
    private static final String VERSION = "2.0 beta 1";
    private static final String HELP_STRING = "Jim Conway's Game of Life\nimplemented by Victor Wilson Thanksgiving 2013\n\n"
            + "Any live cell with 2 or 3 neighbors lives. Otherwise, it dies.\n"
            + "Any dead cell with exactly three neighbors springs forth.";

    private static final int DEFAULT_FRAMEWIDTH = 1024, DEFAULT_FRAMEHEIGHT = 768;

    private Life gameBoard;

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle(LABEL + " version " + VERSION);

        Canvas lifeCanvas = new Canvas(1024, 700);
        GraphicsContext gc = lifeCanvas.getGraphicsContext2D();
        //gc.setFill(Color.DARKGOLDENROD);
        //gc.fillRect(0,0,1024,700);

        Button helpButton, clearButton, stepButton, randomizeButton, slowerButton, fasterButton, zoomInButton,
                zoomOutButton;

        helpButton = new Button("Help");
        helpButton.setTooltip(new Tooltip("You should probably just click this button instead of hovering over it."));
        helpButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Stage dialog = new Stage();
                dialog.setResizable(false);
                dialog.setHeight(140);
                dialog.setWidth(450);
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.initModality(Modality.APPLICATION_MODAL);

                Button ok = new Button("OK");
                ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        Node source = (Node)  t.getSource();
                        Stage stage  = (Stage) source.getScene().getWindow();
                        stage.close();
                    }
                });

                HBox hb = new HBox();
                hb.getChildren().add(ok);
                hb.setAlignment(Pos.CENTER);
                hb.setPadding(new Insets(10, 10, 10, 10));

                Text text = new Text(25, 25, HELP_STRING);
                text.setTextAlignment(TextAlignment.CENTER);

                BorderPane bp = new BorderPane();
                bp.setCenter(text);
                bp.setBottom(hb);
                Scene scene = new Scene(bp);
                dialog.setScene(scene);
                dialog.show();
            }
        });

        clearButton = new Button("Clear");
        clearButton.setTooltip(new Tooltip("Clear the board of all life."));

        stepButton = new Button("Step");
        stepButton.setTooltip(new Tooltip("Move the simulation forward by one step."));

        randomizeButton = new Button("Randomize");
        randomizeButton.setTooltip(new Tooltip("Randomize the board."));

        slowerButton = new Button("Slower");
        slowerButton.setTooltip(new Tooltip("Decrease the speed in Automatic Mode."));
        //slowerButton.setDisable(true);

        fasterButton = new Button("Faster");
        fasterButton.setTooltip(new Tooltip("Increase the speed in Automatic Mode."));
        //fasterButton.setDisable(true);

        zoomInButton = new Button("Zoom In");
        zoomInButton.setTooltip(new Tooltip("Zoom in, thus decreasing the number of cells visible."));

        zoomOutButton = new Button("Zoom Out");
        zoomOutButton.setTooltip(new Tooltip("Zoom out, thus increasing the number of cells visible."));

        Label frequencyLabel = new Label("2 FPS");

        CheckBox toroidalCheckBox, autoCheckBox;
        autoCheckBox = new CheckBox("Auto-Mode");
        autoCheckBox.setTooltip(new Tooltip("Checking this box will automatically step the game forward at the specified frequency."));
        toroidalCheckBox = new CheckBox("Toroidal");
        toroidalCheckBox.setTooltip(new Tooltip("Checking this box will cause the field behave as though it was on the surface of a torus."));

        HBox controlBox = new HBox(10);
        controlBox.setPadding(new Insets(10, 10, 10, 10));
        controlBox.setMaxHeight(60);
        controlBox.setMinHeight(40);
        controlBox.getChildren().addAll(helpButton, clearButton, randomizeButton, stepButton, autoCheckBox, slowerButton,
                frequencyLabel, fasterButton, toroidalCheckBox, zoomInButton, zoomOutButton);
        controlBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(lifeCanvas);
        root.setBottom(controlBox);

        Scene scene = new Scene(root, DEFAULT_FRAMEWIDTH, DEFAULT_FRAMEHEIGHT);
        //scene.getStylesheets().add("style.css");
        /*stage.titleProperty().bind(
                scene.widthProperty().asString().
                        concat(" : ").
                        concat(scene.heightProperty().asString()));
        */
        stage.setScene(scene);

        stage.show();
    }

    private void drawLifeBoard() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
