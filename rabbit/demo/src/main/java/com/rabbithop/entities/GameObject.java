package com.rabbithop.entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Base class for all game objects (entities)
 */
public abstract class GameObject {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected Image sprite;
    
    public GameObject(double x, double y, double width, double height, String spritePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        try {
            this.sprite = new Image(getClass().getResourceAsStream(spritePath));
        } catch (Exception e) {
            System.out.println("Could not load sprite: " + spritePath + " - " + e.getMessage());
        }
    }
    
    /**
     * Update the object state
     * @param deltaTime Time passed since last update
     */
    public abstract void update(double deltaTime);
    
    /**
     * Render the object
     * @param gc Graphics context to draw on
     */
    public void render(GraphicsContext gc) {
        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        }
    }
    
    /**
     * Get the collision boundary
     * @return Rectangle2D representing the collision boundary
     */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }
    
    /**
     * Check if this object intersects with another
     * @param other The other game object
     * @return true if they intersect
     */
    public boolean intersects(GameObject other) {
        return other.getBoundary().intersects(this.getBoundary());
    }
    
    // Getters and setters
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
}