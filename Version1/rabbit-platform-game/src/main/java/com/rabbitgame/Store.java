package com.rabbitgame;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class Store {
    private Stage storeStage;
    private Rabbit rabbit;
    private Text coinText;
    private Text hpText;
    
    public Store(Rabbit rabbit) {
        this.rabbit = rabbit;
        storeStage = new Stage();
        storeStage.setTitle("Game Store");
        storeStage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.setAlignment(Pos.CENTER);
        
        // Status section
        Label statusLabel = new Label("Status");
        statusLabel.setFont(Font.font(18));
        coinText = new Text("Coins: " + rabbit.getCoins());
        hpText = new Text("HP: " + rabbit.getHp() + "/10");
        
        // Upgrade buttons
        Button speedUpgrade = createStoreButton("Speed Upgrade (1 coin)", () -> {
            if (rabbit.getCoins() >= 1) {
                rabbit.setCoins(rabbit.getCoins() - 1);
                rabbit.upgradeSpeed();
                updateStatus();
            }
        });
        
        Button jumpUpgrade = createStoreButton("Jump Upgrade (1 coin)", () -> {
            if (rabbit.getCoins() >= 1) {
                rabbit.setCoins(rabbit.getCoins() - 1);
                rabbit.upgradeJump();
                updateStatus();
            }
        });
        
        Button healthPotion = createStoreButton("Health Potion (1 coin)", () -> {
            if (rabbit.getCoins() >= 1 && rabbit.getHp() < 10) {
                rabbit.setCoins(rabbit.getCoins() - 1);
                rabbit.heal();
                updateStatus();
            }
        });
        
        root.getChildren().addAll(
            statusLabel,
            coinText,
            hpText,
            new Label("Store Items:"),
            speedUpgrade,
            jumpUpgrade,
            healthPotion
        );
        
        Scene scene = new Scene(root, 300, 400);
        storeStage.setScene(scene);
    }
    
    private Button createStoreButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10;");
        button.setOnAction(e -> action.run());
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10;"));
        return button;
    }
    
    private void updateStatus() {
        coinText.setText("Coins: " + rabbit.getCoins());
        hpText.setText("HP: " + rabbit.getHp() + "/10");
    }
    
    public void show() {
        updateStatus();
        storeStage.show();
    }
    
    public void hide() {
        storeStage.hide();
    }
}