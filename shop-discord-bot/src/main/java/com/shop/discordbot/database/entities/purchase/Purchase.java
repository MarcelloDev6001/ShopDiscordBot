package com.shop.discordbot.database.entities.purchase;

import com.google.cloud.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private long id = 0L;
    private long buyerID = 0L;
    private List<PurchaseItem> items = new ArrayList<>();
    private Timestamp purchaseDate = null;

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

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public long getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(long buyerID) {
        this.buyerID = buyerID;
    }
}
