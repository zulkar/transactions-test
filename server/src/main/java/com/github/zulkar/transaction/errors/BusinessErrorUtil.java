package com.github.zulkar.transaction.errors;

import com.github.zulkar.transaction.model.User;

import java.math.BigDecimal;

public final class BusinessErrorUtil {
    private BusinessErrorUtil(){}

    public static void throwAlreadyExistsException(){

    }

    public static void throwUserNotExists(User user) {

    }

    public static void throwTransferAmountShouldBePositive(BigDecimal amount) {

    }
}
