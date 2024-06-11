package com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions;

public class CustomUpdateStockException extends RuntimeException{
    public CustomUpdateStockException(String message, Exception e) {
        super(message);
    }
}
