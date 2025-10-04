package com.shop.discordbot.commands.objects.categorymanagement;

import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.messages.components.dropdown.DropdownMenuOption;
import com.shop.discordbot.messages.components.modal.ModalObject;
import com.shop.discordbot.messages.components.modal.ModalObjectOnCompleteInteraction;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public abstract class UpdateCategoryModal implements Modal {
    public static Modal create(String id, List<ShopCategory> categories, ModalObjectOnCompleteInteraction onCompleteInteraction)
    {
        ModalObject modalObject = new ModalObject(id, "Updating Category:", onCompleteInteraction);
        List<DropdownMenuOption> options = new ArrayList<>();

        for (ShopCategory category : categories)
        {
            options.add(
                    new DropdownMenuOption(category.getName(), category.getName().toLowerCase(), category.getDescription(), null)
            );
        }

        modalObject.addTextInput("category_selected", "Category name", "Category name", TextInputStyle.SHORT, true);
        modalObject.addTextInput("name_input", "New name", "My new category", TextInputStyle.SHORT, true);
        modalObject.addTextInput("description_input", "new description", "A category of X stuffs", TextInputStyle.PARAGRAPH, true);
        return modalObject.build();
    }
}
