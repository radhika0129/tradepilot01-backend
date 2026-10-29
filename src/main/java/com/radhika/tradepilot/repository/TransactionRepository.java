package com.radhika.tradepilot.repository;

import com.radhika.tradepilot.entity.Transaction;
import com.radhika.tradepilot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

}