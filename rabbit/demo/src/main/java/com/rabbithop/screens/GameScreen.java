package com.rabbithop.screens;
 
import com.rabbithop.GameManager;
import com.rabbithop.entities.*;
 
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
 
/**
 * The main gameplay screen
 */
public class GameScreen extends Screen {
 
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int GROUND_HEIGHT = 100;
    private static final int TOTAL_COINS_PER_LEVEL = 12;
    private static final int TOTAL_BUSHES_PER_LEVEL = 8;
 
    private Canvas canvas;
    private GraphicsContext gc;
    private AnimationTimer gameLoop;
 
    private Rabbit rabbit;
    private List<Platform> platforms;
    private List<Coin> coins;
    private List<ToxicBush> toxicBushes;
 
    private long lastUpdateTime;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private Random random = new Random();
 
    // Camera system for side-scrolling
    private double cameraX = 0;
    private static final double LEVEL_WIDTH = 3000;
 
    // HUD elements
    private Text coinsText;
    private Text healthText;
    private Text levelText;
 
   
    /**
     * Resume the game without resetting the level
     */
    public void resumeGame() {
        // restart the game loop without regenerating the level
        startGameLoop();
    }
 
    public GameScreen(GameManager gameManager) {
        super(gameManager);
    }
 
    @Override
    protected void initialize() {
        // Create root pane
        BorderPane root = new BorderPane();
 
        // Create canvas for game rendering
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
 
        // Create HUD
        HBox hud = createHUD();
        root.setTop(hud);
 
        // Create the scene
        scene = new Scene(root, WIDTH, HEIGHT);
 
        // Set up input handling
        setupInput();
 
        // Initialize game entities
        initializeEntities();
 
        // Create game loop
        createGameLoop();
    }
 
    /**
     * Create the HUD (heads-up display) with game info
     *
     * @return HBox with HUD elements
     */
    private HBox createHUD() {
        HBox hud = new HBox(20);
        hud.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 10px;");
 
        // Create text elements
        coinsText = new Text("Coins: 0");
        healthText = new Text("Health: 10");
        levelText = new Text("Level: 1");
 
        // Style text elements
        Font hudFont = Font.font("Arial", FontWeight.BOLD, 16);
        coinsText.setFont(hudFont);
        healthText.setFont(hudFont);
        levelText.setFont(hudFont);
        coinsText.setFill(Color.GOLD);
        healthText.setFill(Color.LIGHTGREEN);
        levelText.setFill(Color.WHITE);
 
        // Create store button
        Text storeButton = new Text("STORE");
        storeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        storeButton.setFill(Color.ORANGE);
        storeButton.setOnMouseClicked(e -> gameManager.changeScreen(GameManager.STORE_SCREEN));
 
        // Hover effect for store button
        storeButton.setOnMouseEntered(e -> {
            storeButton.setFill(Color.YELLOW);
            storeButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        });
        storeButton.setOnMouseExited(e -> {
            storeButton.setFill(Color.ORANGE);
            storeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        });
 
        // Add all elements to HUD
        hud.getChildren().addAll(levelText, coinsText, healthText, storeButton);
 
        return hud;
    }
 
    /**
     * Set up keyboard input handling
     */
    private void setupInput() {
        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
    }
 
    /**
     * Initialize game entities
     */
    private void initializeEntities() {
        // Create rabbit
        rabbit = new Rabbit(100, HEIGHT - GROUND_HEIGHT - 100,
                gameManager.getRabbitSpeed(), gameManager.getJumpHeight());
 
        // Create platforms
        platforms = new ArrayList<>();
 
        // Ground is a special platform
        platforms.add(new Platform(0, HEIGHT - GROUND_HEIGHT, LEVEL_WIDTH, GROUND_HEIGHT, true));
 
        // Coins and bushes will be generated when setting up the level
        coins = new ArrayList<>();
        toxicBushes = new ArrayList<>();
    }
 
