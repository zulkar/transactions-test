package com.github.zulkar.transaction.errors;

import org.jetbrains.annotations.NotNull;

public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, @NotNull String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
