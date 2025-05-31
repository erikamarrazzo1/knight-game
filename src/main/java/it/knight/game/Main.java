package it.knight.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.knight.game.model.PositionResult;
import it.knight.game.service.BoardService;
import it.knight.game.service.CommandService;
import it.knight.game.service.GameService;

public class Main {
    public static void main(String[] args) {

        BoardService boardService = new BoardService();
        CommandService commandService = new CommandService();
        GameService gameService = new GameService(boardService, commandService);

        String result = gameService.runPlay();
        System.out.println(result);
    }


}