package com.radhika.tradepilot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperBuyResponse {

    private String stockSymbol;

    private String stockName;

    private Integer quantity;

    private Double price;

    private Double totalAmount;

    private Double remainingBalance;

    private String message;

}