package com.shop.discordbot.shop.exceptions;

public class CategoryAlreadyExist extends RuntimeException {
    public CategoryAlreadyExist(String message) {
        super(message);
    }
}
