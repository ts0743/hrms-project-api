package com.osi.hris.exception;

public class LeaveBalanceInsufficientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LeaveBalanceInsufficientException(String message) {
        super(message);
    }
}
