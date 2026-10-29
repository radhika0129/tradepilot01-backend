package com.radhika.tradepilot.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaperTradingAccountRequest {

    @Positive(message = "Initial balance must be greater than 0")
    private Double initialBalance;
}