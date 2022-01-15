package swen.mtcg.repository;

import swen.mtcg.app.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ScoreboardRepository extends Repository{

    public List<String> getScoreboard(String username){

        List<String> scoreboard = new ArrayList<>();

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT row_number() over (ORDER BY elo DESC) as rank, elo, username from player LIMIT 100"
                );
                PreparedStatement statement1 = connection.prepareStatement(
                        "SELECT * FROM (SELECT row_number() over (ORDER BY elo DESC) as rank, elo, username from player) a WHERE username = ?;"
                )
        ) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String stringBuilder = "Rank " +
                        resultSet.getString("rank") +
                        " - " +
                        "User: " +
                        resultSet.getString("username") +
                        " | ELO: " +
                        resultSet.getInt("elo");
                scoreboard.add(stringBuilder);

            }

            statement1.setString(1, username);
            resultSet = statement1.executeQuery();
            scoreboard.add("");
            if (resultSet.next()){
                String stringBuilder = "YOUR RANK -> " +
                        "Rank " +
                        resultSet.getString("rank") +
                        " - " +
                        "User: " +
                        resultSet.getString("username") +
                        " | ELO: " +
                        resultSet.getInt("elo");
                scoreboard.add(stringBuilder);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return scoreboard;
    }
}
