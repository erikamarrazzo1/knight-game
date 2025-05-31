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

    public static final String COMMANDS_API = System.getenv().getOrDefault("COMMANDS_API", "http://localhost:8080");

    public List<String> getCommandsToExecute() {
        try {
            return fetchCommands(COMMANDS_API).commands();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch commands", e);
        }
    }

    public Position executePlayCommand(int[][] board, Position currentPosition, String command) {
        String[] commandParts = command.split(" ");
        Command commandToExecute = Command.valueOf(commandParts[0]);

        return switch (commandToExecute) {
            case START -> handleStartCommand(board, commandParts[1]);
            case ROTATE -> handleRotateCommand(currentPosition, commandParts[1]);
            case MOVE -> handleMoveCommand(board, currentPosition, Integer.parseInt(commandParts[1]));
        };
    }

    private CommandResponse fetchCommands(String uri) throws IOException, InterruptedException {
        HttpResponse<String> response = Utils.getAPIResponse(uri);
        return new Gson().fromJson(response.body(), CommandResponse.class);
    }

    private Position handleStartCommand(int[][] board, String data) {
        String[] parts = data.split(",");
        int startX = Integer.parseInt(parts[0]);
        int startY = Integer.parseInt(parts[1]);
        Direction direction = Direction.valueOf(parts[2]);

        validateStartPosition(board, startX, startY);

        Coordinates coordinatesConverted = Utils.convertCoordinatesFromCartesian(board.length, startX, startY);
        if (board[coordinatesConverted.getX()][coordinatesConverted.getY()] == 1) {
            throw new InvalidStartPosition("The start position is on an obstacle.");
        }

        board[coordinatesConverted.getX()][coordinatesConverted.getY()] = 2;
        return new Position(coordinatesConverted.getX(), coordinatesConverted.getY(), direction);
    }

    private void validateStartPosition(int[][] board, int x, int y) {
        if (x < 0 || x >= board[0].length || y < 0 || y >= board.length) {
            throw new InvalidStartPosition("The start position is out of the board.");
        }
    }

    private Position handleRotateCommand(Position currentPosition, String direction) {
        Direction newDirection = Direction.valueOf(direction);
        return new Position(currentPosition.getX(), currentPosition.getY(), newDirection);
    }

    private Position handleMoveCommand(int[][] board, Position currentPosition, int steps) {
        for (int i = 0; i < steps; i++) {
            int newX = currentPosition.getX();
            int newY = currentPosition.getY();

            switch (currentPosition.getDirection()) {
                case NORTH -> newX--;
                case SOUTH -> newX++;
                case EAST -> newY++;
                case WEST -> newY--;
            }

            if (!isValidMove(board, newX, newY)) {
                break;
            }

            board[newX][newY] = 2;
            currentPosition.setX(newX);
            currentPosition.setY(newY);
        }

        return currentPosition;
    }

    private boolean isValidMove(int[][] board, int x, int y) {
        try {
            return board[x][y] != 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new OutOfBoardException("The knight is out of the board.");
        }
    }

    private record CommandResponse(List<String> commands) {}
}
