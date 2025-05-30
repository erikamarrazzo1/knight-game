package it.demo.interview.service;

import it.demo.interview.model.Board;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import it.demo.interview.model.Obstacle;
import it.demo.interview.utils.Utils;

import static it.demo.interview.constant.ClientAPIReferences.GET_BOARD_API;

public class BoardService {

    public final Utils utils;

    public BoardService(Utils utils) {
        this.utils = utils;
    }

    private Board getBoardData(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = utils.getAPIResponse(uri);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), Board.class);
    }

    public int[][] initializeBoard() {
        int[][] boardInitialized = null;

        try {
            Board boardData = getBoardData(GET_BOARD_API);
            boardInitialized = new int[boardData.getWidth()][boardData.getHeight()];
            for (int i=0; i < boardData.getObstacles().size(); i++) {
                Obstacle obstacle = boardData.getObstacles().get(i);
                boardInitialized[obstacle.getX()][obstacle.getY()] = 1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return boardInitialized;
    }

}
