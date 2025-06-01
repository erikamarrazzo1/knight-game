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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class GameService {

    private static final Logger logger = LogManager.getLogger(GameService.class);

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

            // 0 is a walkable cell
            // 1 is a cell with an obstacle
            logger.debug("Board initialized with 0 as walkable cells and 1 as obstacle cells:");
            Arrays.stream(boardGame)
                    .map(Arrays::toString)
                    .forEach(logger::debug);

            List<String> listCommandsToExecute = commandService.getCommandsToExecute();
            Position position = new Position();
            for (String command : listCommandsToExecute) {
                logger.debug("Executing command: {}", command);
                position = commandService.executePlayCommand(boardGame, position, command);
            }

            Coordinates coordinates =
                    Utils.convertCoordinatesToCartesian(boardGame.length, position.getX(), position.getY());
            position.setX(coordinates.getX());
            position.setY(coordinates.getY());

            positionResult.setPosition(position);
            positionResult.setStatus(ResultStatus.SUCCESS);

            logger.debug("Board at the end of the game, with 2 indicating cells visited by the knight:");
            Arrays.stream(boardGame)
                    .map(Arrays::toString)
                    .forEach(logger::debug);

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
