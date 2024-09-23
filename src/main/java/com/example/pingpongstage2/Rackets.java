package com.example.pingpongstage2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rackets {
    private Rectangle racket1, racket2;
    private int racketWidth, racketHeight;

    public Rackets(int initialWidth, int initialHeight, Color racket1Color, Color racket2Color, double initialX1, double initialY1, double initialX2, double initialY2) {
        racketWidth = initialWidth;
        racketHeight = initialHeight;
        racket1 = new Rectangle(initialX1, initialY1, racketWidth, racketHeight);
        racket1.setFill(racket1Color);
        racket2 = new Rectangle(initialX2, initialY2, racketWidth, racketHeight);
        racket2.setFill(racket2Color);
    }

}
