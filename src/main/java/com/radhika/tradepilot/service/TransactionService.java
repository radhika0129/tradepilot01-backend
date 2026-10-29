package com.radhika.tradepilot.service;
import com.radhika.tradepilot.entity.Portfolio;
import com.radhika.tradepilot.repository.PortfolioRepository;
import com.radhika.tradepilot.entity.Transaction;
import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.repository.TransactionRepository;
import com.radhika.tradepilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.radhika.tradepilot.exception.CustomException;
@Service
@Transactional
public class TransactionService {

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Transaction buyStock(Transaction transaction) {

        User user = getLoggedInUser();

        Double livePrice = marketPriceService.getCurrentPrice(
                transaction.getStockSymbol());

        transaction.setPrice(livePrice);

        if (transaction.getQuantity() <= 0) {
            throw new CustomException("Quantity must be greater than 0");
        }

        transaction.setUser(user);
        Portfolio portfolio = portfolioRepository
                .findByUserAndStockSymbol(user, transaction.getStockSymbol())
                .orElse(null);
        if (portfolio != null) {

            int oldQuantity = portfolio.getQuantity();
            int newQuantity = transaction.getQuantity();

            double oldPrice = portfolio.getBuyPrice();
            double newPrice = transaction.getPrice();

            int totalQuantity = oldQuantity + newQuantity;

            double averagePrice =
                    ((oldQuantity * oldPrice) + (newQuantity * newPrice))
                            / totalQuantity;

            portfolio.setQuantity(totalQuantity);
            portfolio.setBuyPrice(averagePrice);

            portfolioRepository.save(portfolio);
        }
        else {

            Portfolio newPortfolio = new Portfolio();

            newPortfolio.setUser(user);
            newPortfolio.setStockName(transaction.getStockName());
            newPortfolio.setStockSymbol(transaction.getStockSymbol());
            newPortfolio.setQuantity(transaction.getQuantity());
            newPortfolio.setBuyPrice(transaction.getPrice());

            portfolioRepository.save(newPortfolio);
        }
        transaction.setType("BUY");
        transaction.setTransactionDate(java.time.LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction sellStock(Long portfolioId, int quantity) {

        User user = getLoggedInUser();

        Portfolio portfolio = portfolioRepository
                .findByIdAndUser(portfolioId, user)
                .orElseThrow(() -> new CustomException("Portfolio not found"));

        if (quantity <= 0) {
            throw new CustomException("Quantity must be greater than 0");
        }

        if (portfolio.getQuantity() < quantity) {
            throw new CustomException("Not enough shares to sell");
        }

        Transaction transaction = new Transaction();

        transaction.setUser(portfolio.getUser());
        transaction.setStockSymbol(portfolio.getStockSymbol());
        transaction.setStockName(portfolio.getStockName());
        transaction.setQuantity(quantity);
        Double livePrice = marketPriceService.getCurrentPrice(
                portfolio.getStockSymbol());

        transaction.setPrice(livePrice);
        transaction.setType("SELL");
        transaction.setTransactionDate(java.time.LocalDateTime.now());

        portfolio.setQuantity(portfolio.getQuantity() - quantity);

        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        return transactionRepository.save(transaction);


    }
    public List<Transaction> getTransactionHistory() {

        User user = getLoggedInUser();

        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }
    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));
    }
}