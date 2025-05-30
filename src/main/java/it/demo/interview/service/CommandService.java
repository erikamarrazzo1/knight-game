package it.demo.interview.service;

import com.google.gson.Gson;
import it.demo.interview.model.Board;
import it.demo.interview.model.Position;
import it.demo.interview.model.enums.Command;
import it.demo.interview.model.enums.Direction;
import it.demo.interview.utils.Utils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class CommandService {

    public final Utils utils;

    CommandService(Utils utils) {
        this.utils = utils;
    }

    private CommandResponse getCommands(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = utils.getAPIResponse(uri);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), CommandResponse.class);
    }

    private Position moveUsingDirection(int[][] board,
                                        Position currentPosition,
                                        int increment) {
        switch (currentPosition.getDirection()) {
            case NORTH -> {
                for (int i = 1; i <= increment; i++) {
                    if (board[currentPosition.getX()][currentPosition.getY()+i] == 1) {
                        break;
                    } else {
                        currentPosition.setY(currentPosition.getY()+1);
                    }
                }
            }
            case SOUTH -> {
                for (int i = 1; i <= increment; i++) {
                    if (board[currentPosition.getX()][currentPosition.getY()-i] == 1) {
                        break;
                    } else {
                        currentPosition.setY(currentPosition.getY()-1);
                    }
                }
            }
            case EAST -> {
                for (int i = 1; i <= increment; i++) {
                    if (board[currentPosition.getX()+1][currentPosition.getY()] == 1) {
                        break;
                    } else {
                        currentPosition.setX(currentPosition.getX()+1);
                    }
                }
            }
            case WEST -> {
                for (int i = 1; i <= increment; i++) {
                    if (board[currentPosition.getX()-1][currentPosition.getY()] == 1) {
                        break;
                    } else {
                        currentPosition.setX(currentPosition.getX()-1);
                    }
                }
            }
        };

        return currentPosition;
    }

    public Position getNewPositionOfTheKnight(Board board,
                                              int[][] boardInitialized,
                                              Position currentPosition,
                                              String command) {
        Position newPosition = new Position();

        String[] commandSplit = command.split(" ");
        Command commandOperation = Command.convertFromString(commandSplit[0]);
        switch (commandOperation) {
            case START:
                String[] startPositionData = commandSplit[1].split(",");
                int startX = Integer.parseInt(startPositionData[0]);
                int startY = Integer.parseInt(startPositionData[1]);
                if (startX < 0 ||
                        startY > board.getWidth() ||
                        startY < 0 ||
                        startY > board.getHeight()) {
                    return null;
                }
                Direction startDirection = Direction.valueOf(startPositionData[2]);
                newPosition.setX(startX);
                newPosition.setY(startY);
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
                newPosition = moveUsingDirection(boardInitialized, currentPosition, increment);
                break;
            default: return null;
        }


        return newPosition;
    }

    record CommandResponse(List<String> commands) {}

}

