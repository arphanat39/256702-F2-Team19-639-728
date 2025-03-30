package com.rabbithop.screens;

import com.rabbithop.GameManager;

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
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        
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
        } catch (Exception e) {
            // If background image cannot be loaded, set a color
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #3CB371);");
            System.out.println("Could not load menu background: " + e.getMessage());
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
        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/rabbit_logo.png")));
            logoView.setFitWidth(200);
            logoView.setFitHeight(200);
            logoView.setPreserveRatio(true);
            root.getChildren().add(logoView);
        } catch (Exception e) {
            System.out.println("Could not load logo image: " + e.getMessage());
        }
        
        // Create buttons
        Button startButton = createStyledButton("Start Game");
        Button quitButton = createStyledButton("Quit");
        
        // Button actions
        startButton.setOnAction(e -> {
            System.out.println("Start button clicked!");
            gameManager.startNewGame();
        });
        quitButton.setOnAction(e -> System.exit(0));
        
        // Add all elements to the root
        root.getChildren().addAll(title, startButton, quitButton);
        
        // Create the scene
        scene = new Scene(root, WIDTH, HEIGHT);
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