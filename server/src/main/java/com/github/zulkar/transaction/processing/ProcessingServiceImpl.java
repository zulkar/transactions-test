package com.github.zulkar.transaction.processing;

import com.github.zulkar.transaction.errors.BusinessErrorUtil;
import com.github.zulkar.transaction.model.Balance;
import com.github.zulkar.transaction.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProcessingServiceImpl implements ProcessingService {
    private final Logger TRANSACTION_LOG = LogManager.getLogger("TRANSACTION");
    private ConcurrentHashMap<User, Balance> database = new ConcurrentHashMap<>();

    @Override
    public void createAccount(@NotNull User user) {
        if (database.putIfAbsent(user, new Balance()) != null) {
            BusinessErrorUtil.throwAlreadyExistsException(user);
        }
        TRANSACTION_LOG.info("Account {} created", user);
    }

    @Override
    public BigDecimal replenish(@NotNull User user, @NotNull BigDecimal amount) {
        validateUserActive(user);
        validateAmountPositive(amount);
        TRANSACTION_LOG.info("Account {} replenished with {}", user, amount);
        return database.get(user).add(amount);
    }

    @Override
    public void transfer(@NotNull User from, @NotNull User to, @NotNull BigDecimal amount) {
        validateUserActive(from);
        validateUserActive(to);
        validateUserDifferent(from, to);
        validateAmountPositive(amount);

        Balance fromBalance = database.get(from);
        Balance toBalance = database.get(to);
        TRANSACTION_LOG.info("trying to transfer {} from {} to {}", amount, from, to);
        if (fromBalance.tryBlock(amount)) {
            toBalance.add(amount);
            TRANSACTION_LOG.info("transferred {} from {} to {}", amount, from, to);
        } else {
            TRANSACTION_LOG.info("transfer {} from {} to {} failed", amount, from, to);
            BusinessErrorUtil.throwNotEnoughMoney(from, amount);
        }
    }

    private void validateUserDifferent(@NotNull User from, @NotNull User to) {
        if (from.equals(to)){
            BusinessErrorUtil.throwCannotTransferSelf(from);
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

    @NotNull
    @Override
    public Map<User, BigDecimal> getAllUsersWithBalance() {
        //can be replaced to transformation on-the fly
        return database.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    @Override
    @NotNull
    public BigDecimal getBalance(@NotNull User user) {
        validateUserActive(user);
        return database.get(user).get();
    }
}
