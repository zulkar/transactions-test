package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.model.User;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface ProcessingService {
    void createAccount(@NotNull User user);

    BigDecimal replenish(@NotNull User user, @NotNull BigDecimal amount);

    void transfer(@NotNull User from, @NotNull User to, @NotNull BigDecimal amount);

    @NotNull
    BigDecimal getBalance(@NotNull User user);
}
