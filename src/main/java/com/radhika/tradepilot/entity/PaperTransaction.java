package com.radhika.tradepilot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockSymbol;

    private String stockName;

    private Integer quantity;

    private Double price;

    private Double totalAmount;

    private String transactionType;

    private LocalDateTime transactionTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}