    /**
     * Create the game loop
     */
    private void createGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate delta time
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    return;
                }
 
                double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = now;
 
                // Update and render game
                update(deltaTime);
                render();
            }
        };
    }
 
    /**
     * Start the game loop
     */
    public void startGameLoop() {
        if (gameLoop != null) {
            lastUpdateTime = 0;
            gameLoop.start();
        }
    }
 
    /**
     * Stop the game loop
     */
    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
 
   /**
 * Set up a level
 * @param level Level number
 */
public void setupLevel(int level) {
    // Reset entities
    platforms.clear();
    coins.clear();
    toxicBushes.clear();
   
    // Create ground platform
    Platform groundPlatform = new Platform(0, HEIGHT - GROUND_HEIGHT, LEVEL_WIDTH, GROUND_HEIGHT, true);
    platforms.add(groundPlatform);
   
    // Calculate maximum jump height
    double maxJumpHeight = gameManager.getJumpHeight() ;
   
    // Divide the level into vertical sections for better platform distribution
    int numVerticalSections = 6;
    double sectionHeight = (HEIGHT - GROUND_HEIGHT - 100) / numVerticalSections;
   
    // Generate platforms based on level
    int numPlatforms = 15 + (level * 5); // More platforms in higher levels
    List<Platform> tempPlatforms = new ArrayList<>();
    tempPlatforms.add(groundPlatform);
   
    // Place platforms starting from the bottom, ensuring they're reachable
    for (int section = numVerticalSections - 1; section >= 0; section--) {
        int platformsInSection = numPlatforms / numVerticalSections;
        if (section == 0) {
            // Add any remaining platforms to the top section
            platformsInSection += numPlatforms % numVerticalSections;
        }
       
        // Calculate y range for this section
        double minY = section * sectionHeight + 100;
        double maxY = (section + 1) * sectionHeight + 100;
       
        // Create platforms in this section
        for (int i = 0; i < platformsInSection; i++) {
            boolean validPosition = false;
            Platform newPlatform = null;
            int attempts = 0;
           
            // Try to find a non-overlapping position that's reachable
            while (!validPosition && attempts < 20) {
                double x = random.nextDouble() * (LEVEL_WIDTH - 200);
                double y = minY + random.nextDouble() * (maxY - minY);
                double width = 100 + random.nextDouble() * 150;
               
                newPlatform = new Platform(x, y, width, 20, false);
               
                // Check for platform overlap
                boolean overlaps = false;
                for (Platform p : tempPlatforms) {
                    if (platformsOverlap(newPlatform, p)) {
                        overlaps = true;
                        break;
                    }
                }
               
                // Check if this platform is reachable from at least one platform below
                boolean isReachable = false;
               
                // Ground is always a reachable starting point
                if (y > HEIGHT - GROUND_HEIGHT - maxJumpHeight) {
                    isReachable = true;
                } else {
                    for (Platform p : tempPlatforms) {
                        // Only consider platforms below this one
                        if (p.getY() > y && p.getY() - maxJumpHeight <= y) {
                            // Check horizontal overlap or proximity
                            if ((x < p.getX() + p.getWidth() + 100 &&
                                 x + width + 100 > p.getX())) {
                                isReachable = true;
                                break;
                            }
                        }
                    }
                }
               
                validPosition = !overlaps && isReachable;
                attempts++;
            }
           
            if (validPosition && newPlatform != null) {
                tempPlatforms.add(newPlatform);
            }
        }
    }
   
    // Add all platforms except the ground (which was already added)
    for (int i = 1; i < tempPlatforms.size(); i++) {
        platforms.add(tempPlatforms.get(i));
    }
   
    // Generate coins
    for (int i = 0; i < TOTAL_COINS_PER_LEVEL; i++) {
        placeCoin();
    }
   
    // Generate toxic bushes
    for (int i = 0; i < TOTAL_BUSHES_PER_LEVEL; i++) {
        placeToxicBush();
    }
   
    // Reset rabbit
    rabbit.reset(100, HEIGHT - GROUND_HEIGHT - 100);
    rabbit.setSpeed(gameManager.getRabbitSpeed());
    rabbit.setJumpHeight(gameManager.getJumpHeight());
   
    // Reset camera
    cameraX = 0;
   
    // Update HUD
    updateHUD();
   
    // Start game loop
    startGameLoop();
}
 
