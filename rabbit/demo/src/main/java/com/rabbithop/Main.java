package com.rabbithop;

import javafx.application.Application;
import javafx.scene.Scene;
// import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main entry point for the Rabbit Platform Game
 */
public class Main extends Application {

    private static final String TITLE = "Rabbit Hop - Platform Adventure";
   // private static final int WIDTH = 1024;
   // private static final int HEIGHT = 768;
    
    private GameManager gameManager;

   // In Main.java
@Override
public void start(Stage primaryStage) {
    try {
        // Initialize game manager with stage reference
        gameManager = new GameManager(primaryStage); // Pass the stage
        
        // Set up the primary stage
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        
        // Create scene with the menu screen
        Scene menuScene = gameManager.getMenuScene();
        primaryStage.setScene(menuScene);
        
        primaryStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @Override
    public void stop() {
        // Cleanup resources when the application closes
        gameManager.cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}