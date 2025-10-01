package com.shop.discordbot.database.entities.guild;

import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.shop.ShopCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    private long id = 0L;
    private Map<String, Purchase> purchases = new HashMap<>();
    private List<ShopCategory> categories = new ArrayList<>();

    public Guild() {}

    public static Guild getDefault(long id)
    {
        Guild carolDatabaseGuild = new Guild();
        carolDatabaseGuild.setId(id);
        return carolDatabaseGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Map<String, Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<ShopCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ShopCategory> categories) {
        this.categories = categories;
    }
}