/**
 * Check if two platforms overlap
 * @param p1 First platform
 * @param p2 Second platform
 * @return true if the platforms overlap
 */
private boolean platformsOverlap(Platform p1, Platform p2) {
    // Add a small buffer to prevent platforms from being too close
    double buffer = 10;
   
    // Check for horizontal and vertical overlap
    boolean horizontalOverlap = p1.getX() < p2.getX() + p2.getWidth() + buffer &&
                                p1.getX() + p1.getWidth() + buffer > p2.getX();
   
    boolean verticalOverlap = p1.getY() < p2.getY() + p2.getHeight() + buffer &&
                              p1.getY() + p1.getHeight() + buffer > p2.getY();
   
    return horizontalOverlap && verticalOverlap;
}
 
    /**
     * Place a coin on a platform
     */
    private void placeCoin() {
        // Try to find a suitable platform (not ground)
        Platform platform = null;
        while (platform == null || platform.isGround()) {
            int index = random.nextInt(platforms.size());
            platform = platforms.get(index);
        }
 
        // Place coin on the platform
        double coinX = platform.getX() + random.nextDouble() * (platform.getWidth() - 32);
        double coinY = platform.getY() - 32 - 5; // Above the platform
 
        coins.add(new Coin(coinX, coinY));
    }
 
    /**
     * Place a toxic bush on a platform
     */
    private void placeToxicBush() {
        // Try to find a suitable platform (not ground)
        Platform platform = null;
        while (platform == null || platform.isGround()) {
            int index = random.nextInt(platforms.size());
            platform = platforms.get(index);
        }
 
        // Place bush on the platform
        double bushX = platform.getX() + random.nextDouble() * (platform.getWidth() - 48);
        double bushY = platform.getY() - 48; // Above the platform
 
        // Check if bush overlaps with coins
        boolean overlapsWithCoin = false;
        for (Coin coin : coins) {
            if (Math.abs(bushX - coin.getX()) < 48 && Math.abs(bushY - coin.getY()) < 48) {
                overlapsWithCoin = true;
                break;
            }
        }
 
        if (!overlapsWithCoin) {
            toxicBushes.add(new ToxicBush(bushX, bushY));
        } else {
            // Try again if there's overlap
            placeToxicBush();
        }
    }
 
    /**
     * Update the game state
     *
     * @param deltaTime Time since last update
     */
    private void update(double deltaTime) {
        // Handle input
        handleInput(deltaTime);
 
        // Update rabbit
        rabbit.update(deltaTime);
 
        // Check platform collisions
        handlePlatformCollisions();
 
        // Update all entities
        for (Coin coin : coins) {
            coin.update(deltaTime);
        }
 
        for (ToxicBush bush : toxicBushes) {
            bush.update(deltaTime);
        }
 
        // Check coin collisions
        handleCoinCollisions();
 
        // Check toxic bush collisions
        handleBushCollisions();
 
        // Update camera
        updateCamera();
 
        // Check level completion
        checkLevelCompletion();
 
        // Update HUD
        updateHUD();
    }
 
    /**
     * Handle user input
     *
     * @param deltaTime Time since last update
     */
    private void handleInput(double deltaTime) {
        // Reset velocity first
        rabbit.stopMoving();
 
        // Handle left/right movement
        if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
            rabbit.moveLeft();
        }
 
        if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
            rabbit.moveRight();
        }
 
        // Handle jumping
        if (pressedKeys.contains(KeyCode.SPACE) || pressedKeys.contains(KeyCode.W)
                || pressedKeys.contains(KeyCode.UP)) {
            rabbit.jump();
        }
    }
 
    /**
     * Handle collisions with platforms
     */
    private void handlePlatformCollisions() {
        boolean onAnyPlatform = false;
 
        for (Platform platform : platforms) {
            if (rabbit.shouldLandOn(platform)) {
                rabbit.land(platform.getY());
                onAnyPlatform = true;
                break;
            }
        }
        System.out.println("On platform: " + onAnyPlatform + ", y-pos: " + rabbit.getY());
        // Check if rabbit is off-screen (fell)
        if (rabbit.getY() > HEIGHT) {
            rabbit.reset(100, HEIGHT - GROUND_HEIGHT - 100);
            gameManager.hitToxicBerries(); // Penalize falling
        }
 
        // Check level boundaries
        if (rabbit.getX() < 0) {
            rabbit.setX(0);
        } else if (rabbit.getX() > LEVEL_WIDTH - rabbit.getWidth()) {
            rabbit.setX(LEVEL_WIDTH - rabbit.getWidth());
        }
    }
 
    /**
     * Handle collisions with coins
     */
    private void handleCoinCollisions() {
        for (Coin coin : coins) {
            if (!coin.isCollected() && rabbit.intersects(coin)) {
                coin.collect();
                gameManager.addCoins(1);
 
                // Play sound
                // TODO: Add sound effect
 
            }
        }
    }
 
    /**
     * Handle collisions with toxic bushes
     */
    private void handleBushCollisions() {
        for (ToxicBush bush : toxicBushes) {
            if (bush.canDamageRabbit() && rabbit.intersects(bush)) {
                bush.hitByRabbit();
                gameManager.hitToxicBerries();
 
                // Play sound
                // TODO: Add sound effect
            }
        }
    }
 
    /**
     * Update camera position to follow the rabbit
     */
    private void updateCamera() {
        // Center the camera on the rabbit
        double targetX = rabbit.getX() - WIDTH / 2 + rabbit.getWidth() / 2;
 
        // Clamp camera to level boundaries
        if (targetX < 0) {
            targetX = 0;
        } else if (targetX > LEVEL_WIDTH - WIDTH) {
            targetX = LEVEL_WIDTH - WIDTH;
        }
 
        // Smooth camera movement
        cameraX = cameraX * 0.9 + targetX * 0.1;
    }
 
    /**
     * Check if the level is completed
     */
    private void checkLevelCompletion() {
        int collectedCoins = 0;
 
        for (Coin coin : coins) {
            if (coin.isCollected()) {
                collectedCoins++;
            }
        }
 
        // Level is completed when all coins are collected
        if (collectedCoins >= TOTAL_COINS_PER_LEVEL) {
            stopGameLoop();
            gameManager.levelComplete();
        }
    }
 
    /**
     * Update HUD information
     */
    private void updateHUD() {
        coinsText.setText("Coins: " + gameManager.getCoins());
        healthText.setText("Health: " + gameManager.getHealth());
        levelText.setText("Level: " + gameManager.getCurrentLevel());
 
        // Update health color based on health value
        if (gameManager.getHealth() <= 3) {
            healthText.setFill(Color.RED);
        } else if (gameManager.getHealth() <= 5) {
            healthText.setFill(Color.ORANGE);
        } else {
            healthText.setFill(Color.LIGHTGREEN);
        }
    }
 
    /**
     * Render the game
     */
    private void render() {
        // Clear the canvas
        gc.clearRect(0, 0, WIDTH, HEIGHT);
 
        // Draw background
        renderBackground();
 
        // Apply camera transform
        gc.save();
        gc.translate(-cameraX, 0);
 
        // Render platforms
        for (Platform platform : platforms) {
            // Only render platforms that are visible on screen
            if (platform.getX() + platform.getWidth() >= cameraX &&
                    platform.getX() <= cameraX + WIDTH) {
                platform.render(gc);
            }
        }
 
        // Render coins
        for (Coin coin : coins) {
            // Only render coins that are visible and not collected
            if (!coin.isCollected() &&
                    coin.getX() + coin.getWidth() >= cameraX &&
                    coin.getX() <= cameraX + WIDTH) {
                coin.render(gc);
            }
        }
 
        // Render toxic bushes
        for (ToxicBush bush : toxicBushes) {
            // Only render bushes that are visible
            if (bush.getX() + bush.getWidth() >= cameraX &&
                    bush.getX() <= cameraX + WIDTH) {
                bush.render(gc);
            }
        }
 
        // Render rabbit
        rabbit.render(gc);
 
        // Restore transform
        gc.restore();
    }
 
    /**
     * Render the background
     */
    private void renderBackground() {
        // Simple gradient background
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
 
        // Draw clouds or other background elements
        // TODO: Add parallax scrolling background
    }
}
 