package com.shop.discordbot.database.entities.purchase.exceptions;

public class PurchaseCancelledOrRefunded extends RuntimeException {
    public PurchaseCancelledOrRefunded(String message) {
        super(message);
    }
}
