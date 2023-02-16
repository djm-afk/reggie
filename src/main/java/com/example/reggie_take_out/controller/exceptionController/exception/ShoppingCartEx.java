package com.example.reggie_take_out.controller.exceptionController.exception;

public class ShoppingCartEx extends RuntimeException {

    public ShoppingCartEx(String message) {
        super(message);
    }
}