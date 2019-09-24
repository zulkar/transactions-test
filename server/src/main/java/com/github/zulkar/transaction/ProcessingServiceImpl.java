package com.github.zulkar.transaction;

import com.github.zulkar.transaction.errors.BusinessErrorUtil;
import com.github.zulkar.transaction.model.Balance;
import com.github.zulkar.transaction.model.User;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessingServiceImpl {
    private ConcurrentHashMap<User, Balance> database = new ConcurrentHashMap<>();

    public void createAccount(User user, BigDecimal initialBalance) {
        validateUsername(user);
        if (database.putIfAbsent(user, new Balance(initialBalance)) != null) {
            BusinessErrorUtil.throwAlreadyExistsException();
        }
    }

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
}
