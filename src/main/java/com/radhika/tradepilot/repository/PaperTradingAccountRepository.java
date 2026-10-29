package com.radhika.tradepilot.repository;

import com.radhika.tradepilot.entity.PaperTradingAccount;
import com.radhika.tradepilot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaperTradingAccountRepository
        extends JpaRepository<PaperTradingAccount, Long> {

    Optional<PaperTradingAccount> findByUser(User user);

}