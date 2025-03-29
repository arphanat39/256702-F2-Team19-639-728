package com.rabbithop.screens;

import com.rabbithop.GameManager;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * Base abstract class for all game screens
 */
public abstract class Screen {
    protected GameManager gameManager;
    protected VBox root;  // Changed from Pane to VBox
    protected Scene scene;
    
    public Screen(GameManager gameManager) {
        this.gameManager = gameManager;
        root = new VBox(); // Initialize root here!
        initialize();
    }
    
    /**
     * Initialize the screen components
     */
    protected abstract void initialize();
    
    /**
     * Get the scene associated with this screen
     * @return Scene object
     */
    public Scene getScene() {
        return scene;
    }
}