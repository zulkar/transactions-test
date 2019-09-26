package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.errors.BusinessErrorUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public final class Utils {
    private Utils() {
    }


    @Contract("null -> fail")
    public static void validateUserNotNull(@Nullable String username) {
        if (username == null || username.isEmpty()) {
            BusinessErrorUtil.throwUserNotExists(null);
        }
    }

    @Contract("null -> fail")
    public static void validateAmountNotNull(@Nullable BigDecimal amount) {
        if (amount == null) {
            BusinessErrorUtil.throwTransferAmountShouldBePositive(null);
        }
    }
}
