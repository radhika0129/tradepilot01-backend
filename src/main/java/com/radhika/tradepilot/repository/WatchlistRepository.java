package com.radhika.tradepilot.repository;

import com.radhika.tradepilot.entity.User;
import com.radhika.tradepilot.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUser(User user);

    Optional<Watchlist> findByUserAndStockSymbol(User user, String stockSymbol);
}