package com.rabbithop.entities;

import com.rabbithop.GameManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The player character (rabbit)
 */
public class Rabbit extends GameObject {
    
    // Movement variables
    private double velocityX = 0;
    private double velocityY = 0;
    private double speed;
    private double jumpHeight ;
    private boolean isJumping = false;
    private boolean isFalling = false;
    private boolean isOnGround = true;
    private boolean facingRight = true;
    
    // Animation
    private static final int FRAME_COUNT = 4; // Number of animation frames
    private static final double FRAME_DURATION = 0.1; // Duration of each frame in seconds
    private Image[] runFrames;
    private Image jumpFrame;
    private Image idleFrame;
    private int currentFrame = 0;
    private double frameTimer = 0;
    
    // Physics constants
    private static final double GRAVITY = 0.5;
    
   
public Rabbit(double x, double y, double speed, double jumpHeight, GameManager gameManager) {
    super(x, y, 64, 64, "/images/rabbit_idle.png");
    this.speed = speed;
    this.jumpHeight = jumpHeight;
    this.gameManager = gameManager;
    
    // Add debug code to verify sprite loading
    System.out.println("Attempting to load rabbit sprite...");
    
    // Load animation frames
    runFrames = new Image[FRAME_COUNT];
    try {
        for (int i = 0; i < FRAME_COUNT; i++) {
            runFrames[i] = new Image(getClass().getResourceAsStream("/images/rabbit_run_" + i + ".png"));
            //System.out.println("Loaded rabbit_run_" + i + ".png");
        }
        jumpFrame = new Image(getClass().getResourceAsStream("/images/rabbit_jump.png"));
        idleFrame = new Image(getClass().getResourceAsStream("/images/rabbit_idle.png"));
        //System.out.println("All rabbit sprites loaded successfully");
    } catch (Exception e) {
        System.out.println("Could not load rabbit animation frames: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    @Override
    public void update(double deltaTime) {
        // Apply gravity
        
        if (!isOnGround) {
            velocityY += GRAVITY;
        }
        
        // Update position based on velocity
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        isOnGround = false;
        
        // Update animation
        updateAnimation(deltaTime);
        
        // Check if falling
        if (velocityY > 0) {
            isFalling = true;
        } else {
            isFalling = false;
        }
    }
    
    @Override
public void render(GraphicsContext gc) {
    // Try to render the proper sprite first
    Image currentSprite;
    
    if (isJumping || isFalling) {
        currentSprite = jumpFrame;
    } else if (Math.abs(velocityX) > 0.1) {
        currentSprite = runFrames[currentFrame];
    } else {
        currentSprite = idleFrame;
    }
    
    // Print position for debugging
    //System.out.println("Rabbit position: " + x + ", " + y);
    
    // Try to draw the sprite on top
    if (currentSprite != null) {
        if (facingRight) {
            gc.drawImage(currentSprite, x, y, width, height);
        } else {
            gc.drawImage(currentSprite, x + width, y, -width, height);
        }
    } else {
        System.out.println("Warning: Rabbit sprite is null");
    }
}
    
    
    /**
     * Update animation frames
     * @param deltaTime Time since last update
     */
    private void updateAnimation(double deltaTime) {
        if (Math.abs(velocityX) > 0.1) {
            // Only animate when moving
            frameTimer += deltaTime;
            if (frameTimer >= FRAME_DURATION) {
                frameTimer = 0;
                currentFrame = (currentFrame + 1) % FRAME_COUNT;
            }
        }
    }
    
    /**
     * Move the rabbit left
     */
    public void moveLeft() {
        velocityX = -speed;
        facingRight = false;
    }
    
    /**
     * Move the rabbit right
     */
    public void moveRight() {
        velocityX = speed;
        facingRight = true;
    }
    
    /**
     * Stop horizontal movement
     */
    public void stopMoving() {
        velocityX = 0;
    }
    
    /**
     * Make the rabbit jump
     */
    public void jump() {
        if (isOnGround) {
            velocityY = -jumpHeight;
            isJumping = true;
            isOnGround = false;
            
            try {
                gameManager.getSoundManager().playSound("jump");
            } catch (Exception e) {
                // Just jump without sound if there's an error
                System.out.println("Could not play jump sound: " + e.getMessage());
            }
        }
    }
    
    /**
     * Called when rabbit lands on a platform
     * @param platformY The Y coordinate of the platform top
     */
    public void land(double platformY) {
        y = platformY - height;
        velocityY = 0;
        isJumping = false;
        isOnGround = true;
    }
    
    /**
     * Check if rabbit should land on platform
     * @param platform The platform to check
     * @return true if should land
     */
    public boolean shouldLandOn(Platform platform) {
        double rabbitBottom = y + height;
        double platformTop = platform.getY();
        
        // Check if rabbit is falling
        boolean isFalling = velocityY > 0;
        
        // Check if rabbit's bottom is at or just past the platform top
        boolean isAtPlatformLevel = rabbitBottom >= platformTop && rabbitBottom <= platformTop + 15;
        
        // Check if rabbit is horizontally within the platform
        boolean isOverPlatform = x + width * 0.3 < platform.getX() + platform.getWidth() &&
                                x + width * 0.7 > platform.getX();
        
        return isFalling && isAtPlatformLevel && isOverPlatform;
    }
    
    /**
     * Set the rabbit's speed
     * @param speed New speed value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Set the rabbit's jump height
     * @param jumpHeight New jump height value
     */
    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
    
    /**
     * Reset the rabbit position
     * @param x New X position
     * @param y New Y position
     */
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        this.isFalling = false;
        this.isOnGround = true;
    }

    
    private GameManager gameManager;

}