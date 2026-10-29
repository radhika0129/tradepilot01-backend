package com.radhika.tradepilot.service;

import com.radhika.tradepilot.repository.PaperTradingAccountRepository;
import com.radhika.tradepilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.entity.PaperTradingAccount;
import com.radhika.tradepilot.dto.PaperTradingAccountRequest;
import com.radhika.tradepilot.dto.PaperTradingAccountResponse;
import com.radhika.tradepilot.dto.PaperBuyRequest;
import com.radhika.tradepilot.dto.PaperBuyResponse;
import com.radhika.tradepilot.repository.PaperPortfolioRepository;
import com.radhika.tradepilot.entity.PaperPortfolio;
import com.radhika.tradepilot.repository.PaperTransactionRepository;
import com.radhika.tradepilot.entity.PaperTransaction;
import java.time.LocalDateTime;
import com.radhika.tradepilot.dto.PaperSellRequest;
import com.radhika.tradepilot.dto.PaperSellResponse;
import java.util.List;
import com.radhika.tradepilot.dto.PaperHoldingResponse;
import java.util.ArrayList;
import com.radhika.tradepilot.dto.PaperTransactionResponse;
import com.radhika.tradepilot.exception.CustomException;

@Service
public class PaperTradingAccountService {

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private PaperTransactionRepository paperTransactionRepository;

