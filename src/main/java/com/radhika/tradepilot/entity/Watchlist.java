package com.radhika.tradepilot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "watchlist")
@Data
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;

    private String stockSymbol;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}