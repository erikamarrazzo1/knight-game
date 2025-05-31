package it.knight.game.model;

import java.util.List;

public class Board {

    private int width;
    private int height;
    private List<Coordinates> obstacles;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Coordinates> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Coordinates> obstacles) {
        this.obstacles = obstacles;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Board{" +
                "width=" + width +
                ", height=" + height +
                ", obstacles=" + obstacles +
                '}';
    }
}
