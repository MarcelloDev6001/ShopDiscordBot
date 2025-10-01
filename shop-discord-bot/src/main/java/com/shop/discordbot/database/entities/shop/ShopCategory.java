package com.shop.discordbot.database.entities.shop;

import com.shop.discordbot.database.entities.purchase.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class ShopCategory {
    private long id = 0L;
    private List<PurchaseItem> items = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }
}
