package com.shop.discordbot.database.entities.guild;

import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.shop.ShopCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    private long id = 0L;
    private Map<String, Long> purchasesIDs = new HashMap<>();
    private List<ShopCategory> categories = new ArrayList<>();

    public Guild() {}

    public static Guild getDefault(long id)
    {
        Guild defaultGuild = new Guild();
        defaultGuild.setId(id);
        return defaultGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ShopCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ShopCategory> categories) {
        this.categories = categories;
    }

    public Map<String, Long> getPurchasesIDs() {
        return purchasesIDs;
    }

    public void setPurchasesIDs(Map<String, Long> purchasesIDs) {
        this.purchasesIDs = purchasesIDs;
    }
}