package com.shop.discordbot.database.entities.purchase.exceptions;

public class ItemNotFound extends RuntimeException {
    public String itemId;

    public ItemNotFound(String itemId, String message) {
        super(message);
        this.itemId = itemId;
    }
}
