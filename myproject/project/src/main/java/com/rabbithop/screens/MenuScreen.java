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
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;

/**
 * The main menu screen
 */
public class MenuScreen extends Screen {
    // Create the scene
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    public MenuScreen(GameManager gameManager) {
        super(gameManager);
    }

   

    @Override
    protected void initialize() {
        // Create root pane with proper spacing and alignment
        root = new VBox(20); // Set spacing to 20px between elements
        root.setAlignment(Pos.CENTER); // Center all children horizontally and vertically
        root.setPadding(new Insets(50)); // Add some padding
        
        // Try to set background image
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/menu_background.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage, 
                    BackgroundRepeat.NO_REPEAT, 
                    BackgroundRepeat.NO_REPEAT, 
                    BackgroundPosition.CENTER, 
                    new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(backgroundImage));
            System.out.println("Background image loaded successfully");
        } catch (Exception e) {
            // If background image cannot be loaded, set a color
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #3CB371);");
            System.out.println("Could not load menu background: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create and style title
        Text title = new Text("Rabbit Hop");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 72));
        title.setFill(Color.WHITE);
        title.setStroke(Color.BLACK);
        title.setStrokeWidth(2);
        
        // Add drop shadow effect to title
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        title.setEffect(dropShadow);
        
        // Try to load logo image
        ImageView logoView = null;
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/rabbit_logo.png"));
            logoView = new ImageView(logoImage);
            logoView.setFitWidth(200);
            logoView.setFitHeight(200);
            logoView.setPreserveRatio(true);
            System.out.println("Logo image loaded successfully");
        } catch (Exception e) {
            System.out.println("Could not load logo image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create buttons
        Button startButton = createStyledButton("Start Game");
        Button loadButton = createStyledButton("Continue");
        Button quitButton = createStyledButton("Quit");
        
        // Button actions
        startButton.setOnAction(e -> gameManager.startNewGame());
        loadButton.setOnAction(e -> {
            if (gameManager.loadGame()) {
                gameManager.changeScreen(GameManager.GAME_SCREEN);
            } else {
                showNoSaveMessage();
            }
        });
        quitButton.setOnAction(e -> System.exit(0));
        
        // Disable load button if no save exists
        loadButton.setDisable(!gameManager.hasSaveGame());
        
        // Add sound toggle button
        Button soundButton = new Button(gameManager.getSoundManager().isSoundEnabled() ? 
                                       "Sound: ON" : "Sound: OFF");
        soundButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        soundButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;");
        
        soundButton.setOnAction(e -> {
            boolean soundOn = gameManager.getSoundManager().toggleSound();
            soundButton.setText(soundOn ? "Sound: ON" : "Sound: OFF");
        });
        
        // Add all elements to the root
        root.getChildren().add(title);
        
        // Add logo if it was loaded successfully
        if (logoView != null) {
            root.getChildren().add(logoView);
        }
        
        // Add buttons
        root.getChildren().addAll(startButton, loadButton, quitButton, soundButton);
        
        // Create the scene
        scene = new Scene(root, WIDTH, HEIGHT);
    }
    
    // Show message that no save file exists
    private void showNoSaveMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Save Found");
        alert.setHeaderText(null);
        alert.setContentText("No saved game found. Please start a new game.");
        alert.showAndWait();
    }
    
    /**
     * Create styled button for menu
     * @param text Button text
     * @return Styled button
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(200, 50);
        button.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;");
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;"));
        
        return button;
    }

}