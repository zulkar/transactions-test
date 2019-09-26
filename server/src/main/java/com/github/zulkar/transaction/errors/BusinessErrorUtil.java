package com.github.zulkar.transaction.errors;

import com.github.zulkar.transaction.model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public final class BusinessErrorUtil {
    private BusinessErrorUtil() {
    }

    @Contract("_ -> fail")
    public static void throwAlreadyExistsException(@NotNull User user) {
        throw new BusinessErrorException("User already exists");
    }

    @Contract("_ -> fail")
    public static void throwUserNotExists(@Nullable User user) {
        throw new BusinessErrorException("User not exists");
    }

    @Contract("_ -> fail")
    public static void throwTransferAmountShouldBePositive(@Nullable BigDecimal amount) {
        throw new BusinessErrorException("Transfer amount should be positive");

    }

    @Contract("_, _ -> fail")
    public static void throwNotEnoughMoney(User user, BigDecimal amount) {
        throw new BusinessErrorException("We Require More Minerals");
    }
}
