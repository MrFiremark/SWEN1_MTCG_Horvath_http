package swen.mtcg.repository;

import swen.mtcg.app.model.*;

import java.awt.*;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class UserRepository extends Repository{

    public Boolean register(String username, String password) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO player (id, username, password, coin, elo) VALUES (?, ?, ?, ?, ?);"
                )
        ) {
            statement.setObject(1, UUID.randomUUID().toString());
            statement.setString(2, username);
            statement.setString(3, String.valueOf(password.hashCode()));
            statement.setInt(4, 20);
            statement.setInt(5, 100);

            statement.execute();

            return true;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String password) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT " +
                                "   id, username, password, coin, elo " +
                                "   FROM player " +
                                "       WHERE username = ? AND password = ?"
                );
        ) {
            statement.setString(1, username);
            statement.setString(2, String.valueOf(password.hashCode()));
            ResultSet resultSet = statement.executeQuery();

            User user = null;

            if (resultSet.next()) {
                 user = new User(
                         resultSet.getString("id"),
                         resultSet.getString("username"),
                         resultSet.getInt("coin"),
                         resultSet.getInt("elo")
                );
            }

            Objects.requireNonNull(user).setStack(getStack(user.getUserid()));

            Objects.requireNonNull(user).setDeck(getDeck(user.getUserid()));

            return user;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Card> getStack(String userid){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT " +
                                "card.id, card.name, card.damage, card.cardtype, card.element, card.monstertype, card.rarity " +
                                "   FROM stack" +
                                "   JOIN card " +
                                "       ON stack.cardid=card.id AND stack.userid = ?"
                );
        ) {
            statement.setString(1, userid);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Card> stack = new ArrayList<>();

            while (resultSet.next()){

                if(resultSet.getString("cardtype").equals("Monster")){

                    stack.add( new Monster(
                            resultSet.getString("id"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage"),
                            resultSet.getString("monstertype"))
                    );

                }else{

                    stack.add( new Spell(
                            resultSet.getString("id"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage"))
                    );
                }
            }

            return stack;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Deck getDeck(String userid){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT " +
                                "card.id, card.name, card.damage, card.cardtype, card.element, card.monstertype, card.rarity " +
                                "   FROM deck" +
                                "   JOIN card" +
                                "       ON deck.cardid=card.id AND deck.userid = ?"
                )
        ) {
            statement.setString(1, userid);
            ResultSet resultSet = statement.executeQuery();
            Deck deck = new Deck();

            while (resultSet.next()){

                if(resultSet.getString("cardtype").equals("Monster")){

                    deck.addCard( new Monster(
                            resultSet.getString("id"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage"),
                            resultSet.getString("monstertype"))
                    );

                }else{

                    deck.addCard( new Spell(
                            resultSet.getString("id"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage"))
                    );
                }
            }

            return deck;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserData(User user) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE player SET username = ?, coin = ?, elo = ? WHERE id = ?;"
                )
        ) {
            statement.setObject(1, user.getUsername());
            statement.setInt(2, user.getCoins());
            statement.setInt(3, user.getElo());
            statement.setString(4, user.getUserid());

            statement.execute();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserStack(User user) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO stack (userid, cardid) VALUES(?, ?);"
                );
                //PreparedStatement statement1 = connection.prepareStatement("UPDATE stack SET userid = ?, cardid = ?")

        ) {
            ArrayList<Card> stack = new ArrayList<>(user.getStack());

            for (Card card: stack) {
                statement.setString(1, user.getUserid());
                statement.setString(2, card.getId());
                statement.execute();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void createDeck(User user, String cardId){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO deck (userid, cardid) VALUES(?, ?);"
                )
        ) {

            statement.setString(1, user.getUserid());
            statement.setString(2, cardId);

            statement.execute();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDeck(User user, String cardId){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM deck WHERE userid = ?;"
                )
        ) {

            statement.setString(1, user.getUserid());
            statement.execute();

            createDeck(user, cardId);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
