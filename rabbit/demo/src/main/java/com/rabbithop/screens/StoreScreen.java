package com.rabbithop;

import com.rabbithop.screens.Screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * The in-game store screen
 */
public class StoreScreen extends Screen {
    
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    
    private Text coinsText;
    private Text messageText;
    
    private Button healthPotionButton;
    private Button speedPotionButton;
    private Button jumpPotionButton;
    private Button keyButton;

    public StoreScreen(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    protected void initialize() {
        // Create root pane with border layout
        BorderPane rootPane = new BorderPane();
        
        // Apply background
        LinearGradient gradient = new LinearGradient(
            0, 0, 0, 1, true, null,
            new Stop(0, Color.DARKBLUE),
            new Stop(1, Color.PURPLE)
        );
        rootPane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Create title
        Text title = createTitle("Rabbit Shop");
        
        // Create coins display
        coinsText = new Text("Coins: 0");
        coinsText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        coinsText.setFill(Color.GOLD);
        
        // Create message text
        messageText = new Text("");
        messageText.setFont(Font.font("Arial", 16));
        messageText.setFill(Color.WHITE);
        
        // Create top section with title and coins
        VBox topSection = new VBox(10, title, coinsText);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
        
        // Create store items
        VBox storeItems = createStoreItems();
        
        // Create return button
        Button returnButton = new Button("Return to Game");
        returnButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        returnButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;");
        returnButton.setOnAction(e -> gameManager.returnFromStore());
        
        // Create bottom section with message and return button
        VBox bottomSection = new VBox(15, messageText, returnButton);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(20));
        
        // Add all sections to the border pane
        rootPane.setTop(topSection);
        rootPane.setCenter(storeItems);
        rootPane.setBottom(bottomSection);
        
        // Create the scene
        scene = new Scene(rootPane, WIDTH, HEIGHT);
    }
    
    /**
     * Create the store title
     * @param titleText The title text
     * @return Styled Text object
     */
    private Text createTitle(String titleText) {
        Text title = new Text(titleText);
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 48));
        title.setFill(Color.WHITE);
        
        // Add drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        title.setEffect(shadow);
        
        return title;
    }
    
    /**
     * Create the store items section
     * @return VBox with store items
     */
    private VBox createStoreItems() {
        VBox itemsContainer = new VBox(30);
        itemsContainer.setAlignment(Pos.CENTER);
        itemsContainer.setPadding(new Insets(20));
        
        // Health Potion
        HBox healthPotion = createStoreItem(
                "/images/health_potion.png", 
                "Health Potion", 
                "Restores 5 health points", 
                "2 coins");
        
        healthPotionButton = createBuyButton("Buy Health Potion");
        healthPotionButton.setOnAction(e -> {
            boolean success = gameManager.buyHealthPotion();
            handlePurchaseResult(success, "Health potion purchased!");
        });
        
        healthPotion.getChildren().add(healthPotionButton);
        
        // Speed Potion
        HBox speedPotion = createStoreItem(
                "/images/speed_potion.png", 
                "Speed Potion", 
                "Increases rabbit's movement speed", 
                "1 coins");
        
        speedPotionButton = createBuyButton("Buy Speed Potion");
        speedPotionButton.setOnAction(e -> {
            boolean success = gameManager.buySpeedPotion();
            handlePurchaseResult(success, "Speed potion purchased!");
        });
        
        speedPotion.getChildren().add(speedPotionButton);
        
        // Jump Potion
        HBox jumpPotion = createStoreItem(
                "/images/jump_potion.png", 
                "Jump Potion", 
                "Increases rabbit's jump height", 
                "1 coins");
        
        jumpPotionButton = createBuyButton("Buy Jump Potion");
        jumpPotionButton.setOnAction(e -> {
            boolean success = gameManager.buyJumpPotion();
            handlePurchaseResult(success, "Jump potion purchased!");
        });
        
        jumpPotion.getChildren().add(jumpPotionButton);
        
        // Key
        HBox key = createStoreItem(
                "/images/key.png", 
                "Level Key", 
                "Unlocks the next level", 
                "10 coins");
        
        keyButton = createBuyButton("Buy Level Key");
        keyButton.setOnAction(e -> {
            boolean success = gameManager.buyKey();
            handlePurchaseResult(success, "Level key purchased!");
        });
        
        key.getChildren().add(keyButton);
        
        // Add all items to the container
        itemsContainer.getChildren().addAll(healthPotion, speedPotion, jumpPotion, key);
        
        return itemsContainer;
    }
    
    /**
     * Create a store item row
     * @param imagePath Path to item image
     * @param name Item name
     * @param description Item description
     * @param price Item price
     * @return HBox with the item
     */
    private HBox createStoreItem(String imagePath, String name, String description, String price) {
        HBox item = new HBox(20);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 10;");
        
        // Item image
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load item image: " + e.getMessage());
        }
        
        // Item details
        VBox details = new VBox(5);
        
        Text nameText = new Text(name);
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameText.setFill(Color.WHITE);
        
        Text descText = new Text(description);
        descText.setFont(Font.font("Arial", 14));
        descText.setFill(Color.LIGHTGREY);
        
        Text priceText = new Text(price);
        priceText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceText.setFill(Color.GOLD);
        
        details.getChildren().addAll(nameText, descText, priceText);
        
        // Add image and details to the item
        item.getChildren().addAll(imageView, details);
        
        return item;
    }
    
    /**
     * Create a buy button
     * @param text Button text
     * @return Styled button
     */
    private Button createBuyButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-radius: 5;");
        
        // Add hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> 
            button.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-radius: 5;"));
        
        return button;
    }
    
    /**
     * Handle the result of a purchase attempt
     * @param success Whether the purchase was successful
     * @param successMessage Message to display on success
     */
    private void handlePurchaseResult(boolean success, String successMessage) {
        if (success) {
            messageText.setText(successMessage);
            messageText.setFill(Color.LIGHTGREEN);
        } else {
            messageText.setText("Not enough coins!");
            messageText.setFill(Color.RED);
        }
        
        // Update coins display
        updateStore();
    }
    
    /**
     * Update the store display
     */
    public void updateStore() {
        coinsText.setText("Coins: " + gameManager.getCoins());
        
        // Reset message
        messageText.setText("");
        
        // Update button states based on whether player has key already
        if (gameManager.hasKey()) {
            keyButton.setDisable(true);
            keyButton.setText("Already Purchased");
        } else {
            keyButton.setDisable(false);
            keyButton.setText("Buy Level Key");
        }
        
        // Update button states based on whether current level is max level
        if (gameManager.getCurrentLevel() >= gameManager.getMaxLevel()) {
            keyButton.setDisable(true);
            keyButton.setText("Final Level Reached");
        }
    }
}