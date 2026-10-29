package com.radhika.tradepilot.controller;

import java.util.List;

import com.radhika.tradepilot.dto.PortfolioRequest;
import com.radhika.tradepilot.dto.PortfolioResponse;
import com.radhika.tradepilot.dto.PortfolioSummaryResponse;
import com.radhika.tradepilot.service.PortfolioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/add")
    public PortfolioResponse addStock(
            @Valid @RequestBody PortfolioRequest request) {

        return portfolioService.addStock(request);
    }

    @GetMapping
    public List<PortfolioResponse> getPortfolio() {

        return portfolioService.getPortfolio();
    }

    @GetMapping("/summary")
    public List<PortfolioSummaryResponse> getPortfolioSummary() {

        return portfolioService.getPortfolioSummary();
    }

    @DeleteMapping("/{id}")
    public String deleteStock(@PathVariable Long id) {

        return portfolioService.deleteStock(id);
    }

    @PutMapping("/{id}")
    public PortfolioResponse updateStock(
            @PathVariable Long id,
            @Valid @RequestBody PortfolioRequest request) {

        return portfolioService.updateStock(id, request);
    }
}