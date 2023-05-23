package application;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

	private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int LEVEL_WIDTH = 2000;
    private static final int LEVEL_HEIGHT = 600;
    private static final int VIEWPORT_WIDTH = 800;
    private static final int VIEWPORT_HEIGHT = 600;
    private double playerX = 100; // Initial player X position
    private double playerY = 150; // Initial player Y position
    private double playerSpeed = 5; // Player movement speed
    private double viewportX = 0;
    private List<Enemy> enemies = new ArrayList<>();
    private Level level;
    private Set<KeyCode> keysPressed = new HashSet<>();
    
    private SpriteSheet playerSpriteSheet;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	int numFrames = 8; // Number of frames in the player animation
    	double frameDuration = 0.1;
    	File spriteSheetFile = new File("C:\\Users\\Victor\\Downloads\\test\\1\\src\\main\\java\\application\\Sprites\\SpritePlayerSheet.png");

    	String spriteSheetPath = spriteSheetFile.toURI().toString();
    	playerSpriteSheet = new SpriteSheet(spriteSheetPath, numFrames, frameDuration);
    	playerSpriteSheet.startAnimation();
    	
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        level = new Level();
        level.addWall(100, 200, 50, 50);
        level.addWall(400, 350, 80, 20);

        enemies = new ArrayList<>();
        enemies.add(new Enemy(300, 250, 40, 40, Color.BLUE));

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw(gc);
            }
        };
        gameLoop.start();
    }

    private void update() {
        double prevPlayerX = playerX;
        double prevPlayerY = playerY;

        // Update the player position based on the key events
        if (isKeyPressed(KeyCode.UP)) {
            playerY -= playerSpeed;
        } else if (isKeyPressed(KeyCode.DOWN)) {
            playerY += playerSpeed;
        } else if (isKeyPressed(KeyCode.LEFT)) {
            playerX -= playerSpeed;
        } else if (isKeyPressed(KeyCode.RIGHT)) {
            playerX += playerSpeed;
        }

        // Check for collisions with walls
        for (Wall wall : level.getWalls()) {
            if (checkCollision(playerX, playerY, 30, 30, wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight())) {
                // Collision detected, adjust player position to prevent movement into the wall
                double overlapX = Math.max(0, Math.min(playerX + 30, wall.getX() + wall.getWidth()) - Math.max(playerX, wall.getX()));
                double overlapY = Math.max(0, Math.min(playerY + 30, wall.getY() + wall.getHeight()) - Math.max(playerY, wall.getY()));
                if (overlapX < overlapY) {
                    // Adjust X position
                    if (playerX < wall.getX()) {
                        playerX -= overlapX;
                    } else {
                        playerX += overlapX;
                    }
                } else {
                    // Adjust Y position
                    if (playerY < wall.getY()) {
                        playerY -= overlapY;
                    } else {
                        playerY += overlapY;
                    }
                }
            }
            playerSpriteSheet.update();

        }

        // Update the viewport position to center around the player
        viewportX = playerX - (VIEWPORT_WIDTH / 2) + (30 / 2); // Adjust for player size

        // Ensure the viewport stays within the level boundaries
        if (viewportX < 0) {
            viewportX = 0;
        } else if (viewportX > LEVEL_WIDTH - VIEWPORT_WIDTH) {
            viewportX = LEVEL_WIDTH - VIEWPORT_WIDTH;
        }
    }

    private boolean isKeyPressed(KeyCode keyCode) {
        return keysPressed.contains(keyCode);
    }

    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        keysPressed.add(keyCode);
    }

    private void handleKeyReleased(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        keysPressed.remove(keyCode);
    }

    private boolean checkCollision(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) {
        // Check if two rectangles (x1, y1, width1, height1) and (x2, y2, width2, height2) intersect
        return x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 < y2 + height2 &&
                y1 + height1 > y2;
    }


    private void draw(GraphicsContext gc) {
    	
    	// Clear the canvas
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // Draw the level within the viewport
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        // Draw walls
        for (Wall wall : level.getWalls()) {
            gc.setFill(wall.getColor());
            gc.fillRect(wall.getX() - viewportX, wall.getY(), wall.getWidth(), wall.getHeight());
        }

         //Draw enemies
        for (Enemy enemy : enemies) {
            gc.setFill(enemy.getColor());
            gc.fillRect(enemy.getX() - viewportX, enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
        // Draw player sprite
        double elapsedTime = (System.nanoTime() - playerSpriteSheet.getStartTime()) / 1e9;
        int frameIndex = (int) (elapsedTime / playerSpriteSheet.getFrameDuration()) % playerSpriteSheet.getNumFrames();
        Image playerFrameImage = playerSpriteSheet.getFrame(frameIndex);
        gc.drawImage(playerFrameImage, playerX, playerY);
    }
    

}
