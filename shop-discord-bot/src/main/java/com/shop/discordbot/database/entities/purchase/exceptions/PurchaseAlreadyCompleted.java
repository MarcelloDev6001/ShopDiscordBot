package com.shop.discordbot.database.entities.purchase.exceptions;

public class PurchaseAlreadyCompleted extends RuntimeException {
    public PurchaseAlreadyCompleted(String message) {
        super(message);
    }
}
