package com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions;

public class ProductRetrievalException extends RuntimeException{
    public ProductRetrievalException(String message, Exception e) {
        super(message);
    }
}
