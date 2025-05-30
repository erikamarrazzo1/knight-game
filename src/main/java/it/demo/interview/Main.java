package it.demo.interview;

import it.demo.interview.model.Board;
import it.demo.interview.service.BoardService;
import it.demo.interview.utils.Utils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Utils utils = new Utils();
        BoardService boardService = new BoardService(utils);

        int[][] boardInitialized = boardService.initializeBoard();

        for (int i = boardInitialized.length - 1; i >= 0; i--) {
            for (int j = 0; j < boardInitialized[i].length; j++) {
                System.out.print(boardInitialized[i][j] + " ");
            }
            System.out.println();
        }
    }


}