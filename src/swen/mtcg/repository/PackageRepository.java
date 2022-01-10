package swen.mtcg.repository;

import swen.mtcg.app.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageRepository extends Repository{

    public void createPackage(Card[] cards) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO package (packageid, cardid) VALUES (?, ?);"
                )
        ) {
            createPackageCards(cards);

            int packageid = getpackageid();

            for (Card card : cards) {
                statement.setInt(1, packageid);
                statement.setObject(2, card.getId());
                statement.execute();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        //return new User(username, 20, 100);
    }

    public void createPackageCards(Card[] cards){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO card (id, name, damage, cardtype, element, monstertype, rarity) VALUES (?, ?, ?, ?, ?, ?, ?);"
                )
        ) {

            for (Card card : cards) {
                statement.setString(1, card.getId());
                statement.setString(2, card.getName());
                statement.setDouble(3, card.getDamage());
                statement.setString(4, card.getCardType());
                statement.setString(5, card.getElement().toString());
                if (card instanceof Monster){
                    String type = ((Monster) card).getMonsterType();
                    statement.setString(6, type);
                }else {
                    statement.setString(6, null);
                }
                statement.setString(7, card.getRarity().toString());
                statement.execute();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getpackageid(){
        try (
                Connection connection = getConnection();
                PreparedStatement statement1 = connection.prepareStatement(
                        "SELECT MAX(packageid) from package"
                )
        ) {
            ResultSet resultSet = statement1.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1)+1;
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public Card[] buyPackage(Card[] cards){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "Select \n" +
                                "\tb.cardid, name, damage, cardtype, element, monstertype, rarity\n" +
                                "\tFROM (\n" +
                                "\t\t\tSelect \n" +
                                "\t\t\t\tpackageid, cardid \n" +
                                "\t\t\t\t\tfrom package \n" +
                                "\t\t\t\t\tJOIN (\n" +
                                "\t\t\t\t\t\tSelect \n" +
                                "\t\t\t\t\t\t\tMIN(packageid) as min \n" +
                                "\t\t\t\t\t\t\t\tfrom package\n" +
                                "\t\t\t\t\t) a \n" +
                                "\t\t\t\t\t\tON package.packageid=a.min\n" +
                                "\t\t) b\n" +
                                "\t\tJOIN card\t\t\n" +
                                "\t\t\tON b.cardid=card.id;"
                );
                PreparedStatement statement1 = connection.prepareStatement("DELETE FROM package WHERE packageid = (SELECT MIN(packageid) FROM package)")

        ) {
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()){

                if(resultSet.getString("cardtype").equals("Monster")){

                    cards[i] = new Monster(
                            resultSet.getString("cardid"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage"),
                            resultSet.getString("monstertype"));

                }else{

                    cards[i] = new Spell(
                            resultSet.getString("cardid"),
                            resultSet.getString("cardtype"),
                            resultSet.getString("name"),
                            Elements.valueOf(resultSet.getString("element")),
                            Rarities.valueOf(resultSet.getString("rarity")),
                            resultSet.getDouble("damage")
                    );
                }
                i++;
            }

            statement1.execute();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
