package it.knight.game.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.knight.game.exception.InvalidStartPosition;
import it.knight.game.exception.OutOfBoardException;
import it.knight.game.model.Coordinates;
import it.knight.game.model.Position;
import it.knight.game.model.PositionResult;
import it.knight.game.model.enums.ResultStatus;
import it.knight.game.utils.Utils;

import java.util.List;

public class GameService {

    private final BoardService boardService;
    private final CommandService commandService;

    public GameService(final BoardService boardService,
                       final CommandService commandService) {
        this.boardService = boardService;
        this.commandService = commandService;
    }

    public PositionResult play() {
        PositionResult positionResult = new PositionResult(null);
        try {
            int[][] boardGame = boardService.initializeBoard();

            // used for debug - initial board
            // 0 is a walkable cell
            // 1 is a cell with an obstacle
            Utils.printBoard(boardGame);

            List<String> listCommandsToExecute = commandService.getCommandsToExecute();
            Position position = new Position();
            for (String command : listCommandsToExecute) {
                System.out.println("Run the command: " + command);
                position = commandService.executePlayCommand(boardGame, position, command);
            }

            Coordinates coordinates =
                    Utils.convertCoordinatesToCartesian(boardGame.length, position.getX(), position.getY());
            position.setX(coordinates.getX());
            position.setY(coordinates.getY());

            positionResult.setPosition(position);
            positionResult.setStatus(ResultStatus.SUCCESS);

            // used for debug - board with knight path
            // 0 is a walkable cell
            // 1 is a cell with an obstacle
            // 2 is a cell walked by knight
            Utils.printBoard(boardGame);

        } catch (RuntimeException e) {
            if (e instanceof OutOfBoardException) {
                positionResult.setStatus(ResultStatus.OUT_OF_THE_BOARD);
            } else if (e instanceof InvalidStartPosition) {
                positionResult.setStatus(ResultStatus.INVALID_START_POSITION);
            } else {
                positionResult.setStatus(ResultStatus.GENERIC_ERROR);
            }
        }

        return positionResult;
    }

    public String runPlay() {
        PositionResult positionResult = play();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(positionResult);
    }

}
