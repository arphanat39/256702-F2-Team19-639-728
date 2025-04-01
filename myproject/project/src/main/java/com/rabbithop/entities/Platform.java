package com.rabbithop.entities;

/**
 * Platforms that the rabbit can jump on
 */
public class Platform extends GameObject {
    
    private boolean isGround;

    /**
     * Create a platform
     * @param x X position
     * @param y Y position
     * @param width Width of the platform
     * @param height Height of the platform
     * @param isGround Whether this is the ground platform
     */
    public Platform(double x, double y, double width, double height, boolean isGround) {
        super(x, y, width, height, isGround ? 
              "/images/ground.png" : "/images/platform.png");
        this.isGround = isGround;
    }

    @Override
    public void update(double deltaTime) {
        // Platforms don't need updating
    }
    
    /**
     * Check if this is the ground platform
     * @return true if this is the ground
     */
    public boolean isGround() {
        return isGround;
    }
}