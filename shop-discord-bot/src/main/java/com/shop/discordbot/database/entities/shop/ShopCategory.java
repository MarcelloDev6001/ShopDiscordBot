package com.shop.discordbot.database.entities.shop;

import com.shop.discordbot.database.entities.purchase.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

public class ShopCategory {
    private String name = "";
    private String description = "";
    private List<PurchaseItem> items = new ArrayList<>();

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
