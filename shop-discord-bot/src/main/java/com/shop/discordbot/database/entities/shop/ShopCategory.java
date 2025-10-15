package com.shop.discordbot.database.entities.shop;

import com.shop.discordbot.database.entities.purchase.PurchaseItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void addItem(PurchaseItem item)
    {
        items.add(item);
    }

    public void addItem(String name, String details, String secretDetails)
    {
        PurchaseItem item = new PurchaseItem();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setDetails(details);
        item.setSecretDetails(secretDetails);
        items.add(item);
    }

    public PurchaseItem getItem(String name)
    {
        for (PurchaseItem item : getItems())
        {
            if (item.getName().equals(name))
            {
                return item;
            }
        }
        return null;
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
