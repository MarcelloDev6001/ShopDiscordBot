package com.shop.discordbot.database.entities.purchase;

import com.google.cloud.Timestamp;
import com.shop.discordbot.database.FirebaseManager;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private long id = 0L;
    private String title = "";
    private long buyerID = 0L;
    private long sellerGuildOwnerID = 0L;
    private PurchaseStatus status = PurchaseStatus.PENDING;
    private List<PurchaseItem> items = new ArrayList<>();
    private Timestamp purchaseDate = null;

    public static Purchase getDefault(long id, long buyerID, long sellerGuildOwnerID)
    {
        Purchase defaultPurchase = new Purchase();
        defaultPurchase.setId(id);
        defaultPurchase.setBuyerID(buyerID);
        defaultPurchase.setSellerGuildOwnerID(sellerGuildOwnerID);
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

    public void addItem(PurchaseItem item)
    {
        items.add(item);
    }

    public void addItem(long id, String name, String details)
    {
        PurchaseItem newItem = new PurchaseItem();
        newItem.setId(id);
        newItem.setName(name);
        newItem.setDetails(details);
        items.add(newItem);
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

    public void markAsCompleted()
    {
        if (getStatus() == PurchaseStatus.CANCELLED)
        {
            System.out.println("Cannot complete a Purchase that was cancelled");
            return;
        }
        if (getStatus() == PurchaseStatus.COMPLETED)
        {
            System.out.println("Purchase " + getId() + " is already completed");
            return;
        }
        setStatus(PurchaseStatus.COMPLETED);
        updateOnFirestore();
    }

    public PurchaseUpdateStatus updateOnFirestore()
    {
        try {
            FirebaseManager.updatePurchase(this.id, this);
            return PurchaseUpdateStatus.SUCCESS;
        } catch (Exception e) {
            return PurchaseUpdateStatus.FAIL;
        }
    }

    public long getSellerGuildOwnerID() {
        return sellerGuildOwnerID;
    }

    public void setSellerGuildOwnerID(long sellerGuildOwnerID) {
        this.sellerGuildOwnerID = sellerGuildOwnerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
