package com.example.pingpongstage2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.util.Optional;

public class PingPongGame extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private int racketWidth = 10;
    private int racketHeight = 80;
    private int ballSpeed = 5;
    private int scoreLimit = 10;

    public String player1Name = "Player 1";
    public String player2Name = "Player 2";

    private Rectangle racket1;
    private Rectangle racket2;
    private Circle ball;

    private Label scorePlayer1;
    private Label scorePlayer2;

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu menuSettings = new Menu("Settings");
        MenuItem menuItemPlayerNames = new MenuItem("Select Player Names");
        MenuItem menuItemBallSpeed = new MenuItem("Set Ball Speed");
        MenuItem menuItemRacketDimensions = new MenuItem("Adjust Racket Dimensions");
        MenuItem menuItemScoreLimit = new MenuItem("Set Score Limit");
        MenuItem menuItemExit = new MenuItem("Exit");
        menuSettings.getItems().addAll(menuItemPlayerNames, menuItemBallSpeed, menuItemRacketDimensions, menuItemScoreLimit, menuItemExit);
        menuBar.getMenus().add(menuSettings);

        menuItemPlayerNames.setOnAction(e -> selectPlayerNames());
        menuItemBallSpeed.setOnAction(e -> setBallSpeed());
        menuItemRacketDimensions.setOnAction(e -> adjustRacketDimensions());
        menuItemScoreLimit.setOnAction(e -> setScoreLimit());
        menuItemExit.setOnAction(e -> System.exit(0));

        scorePlayer1 = new Label(player1Name + " Score: 0");
        scorePlayer2 = new Label(player2Name + " Score: 0");
        HBox scorePane = new HBox(10, scorePlayer1, scorePlayer2);
        scorePane.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setBottom(scorePane);

        Pane gamePane = new Pane();
        root.setCenter(gamePane);

        racket1 = new Rectangle(20, (WINDOW_HEIGHT - racketHeight) / 2, racketWidth, racketHeight);
        racket2 = new Rectangle(WINDOW_WIDTH - 30 - racketWidth, (WINDOW_HEIGHT - racketHeight) / 2, racketWidth, racketHeight);
        ball = new Circle(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 10, Color.BLACK);
        racket1.setFill(Color.BLUE);
        racket2.setFill(Color.RED);
        gamePane.getChildren().addAll(racket1, racket2, ball);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ping Pong Game");

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    racket2.setY(Math.max(racket2.getY() - 10, 0));
                    break;
                case DOWN:
                    racket2.setY(Math.min(racket2.getY() + 10, WINDOW_HEIGHT - racketHeight));
                    break;
                case W:
                    racket1.setY(Math.max(racket1.getY() - 10, 0));
                    break;
                case S:
                    racket1.setY(Math.min(racket1.getY() + 10, WINDOW_HEIGHT - racketHeight));
                    break;
            }
        });

        Game game = new Game(scoreLimit);
        BallManager ballManager = new BallManager(ball, game, racket1, racket2, this);
        Thread thread = new Thread(ballManager);
        thread.start();

        primaryStage.show();
    }

    public void updateScores(int scorePlayer1, int scorePlayer2) {
        Platform.runLater(() -> {
            this.scorePlayer1.setText(player1Name + " Score: " + scorePlayer1);
            this.scorePlayer2.setText(player2Name + " Score: " + scorePlayer2);
        });
    }

    public void displayGoalMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Goal Scored!");
            alert.setHeaderText(null);
            alert.setContentText(message);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> alert.hide());
            alert.show();
            delay.play();
        });
    }

    private void selectPlayerNames() {
        TextInputDialog dialog = new TextInputDialog(player1Name);
        dialog.setTitle("Player Names");
        dialog.setHeaderText("Set Player Names");
        dialog.setContentText("Enter Player 1 Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            player1Name = name;
            scorePlayer1.setText(player1Name + " Score: 0");
        });

        dialog.setContentText("Enter Player 2 Name:");
        result = dialog.showAndWait();
        result.ifPresent(name -> {
            player2Name = name;
            scorePlayer2.setText(player2Name + " Score: 0");
        });
    }

    private void setBallSpeed() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(ballSpeed));
        dialog.setTitle("Ball Speed");
        dialog.setHeaderText("Set Ball Speed");
        dialog.setContentText("Enter ball speed:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(speed -> ballSpeed = Integer.parseInt(speed));
    }

    private void adjustRacketDimensions() {
        TextInputDialog dialog = new TextInputDialog(racketWidth + "x" + racketHeight);
        dialog.setTitle("Racket Dimensions");
        dialog.setHeaderText("Adjust Racket Dimensions");
        dialog.setContentText("Enter width x height:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(dimensions -> {
            String[] parts = dimensions.split("x");
            racketWidth = Integer.parseInt(parts[0]);
            racketHeight = Integer.parseInt(parts[1]);
            updateRacketDimensions();
        });
    }

    private void updateRacketDimensions() {
        racket1.setWidth(racketWidth);
        racket1.setHeight(racketHeight);

        racket2.setWidth(racketWidth);
        racket2.setHeight(racketHeight);

        racket1.setY((WINDOW_HEIGHT - racketHeight) / 2);
        racket2.setY((WINDOW_HEIGHT - racketHeight) / 2);
    }


    private void setScoreLimit() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(scoreLimit));
        dialog.setTitle("Score Limit");
        dialog.setHeaderText("Set Score Limit");
        dialog.setContentText("Enter score limit:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(limit -> scoreLimit = Integer.parseInt(limit));
    }

    private void setSpeedIncreaseFrequency() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(scoreLimit));
        dialog.setTitle("Speed Increase Frequency");
        dialog.setHeaderText("Set Speed Increase Frequency");
        dialog.setContentText("Enter frequency after how many bounces:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(frequency -> scoreLimit = Integer.parseInt(frequency));
    }

    public static void main(String[] args) {
        launch(args);
    }
}