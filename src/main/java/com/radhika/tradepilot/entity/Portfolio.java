package com.radhika.tradepilot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "portfolio")
@Data
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;

    private String stockSymbol;

    private Integer quantity;

    private Double buyPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}