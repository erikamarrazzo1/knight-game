package it.knight.game;

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