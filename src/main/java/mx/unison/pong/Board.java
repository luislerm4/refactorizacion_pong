package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends JComponent implements KeyListener, ActionListener {
    private Dimension preferredSize = null;
    private Ball ball;
    private Paddle paddle;

    private Timer gameTimer;
    private GameState state = GameState.RUNNING;

    public static final int MAX_X = 640;
    public static final int MAX_Y = 480;

    private final Font theFont = new Font("SansSerif", Font.BOLD, 24);

    public Board() {
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        ball = new Ball(320, 240);
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
        ball.move();

        // Limites laterales
        if (ball.getShape().getMinX() <= 0) {
            ball.setDirectionX(1);
            ball.setX(0);
        } else if (ball.getShape().getMaxX() >= MAX_X) {
            ball.setDirectionX(-1);
            ball.setX(MAX_X - Ball.DIAMETER);
        }

        // Limite superior
        if (ball.getShape().getMinY() <= 0) {
            ball.setDirectionY(1);
            ball.setY(0);
        }

        // Colision con la raqueta
        if (paddle.intersects(ball.getShape())) {
            ball.setDirectionY(-1);
            ball.setY(paddle.getPaddle().getMinY() - Ball.DIAMETER);
        }

        // Limite inferior (Pelota perdida)
        if (ball.getShape().getMinY() >= MAX_Y) {
            ball.setX(320);
            ball.setY(240);
            ball.setDirectionY(-1);
        }
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
        g.fill(ball.getShape());
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