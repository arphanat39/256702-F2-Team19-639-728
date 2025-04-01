package com.rabbithop.entities;

/**
 * Toxic berry bushes that damage the player
 */
public class ToxicBush extends GameObject {
    
    // Track if rabbit already collided with this bush
    private boolean hitByRabbit = false;
    
    // Cooldown before rabbit can be hit again by the same bush
    private double hitCooldown = 0;
    private static final double HIT_COOLDOWN_DURATION = 3.0; // seconds

    public ToxicBush(double x, double y) {
        super(x, y, 48, 48, "/images/toxic_bush.png");
    }

    @Override
    public void update(double deltaTime) {
        // Update cooldown if rabbit has hit this bush
        if (hitByRabbit) {
            hitCooldown -= deltaTime;
            if (hitCooldown <= 0) {
                hitByRabbit = false;
            }
        }
    }
    
    /**
     * Check if rabbit can be damaged by this bush
     * @return true if the bush can damage the rabbit
     */
    public boolean canDamageRabbit() {
        return !hitByRabbit;
    }
    
    /**
     * Mark that the rabbit has hit this bush
     */
    public void hitByRabbit() {
        hitByRabbit = true;
        hitCooldown = HIT_COOLDOWN_DURATION;
    }
}