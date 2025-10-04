package com.shop.discordbot.database.entities.purchase;

import com.google.cloud.Timestamp;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.exceptions.ItemNotFound;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseAlreadyCompleted;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseCancelledOrRefunded;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseNotConfirmed;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Purchase {
    private long id = 0L;
    private String title = "";
    private long buyerID = 0L;
    private long sellerGuildOwnerID = 0L;
    private PurchaseStatus status = PurchaseStatus.PENDING;
    private List<PurchaseItem> items = new ArrayList<>();
    private Timestamp purchaseDate = null;

    public Purchase(long id, long buyerID, Guild guild, List<Long> itemsIDs, boolean needConfirmation)
    {
        this.id = id;
        this.buyerID = buyerID;
        this.sellerGuildOwnerID = guild.getOwnerIdLong();

        List<Long> itemsFound = new ArrayList<>();

        if (needConfirmation) { status = PurchaseStatus.NEED_CONFIRMATION; }

        for (ShopCategory category : FirebaseManager.getOrCreateGuild(guild.getIdLong()).getCategories())
        {
            for (PurchaseItem item : category.getItems())
            {
                if (itemsIDs.contains(item.getId())) {
                    itemsFound.add(item.getId());
                }
            }
        }

        // we can use any type of sort here, what matter is they are equals and on the same order to check
        itemsIDs.sort(Comparator.reverseOrder());
        itemsFound.sort(Comparator.reverseOrder());

        for (int i = 0; i < itemsIDs.size(); i++)
        {
            if (!itemsIDs.get(i).equals(itemsFound.get(i)))
            {
                throw new ItemNotFound(
                        itemsIDs.get(i),
                        "Item not found: " + itemsIDs.get(i)
                );
            }
        }

        this.purchaseDate = Timestamp.now();
        updateOnFirestore();
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
        if (getStatus() == PurchaseStatus.CANCELLED || getStatus() == PurchaseStatus.REFUNDED)
        {
            throw new PurchaseCancelledOrRefunded("Cannot complete a Purchase that was cancelled/refunded");
        }
        if (getStatus() == PurchaseStatus.COMPLETED)
        {
            throw new PurchaseAlreadyCompleted("Purchase " + getId() + " is already completed");
        }
        if (getStatus() == PurchaseStatus.NEED_CONFIRMATION)
        {
            throw new PurchaseNotConfirmed("Purchase " + getId() + " needs confirmation before can be completed");
        }
        setStatus(PurchaseStatus.COMPLETED);
        updateOnFirestore();
    }

    public PurchaseCreateStatus createOnFirestore()
    {
        Purchase purchase = FirebaseManager.getPurchaseFromDatabase(this.id);
        if (purchase != null)
        {
            return PurchaseCreateStatus.ALREADY_EXIST;
        }

        try {
            FirebaseManager.updatePurchase(this.id, this);
            return PurchaseCreateStatus.SUCCESS;
        } catch (Exception _) {
            return PurchaseCreateStatus.FAIL;
        }
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
