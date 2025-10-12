package com.shop.discordbot.shop;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.shop.CategoryAddStatus;
import com.shop.discordbot.database.entities.shop.CategoryDeleteStatus;
import com.shop.discordbot.database.entities.shop.CategoryUpdateStatus;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.shop.exceptions.CategoryAlreadyExist;
import com.shop.discordbot.shop.exceptions.CategoryNotFound;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class CategoriesManager {
    public static List<ShopCategory> getCategoriesOfGuild(Guild guild)
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        return dbGuild.getCategories();
    }

    public static void updateCategoryOfGuild(Guild guild, String categoryName, String newCategoryName, String newCategoryDescription)
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        List<ShopCategory> categories = dbGuild.getCategories();

        for (ShopCategory category : categories)
        {
            if (category.getName().equals(categoryName))
            {
                category.setName(newCategoryName);
                category.setDescription(newCategoryDescription);

                dbGuild.updateCategory(categoryName, category);
                return;
            }
        }

        throw new CategoryNotFound("Category " + categoryName + " not found!");
    }

    public static void deleteCategoryOfGuild(Guild guild, String categoryName)
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());

        CategoryDeleteStatus status = dbGuild.deleteCategory(categoryName);
        if (status == CategoryDeleteStatus.NOT_FOUND)
        {
            throw new CategoryNotFound("Category " + categoryName + " not found!");
        } else if (status == CategoryDeleteStatus.FAIL) {
            throw new CategoryNotFound("Unknown error!");
        }
    }

    public static void createCategoryForGuild(Guild guild, String name, String description) throws CategoryAlreadyExist
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());

        ShopCategory newCategory = new ShopCategory();
        newCategory.setName(name);
        newCategory.setDescription(description);
        newCategory.setItems(new ArrayList<>());

        CategoryAddStatus status = dbGuild.addCategory(newCategory);
        if (status == CategoryAddStatus.ALREADY_EXIST)
        {
            throw new CategoryAlreadyExist("Category " + name + " already exist!");
        }
        else if (status == CategoryAddStatus.FAIL)
        {
            throw new CategoryAlreadyExist("Unknown error!");
        }
    }
}
