package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.errors.BusinessErrorUtil;
import com.github.zulkar.transaction.model.Balance;
import com.github.zulkar.transaction.model.User;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessingServiceImpl implements ProcessingService {
    private ConcurrentHashMap<User, Balance> database = new ConcurrentHashMap<>();

    @Override
    public void createAccount(User user) {
        validateUsername(user);
        if (database.putIfAbsent(user, new Balance()) != null) {
            BusinessErrorUtil.throwAlreadyExistsException();
        }
    }

    @Override
    public BigDecimal replenish(User user, BigDecimal amount) {
        validateUserActive(user);
        return database.get(user).add(amount);
    }

    @Override
    public void transfer(User from, User to, BigDecimal amount) {
        validateUserActive(from);
        validateUserActive(to);
        validateTransferAmountPositive(amount);

        Balance fromBalance = database.get(from);
        Balance toBalance = database.get(to);
        if (fromBalance.tryBlock(amount)) {
            toBalance.add(amount);
        }
    }

    private void validateTransferAmountPositive(BigDecimal amount) {
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

    @Override
    public Collection<User> getAllUsers() {
        return Collections.list(database.keys());
    }

    @Override
    public BigDecimal getBalance(User user) {
        validateUserActive(user);
        return database.get(user).get();
    }
}
