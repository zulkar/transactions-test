package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.errors.BusinessException;

public class StatusMessage {

    public static final StatusMessage OK = new StatusMessage(0, "OK");
    private final int status;
    private final String message;

    public StatusMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusMessage(BusinessException e) {
        this.status = e.getCode();
        this.message = e.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
