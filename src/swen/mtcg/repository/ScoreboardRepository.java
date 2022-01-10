package swen.mtcg.repository;

import swen.mtcg.app.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ScoreboardRepository extends Repository{

    public Map<String, String> getScoreboard(){

        Map<String, String> scoreboard = new HashMap<>();

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT elo, id FROM player ORDER BY elo"
                )
        ) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                scoreboard.put(resultSet.getString("id"), String.valueOf(resultSet.getInt("elo")));
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return scoreboard;
    }
}
