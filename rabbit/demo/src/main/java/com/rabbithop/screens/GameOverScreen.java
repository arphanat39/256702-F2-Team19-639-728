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
//import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Screen shown when the player loses the game
 */
public class GameOverScreen extends Screen {
    
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    
    private Text statsText;

    public GameOverScreen(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    protected void initialize() {
        // Instead of creating a new VBox, use the one from the parent
        root.setSpacing(20); // or whatever spacing value you want
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        
        // Apply background
        LinearGradient gradient = new LinearGradient(
            0, 0, 0, 1, true, null,
            new Stop(0, Color.DARKRED),
            new Stop(1, Color.BLACK)
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create game over text
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Impact", FontWeight.BOLD, 72));
        gameOverText.setFill(Color.WHITE);
        
        // Add drop shadow to text
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(5);
        gameOverText.setEffect(shadow);
        
        // Try to load sad rabbit image
        ImageView sadRabbitView = new ImageView();
        try {
            Image sadRabbitImage = new Image(getClass().getResourceAsStream("/images/sad_rabbit.png"));
            sadRabbitView.setImage(sadRabbitImage);
            sadRabbitView.setFitWidth(200);
            sadRabbitView.setFitHeight(200);
            sadRabbitView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load sad rabbit image: " + e.getMessage());
        }
        
        // Stats text
        statsText = new Text();
        statsText.setFont(Font.font("Arial", 18));
        statsText.setFill(Color.WHITE);
        
        // Try again button
        Button tryAgainButton = createStyledButton("Try Again");
        tryAgainButton.setOnAction(e -> gameManager.startNewGame());
        
        // Main menu button
        Button menuButton = createStyledButton("Main Menu");
        menuButton.setOnAction(e -> gameManager.changeScreen(GameManager.MENU_SCREEN));
        
        // Exit button
        Button exitButton = createStyledButton("Exit Game");
        exitButton.setOnAction(e -> System.exit(0));
        
        // Add all elements to root
        root.getChildren().addAll(
                gameOverText,
                sadRabbitView,
                statsText,
                tryAgainButton,
                menuButton,
                exitButton
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
        button.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-background-radius: 10;");
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-background-radius: 10;"));
        
        return button;
    }
    
    /**
     * Update the screen based on current game state
     */
    public void update() {
        statsText.setText(
                "You reached Level " + gameManager.getCurrentLevel() + "\n" +
                "Collected Coins: " + gameManager.getCoins() + "\n" +
                "The toxic berries were too much for our little rabbit friend!"
        );
    }
}