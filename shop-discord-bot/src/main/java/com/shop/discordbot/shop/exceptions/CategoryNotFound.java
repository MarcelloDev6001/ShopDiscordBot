package com.shop.discordbot.shop.exceptions;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(String message) {
        super(message);
    }
}
