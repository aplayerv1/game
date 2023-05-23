package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Enemy {

	 private double x;
     private double y;
     private double width;
     private double height;
     private Color color;

     public Enemy(double x, double y, double width, double height, Color color) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         this.color = color;
     }

     public double getX() {
         return x;
     }

     public double getY() {
         return y;
     }

     public double getWidth() {
         return width;
     }

     public double getHeight() {
         return height;
     }

     public Color getColor() {
         return color;
     }
 }
 

