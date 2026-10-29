package com.radhika.tradepilot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PortfolioRequest {

    @NotBlank(message = "Stock name is required")
    private String stockName;

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @Positive(message = "Buy price must be greater than 0")
    private Double buyPrice;
}