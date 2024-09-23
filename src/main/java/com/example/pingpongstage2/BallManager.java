package com.example.pingpongstage2;

import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BallManager implements Runnable {
    private final Circle ball;
    private final Game game;
    private final Rectangle racket1, racket2;
    private final PingPongGame gameController;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private double dx = 1;
    private double dy = 1;

    public BallManager(Circle ball, Game game, Rectangle racket1, Rectangle racket2, PingPongGame gameController) {
        this.ball = ball;
        this.game = game;
        this.racket1 = racket1;
        this.racket2 = racket2;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        while (!game.isGameOver()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Platform.runLater(() -> {
                double nextX = ball.getCenterX() + dx;
                double nextY = ball.getCenterY() + dy;

                if (nextY - ball.getRadius() <= 0 || nextY + ball.getRadius() >= WINDOW_HEIGHT) {
                    dy = -dy;
                }

                if (nextX - ball.getRadius() <= racket1.getX() + racket1.getWidth() && nextY >= racket1.getY() && nextY <= racket1.getY() + racket1.getHeight()
                        || nextX + ball.getRadius() >= racket2.getX() && nextY >= racket2.getY() && nextY <= racket2.getY() + racket2.getHeight()) {
                    dx = -dx;
                }

                ball.setCenterX(ball.getCenterX() + dx);
                ball.setCenterY(ball.getCenterY() + dy);

                if (ball.getCenterX() - ball.getRadius() <= 0) {
                    game.player2Scores();
                    gameController.updateScores(game.getScorePlayer1(), game.getScorePlayer2());
                    gameController.displayGoalMessage("GOAL " + gameController.player2Name + "scored!");
                    resetBall();
                } else if (ball.getCenterX() + ball.getRadius() >= WINDOW_WIDTH) {
                    game.player1Scores();
                    gameController.updateScores(game.getScorePlayer1(), game.getScorePlayer2());
                    gameController.displayGoalMessage("GOAL " + gameController.player1Name + "scored!");
                    resetBall();
                }
            });
        }
    }

    private void resetBall() {
        ball.setCenterX(WINDOW_WIDTH / 2);
        ball.setCenterY(WINDOW_HEIGHT / 2);
        dx = Math.random() < 0.5 ? 1 : -1;
        dy = Math.random() < 0.5 ? 1 : -1;
    }
}