package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.model.User;

import java.math.BigDecimal;
import java.util.Collection;

public interface ProcessingService {
    void createAccount(User user);

    BigDecimal replenish(User user, BigDecimal amount);

    void transfer(User from, User to, BigDecimal amount);

    Collection<User> getAllUsers();

    BigDecimal getBalance(User user);
}
