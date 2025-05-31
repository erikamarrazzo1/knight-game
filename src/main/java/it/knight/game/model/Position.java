package it.knight.game.model;

import it.knight.game.model.enums.Direction;
import it.knight.game.utils.Utils;

public class Position {

    private int x;
    private int y;
    private Direction direction;

    public Position() {}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void convertCoordinatesToCartesian(int matrixLength) {
        Utils.convertCoordinatesToCartesian(matrixLength, this.x, this.y);
    }
}
