package swen.mtcg.repository;

import swen.mtcg.app.model.Collection;
import swen.mtcg.app.model.Elements;
import swen.mtcg.app.model.Rarities;
import swen.mtcg.app.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CollectionRepository extends Repository{

    public Collection loadCollection(){

        Collection collection = new Collection();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name, damage, cardtype, element, monstertype, rarity FROM collection"
                )
        ) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                collection.createCard(
                        resultSet.getString("id"),
                        resultSet.getString("card"),
                        resultSet.getString("name"),
                        Elements.valueOf(resultSet.getString("element")),
                        Rarities.valueOf(resultSet.getString("rarity")),
                        resultSet.getInt("damage"),
                        resultSet.getString("type")
                );
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return collection;
    }
}
