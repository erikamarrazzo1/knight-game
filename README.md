# KNIGHT BOARD GAME

## Overview

The knight-game project involves managing a grid-based board populated with obstacles, fetched dynamically from an external API. The main goal was to build a robust, testable, and maintainable service that initializes the game board correctly according to the provided configuration.

# Documentation

The main objective of this project was to build a service responsible for initializing a game board based on data retrieved from an external API. The board consists of a grid with certain positions marked as obstacles. To implement this, several design considerations and implementation steps were followed.

The first step was to understand the structure of the board and how obstacle data is represented. The external API returns the board dimensions (width and height) along with a list of obstacle coordinates. These coordinates use a Cartesian system with the origin (0,0) located at the bottom-left corner of the board.

In Java, multidimensional arrays are indexed from the top-left corner, which required converting these Cartesian coordinates into array indices that correctly reflect the obstacle positions within the 2D array representing the board.
A `Board` model class was created to hold the board’s properties, including width, height, and a list of obstacles, each represented by a `Coordinates` class with x and y fields.

The `BoardService` class was designed to encapsulate the logic of fetching board data and initializing the game board:

- **API Endpoint Configuration**: the API endpoint URL is configured through an environment variable named BOARD_API.
- **Data Fetching**: The service fetches JSON data from the API using an HTTP client encapsulated within a utility method (Utils.getAPIResponse). The raw JSON is parsed into the `Board` object using the Gson library.
- **Coordinate Conversion**: A dedicated utility method converts Cartesian coordinates to array indices. This is critical for placing obstacles accurately within the board array, compensating for the difference in coordinate system origins.
- **Board Initialization**: The service initializes a two-dimensional integer array with the board’s dimensions. It then iterates over the obstacle list, converting coordinates and marking the corresponding positions in the array with a 1 to indicate an obstacle.
- **Bounds Checking**: Before marking obstacles, the service verifies that converted coordinates are in the board boundaries to prevent runtime errors.
- **Error Handling**: Exceptions thrown during HTTP requests or JSON parsing are caught and wrapped in runtime exceptions, ensuring clear failure signals during execution.

The `CommandService` class is designed to handle the processing of movement commands for the Knight in the game. Its main responsibility is to interpret commands given as strings, validate whether the move is legal within the current game board constraints, and then update the knight's position accordingly. Key functionalities are:
- **Command Interpretation**: the service accepts commands as strings, typically representing directions such as "NORTH", "SOUTH", "EAST", and "WEST". It maps these commands to positional changes on the board.
- **Movement Validation**: for each command, `CommandService` calculates the target coordinates based on the knight’s current position. It checks that the move stays within the board boundaries and does not collide with any obstacles.
- **State Update**: if the move is valid, the knight’s position is updated to the new coordinates. Otherwise, an error is returned specifying the problem (the new position is out of the board).
  The service depends on the BoardService to retrieve the current state of the board, including its dimensions and the placement of obstacles. It keeps track of the knight's current position.
  When `executeCommand(String command)` is called, it parses the command string and computes the new position for the knight based on the command; verifies if the new position is valid: within board bounds and not occupied by an obstacle; updates the knight’s position if valid: if there is an obstacle it stops the move in the last valid position. If the move is out of boundaries, return an exception managed by main method.

The `GameService` is responsible for processing player commands, updating the game state, and determining the result of the game.


## Run Locally

Clone the project

```bash
  git clone https://github.com/erikamarrazzo1/knight-game.git
```

Go to the project directory

```bash
  cd knight-game
```

Build application

```bash
  docker build -t knight_board:latest .
```

Start the server

```bash
  docker run -e BOARD_API=board-api-url -e COMMANDS_API=command-api-url knight_board:latest
```

