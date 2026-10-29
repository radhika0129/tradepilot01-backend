package com.radhika.tradepilot.repository;

import com.radhika.tradepilot.entity.PaperTransaction;
import com.radhika.tradepilot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaperTransactionRepository
        extends JpaRepository<PaperTransaction, Long> {

    List<PaperTransaction> findByUser(User user);

    List<PaperTransaction> findByUserOrderByTransactionTimeDesc(User user);
}