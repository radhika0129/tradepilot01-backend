package com.radhika.tradepilot.dto;

import lombok.Data;

@Data
public class PaperTradingAccountResponse {

    private Double initialBalance;

    private Double balance;

    private String message;

}