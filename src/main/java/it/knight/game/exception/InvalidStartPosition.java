package it.knight.game.exception;

public class InvalidStartPosition extends RuntimeException {
    public InvalidStartPosition(String message) {
        super(message);
    }
}
