import it.knight.game.exception.InvalidStartPosition;
import it.knight.game.exception.OutOfBoardException;
import it.knight.game.model.Position;
import it.knight.game.model.PositionResult;
import it.knight.game.model.enums.ResultStatus;
import it.knight.game.service.BoardService;
import it.knight.game.service.CommandService;
import it.knight.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private BoardService boardService;
    private CommandService commandService;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        boardService = mock(BoardService.class);
        commandService = mock(CommandService.class);
        gameService = new GameService(boardService, commandService);
    }

    @Test
    void test_boardWithNoObstacles_knightMovesCorrectly() {
        when(boardService.initializeBoard()).thenReturn(new int[5][5]);
        when(commandService.getCommandsToExecute()).thenReturn(
                List.of("START 0,0,NORTH", "MOVE 2")
        );
        when(commandService.executePlayCommand(any(), any(), eq("START 0,0,NORTH")))
                .thenAnswer(invocation -> {
                    Position p = new Position();
                    p.setX(4);
                    p.setY(0);
                    p.setDirection(it.knight.game.model.enums.Direction.NORTH);
                    return p;
                });
        when(commandService.executePlayCommand(any(), any(), eq("MOVE 2")))
                .thenAnswer(invocation -> {
                    Position p = invocation.getArgument(1);
                    p.setX(p.getX() - 2);
                    return p;
                });

        PositionResult result = gameService.play();

        assertNotNull(result);
        assertEquals(ResultStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getPosition());
        assertEquals(2, result.getPosition().getX());
        assertEquals(0, result.getPosition().getY());
    }

    @Test
    void test_invalidStartPosition_startPositionOutOfTheBoard_stopGame() {
        when(boardService.initializeBoard()).thenReturn(new int[5][5]);
        when(commandService.getCommandsToExecute()).thenReturn(List.of("START 10,10,NORTH"));
        when(commandService.executePlayCommand(any(), any(), any()))
                .thenThrow(new InvalidStartPosition("The start position is out of the board."));

        PositionResult result = gameService.play();

        assertNull(result.getPosition());
        assertEquals(ResultStatus.INVALID_START_POSITION, result.getStatus());
    }

    @Test
    void test_invalidStartPosition_startPositionOnAnObstacle_stopGame() {
        int[][] board = new int[5][5];
        board[4][0] = 1;
        when(boardService.initializeBoard()).thenReturn(new int[5][5]);
        when(commandService.getCommandsToExecute()).thenReturn(List.of("START 0,0,NORTH"));
        when(commandService.executePlayCommand(any(), any(), any()))
                .thenThrow(new InvalidStartPosition("The start position is on an obstacle."));

        PositionResult result = gameService.play();

        assertNull(result.getPosition());
        assertEquals(ResultStatus.INVALID_START_POSITION, result.getStatus());
    }

    @Test
    void test_outOfTheBoard_stopGame() {
        when(boardService.initializeBoard()).thenReturn(new int[5][5]);
        when(commandService.getCommandsToExecute()).thenReturn(
                List.of("START 0,0,NORTH", "MOVE 10")
        );
        when(commandService.executePlayCommand(any(), any(), any()))
                .thenThrow(new OutOfBoardException("The knight is out of the board."));

        PositionResult result = gameService.play();

        assertNull(result.getPosition());
        assertEquals(ResultStatus.OUT_OF_THE_BOARD, result.getStatus());
    }

    @Test
    void test_GenericError_stopGame() {
        when(boardService.initializeBoard()).thenThrow(new RuntimeException("Unexpected error."));

        PositionResult result = gameService.play();

        assertNull(result.getPosition());
        assertEquals(ResultStatus.GENERIC_ERROR, result.getStatus());
    }
}
