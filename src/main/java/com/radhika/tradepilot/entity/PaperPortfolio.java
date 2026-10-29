package com.radhika.tradepilot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockSymbol;

    private String stockName;

    private Integer quantity;

    private Double averageBuyPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}