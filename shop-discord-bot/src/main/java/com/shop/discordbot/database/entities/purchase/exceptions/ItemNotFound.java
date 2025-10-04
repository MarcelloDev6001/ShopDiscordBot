package com.shop.discordbot.database.entities.purchase.exceptions;

public class ItemNotFound extends RuntimeException {
    public long itemId;

    public ItemNotFound(long itemId, String message) {
        super(message);
        this.itemId = itemId;
    }
}
