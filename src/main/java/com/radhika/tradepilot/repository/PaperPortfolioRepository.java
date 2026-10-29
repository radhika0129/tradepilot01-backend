package com.radhika.tradepilot.repository;

import com.radhika.tradepilot.entity.PaperPortfolio;
import com.radhika.tradepilot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaperPortfolioRepository extends JpaRepository<PaperPortfolio, Long> {

    Optional<PaperPortfolio> findByUserAndStockSymbol(User user, String stockSymbol);

    List<PaperPortfolio> findByUser(User user);
}