package com.shop.discordbot.database.entities.purchase;

import com.google.cloud.Timestamp;
import com.shop.discordbot.database.entities.guild.Guild;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private long id = 0L;
    private long buyerID = 0L;
    private PurchaseStatus status = PurchaseStatus.PENDING;
    private List<PurchaseItem> items = new ArrayList<>();
    private Timestamp purchaseDate = null;

    public static Purchase getDefault(long id, long buyerID)
    {
        Purchase defaultPurchase = new Purchase();
        defaultPurchase.setId(id);
        defaultPurchase.setBuyerID(buyerID);
        defaultPurchase.setStatus(PurchaseStatus.PENDING);
        defaultPurchase.setItems(new ArrayList<>());
        defaultPurchase.setPurchaseDate(Timestamp.now());
        return defaultPurchase;
    }

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

    public PurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseStatus status) {
        this.status = status;
    }
}
