package com.radhika.tradepilot.dto;

public class PaperHoldingResponse {

    private Double currentPrice;
    private Double profitLoss;
    private String stockSymbol;
    private String stockName;
    private Integer quantity;
    private Double averageBuyPrice;
    private Double profitLossPercentage;
    private String profitLossStatus;

    public String getProfitLossStatus() {
        return profitLossStatus;
    }

    public void setProfitLossStatus(String profitLossStatus) {
        this.profitLossStatus = profitLossStatus;
    }

    public Double getProfitLossPercentage() {
        return profitLossPercentage;
    }

    public void setProfitLossPercentage(Double profitLossPercentage) {
        this.profitLossPercentage = profitLossPercentage;
    }

    public Double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(Double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void setAverageBuyPrice(Double averageBuyPrice) {
        this.averageBuyPrice = averageBuyPrice;
    }
}