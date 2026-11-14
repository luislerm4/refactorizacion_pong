package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JComponent implements ActionListener {
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

        setupKeyBindings();

        gameTimer = new Timer(30, this);
        gameTimer.start();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.RUNNING) {
                    paddle.moveLeft();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.RUNNING) {
                    paddle.moveRight();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pauseGame");
        actionMap.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.RUNNING) {
                    state = GameState.PAUSED;
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("R"), "resumeGame");
        actionMap.put("resumeGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.PAUSED) {
                    state = GameState.RUNNING;
                }
            }
        });
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

        if (ball.getShape().getMinX() <= 0) {
            ball.setDirectionX(1);
            ball.setX(0);
        } else if (ball.getShape().getMaxX() >= MAX_X) {
            ball.setDirectionX(-1);
            ball.setX(MAX_X - Ball.DIAMETER);
        }

        if (ball.getShape().getMinY() <= 0) {
            ball.setDirectionY(1);
            ball.setY(0);
        }

        if (paddle.intersects(ball.getShape())) {
            ball.setDirectionY(-1);
            ball.setY(paddle.getPaddle().getMinY() - Ball.DIAMETER);
        }

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
}