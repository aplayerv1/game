package application;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpriteSheet {
    private Image image;
    private int numFrames;
    private double frameDuration;
    private double animationDuration;
    private int currentFrame;
    private long startTime;

    public SpriteSheet(String imagePath, int numFrames, double frameDuration) {
        this.image = new Image(imagePath);
        this.numFrames = numFrames;
        this.frameDuration = frameDuration;
        this.animationDuration = numFrames * frameDuration;
    }

    public void startAnimation() {
        startTime = System.nanoTime();
    }

    public void update() {
        if (startTime == 0) {
            startTime = System.nanoTime();
        }

        double elapsedTime = (System.nanoTime() - startTime) / 1e9; // Convert nanoseconds to seconds
        double currentTime = elapsedTime % animationDuration;
        currentFrame = (int) (currentTime / frameDuration);
    }

    public Image getImage() {
        int frameWidth = (int) (image.getWidth() / numFrames);
        int frameHeight = 64; // Adjust the height as desired
        int sourceX = currentFrame * frameWidth;
        int sourceY = 0;

        return new WritableImage(image.getPixelReader(), sourceX, sourceY, frameWidth, frameHeight);
    }
    
    public Image getFrame(int frameIndex) {
        int frameWidth = (int) (image.getWidth() / numFrames);
        int frameHeight = 64; // Adjust the height as desired
        int sourceX = frameIndex * frameWidth;
        int sourceY = 0;

        return new WritableImage(image.getPixelReader(), sourceX, sourceY, frameWidth, frameHeight);
    }
    
    public long getStartTime() {
        return startTime;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public double getFrameDuration() {
        return frameDuration;
    }
}
