package com.example.pingpongstage2;

public class Game {
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private final int scoreLimit;

    public Game(int scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    public void player1Scores() {
        scorePlayer1++;
    }

    public void player2Scores() {
        scorePlayer2++;
    }

    public boolean isGameOver() {
        return scorePlayer1 >= scoreLimit || scorePlayer2 >= scoreLimit;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

}
