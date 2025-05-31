package it.knight.game.utils;

import it.knight.game.model.Coordinates;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Utils {

    public static HttpResponse<String> getAPIResponse(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static Coordinates convertCoordinatesFromCartesian(int matrixLength, int row, int col) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(matrixLength - 1 - col);
        coordinates.setY(row);
        return coordinates;
    }

    public static Coordinates convertCoordinatesToCartesian(int matrixLength, int row, int col) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(col);
        coordinates.setY(matrixLength - 1 - row);
        return coordinates;
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
