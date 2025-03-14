package com.rabbitgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static final int GROUND_LEVEL = HEIGHT - 100;
    
    private Rabbit rabbit;
    private ArrayList<Platform> platforms;
    private ArrayList<GoldCoin> coins;
    private ArrayList<ToxicBerry> berries;
    private Store store;
    private boolean isStoreView = false;
    private Pane gameRoot;
    private Rectangle ground;

    @Override
    public void start(Stage primaryStage) {
        gameRoot = new Pane();
        Scene scene = new Scene(gameRoot, WIDTH, HEIGHT);
        gameRoot.setStyle("-fx-background-color: #87CEEB;"); // Sky blue background
        
        // Create ground
        ground = new Rectangle(WIDTH, 100);
        ground.setFill(Color.GREEN);
        ground.setTranslateY(GROUND_LEVEL);
        gameRoot.getChildren().add(ground);
        
        // Initialize game objects
        initializeGame();
        
        // Handle keyboard input
        setupKeyboardControls(scene);
        
        // Game loop
        startGameLoop();
        
        primaryStage.setTitle("Rabbit Platform Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initializeGame() {
        rabbit = new Rabbit();
        rabbit.setY(GROUND_LEVEL - rabbit.getHeight()); // Start on ground
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        berries = new ArrayList<>();
        store = new Store(rabbit);
        
        initializeLevel();
        
        gameRoot.getChildren().add(rabbit.getView());
        for (Platform platform : platforms) {
            gameRoot.getChildren().add(platform.getView());
        }
        for (GoldCoin coin : coins) {
            gameRoot.getChildren().add(coin.getView());
        }
        for (ToxicBerry berry : berries) {
            gameRoot.getChildren().add(berry.getView());
        }
    }

    private void setupKeyboardControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    rabbit.moveLeft();
                    break;
                case RIGHT:
                    rabbit.moveRight();
                    break;
                case SPACE:
                    rabbit.jump();
                    break;
                case TAB:
                    toggleStore();
                    break;
            }
        });
        
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                case RIGHT:
                    rabbit.stop();
                    break;
            }
        });
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isStoreView) {
                    update();
                }
            }
        };
        timer.start();
    }
    
    private void update() {
        rabbit.update();
        checkCollisions();
        
        // Check if rabbit is on ground
        if (rabbit.getY() + rabbit.getHeight() >= GROUND_LEVEL) {
            rabbit.setY(GROUND_LEVEL - rabbit.getHeight());
            rabbit.setOnPlatform(true);
        }
        
        // Keep rabbit within bounds
        if (rabbit.getX() < 0) rabbit.setX(0);
        if (rabbit.getX() > WIDTH - rabbit.getWidth()) rabbit.setX(WIDTH - rabbit.getWidth());
        
        if (rabbit.getHp() <= 5) {
            gameOver();
        }
    }
    
    private void checkCollisions() {
        boolean onPlatform = false;
        
        // Only check platform collisions if above ground level
        if (rabbit.getY() + rabbit.getHeight() < GROUND_LEVEL) {
            for (Platform platform : platforms) {
                if (rabbit.getBounds().intersects(platform.getBounds())) {
                    double rabbitBottom = rabbit.getY() + rabbit.getHeight();
                    double platformTop = platform.getY();
                    
                    // Check if rabbit is landing on top of platform
                    if (rabbitBottom >= platformTop && rabbitBottom <= platformTop + 20 && rabbit.getVelocityY() > 0) {
                        rabbit.setY(platformTop - rabbit.getHeight());
                        onPlatform = true;
                        break;
                    }
                }
            }
        } else {
            onPlatform = true; // On ground
        }
        
        rabbit.setOnPlatform(onPlatform);
        
        // Check coin collisions
        coins.removeIf(coin -> {
            if (rabbit.getBounds().intersects(coin.getBounds()) && !coin.isCollected()) {
                rabbit.collectCoin();
                coin.collect();
                return true;
            }
            return false;
        });
        
        // Check berry collisions
        for (ToxicBerry berry : berries) {
            if (berry.isActive() && rabbit.getBounds().intersects(berry.getBounds())) {
                rabbit.takeDamage();
                berry.setVisible(false);
            }
        }
    }

    private void initializeLevel() {
        Random random = new Random();
        
        // Add floating platforms
        for (int i = 0; i < 8; i++) {
            double x = i * (WIDTH/8) + random.nextInt(50); // Spread platforms across width
            double y = GROUND_LEVEL - 150 - random.nextInt(200); // Vary height but keep above ground
            platforms.add(new Platform(x, y));
        }
        
        // Add coins above platforms and ground
        for (Platform platform : platforms) {
            // Add coins above platform
            coins.add(new GoldCoin(
                platform.getX() + platform.getWidth()/2,
                platform.getY() - 50
            ));
        }
        
        // Add some coins on ground level
        for (int i = 0; i < 4; i++) {
            coins.add(new GoldCoin(
                random.nextInt(WIDTH - 50) + 25,
                GROUND_LEVEL - 30
            ));
        }
        
        // Add toxic berries between platforms
        for (int i = 0; i < 5; i++) {
            berries.add(new ToxicBerry(
                random.nextInt(WIDTH - 50) + 25,
                GROUND_LEVEL - random.nextInt(200) - 50
            ));
        }
    }
    
    private void gameOver() {
        System.out.println("Game Over!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}