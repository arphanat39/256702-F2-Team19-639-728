package com.rabbitgame;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Rabbit {
    private Group view;
    private Rectangle body;
    private Circle eye;
    private double x, y;
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean canJump = false;
    private boolean doubleJumpAvailable = true;
    private int coins = 0;
    private int hp = 10;
    private double speed = 5;
    private double jumpForce = -15;
    private boolean facingRight = true;
    private static final double GRAVITY = 0.5;
    private static final double MAX_FALL_SPEED = 10;
    
    public Rabbit() {
        // Create rabbit body
        body = new Rectangle(40, 60, Color.BROWN);
        
        // Create rabbit eye
        eye = new Circle(4, Color.WHITE);
        eye.setTranslateX(25);
        eye.setTranslateY(15);
        
        // Group all parts
        view = new Group(body, eye);
        
        x = 100;
        y = 100;
        updatePosition();
    }
    
    public void update() {
        if (!canJump) {
            velocityY += GRAVITY;
            
            // Limit fall speed
            if (velocityY > MAX_FALL_SPEED) {
                velocityY = MAX_FALL_SPEED;
            }
        }
        
        x += velocityX;
        y += velocityY;
        
        updatePosition();
    }
    
    private void updatePosition() {
        view.setTranslateX(x);
        view.setTranslateY(y);
        
        // Update eye position based on direction
        if (facingRight) {
            eye.setTranslateX(25);
        } else {
            eye.setTranslateX(10);
        }
    }
    
    public void moveLeft() {
        velocityX = -speed;
        facingRight = false;
    }
    
    public void moveRight() {
        velocityX = speed;
        facingRight = true;
    }
    
    public void stop() {
        velocityX = 0;
    }
    
    public void jump() {
        if (canJump) {
            velocityY = jumpForce;
            canJump = false;
            doubleJumpAvailable = true;
        } else if (doubleJumpAvailable) {
            velocityY = jumpForce * 0.7;
            doubleJumpAvailable = false;
        }
    }
    
    public void setOnPlatform(boolean onPlatform) {
        canJump = onPlatform;
        if (onPlatform) {
            velocityY = 0;
        }
    }
    
    public Group getView() {
        return view;
    }
    
    public void collectCoin() {
        coins++;
        System.out.println("Coins collected: " + coins);
    }
    
    public void takeDamage() {
        hp--;
        System.out.println("HP: " + hp);
    }
    
    // Getters and setters
    public int getCoins() { return coins; }
    public int getHp() { return hp; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return body.getWidth(); }
    public double getHeight() { return body.getHeight(); }
    public javafx.geometry.Bounds getBounds() { return body.getBoundsInParent(); }
    public double getVelocityY() { return velocityY; }
    
    public void setX(double x) { 
        this.x = x; 
        updatePosition();
    }
    
    public void setY(double y) { 
        this.y = y; 
        updatePosition();
    }
    
    public void setCoins(int coins) { this.coins = coins; }
}