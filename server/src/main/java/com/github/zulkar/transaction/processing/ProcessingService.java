package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.model.User;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;

public interface ProcessingService {
    void createAccount(User user);

    BigDecimal replenish(@NotNull User user, @NotNull BigDecimal amount);

    void transfer(@NotNull User from, @NotNull User to, @NotNull BigDecimal amount);

    Collection<User> getAllUsers();

    BigDecimal getBalance(@NotNull User user);
}
