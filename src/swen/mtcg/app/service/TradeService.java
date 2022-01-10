package swen.mtcg.app.service;

import swen.mtcg.app.model.Trade;
import swen.mtcg.app.model.User;

import java.util.HashMap;
import java.util.Map;

public class TradeService {

    private Map<String, Trade> trades = new HashMap<>();

    public void addTrade(Trade trade){
        trades.put(trade.getTradeId(), trade);
        System.out.println(trades.toString());
    }


    public Trade getTrade(String tradeid){
        return trades.get(tradeid);
    }

    public void remoceTrade(String tradeid){
        trades.remove(tradeid);
    }

}
