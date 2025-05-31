FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

COPY src /app/src

RUN mvn clean package

FROM openjdk:17

WORKDIR /app

ENV GET_COMMANDS_API=http://default.com/api/commands
ENV GET_BOARD_API=http://default.com/api/board

COPY --from=build /app/target/knight-game-1.0-SNAPSHOT.jar ./knight-game.jar

CMD ["java", "-jar", "knight-game.jar"]