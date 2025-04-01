package com.rabbithop;
import java.io.Serializable;

/**
 * Class for storing game progress data for save/load functionality
 */
public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L; // For versioning
    
    private int currentLevel;
    private int coins;
    private int health;
    private boolean hasKey;
    private double rabbitSpeed;
    private double jumpHeight;
    
    /**
     * Constructor to create save data object
     * @param currentLevel Current game level
     * @param coins Number of collected coins
     * @param health Player health
     * @param hasKey Whether player has a key
     * @param rabbitSpeed Rabbit movement speed
     * @param jumpHeight Rabbit jump height
     */
    public GameSaveData(int currentLevel, int coins, int health, 
                       boolean hasKey, double rabbitSpeed, double jumpHeight) {
        this.currentLevel = currentLevel;
        this.coins = coins;
        this.health = health;
        this.hasKey = hasKey;
        this.rabbitSpeed = rabbitSpeed;
        this.jumpHeight = jumpHeight;
    }
    
    // Getter methods
    public int getCurrentLevel() { return currentLevel; }
    public int getCoins() { return coins; }
    public int getHealth() { return health; }
    public boolean hasKey() { return hasKey; }
    public double getRabbitSpeed() { return rabbitSpeed; }
    public double getJumpHeight() { return jumpHeight; }
}