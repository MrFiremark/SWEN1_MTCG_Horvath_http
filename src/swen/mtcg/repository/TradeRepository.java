package swen.mtcg.repository;

import swen.mtcg.app.model.Trade;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeRepository extends Repository{

    public Trade checkTrades(Trade trade){
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, cardid, cardtype, filter, userid FROM trade;"
                )
        ) {

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                trade.setTradeId(resultSet.getString("id"));
                trade.setTradeId(resultSet.getString("cardid"));
                trade.setTradeId(resultSet.getString("cardtype"));
                trade.setTradeId(resultSet.getString("filter"));
                trade.setTradeId(resultSet.getString("userid"));
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return trade;
    }
}
