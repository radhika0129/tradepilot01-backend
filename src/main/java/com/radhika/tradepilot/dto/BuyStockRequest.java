package com.radhika.tradepilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class BuyStockRequest {

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotBlank(message = "Stock name is required")
    private String stockName;

    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

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
}