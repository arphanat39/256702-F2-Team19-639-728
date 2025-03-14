package com.rabbitgame;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class GoldCoin {
    private Circle view;
    private boolean collected = false;
    
    public GoldCoin(double x, double y) {
        view = new Circle(10, Color.GOLD);
        view.setCenterX(x);
        view.setCenterY(y);
    }
    
    public Circle getView() {
        return view;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void collect() {
        collected = true;
        view.setVisible(false);
    }
    
    public javafx.geometry.Bounds getBounds() {
        return view.getBoundsInParent();
    }
    
    public double getX() {
        return view.getCenterX();
    }
    
    public double getY() {
        return view.getCenterY();
    }
}