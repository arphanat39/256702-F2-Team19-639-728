package com.rabbithop;

import com.rabbithop.screens.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.rabbithop.GameSaveData;
import java.io.*;

/**
 * Manages the game state and screen transitions
 */
public class GameManager {
    
    // Game state constants
    public static final int MENU_SCREEN = 0;
    public static final int GAME_SCREEN = 1;
    public static final int STORE_SCREEN = 2;
    public static final int LEVEL_COMPLETE_SCREEN = 3;
    public static final int GAME_OVER_SCREEN = 4;
    
    // Game progress and stats
    private int currentLevel = 1;
    private int maxLevel = 3;
    private int coins = 0;
    private int health = 15;
    private boolean hasKey = false;
    private double rabbitSpeed = 50;
    private double jumpHeight = 90;
    
    // Screen instances
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private StoreScreen storeScreen;
    private LevelCompleteScreen levelCompleteScreen;
    private GameOverScreen gameOverScreen;
    
    // Current active scene
    private Scene currentScene;
    private int currentScreenId;
    
    private Stage primaryStage; // Add this as a class field

    public GameManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.soundManager = new SoundManager(); 
        initializeScreens();
    }
    
    private void initializeScreens() {
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        storeScreen = new StoreScreen(this);
        levelCompleteScreen = new LevelCompleteScreen(this);
        gameOverScreen = new GameOverScreen(this);
        
        // Set the menu screen as default
        currentScene = menuScreen.getScene();
        currentScreenId = MENU_SCREEN;
    }
    
    /**
     * Change to a different screen
     * @param screenId The screen ID to switch to
     */
    public void changeScreen(int screenId) {
        Scene newScene = null;
        
        switch(screenId) {
            case MENU_SCREEN:
                newScene = menuScreen.getScene();
                break;
            case GAME_SCREEN:
                 // Only setup the level if we're not returning from the store
            if (currentScreenId != STORE_SCREEN) {
                gameScreen.setupLevel(currentLevel);
            } else {
                // If returning from store, just resume the game
                gameScreen.resumeGame();
            }
            newScene = gameScreen.getScene();
            break;
            case STORE_SCREEN:
                storeScreen.updateStore();
                newScene = storeScreen.getScene();
                break;
            case LEVEL_COMPLETE_SCREEN:
                levelCompleteScreen.update();
                newScene = levelCompleteScreen.getScene();
                break;
            case GAME_OVER_SCREEN:
                gameOverScreen.update();
                newScene = gameOverScreen.getScene();
                break;
            default:
                return;
        }
        
        if (newScene != null) {
            currentScene = newScene;
            currentScreenId = screenId;
            
            // Get the current stage and set the new scene
            primaryStage.setScene(currentScene);
            }
        }
    
    
    /**
     * Return to the previous screen before the store
     */
    public void returnFromStore() {
        if (currentScreenId == STORE_SCREEN) {
            changeScreen(GAME_SCREEN);
        }
    }
    
    /**
     * Start a new game
     */
    public void startNewGame() {
        this.currentLevel = 1;
        this.coins = 0;
        this.health = 15;
        this.hasKey = false;
        this.rabbitSpeed = 50.0;
        this.jumpHeight = 90.0;
        
        changeScreen(GAME_SCREEN);
    }

    public void loadSavedGame() {
        if (loadGame()) {
            changeScreen(GAME_SCREEN);
        } else {
            //reload
            startNewGame();
        }
    }
    
    /**
     * Advance to the next level
     */
    public void nextLevel() {
        if (currentLevel < maxLevel && hasKey) {
            currentLevel++;
            hasKey = false; // Reset key for the next level
            changeScreen(GAME_SCREEN);
        }
    }
    
    public void gameOver() {
        soundManager.playSound("game_over");
        changeScreen(GAME_OVER_SCREEN);
    }
    
    public void levelComplete() {
        soundManager.playSound("level_complete");
        changeScreen(LEVEL_COMPLETE_SCREEN);
    } 
    /**
     * Add coins to player inventory
     * @param amount Amount of coins to add
     */
    public void addCoins(int amount) {
        this.coins += amount;
    }
    
    /**
     * Reduce health when rabbit hits toxic berries
     */
    public void hitToxicBerries() {
        health -= 2;
        if (health <= 3) {
            gameOver();
        }
    }
    
    /**
     * Purchase health potion from the store
     * @return true if purchase successful
     */
    public boolean buyHealthPotion() {
        if (coins >= 2) {
            coins -= 2;
            health = (health + 5 ); 
            return true;
        }
        return false;
    }
    
    /**
     * Purchase speed potion from the store
     * @return true if purchase successful
     */
    public boolean buySpeedPotion() {
        if (coins >= 1) {
            coins -= 1;
            rabbitSpeed += 10;
            return true;
        }
        return false;
    }
    
    /**
     * Purchase jump potion from the store
     * @return true if purchase successful
     */
    public boolean buyJumpPotion() {
        if (coins >= 1) {
            coins -= 1;
            jumpHeight += 25;
            return true;
        }
        return false;
    }
    
    /**
     * Purchase key to the next level
     * @return true if purchase successful
     */
    public boolean buyKey() {
        if (coins >= 10) {
            coins -= 10;
            hasKey = true;
            return true;
        }
        return false;
    }
    
    /**
     * Clean up resources
     */
    public void cleanup() {
        // Stop game loops and release resources
        if (gameScreen != null) {
            gameScreen.stopGameLoop();
        }
    }
    
    // Getters for the scenes
    public Scene getMenuScene() {
        return menuScreen.getScene();
    }
    
    public Scene getGameScene() {
        return gameScreen.getScene();
    }
    
    // Getters and setters for game state
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean hasKey() {
        return hasKey;
    }
    
    public double getRabbitSpeed() {
        return rabbitSpeed;
    }
    
    public double getJumpHeight() {
        return jumpHeight;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }

    private SoundManager soundManager;
    // Add this method to your GameManager class
public SoundManager getSoundManager() {
    return soundManager;
  }




// savegame
public void saveGame() {
    GameSaveData saveData = new GameSaveData(
        currentLevel,
        coins,
        health,
        hasKey,
        rabbitSpeed,
        jumpHeight
    );
    
    try (ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream("rabbithop_save.dat"))) {
        out.writeObject(saveData);
        System.out.println("Saved");
    } catch (IOException e) {
        System.out.println("Unable to save: " + e.getMessage());
    }
}

// load game
public boolean loadGame() {
    try (ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("rabbithop_save.dat"))) {
        GameSaveData saveData = (GameSaveData) in.readObject();
        
        // load to GameManager
        this.currentLevel = saveData.getCurrentLevel();
        this.coins = saveData.getCoins();
        this.health = saveData.getHealth();
        this.hasKey = saveData.hasKey();
        this.rabbitSpeed = saveData.getRabbitSpeed();
        this.jumpHeight = saveData.getJumpHeight();
        
        System.out.println("Load successful: Level " + currentLevel);
        return true;
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Unable to load: " + e.getMessage());
        return false;
    }
}


public boolean hasSaveGame() {
    File saveFile = new File("rabbithop_save.dat");
    return saveFile.exists() && saveFile.isFile();
}



}
