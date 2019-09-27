package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.errors.BusinessErrorUtil;
import com.github.zulkar.transaction.model.Balance;
import com.github.zulkar.transaction.model.User;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessingServiceImpl implements ProcessingService {
    private ConcurrentHashMap<User, Balance> database = new ConcurrentHashMap<>();

    @Override
    public void createAccount(@NotNull User user) {
        validateUsername(user);
        if (database.putIfAbsent(user, new Balance()) != null) {
            BusinessErrorUtil.throwAlreadyExistsException(user);
        }

    }

    @Override
    public BigDecimal replenish(@NotNull User user, @NotNull BigDecimal amount) {
        validateUserActive(user);
        validateAmountPositive(amount);
        return database.get(user).add(amount);
    }

    @Override
    public void transfer(@NotNull User from, @NotNull User to, @NotNull BigDecimal amount) {
        validateUserActive(from);
        validateUserActive(to);
        validateAmountPositive(amount);

        Balance fromBalance = database.get(from);
        Balance toBalance = database.get(to);

        if (fromBalance.tryBlock(amount)) {
            toBalance.add(amount);
        } else {
            BusinessErrorUtil.throwNotEnoughMoney(from, amount);
        }
    }

    private void validateAmountPositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            BusinessErrorUtil.throwTransferAmountShouldBePositive(amount);
        }
    }

    private void validateUserActive(User user) {
        if (!database.containsKey(user)) {
            BusinessErrorUtil.throwUserNotExists(user);
        }
    }

    private void validateUsername(User user) {
        BusinessErrorUtil.throwUserNotExists(user);
    }

    @NotNull
    public Collection<User> getAllUsers() {
        return Collections.list(database.keys());
    }

    @Override
    @NotNull
    public BigDecimal getBalance(@NotNull User user) {
        validateUserActive(user);
        return database.get(user).get();
    }
}
