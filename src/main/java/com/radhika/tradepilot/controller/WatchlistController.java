package com.radhika.tradepilot.controller;

import com.radhika.tradepilot.entity.Watchlist;
import com.radhika.tradepilot.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/add")
    public Watchlist addToWatchlist(@RequestBody Watchlist watchlist) {

        return watchlistService.addToWatchlist(watchlist);
    }
    @GetMapping
    public List<Watchlist> getWatchlist() {

        return watchlistService.getWatchlist();
    }
    @DeleteMapping("/{id}")
    public String removeFromWatchlist(@PathVariable Long id) {

        return watchlistService.removeFromWatchlist(id);
    }
}