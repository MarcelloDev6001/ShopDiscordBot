package com.shop.discordbot.database.entities.guild;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.purchase.PurchaseUpdateStatus;
import com.shop.discordbot.database.entities.shop.CategoryAddStatus;
import com.shop.discordbot.database.entities.shop.CategoryDeleteStatus;
import com.shop.discordbot.database.entities.shop.CategoryUpdateStatus;
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

    public ShopCategory getCategory(String name)
    {
        for (ShopCategory category : getCategories())
        {
            if (category.getName().equals(name))
            {
                return category;
            }
        }
        return null;
    }

    public CategoryAddStatus addCategory(ShopCategory category)
    {
        try {
            for (ShopCategory cat : getCategories())
            {
                if (cat.getName().equals(category.getName()))
                {
                    return CategoryAddStatus.ALREADY_EXIST;
                }
            }
            categories.add(category);
            updateOnFirestore();
            return CategoryAddStatus.SUCCESS;
        } catch (Exception e) {
            return CategoryAddStatus.FAIL;
        }
    }

    public CategoryAddStatus addCategories(List<ShopCategory> categories)
    {
        try {
            this.categories.addAll(categories);
            updateOnFirestore();
            return CategoryAddStatus.SUCCESS;
        } catch (Exception e) {
            return CategoryAddStatus.FAIL;
        }
    }

    public CategoryUpdateStatus updateCategory(String name, ShopCategory newCategory)
    {
        try {
            for (ShopCategory category : categories)
            {
                if (category.getName() == name)
                {
                    categories.set(categories.indexOf(category), newCategory);
                    updateOnFirestore();
                    return CategoryUpdateStatus.SUCCESS;
                }
            }
            return CategoryUpdateStatus.NOT_FOUND;
        } catch (Exception _) {
            return CategoryUpdateStatus.FAIL;
        }
    }

    public CategoryDeleteStatus deleteCategory(String categoryName)
    {
        try {
            for (ShopCategory category : categories)
            {
                if (category.getName().equals(categoryName))
                {
                    categories.remove(category);
                    updateOnFirestore();
                    return CategoryDeleteStatus.SUCCESS;
                }
            }
            return CategoryDeleteStatus.NOT_FOUND;
        } catch (Exception _) {
            return CategoryDeleteStatus.FAIL;
        }
    }

    public void updateOnFirestore()
    {
        try {
            FirebaseManager.updateGuild(this.id, this);
        } catch (Exception _) {}
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