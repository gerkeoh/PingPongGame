package com.example.pingpongstage2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private int ballSpeed;

    public Ball(double centerX, double centerY, double radius, Color color, int initialSpeed) {
        super(centerX, centerY, radius, color);
        this.ballSpeed = initialSpeed;
    }
}
