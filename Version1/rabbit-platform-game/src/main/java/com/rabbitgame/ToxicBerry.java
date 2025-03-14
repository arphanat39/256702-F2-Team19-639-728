package com.rabbitgame;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Group;

public class ToxicBerry {
    private Group view;
    private Circle berry;
    private Circle leaf;
    private boolean active = true;
    
    public ToxicBerry(double x, double y) {
        view = new Group();
        
        // Create the berry
        berry = new Circle(8, Color.PURPLE);
        
        // Create a leaf
        leaf = new Circle(4, Color.GREEN);
        leaf.setCenterX(5);
        leaf.setCenterY(-5);
        
        view.getChildren().addAll(berry, leaf);
        view.setTranslateX(x);
        view.setTranslateY(y);
    }
    
    public Group getView() {
        return view;
    }
    
    public void setVisible(boolean visible) {
        view.setVisible(visible);
        active = visible;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public javafx.geometry.Bounds getBounds() {
        return berry.getBoundsInParent();
    }
    
    public double getX() {
        return view.getTranslateX();
    }
    
    public double getY() {
        return view.getTranslateY();
    }
}