package com.shop.discordbot.commands.category;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.commands.objects.categorymanagement.CreateCategoryModal;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.messages.components.button.MessageButton;
import com.shop.discordbot.shop.CategoriesManager;
import com.shop.discordbot.shop.exceptions.CategoryAlreadyExist;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementCommand extends Command {
    public CategoryManagementCommand() {
        super("category-management", "Manage all of your categories");
        setGuildOnly(true);
        setPermissionsRequired(List.of(Permission.ADMINISTRATOR));
    }

    private void handleCreateCategory(ModalInteraction interaction, User user) {
        try {
            String name = interaction.getValue("name_input").getAsString();
            String description = interaction.getValue("description_input").getAsString();

            try {
                CategoriesManager.createCategoryForGuild(interaction.getGuild(), name, description);
            } catch (CategoryAlreadyExist e) {
                interaction.reply("Error: Category `" + name + "` already exists!").setEphemeral(true).queue();
                return;
            }
            interaction.reply("Category `" + name + "` created successfully!").setEphemeral(true).queue();
        } catch (Exception e) {
            interaction.reply("Unknown Error: " + e).setEphemeral(true).queue();
        }
    }

    private void createCategory(ButtonInteraction interaction, User user) {
        if (CategoriesManager.getCategoriesOfGuild(interaction.getGuild()).size() >= CategoriesManager.maxCategoriesPerGuild)
        {
            interaction.reply("Error: You passed the limit of categories per guild (" + Integer.toString(CategoriesManager.maxCategoriesPerGuild) + ")")
                    .setEphemeral(true).queue();

            return;
        }
        interaction.replyModal(
                CreateCategoryModal.create("create_category_modal_" + interaction.getId(), this::handleCreateCategory)
        ).queue();
    }

    private void manageCategories(ButtonInteraction interaction, User user) {}

    private void deleteCategory(ButtonInteraction interaction, User user) {}

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        if (interaction.getGuild() == null) {
            interaction.reply("This command can only be used on a guild!").setEphemeral(true).queue();
            return;
        }

        MessageButton createCategoryButton = new MessageButton(
                ButtonStyle.SUCCESS,
                "create_category_" + interaction.getId(),
                "Create Category",
                this::createCategory,
                ""
        );

        MessageButton manageCategoriesButton = new MessageButton(
                ButtonStyle.SECONDARY,
                "manage_category_" + interaction.getId(),
                "Manage Category",
                this::manageCategories,
                ""
        );

        MessageButton deleteCategoryButton = new MessageButton(
                ButtonStyle.DANGER,
                "delete_category_" + interaction.getId(),
                "Delete Category",
                this::deleteCategory,
                ""
        );

        interaction.reply("").setActionRow(
                createCategoryButton.toComponentButton(),
                manageCategoriesButton.toComponentButton(),
                deleteCategoryButton.toComponentButton()
        ).setEphemeral(true).queue();
    }
}
