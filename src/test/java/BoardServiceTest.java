import com.google.gson.Gson;
import it.knight.game.model.Board;
import it.knight.game.model.Coordinates;
import it.knight.game.service.BoardService;
import it.knight.game.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    private BoardService boardService;
    private HttpResponse<String> mockResponse;

    @BeforeEach
    void setUp() {
        boardService = new BoardService();
        mockResponse = mock(HttpResponse.class);
    }

    @Test
    void test_initializeBoard_withValidObstacles() {
        Board board = new Board();
        board.setWidth(5);
        board.setHeight(5);

        Coordinates coordinate1 = new Coordinates();
        coordinate1.setX(1);
        coordinate1.setY(1);
        Coordinates coordinate2 = new Coordinates();
        coordinate2.setX(0);
        coordinate2.setY(4);
        board.setObstacles(List.of(coordinate1, coordinate2));

        String jsonResponse = new Gson().toJson(board);
        when(mockResponse.body()).thenReturn(jsonResponse);

        try (MockedStatic<Utils> mockedStatic = Mockito.mockStatic(Utils.class)) {
            mockedStatic.when(() -> Utils.getAPIResponse(anyString())).thenReturn(mockResponse);

            mockedStatic.when(() -> Utils.convertCoordinatesFromCartesian(anyInt(), anyInt(), anyInt()))
                    .thenAnswer(invocation -> {
                        int height = invocation.getArgument(0);
                        int x = invocation.getArgument(1);
                        int y = invocation.getArgument(2);
                        Coordinates coordinates = new Coordinates();
                        coordinates.setX(height - 1 - y);
                        coordinates.setY(x);
                        return coordinates;
                    });

            int[][] result = boardService.initializeBoard();

            assertEquals(5, result.length);
            assertEquals(5, result[0].length);

            assertEquals(1, result[3][1]);
            assertEquals(1, result[0][0]);

            assertEquals(0, result[2][2]);
        }
    }

    @Test
    void test_initializeBoard_withObstacleOutOfBounds_shouldIgnoreThem() throws Exception {
        Board board = new Board();
        board.setWidth(3);
        board.setHeight(3);

        Coordinates coordinate1 = new Coordinates();
        coordinate1.setX(1);
        coordinate1.setY(1);

        Coordinates coordinate2 = new Coordinates();
        coordinate2.setX(5);
        coordinate2.setY(5);

        Coordinates coordinate3 = new Coordinates();
        coordinate3.setX(-1);
        coordinate3.setY(2);

        board.setObstacles(List.of(coordinate1, coordinate2, coordinate3));

        String boardJson = new Gson().toJson(board);

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(boardJson);

        try (MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.getAPIResponse(anyString())).thenReturn(mockResponse);
            utilsMock.when(() -> Utils.convertCoordinatesFromCartesian(anyInt(), anyInt(), anyInt()))
                    .thenAnswer(invocation -> {
                        int height = invocation.getArgument(0);
                        int x = invocation.getArgument(1);
                        int y = invocation.getArgument(2);
                        Coordinates coordinates = new Coordinates();
                        coordinates.setX(height - 1 - y);
                        coordinates.setY(x);
                        return coordinates;
                    });

            int[][] result = boardService.initializeBoard();

            assertEquals(1, result[1][1]);
            assertDoesNotThrow(() -> boardService.initializeBoard());
        }
    }

    @Test
    void test_initializeBoard_httpsCall_fail() throws Exception {
        try (MockedStatic<Utils> mockedStatic = Mockito.mockStatic(Utils.class)) {
            mockedStatic.when(() -> Utils.getAPIResponse(nullable(String.class)))
                    .thenThrow(new IOException("Connection error."));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> boardService.initializeBoard());

            assertInstanceOf(IOException.class, exception.getCause());
            assertEquals("Connection error.", exception.getCause().getMessage());
        }
    }
}
