package com.radhika.tradepilot.dto;

import lombok.Data;

@Data
public class PortfolioSummaryResponse {

    private String stockName;
    private String stockSymbol;
    private Integer quantity;
    private Double averageBuyPrice;
    private Double currentPrice;
    private Double totalInvestment;
    private Double currentValue;
    private Double profitLoss;
    private Double profitLossPercentage;
}