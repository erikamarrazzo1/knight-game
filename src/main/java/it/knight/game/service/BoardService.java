package it.knight.game.service;

import it.knight.game.model.Board;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import it.knight.game.model.Coordinates;
import it.knight.game.utils.Utils;

import static it.knight.game.constant.ClientAPIReferences.GET_BOARD_API;

public class BoardService {

    private Board getBoardData(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = Utils.getAPIResponse(uri);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), Board.class);
    }

    public int[][] initializeBoard() {
        int[][] boardInitialized;

        try {
            Board boardData = getBoardData(GET_BOARD_API);
            System.out.println("Initializing board with " + boardData.getHeight() +" rows and " + boardData.getWidth() + " columns...");
            boardInitialized = new int[boardData.getHeight()][boardData.getWidth()];

            for (int i=0; i < boardData.getObstacles().size(); i++) {
                Coordinates obstacle = boardData.getObstacles().get(i);

                // convert coordinates to put the obstacle follow (0,0) bottom left
                int xObstacleInTheBoard = boardData.getHeight() - 1 - obstacle.getY();
                int yObstacleInTheBoard = obstacle.getX();

                // put the obstacle
                if (xObstacleInTheBoard >= 0 && xObstacleInTheBoard < boardData.getHeight() &&
                        yObstacleInTheBoard >= 0 && yObstacleInTheBoard < boardData.getWidth()) {
                    boardInitialized[xObstacleInTheBoard][yObstacleInTheBoard] = 1;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return boardInitialized;
    }

}
