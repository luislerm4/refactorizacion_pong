package mx.unison.pong;

import java.awt.geom.Ellipse2D;

public class Ball {
    public static final int DIAMETER = 20;
    public static final double DX = 5;
    public static final double DY = 5;

    private final Ellipse2D.Double ballShape;
    private double x;
    private double y;
    private int directionX;
    private int directionY;

    public Ball(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.directionX = 1;
        this.directionY = 1;
        this.ballShape = new Ellipse2D.Double(x, y, DIAMETER, DIAMETER);
    }

    public void move() {
        x += DX * directionX;
        y += DY * directionY;
        updateShape();
    }

    public void setX(double x) {
        this.x = x;
        updateShape();
    }

    public void setY(double y) {
        this.y = y;
        updateShape();
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    private void updateShape() {
        ballShape.x = x;
        ballShape.y = y;
    }

    public Ellipse2D.Double getShape() {
        return ballShape;
    }
}