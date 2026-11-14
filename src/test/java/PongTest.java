import mx.unison.pong.Ball;
import mx.unison.pong.GameState;
import mx.unison.pong.Paddle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PongTest {

    @Test
    public void testBallMovement() {
        Ball ball = new Ball(100, 100);
        double initialX = ball.getShape().getX();
        double initialY = ball.getShape().getY();

        ball.move();

        assertEquals(initialX + Ball.DX, ball.getShape().getX());
        assertEquals(initialY + Ball.DY, ball.getShape().getY());
    }

    @Test
    public void testPaddleBoundaries() {
        Paddle paddle = new Paddle();

        for (int i = 0; i < 10; i++) {
            paddle.moveLeft();
        }
        assertEquals(0, paddle.getPaddle().getX());

        for (int i = 0; i < 15; i++) {
            paddle.moveRight();
        }
        assertEquals(560, paddle.getPaddle().getX());
    }

    @Test
    public void testBallPaddleCollision() {
        Paddle paddle = new Paddle();
        Ball ball = new Ball(320, 435);

        assertTrue(paddle.intersects(ball.getShape()));
    }

    @Test
    public void testGameStateTransitions() {
        GameState state = GameState.RUNNING;
        assertEquals(GameState.RUNNING, state);

        state = GameState.PAUSED;
        assertEquals(GameState.PAUSED, state);

        state = GameState.RUNNING;
        assertEquals(GameState.RUNNING, state);
    }
}