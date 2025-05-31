package it.knight.game.model;

import it.knight.game.model.enums.ResultStatus;

public class PositionResult {

    private Position position;
    private ResultStatus status;

    public PositionResult(ResultStatus status) {
        this.status = status;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

}
