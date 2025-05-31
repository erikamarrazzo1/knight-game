import it.knight.game.exception.InvalidStartPosition;
import it.knight.game.exception.OutOfBoardException;
import it.knight.game.model.Position;
import it.knight.game.model.enums.Direction;
import it.knight.game.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandServiceTest {

    private CommandService commandService;

    @BeforeEach
    void setUp() {
        commandService = new CommandService();
    }

    @Test
    void test_setStartPosition_success() {
        int[][] board = new int[5][5];

        Position result = commandService.executePlayCommand(board, new Position(), "START 0,0,NORTH");

        assertEquals(4, result.getX());
        assertEquals(0, result.getY());

        assertEquals(Direction.NORTH, result.getDirection());
        assertEquals(2, board[4][0]);
    }

    @Test
    void test_setStartPosition_failed_coordinatesHasAnObstacle() {
        int[][] board = new int[5][5];
        board[4][0] = 1;

        assertThrows(InvalidStartPosition.class,
                () -> commandService.executePlayCommand(board, new Position(), "START 0,0,NORTH"));
    }

    @Test
    void test_rotateKnight_success() {
        Position current = new Position();
        current.setX(4);
        current.setY(0);
        current.setDirection(Direction.NORTH);

        Position result = commandService.executePlayCommand(new int[5][5], current, "ROTATE EAST");

        assertEquals(4, result.getX());
        assertEquals(0, result.getY());
        assertEquals(Direction.EAST, result.getDirection());
    }

    @Test
    void test_moveKnight_withoutObstacles_success() {
        int[][] board = new int[5][5];
        Position position = new Position();
        position.setX(4);
        position.setY(0);
        position.setDirection(Direction.NORTH);

        Position result = commandService.executePlayCommand(board, position, "MOVE 2");

        assertEquals(2, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void test_moveKnight_withObstacle_stopped() {
        int[][] board = new int[5][5];
        board[1][2] = 1;
        Position position = new Position();
        position.setX(3);
        position.setY(2);
        position.setDirection(Direction.NORTH);

        Position result = commandService.executePlayCommand(board, position, "MOVE 3");

        assertEquals(2, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void test_moveKnight_outOfTheBoard_exception() {
        int[][] board = new int[5][5];
        Position position = new Position();
        position.setX(0);
        position.setY(0);
        position.setDirection(Direction.NORTH);

        assertThrows(OutOfBoardException.class,
                () -> commandService.executePlayCommand(board, position, "MOVE 2"));
    }
}
