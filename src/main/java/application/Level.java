package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level {
	private List<Wall> walls;

    public Level() {
        walls = new ArrayList<>();
    }

    public void addWall(double x, double y, double width, double height) {
        walls.add(new Wall(x, y, width, height, Color.GREEN));
    }

    public List<Wall> getWalls() {
        return walls;
    }
}
