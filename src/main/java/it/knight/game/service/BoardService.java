package it.knight.game.service;

import it.knight.game.model.Board;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import it.knight.game.model.Coordinates;
import it.knight.game.utils.Utils;

public class BoardService {

    private final static String BOARD_API = System.getenv().getOrDefault("BOARD_API", "http://localhost:8080");

    private Board fetchBoardData(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = Utils.getAPIResponse(uri);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), Board.class);
    }

    public int[][] initializeBoard() {
        int[][] boardInitialized;

        try {
            Board boardData = fetchBoardData(BOARD_API);
            System.out.println("Initializing board with " + boardData.getHeight() +" rows and " + boardData.getWidth() + " columns...");
            boardInitialized = new int[boardData.getHeight()][boardData.getWidth()];

            for (int i=0; i < boardData.getObstacles().size(); i++) {
                Coordinates obstacle = boardData.getObstacles().get(i);

                // convert coordinates to put the obstacle follow (0,0) bottom left
                Coordinates coordinatesConverted =
                        Utils.convertCoordinatesFromCartesian(boardData.getHeight(), obstacle.getX(), obstacle.getY());

                // put the obstacle
                if (coordinatesConverted.getX() >= 0 && coordinatesConverted.getX() < boardData.getHeight() &&
                        coordinatesConverted.getY() >= 0 && coordinatesConverted.getY() < boardData.getWidth()) {
                    boardInitialized[coordinatesConverted.getX()][coordinatesConverted.getY()] = 1;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return boardInitialized;
    }

}
