package it.demo.interview.model.enums;

public enum Command {
    START,
    MOVE,
    ROTATE;

    public static Command convertFromString(String command) {
        return switch (command) {
            case "START" -> START;
            case "MOVE" -> MOVE;
            case "ROTATE" -> ROTATE;
            default -> null;
        };
    }
}
