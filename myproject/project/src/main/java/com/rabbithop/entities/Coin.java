package com.rabbithop.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Collectible coins
 */
public class Coin extends GameObject {
    
    private boolean collected = false;
    
    // Animation
    private static final int FRAME_COUNT = 6; // Number of spinning animation frames
    private static final double FRAME_DURATION = 0.1; // Duration of each frame in seconds
    private Image[] frames;
    private int currentFrame = 0;
    private double frameTimer = 0;

    public Coin(double x, double y) {
        super(x, y, 32, 32, "/images/coin_0.png");
        
        // Load animation frames
        frames = new Image[FRAME_COUNT];
        try {
            for (int i = 0; i < FRAME_COUNT; i++) {
                frames[i] = new Image(getClass().getResourceAsStream("/images/coin_" + i + ".png"));
            }
        } catch (Exception e) {
            System.out.println("Could not load coin animation frames: " + e.getMessage());
        }
    }

    @Override
    public void update(double deltaTime) {
        if (collected) {
            return;
        }
        
        // Animate the spinning coin
        frameTimer += deltaTime;
        if (frameTimer >= FRAME_DURATION) {
            frameTimer = 0;
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (collected) {
            return;
        }
        
        if (frames != null && frames[currentFrame] != null) {
            gc.drawImage(frames[currentFrame], x, y, width, height);
        } else {
            super.render(gc);
        }
    }
    
    /**
     * Mark the coin as collected
     */
    public void collect() {
        collected = true;
    }
    
    /**
     * Check if the coin has been collected
     * @return true if collected
     */
    public boolean isCollected() {
        return collected;
    }
}