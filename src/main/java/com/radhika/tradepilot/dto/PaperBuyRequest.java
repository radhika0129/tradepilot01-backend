package com.radhika.tradepilot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperBuyRequest {

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotBlank(message = "Stock name is required")
    private String stockName;

    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
}