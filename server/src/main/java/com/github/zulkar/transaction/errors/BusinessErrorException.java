package com.github.zulkar.transaction.errors;

import org.jetbrains.annotations.NotNull;

public class BusinessErrorException extends RuntimeException {
    public BusinessErrorException(@NotNull String message) {
        super(message);
    }
}
