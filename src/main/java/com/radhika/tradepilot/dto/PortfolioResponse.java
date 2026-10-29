package com.radhika.tradepilot.dto;

import lombok.Data;

@Data
public class PortfolioResponse {

    private Long id;
    private String stockName;
    private String stockSymbol;
    private Integer quantity;
    private Double buyPrice;
}