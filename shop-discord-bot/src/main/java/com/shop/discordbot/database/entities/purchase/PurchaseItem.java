package com.shop.discordbot.database.entities.purchase;

public class PurchaseItem {
    private String id = "";
    private String name = "";
    private String details = "";

    // this here will be visible to the buyer ONCE he completes the purchase.
    // ex: this item is a roblox account, then the secret details will be the username and the password for this roblox account
    private String secretDetails = "";

    public PurchaseItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSecretDetails() {
        return secretDetails;
    }

    public void setSecretDetails(String secretDetails) {
        this.secretDetails = secretDetails;
    }
}
