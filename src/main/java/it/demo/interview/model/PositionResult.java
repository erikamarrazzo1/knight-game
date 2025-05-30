package it.demo.interview.model;

import it.demo.interview.model.enums.ResultStatus;

public class PositionResult {

    private Position position;
    private ResultStatus status;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
