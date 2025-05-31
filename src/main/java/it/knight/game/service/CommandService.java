package it.knight.game.service;

import com.google.gson.Gson;
import it.knight.game.exception.InvalidStartPosition;
import it.knight.game.exception.OutOfBoardException;
import it.knight.game.model.Coordinates;
import it.knight.game.model.Position;
import it.knight.game.model.enums.Command;
import it.knight.game.model.enums.Direction;
import it.knight.game.utils.Utils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class CommandService {

    private final static String COMMANDS_API = System.getenv("COMMANDS_API");

    private CommandResponse getCommands(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = Utils.getAPIResponse(uri);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), CommandResponse.class);
    }

    public List<String> getCommandsToExecute() {
        try {
            CommandResponse commandResponse = getCommands(COMMANDS_API);
            return commandResponse.commands;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Position moveUsingDirection(int[][] board,
                                        Position currentPosition,
                                        int increment) {

        Position newPosition;

        switch (currentPosition.getDirection()) {
            case NORTH -> {
                for (int i = 1; i <= increment; i++) {
                    int newX = currentPosition.getX() - 1;
                    int newY = currentPosition.getY();
                    newPosition = getNewPosition(board, currentPosition, newX, newY);

                    if (newPosition != null) {
                        currentPosition = newPosition;
                    } else break;
                }
            }
            case SOUTH -> {
                for (int i = 1; i <= increment; i++) {
                    int newX = currentPosition.getX() + 1;
                    int newY = currentPosition.getY();
                    newPosition = getNewPosition(board, currentPosition, newX, newY);

                    if (newPosition != null) {
                        currentPosition = newPosition;
                    } else break;
                }
            }
            case EAST -> {
                for (int i = 1; i <= increment; i++) {
                    int newX = currentPosition.getX();
                    int newY = currentPosition.getY() + 1;
                    newPosition = getNewPosition(board, currentPosition, newX, newY);

                    if (newPosition != null) {
                        currentPosition = newPosition;
                    } else break;
                }
            }
            case WEST -> {
                for (int i = 1; i <= increment; i++) {
                    int newX = currentPosition.getX();
                    int newY = currentPosition.getY() - 1;
                    newPosition = getNewPosition(board, currentPosition, newX, newY);

                    if (newPosition != null) {
                        currentPosition = newPosition;
                    } else break;
                }
            }
        };

        return currentPosition;
    }

    public Position executePlayCommand(int[][] board,
                                       Position currentPosition,
                                       String command) {
        Position newPosition = new Position();

        String[] commandSplit = command.split(" ");
        Command commandToExecute = Command.valueOf(commandSplit[0]);

        switch (commandToExecute) {
            case START:
                String[] startPositionData = commandSplit[1].split(",");
                int startX = Integer.parseInt(startPositionData[0]);
                int startY = Integer.parseInt(startPositionData[1]);

                if (startX < 0 || startX > board[0].length ||
                        startY < 0 || startY > board.length) {
                    throw new InvalidStartPosition("The start position is out of the board.");
                }

                Direction startDirection = Direction.valueOf(startPositionData[2]);

                Coordinates coordinatesConverted =
                        Utils.convertCoordinatesFromCartesian(board.length, startX, startY);

                if (board[coordinatesConverted.getX()][coordinatesConverted.getY()] == 1) {
                    throw new InvalidStartPosition("The start position is on an obstacle.");
                }

                board[coordinatesConverted.getX()][coordinatesConverted.getY()] = 2;
                newPosition.setX(coordinatesConverted.getX());
                newPosition.setY(coordinatesConverted.getY());
                newPosition.setDirection(startDirection);

                break;
            case ROTATE:

                Direction newDirection = Direction.valueOf(commandSplit[1]);
                newPosition.setX(currentPosition.getX());
                newPosition.setY(currentPosition.getY());
                newPosition.setDirection(newDirection);

                break;
            case MOVE:

                int increment = Integer.parseInt(commandSplit[1]);
                newPosition = moveUsingDirection(board, currentPosition, increment);

                break;
            default: throw new RuntimeException("Command not supported.");
        }

        return newPosition;
    }

    private Position getNewPosition(int[][] board,
                                    Position position,
                                    int newX,
                                    int newY) {
        try {
            if (board[newX][newY] == 1) {
                return null;
            } else {
                board[newX][newY] = 2;
                position.setX(newX);
                position.setY(newY);
            }
        } catch (RuntimeException e) {
            if (e instanceof ArrayIndexOutOfBoundsException) {
                throw new OutOfBoardException("The knight is out of the board.");
            }
            throw new RuntimeException(e);
        }

        return position;
    }

    record CommandResponse(List<String> commands) {}

}

