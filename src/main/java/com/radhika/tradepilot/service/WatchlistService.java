package com.radhika.tradepilot.service;

import com.radhika.tradepilot.repository.WatchlistRepository;
import com.radhika.tradepilot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.radhika.tradepilot.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import com.radhika.tradepilot.entity.Watchlist;
import java.util.Optional;
import java.util.List;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public Watchlist addToWatchlist(Watchlist watchlist) {

        User user = getLoggedInUser();

        Optional<Watchlist> existingWatchlist =
                watchlistRepository.findByUserAndStockSymbol(
                        user,
                        watchlist.getStockSymbol()
                );

        if (existingWatchlist.isPresent()) {
            throw new RuntimeException("Stock already exists in watchlist");
        }

        watchlist.setUser(user);

        return watchlistRepository.save(watchlist);
    }
    public List<Watchlist> getWatchlist() {

        User user = getLoggedInUser();

        return watchlistRepository.findByUser(user);
    }
    public String removeFromWatchlist(Long id) {

        User user = getLoggedInUser();

        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (!watchlist.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this stock");
        }

        watchlistRepository.delete(watchlist);

        return "Stock removed from watchlist successfully";
    }

}