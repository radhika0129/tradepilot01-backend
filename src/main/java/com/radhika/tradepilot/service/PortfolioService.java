package com.radhika.tradepilot.service;
import java.util.List;
import com.radhika.tradepilot.entity.Portfolio;
import com.radhika.tradepilot.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.radhika.tradepilot.dto.PortfolioSummaryResponse;
import java.util.ArrayList;
import com.radhika.tradepilot.dto.PortfolioResponse;
import com.radhika.tradepilot.dto.PortfolioRequest;


@Service
public class PortfolioService {

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    public PortfolioResponse addStock(PortfolioRequest request) {

        User user = getLoggedInUser();

        Portfolio portfolio = new Portfolio();
        portfolio.setStockName(request.getStockName());
        portfolio.setStockSymbol(request.getStockSymbol());
        portfolio.setQuantity(request.getQuantity());
        portfolio.setBuyPrice(request.getBuyPrice());
        portfolio.setUser(user);

        Portfolio saved = portfolioRepository.save(portfolio);

        PortfolioResponse response = new PortfolioResponse();
        response.setId(saved.getId());
        response.setStockName(saved.getStockName());
        response.setStockSymbol(saved.getStockSymbol());
        response.setQuantity(saved.getQuantity());
        response.setBuyPrice(saved.getBuyPrice());

        return response;
    }
    public List<PortfolioResponse> getPortfolio() {

        User user = getLoggedInUser();

        List<Portfolio> portfolios = portfolioRepository.findByUser(user);

        List<PortfolioResponse> responseList = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {

            PortfolioResponse response = new PortfolioResponse();

            response.setId(portfolio.getId());
            response.setStockName(portfolio.getStockName());
            response.setStockSymbol(portfolio.getStockSymbol());
            response.setQuantity(portfolio.getQuantity());
            response.setBuyPrice(portfolio.getBuyPrice());

            responseList.add(response);
        }

        return responseList;
    }
    public String deleteStock(Long id) {

        User user = getLoggedInUser();

        Portfolio portfolio = portfolioRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolioRepository.delete(portfolio);

        return "Stock Deleted Successfully";
    }
    public PortfolioResponse updateStock(Long id, PortfolioRequest request) {

        User user = getLoggedInUser();

        Portfolio portfolio = portfolioRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        portfolio.setStockName(request.getStockName());
        portfolio.setStockSymbol(request.getStockSymbol());
        portfolio.setQuantity(request.getQuantity());
        portfolio.setBuyPrice(request.getBuyPrice());

        Portfolio saved = portfolioRepository.save(portfolio);

        PortfolioResponse response = new PortfolioResponse();
        response.setId(saved.getId());
        response.setStockName(saved.getStockName());
        response.setStockSymbol(saved.getStockSymbol());
        response.setQuantity(saved.getQuantity());
        response.setBuyPrice(saved.getBuyPrice());

        return response;
    }
    public List<PortfolioSummaryResponse> getPortfolioSummary() {

        List<Portfolio> portfolios = portfolioRepository.findByUser(getLoggedInUser());

        List<PortfolioSummaryResponse> summaryList = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {

            PortfolioSummaryResponse response = new PortfolioSummaryResponse();

            response.setStockName(portfolio.getStockName());
            response.setStockSymbol(portfolio.getStockSymbol());
            response.setQuantity(portfolio.getQuantity());
            response.setAverageBuyPrice(portfolio.getBuyPrice());
            double currentPrice = marketPriceService.getCurrentPrice(
                    portfolio.getStockSymbol()
            );

            response.setCurrentPrice(currentPrice);
            double totalInvestment =
                    portfolio.getQuantity() * portfolio.getBuyPrice();

            response.setTotalInvestment(totalInvestment);
            double currentValue =
                    portfolio.getQuantity() * currentPrice;

            response.setCurrentValue(currentValue);
            double profitLoss = currentValue - totalInvestment;

            response.setProfitLoss(profitLoss);
            double profitLossPercentage =
                    (totalInvestment != 0) ? (profitLoss / totalInvestment) * 100 : 0.0;

            response.setProfitLossPercentage(profitLossPercentage);

            summaryList.add(response);
        }

        return summaryList;
    }
    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}