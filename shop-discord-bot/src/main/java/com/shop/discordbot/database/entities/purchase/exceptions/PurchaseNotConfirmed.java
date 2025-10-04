package com.shop.discordbot.database.entities.purchase.exceptions;

public class PurchaseNotConfirmed extends RuntimeException {
    public PurchaseNotConfirmed(String message) {
        super(message);
    }
}
