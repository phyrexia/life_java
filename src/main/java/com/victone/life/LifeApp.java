package com.victone.life;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LifeApp extends Application {

    private static final String LABEL = "The Game of Life (implemented by Victor Wilson)";
    private static final String VERSION = "2.0RC1";
    private static final String HELP_STRING = "Jim Conway's Game of Life\nimplemented by Victor Wilson Thanksgiving 2013\n\n"
            + "Any live cell with 2 or 3 neighbors lives. Otherwise, it dies.\n"
            + "Any dead cell with exactly three neighbors springs forth.";

    private static final int DEFAULT_FRAMEWIDTH = 1080, DEFAULT_FRAMEHEIGHT = 768;

    private Life life;

    private Canvas lifeCanvas;
    private GraphicsContext gc;

    private Button helpButton, clearButton, stepButton, randomizeButton, invertButton;
    private Label generationLabel;
    private CheckBox autoCheckBox, toroidalCheckBox;
    private Slider zoomSlider, speedSlider;

    private Timeline timeline;

    private int fps = 2,
            lastClickedX = -1,
            lastClickedY = -1;
    private boolean automatic;

    @Override
    public void start(Stage stage) throws Exception {
        life = new Life(80, 60, false);

        initLifeCanvas();

        gc = lifeCanvas.getGraphicsContext2D();

        initHelpButton();
        initClearButton();
        initStepButton();
        initRandomizeButton();
        initInvertButton();

        initZoomSlider();
        initSpeedSlider();

        Label frequencyLabel = new Label("Speed: ");
        generationLabel = new Label("Gen:  0");
        Label zoomLabel = new Label("Zoom: ");

        initAutoCheckBox();

        initToroidalCheckBox();

        HBox speedBox = getSpeedBox(frequencyLabel);
        HBox zoomBox = getZoomBox(zoomLabel);
        HBox controlBox = getControlBox(speedBox, zoomBox);
        HBox graphicsBox = getGraphicsBox();

        BorderPane root = getRootPane(controlBox, graphicsBox);

        Scene scene = getScene(root);

        stage.setScene(scene);
        stage.setMinWidth(975);
        stage.setMinHeight(405);

        drawGameState();

        stage.setTitle(LABEL + " version " + VERSION);
        stage.show();
    }

    private Scene getScene(BorderPane root) {
        Scene scene = new Scene(root, DEFAULT_FRAMEWIDTH, DEFAULT_FRAMEHEIGHT);

        //change the canvas dimensions when the gui window changes size
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                paintCanvas(Color.WHITE);
                lifeCanvas.setHeight(number2.doubleValue() - 60);
                drawGameState();
            }
        });

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                paintCanvas(Color.WHITE);
                lifeCanvas.setWidth(number2.doubleValue() - 20);
                drawGameState();
            }
        });
        return scene;
    }

    private BorderPane getRootPane(HBox controlBox, HBox graphicsBox) {
        BorderPane root = new BorderPane();
        root.setCenter(graphicsBox);
        root.setBottom(controlBox);
        return root;
    }

    private HBox getGraphicsBox() {
        HBox graphicsBox = new HBox(10);
        graphicsBox.setAlignment(Pos.CENTER);
        graphicsBox.getChildren().add(lifeCanvas);
        return graphicsBox;
    }

    private HBox getControlBox(HBox speedBox, HBox zoomBox) {
        HBox controlBox = new HBox(10);
        controlBox.setPadding(new Insets(10, 10, 10, 10));
        controlBox.setMaxHeight(60);
        controlBox.setMinHeight(40);
        controlBox.getChildren().addAll(helpButton, clearButton, randomizeButton, invertButton, stepButton, zoomBox,
                speedBox, autoCheckBox, toroidalCheckBox, generationLabel);
        controlBox.setAlignment(Pos.CENTER);
        return controlBox;
    }

    private HBox getZoomBox(Label zoomLabel) {
        HBox zoomBox = new HBox();
        zoomBox.setPadding(new Insets(10, 0, 10, 0));
        zoomBox.getChildren().addAll(zoomLabel, zoomSlider);
        return zoomBox;
    }

    private HBox getSpeedBox(Label frequencyLabel) {
        HBox speedBox = new HBox();
        speedBox.setPadding(new Insets(10, 0, 10, 0));
        speedBox.getChildren().addAll(frequencyLabel, speedSlider);
        return speedBox;
    }

    private void initToroidalCheckBox() {
        toroidalCheckBox = new CheckBox("Toroidal");
        toroidalCheckBox.setTooltip(new Tooltip("Checking this box will cause the field behave as though it was on the surface of a torus."));
        toroidalCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                life.setToroidal(new_val);
            }
        });
    }

    private void initAutoCheckBox() {
        autoCheckBox = new CheckBox("Auto");
        autoCheckBox.setTooltip(new Tooltip("Checking this box will automatically step the game forward at the specified speed."));
        autoCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                toggleAutoMode();
            }
        });
    }

    private void initSpeedSlider() {
        speedSlider = new Slider();
        speedSlider.setTooltip(new Tooltip("Drag this slider to change the speed of Auto Mode"));
        speedSlider.setMin(1);
        speedSlider.setMax(30);
        speedSlider.setValue(2);
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (number2.intValue() != fps) {
                    fps = number2.intValue();
                    if (automatic) {
                        updateTimer();
                    }
                    updateLabels();
                }
            }
        });
    }

    private void initZoomSlider() {
        zoomSlider = new Slider();
        zoomSlider.setTooltip(new Tooltip("Drag this slider to zoom in to or out from the board."));
        zoomSlider.setMin(-30);
        zoomSlider.setMax(20);
        zoomSlider.setValue(0);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (number.intValue() > number2.intValue()) {
                    //going down
                    for (int i = number.intValue(); i > number2.intValue(); i--) {
                        life.expand();
                    }
                } else {
                    //going up
                    for (int i = number.intValue(); i < number2.intValue(); i++) {
                        life.contract();
                    }
                }
                updateScreen();
            }
        });
    }

    private void initInvertButton() {
        invertButton = new Button("Invert");
        invertButton.setTooltip(new Tooltip("Kill every living cell and resurrect every dead cell."));
        invertButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                life.invert();
                updateScreen();
            }
        });
    }

    private void initRandomizeButton() {
        randomizeButton = new Button("Randomize");
        randomizeButton.setTooltip(new Tooltip("Randomize the board."));
        randomizeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                life.randomize(true);
                updateScreen();
            }
        });
    }

    private void initStepButton() {
        stepButton = new Button("Step");
        stepButton.setTooltip(new Tooltip("Move the simulation forward by one step."));
        stepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                life.step();
                drawGameState();
                updateLabels();
            }
        });
    }

    private void initClearButton() {
        clearButton = new Button("Clear");
        clearButton.setTooltip(new Tooltip("Clear the board of all life."));
        clearButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                life.extinction();
                if (automatic) {
                    autoCheckBox.fire();
                }
                updateScreen();
            }
        });
    }

    private void initHelpButton() {
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
                        Node source = (Node) t.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
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
    }

    private void initLifeCanvas() {
        lifeCanvas = new Canvas(DEFAULT_FRAMEWIDTH - 20, DEFAULT_FRAMEHEIGHT - 100);
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //we're scaling 0 - x to 0 - y...
                int xCoord = (int) (mouseEvent.getX() / (lifeCanvas.getWidth() / life.getWidth()));
                int yCoord = (int) (mouseEvent.getY() / (lifeCanvas.getHeight() / life.getHeight()));

                if (lastClickedX != xCoord || lastClickedY != yCoord) {
                    if (life.isCellCoordinateValid(xCoord, yCoord)) {
                        life.cycleCell(lastClickedX = xCoord, lastClickedY = yCoord);
                        drawGameState();
                    }
                }
            }
        };

        lifeCanvas.setOnMouseClicked(mouseHandler);
        lifeCanvas.setOnMouseDragEntered(mouseHandler);
        lifeCanvas.setOnMouseDragged(mouseHandler);
        lifeCanvas.setOnMouseDragOver(mouseHandler);
        lifeCanvas.setOnMouseDragExited(mouseHandler);
    }

    private void toggleAutoMode() {
        if (automatic = !automatic) {
            stepButton.setDisable(true);
            //start the timer
            timeline = new Timeline(new KeyFrame(Duration.millis(1000 / fps), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    stepButton.fire();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } else {
            stepButton.setDisable(false);
            //stop the timer
            timeline.stop();
        }
    }

    private void updateTimer() {
        timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.millis(1000 / fps), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stepButton.fire();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateScreen() {
        drawGameState();
        updateLabels();
    }

    private void updateLabels() {
        generationLabel.setText("Gen: " + life.getGeneration());
    }

    private void drawGameState() {
        double cellWidth = lifeCanvas.getWidth() / life.getWidth();
        double cellHeight = lifeCanvas.getHeight() / life.getHeight();

        paintCanvas(Color.WHITE);

        double curX, curY = 0.0;
        for (int y = 0; y < life.getHeight(); y++) {
            curX = 0;
            for (int x = 0; x < life.getWidth(); x++) {
                if (life.getCell(x, y)) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(curX, curY, cellWidth, cellHeight);
                curX += cellWidth;
            }
            curY += cellHeight;
        }
    }

    private void paintCanvas(Color color) {
        gc.setFill(color);
        gc.fillRect(0, 0, lifeCanvas.getWidth(), lifeCanvas.getHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}