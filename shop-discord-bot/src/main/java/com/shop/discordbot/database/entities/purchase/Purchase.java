package com.shop.discordbot.database.entities.purchase;

import com.google.cloud.Timestamp;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.exceptions.ItemNotFound;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseAlreadyCompleted;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseCancelledOrRefunded;
import com.shop.discordbot.database.entities.purchase.exceptions.PurchaseNotConfirmed;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import net.dv8tion.jda.api.entities.Guild;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Purchase {
    private String id = "";
    private String title = "";
    private long buyerID = 0L;
    private long sellerGuildOwnerID = 0L;
    private PurchaseStatus status = PurchaseStatus.PENDING;
    private List<PurchaseItem> items = new ArrayList<>();
    private Timestamp purchaseDate = null;

    // thanks connorpark24 (and the coders of the discussion) for optimizing this function here
    // https://github.com/orgs/community/discussions/175829#discussioncomment-14593964
    public Purchase(long buyerID, Guild guild, List<Long> itemIDs, boolean needConfirmation) throws ItemNotFound {
        this.id = UUID.randomUUID().toString();
        this.buyerID = buyerID;
        this.sellerGuildOwnerID = guild.getOwnerIdLong();
        this.purchaseDate = Timestamp.now();

        if (needConfirmation) {
            status = PurchaseStatus.NEED_CONFIRMATION;
        }

        // Flatten all items in all categories for this guild into a single map for quick lookup
        Map<String, PurchaseItem> allItems = FirebaseManager.getOrCreateGuild(guild.getIdLong())
                .getCategories().stream()
                .flatMap(cat -> cat.getItems().stream())
                .collect(Collectors.toMap(PurchaseItem::getId, Function.identity()));

        // Verify that all requested item IDs exist
        for (Long id : itemIDs) {
            if (!allItems.containsKey(id)) {
                throw new ItemNotFound(id, "Item not found: " + id);
            }
        }

        createOnFirestore();
    }

    public String getId() {
        return id;
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

    public void addItem(String id, String name, String details)
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
        System.out.println(purchase);
        System.out.println(purchase != null);
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
