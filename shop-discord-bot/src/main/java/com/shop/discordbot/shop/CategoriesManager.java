package com.shop.discordbot.shop;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.shop.exceptions.CategoryAlreadyExist;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class CategoriesManager {
    public static final int maxCategoriesPerGuild = 10;

    public static List<ShopCategory> getCategoriesOfGuild(Guild guild)
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        return dbGuild.getCategories();
    }

    public static void createCategoryForGuild(Guild guild, String name, String description) throws CategoryAlreadyExist
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        List<ShopCategory> categories = dbGuild.getCategories();

        for (ShopCategory category : categories)
        {
            if (category.getName().equals(name))
            {
                throw new CategoryAlreadyExist("Category " + name + " already exist!");
            }
        }

        ShopCategory newCategory = new ShopCategory();
        newCategory.setName(name);
        newCategory.setDescription(description);
        newCategory.setItems(new ArrayList<>());
        categories.add(newCategory);

        FirebaseManager.updateGuild(guild.getIdLong(), dbGuild);
    }
}
