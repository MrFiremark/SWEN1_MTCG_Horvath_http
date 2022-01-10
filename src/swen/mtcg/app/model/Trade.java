package swen.mtcg.app.model;

public class Trade {

    private String tradeId;
    private String userId;
    private String cardId;
    private String cardType;
    private double filter;

    public Trade(String tradeId, String userId, String cardId, String cardType) {
        this.tradeId = tradeId;
        this.userId = userId;
        this.cardId = cardId;
        this.cardType = cardType;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

}