    @Autowired
    private PaperTradingAccountRepository paperTradingAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaperPortfolioRepository paperPortfolioRepository;

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));
    }

    public PaperTradingAccountResponse createPaperTradingAccount(
            PaperTradingAccountRequest request) {

        User user = getLoggedInUser();

        if (paperTradingAccountRepository.findByUser(user).isPresent()) {
            throw new CustomException("Paper Trading Account already exists");
        }

        if (request.getInitialBalance() > 1000000) {
            throw new CustomException(
                    "Maximum virtual balance allowed is ₹10,00,000");
        }

        PaperTradingAccount account = new PaperTradingAccount();

        account.setUser(user);
        account.setInitialBalance(request.getInitialBalance());
        account.setBalance(request.getInitialBalance());

        paperTradingAccountRepository.save(account);

        PaperTradingAccountResponse response = new PaperTradingAccountResponse();

        response.setInitialBalance(account.getInitialBalance());
        response.setBalance(account.getBalance());
        response.setMessage("Paper Trading Account Created Successfully");

        return response;
    }

    public PaperTradingAccountResponse getPaperTradingAccount() {

        User user = getLoggedInUser();

        PaperTradingAccount account =
                paperTradingAccountRepository.findByUser(user)
                        .orElseThrow(() ->
                                new CustomException("Paper Trading Account not found"));

        PaperTradingAccountResponse response =
                new PaperTradingAccountResponse();

        response.setInitialBalance(account.getInitialBalance());
        response.setBalance(account.getBalance());
        response.setMessage("Paper Trading Account Found");

        return response;
    }

    public PaperTradingAccountResponse resetPaperTradingAccount(
            PaperTradingAccountRequest request) {

        User user = getLoggedInUser();

        PaperTradingAccount account =
                paperTradingAccountRepository.findByUser(user)
                        .orElseThrow(() ->
                                new CustomException("Paper Trading Account not found"));

        if (request.getInitialBalance() > 1000000) {
            throw new CustomException(
                    "Maximum virtual balance allowed is ₹10,00,000");
        }

        account.setInitialBalance(request.getInitialBalance());
        account.setBalance(request.getInitialBalance());

        paperTradingAccountRepository.save(account);

        PaperTradingAccountResponse response =
                new PaperTradingAccountResponse();

        response.setInitialBalance(account.getInitialBalance());
        response.setBalance(account.getBalance());
        response.setMessage("Paper Trading Account Reset Successfully");

        return response;
    }

    public PaperBuyResponse buyStock(PaperBuyRequest request) {

        User user = getLoggedInUser();

        if (request.getQuantity() <= 0) {
            throw new CustomException("Quantity must be greater than 0");
        }

        Double livePrice = marketPriceService.getCurrentPrice(
                request.getStockSymbol()
        );

        PaperTradingAccount account =
                paperTradingAccountRepository.findByUser(user)
                        .orElseThrow(() ->
                                new CustomException("Paper Trading Account not found"));

        Double totalAmount =
                request.getQuantity() * livePrice;

        if (account.getBalance() < totalAmount) {
            throw new CustomException("Insufficient Balance");
        }

        PaperPortfolio portfolio =
                paperPortfolioRepository
                        .findByUserAndStockSymbol(
                                user,
                                request.getStockSymbol())
                        .orElse(null);

        if (portfolio == null) {

            portfolio = new PaperPortfolio();

            portfolio.setUser(user);
            portfolio.setStockSymbol(request.getStockSymbol());
            portfolio.setStockName(request.getStockName());
            portfolio.setQuantity(request.getQuantity());
            portfolio.setAverageBuyPrice(livePrice);

        } else {

            Double oldTotal =
                    portfolio.getQuantity()
                            * portfolio.getAverageBuyPrice();

            Double newTotal =
                    request.getQuantity() * livePrice;

            Integer newQuantity =
                    portfolio.getQuantity()
                            + request.getQuantity();

            Double newAveragePrice =
                    (oldTotal + newTotal) / newQuantity;

            portfolio.setQuantity(newQuantity);
            portfolio.setAverageBuyPrice(newAveragePrice);
        }

        paperPortfolioRepository.save(portfolio);

        account.setBalance(
                account.getBalance() - totalAmount);

        paperTradingAccountRepository.save(account);

        PaperTransaction transaction =
                new PaperTransaction();

        transaction.setUser(user);
        transaction.setStockSymbol(request.getStockSymbol());
        transaction.setStockName(request.getStockName());
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(livePrice);
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionType("BUY");
        transaction.setTransactionTime(LocalDateTime.now());

        paperTransactionRepository.save(transaction);

        PaperBuyResponse response =
                new PaperBuyResponse();

        response.setStockSymbol(request.getStockSymbol());
        response.setStockName(request.getStockName());
        response.setQuantity(request.getQuantity());
        response.setPrice(livePrice);
        response.setTotalAmount(totalAmount);
        response.setRemainingBalance(account.getBalance());
        response.setMessage("Stock bought successfully");

        return response;
    }

    public PaperSellResponse sellStock(PaperSellRequest request) {

        User user = getLoggedInUser();

        PaperTradingAccount account =
                paperTradingAccountRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new CustomException("Paper Trading Account not found"));

        PaperPortfolio portfolio =
                paperPortfolioRepository
                        .findByUserAndStockSymbol(
                                user,
                                request.getStockSymbol())
                        .orElseThrow(() ->
                                new CustomException("Stock not found in paper portfolio"));

        if (request.getQuantity() <= 0) {
            throw new CustomException("Quantity must be greater than 0");
        }

        if (portfolio.getQuantity() < request.getQuantity()) {
            throw new CustomException("Not enough shares to sell");
        }

        Double livePrice = marketPriceService.getCurrentPrice(
                request.getStockSymbol()
        );

        Double totalAmount =
                request.getQuantity() * livePrice;
        account.setBalance(
                account.getBalance() + totalAmount);

        portfolio.setQuantity(
                portfolio.getQuantity()
                        - request.getQuantity());

        if (portfolio.getQuantity() == 0) {

            paperPortfolioRepository.delete(portfolio);

        } else {

            paperPortfolioRepository.save(portfolio);
        }

        paperTradingAccountRepository.save(account);

        PaperTransaction transaction =
                new PaperTransaction();

        transaction.setUser(user);
        transaction.setStockSymbol(request.getStockSymbol());
        transaction.setStockName(portfolio.getStockName());
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(livePrice);
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionType("SELL");
        transaction.setTransactionTime(LocalDateTime.now());

        paperTransactionRepository.save(transaction);

        PaperSellResponse response =
                new PaperSellResponse();

        response.setStockSymbol(request.getStockSymbol());
        response.setStockName(portfolio.getStockName());
        response.setQuantity(request.getQuantity());
        response.setPrice(livePrice);
        response.setTotalAmount(totalAmount);
        response.setRemainingBalance(account.getBalance());
        response.setMessage("Stock sold successfully");

        return response;
    }

    public List<PaperTransactionResponse> getPaperTransactionHistory() {

        User user = getLoggedInUser();

        List<PaperTransaction> transactions =
                paperTransactionRepository
                        .findByUserOrderByTransactionTimeDesc(user);

        List<PaperTransactionResponse> responses =
                new ArrayList<>();

        for (PaperTransaction transaction : transactions) {

            PaperTransactionResponse response =
                    new PaperTransactionResponse();

            response.setStockSymbol(
                    transaction.getStockSymbol());

            response.setStockName(
                    transaction.getStockName());

            response.setTransactionType(
                    transaction.getTransactionType());

            response.setQuantity(
                    transaction.getQuantity());

            response.setPrice(
                    transaction.getPrice());

            response.setTotalAmount(
                    transaction.getTotalAmount());

            response.setTransactionTime(
                    transaction.getTransactionTime());

            responses.add(response);
        }

        return responses;
    }

    public List<PaperPortfolio> getPaperPortfolio() {

        User user = getLoggedInUser();

        return paperPortfolioRepository.findByUser(user);
    }

    public List<PaperHoldingResponse> getHoldings() {

        User user = getLoggedInUser();

        List<PaperPortfolio> portfolios =
                paperPortfolioRepository.findByUser(user);

        List<PaperHoldingResponse> responses =
                new ArrayList<>();

        for (PaperPortfolio portfolio : portfolios) {

            PaperHoldingResponse response =
                    new PaperHoldingResponse();

            response.setStockSymbol(
                    portfolio.getStockSymbol());

            response.setStockName(
                    portfolio.getStockName());

            response.setQuantity(
                    portfolio.getQuantity());

            response.setAverageBuyPrice(
                    portfolio.getAverageBuyPrice());

            Double currentPrice =
                    marketPriceService.getCurrentPrice(
                            portfolio.getStockSymbol());

            response.setCurrentPrice(currentPrice);

            Double investment =
                    portfolio.getQuantity()
                            * portfolio.getAverageBuyPrice();

            Double currentValue =
                    portfolio.getQuantity()
                            * currentPrice;

            Double profitLoss =
                    currentValue - investment;

            response.setProfitLoss(profitLoss);

            Double profitLossPercentage =
                    Math.round(
                            (profitLoss / investment) * 10000.0)
                            / 100.0;

            response.setProfitLossPercentage(
                    profitLossPercentage);

            if (profitLoss > 0) {

                response.setProfitLossStatus("Profit");

            } else if (profitLoss < 0) {

                response.setProfitLossStatus("Loss");

            } else {

                response.setProfitLossStatus(
                        "No Profit No Loss");
            }

            responses.add(response);
        }

        return responses;
    }
}