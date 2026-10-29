package com.radhika.tradepilot.controller;

import com.radhika.tradepilot.dto.PaperBuyRequest;
import com.radhika.tradepilot.dto.PaperBuyResponse;
import com.radhika.tradepilot.service.PaperTradingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.radhika.tradepilot.dto.PaperTradingAccountRequest;
import com.radhika.tradepilot.dto.PaperTradingAccountResponse;
import com.radhika.tradepilot.dto.PaperSellRequest;
import com.radhika.tradepilot.dto.PaperSellResponse;
import com.radhika.tradepilot.entity.PaperTransaction;
import java.util.List;
import com.radhika.tradepilot.entity.PaperPortfolio;
import com.radhika.tradepilot.dto.PaperHoldingResponse;
import com.radhika.tradepilot.dto.PaperTransactionResponse;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/paper-trading")
public class PaperTradingAccountController {

    @Autowired
    private PaperTradingAccountService paperTradingAccountService;

    @PostMapping("/start")
    public PaperTradingAccountResponse startPaperTrading(
            @Valid @RequestBody PaperTradingAccountRequest request) {

        return paperTradingAccountService.createPaperTradingAccount(request);
    }
    @GetMapping
    public PaperTradingAccountResponse getPaperTradingAccount() {

        return paperTradingAccountService.getPaperTradingAccount();

    }
    @GetMapping("/portfolio")
    public List<PaperHoldingResponse> getHoldings() {

        return paperTradingAccountService.getHoldings();
    }

    @PutMapping("/reset")
    public PaperTradingAccountResponse resetPaperTradingAccount(
            @Valid @RequestBody PaperTradingAccountRequest request) {

        return paperTradingAccountService.resetPaperTradingAccount(request);
    }
    @PostMapping("/buy")
    public PaperBuyResponse buyStock(
            @Valid @RequestBody PaperBuyRequest request) {

        return paperTradingAccountService.buyStock(request);
    }
    @GetMapping("/transactions")
    public List<PaperTransactionResponse> getPaperTransactionHistory() {

        return paperTradingAccountService.getPaperTransactionHistory();
    }

    @PostMapping("/sell")
    public PaperSellResponse sellStock(
            @Valid @RequestBody PaperSellRequest request) {

        return paperTradingAccountService.sellStock(request);
    }

}