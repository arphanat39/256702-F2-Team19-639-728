package com.rabbitgame;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Platform {
    private Rectangle view;
    
    public Platform(double x, double y) {
        view = new Rectangle(100, 20, Color.GRAY);
        view.setX(x);
        view.setY(y);
        view.setArcWidth(15);
        view.setArcHeight(15);
    }
    
    public Rectangle getView() {
        return view;
    }
    
    public double getX() {
        return view.getX();
    }
    
    public double getY() {
        return view.getY();
    }
    
    public double getWidth() {
        return view.getWidth();
    }
    
    public double getHeight() {
        return view.getHeight();
    }
    
    public javafx.geometry.Bounds getBounds() {
        return view.getBoundsInParent();
    }
}