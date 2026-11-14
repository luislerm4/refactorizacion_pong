package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Board extends JComponent implements KeyListener, ActionListener {
    private Dimension preferredSize = null;
    private Ellipse2D.Double ball;
    private Paddle paddle;

    private Timer gameTimer;
    private GameState state = GameState.RUNNING;

    public static final int MAX_X = 640;
    public static final int MAX_Y = 480;

    public static final int DX = 5;
    public static final int DY = 5;

    private double ballX = 320;
    private double ballY = 240;
    private int directionX = 1;
    private int directionY = 1;

    private final Font theFont = new Font("SansSerif", Font.BOLD, 24);

    public Board() {
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        ball = new Ellipse2D.Double(ballX, ballY, 20, 20);
        paddle = new Paddle();

        gameTimer = new Timer(30, this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == GameState.RUNNING) {
            updateGameLogic();
        }
        repaint();
    }

    private void updateGameLogic() {
        ballX += DX * directionX;
        ballY += DY * directionY;
        ball.x = ballX;
        ball.y = ballY;

        if (ball.getMinX() <= 0) {
            directionX = 1;
            ballX = 0;
        } else if (ball.getMaxX() >= MAX_X) {
            directionX = -1;
            ballX = MAX_X - ball.width;
        }

        if (ball.getMinY() <= 0) {
            directionY = 1;
            ballY = 0;
        }

        if (paddle.intersects(ball)) {
            directionY = -1;
            ballY = paddle.getPaddle().getMinY() - ball.height;
        }

        if (ball.getMinY() >= MAX_Y) {
            ballX = 320;
            ballY = 240;
            directionY = -1;
        }

        ball.x = ballX;
        ball.y = ballY;
    }

    public void stopGame() {
        state = GameState.STOPPED;
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    protected void paintComponent(Graphics graphicsContext) {
        super.paintComponent(graphicsContext);
        Graphics2D g = (Graphics2D) graphicsContext;
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
        }

        if (state == GameState.PAUSED) {
            g.setFont(theFont);
            g.setColor(Color.BLUE);
            g.drawString("Paused", 20, 30);
        }

        g.setStroke(new BasicStroke(5.0f));
        g.setColor(Color.RED);
        g.fill(ball);
        g.setColor(Color.BLACK);
        g.fill(paddle.getPaddle());
    }

    public Dimension getPreferredSize() {
        return preferredSize == null ? new Dimension(640, 480) : super.getPreferredSize();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (state == GameState.RUNNING) { paddle.moveLeft(); }
                break;
            case KeyEvent.VK_RIGHT:
                if (state == GameState.RUNNING) { paddle.moveRight(); }
                break;
            case KeyEvent.VK_SPACE:
                state = GameState.PAUSED;
                break;
            case KeyEvent.VK_R:
                state = GameState.RUNNING;
                break;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}