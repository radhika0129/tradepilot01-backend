package com.radhika.tradepilot.controller;

import com.radhika.tradepilot.entity.Transaction;
import com.radhika.tradepilot.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.radhika.tradepilot.dto.BuyStockRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/buy")
    public Transaction buyStock(@Valid @RequestBody BuyStockRequest request) {

        Transaction transaction = new Transaction();

        transaction.setStockSymbol(request.getStockSymbol());
        transaction.setStockName(request.getStockName());
        transaction.setQuantity(request.getQuantity());

        return transactionService.buyStock(transaction);
    }
    @PostMapping("/sell/{portfolioId}")
    public ResponseEntity<Transaction> sellStock(
            @PathVariable Long portfolioId,
            @RequestParam int quantity) {

        Transaction transaction = transactionService.sellStock(portfolioId, quantity);

        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory() {

        return ResponseEntity.ok(transactionService.getTransactionHistory());
    }
}