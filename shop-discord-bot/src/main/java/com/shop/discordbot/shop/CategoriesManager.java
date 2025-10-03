package com.shop.discordbot.shop;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class CategoriesManager {
    public static void createCategoryForGuild(Guild guild, String name, String description)
    {
        com.shop.discordbot.database.entities.guild.Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        List<ShopCategory> categories = dbGuild.getCategories();

        ShopCategory newCategory = new ShopCategory();
        newCategory.setName(name);
        newCategory.setDescription(description);
        newCategory.setItems(new ArrayList<>());
        categories.add(newCategory);

        FirebaseManager.updateGuild(guild.getIdLong(), dbGuild);
    }
}
