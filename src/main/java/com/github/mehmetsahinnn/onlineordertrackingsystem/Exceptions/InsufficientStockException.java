package com.github.mehmetsahinnn.onlineordertrackingsystem.Exceptions;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){
        super(message);
    }
}
