package com.rabbithop.screens;

import com.rabbithop.GameManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Screen shown when a level is completed
 */
public class LevelCompleteScreen extends Screen {
    
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    
    private Text levelText;
    private Text coinsText;
    private Text messageText;
    private Button nextLevelButton;

    public LevelCompleteScreen(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    protected void initialize() {
        // Create root container
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        
        // Apply background
        LinearGradient gradient = new LinearGradient(
            0, 0, 0, 1, true, null,
            new Stop(0, Color.DARKGREEN),
            new Stop(1, Color.FORESTGREEN)
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create congratulations text
        Text congratsText = new Text("Level Complete!");
        congratsText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 60));
        congratsText.setFill(Color.GOLD);
        
        // Add drop shadow to text
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        congratsText.setEffect(shadow);
        
        // Try to load trophy image
        ImageView trophyView = new ImageView();
        try {
            Image trophyImage = new Image(getClass().getResourceAsStream("/images/trophy.png"));
            trophyView.setImage(trophyImage);
            trophyView.setFitWidth(200);
            trophyView.setFitHeight(200);
            trophyView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load trophy image: " + e.getMessage());
        }
        
        // Level info
        levelText = new Text("Level " + gameManager.getCurrentLevel() + " of " + gameManager.getMaxLevel());
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        levelText.setFill(Color.WHITE);
        
        // Coins collected
        coinsText = new Text("Total Coins: " + gameManager.getCoins());
        coinsText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        coinsText.setFill(Color.GOLD);
        
        // Message text
        messageText = new Text();
        messageText.setFont(Font.font("Arial", 18));
        messageText.setFill(Color.WHITE);
        
        // Next level button
        nextLevelButton = createStyledButton("Next Level");
        nextLevelButton.setOnAction(e -> gameManager.nextLevel());
        
        // Store button
        Button storeButton = createStyledButton("Go to Store");
        storeButton.setOnAction(e -> gameManager.changeScreen(GameManager.STORE_SCREEN));
        
        // Menu button
        Button menuButton = createStyledButton("Main Menu");
        menuButton.setOnAction(e -> gameManager.changeScreen(GameManager.MENU_SCREEN));
        
        // Add all elements to root
        root.getChildren().addAll(
                congratsText,
                trophyView,
                levelText,
                coinsText,
                messageText,
                nextLevelButton,
                storeButton,
                menuButton
        );
        
        // Create scene
        scene = new Scene(root, WIDTH, HEIGHT);
    }
    
    /**
     * Create a styled button
     * @param text Button text
     * @return Styled button
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(200, 50);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;");
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;"));
        
        return button;
    }
    
    /**
     * Update the screen based on current game state
     */
    public void update() {
        levelText.setText("Level " + gameManager.getCurrentLevel() + " of " + gameManager.getMaxLevel());
        coinsText.setText("Total Coins: " + gameManager.getCoins());
        
        // Check if this is the final level
        if (gameManager.getCurrentLevel() >= gameManager.getMaxLevel()) {
            messageText.setText("Congratulations! You've completed all levels!");
            nextLevelButton.setDisable(true);
        } else {
            // Check if player has a key
            if (gameManager.hasKey()) {
                messageText.setText("You have a key! Press Next Level to continue.");
                nextLevelButton.setDisable(false);
            } else {
                messageText.setText("You need to buy a key from the store to unlock the next level.");
                nextLevelButton.setDisable(true);
            }
        }
    }
}