package com.shop.discordbot.commands.items;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.commands.objects.additem.AddItemModal;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.shop.CategoriesManager;
import com.shop.discordbot.shop.exceptions.CategoryAlreadyExist;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;

import java.util.List;
import java.util.function.BiConsumer;

public class AddItemCommand extends Command {
    public AddItemCommand() {
        super("add-item", "Adds an item to a category");
        setGuildOnly(true);
        setPermissionsRequired(List.of(Permission.ADMINISTRATOR));

        addOption(OptionType.STRING, "category", "The category you will to add the item", true, List.of("categories-names"));
    }

    private void handlerAddItem(ModalInteraction interaction, User user, String category) {
        try {
            interaction.deferReply(true).queue();
            String name = interaction.getValue("name_input").getAsString();
            String description = interaction.getValue("description_input").getAsString();
            String secrets = interaction.getValue("secrets_input").getAsString();

            Guild dbGuild = FirebaseManager.getOrCreateGuild(interaction.getGuild().getIdLong());
            ShopCategory categoryFound = null;
            for (ShopCategory cat : dbGuild.getCategories())
            {
                if (cat.getName().equals(category))
                {
                    categoryFound = cat;
                    break;
                }
            }
            if (categoryFound == null)
            {
                interaction.getHook().sendMessage("Category not found.").setEphemeral(true).queue();
                return;
            }

            categoryFound.addItem(name, description, secrets);
            dbGuild.updateOnFirestore();

            interaction.getHook().sendMessage("Item `" + name + "` added successfully!").setEphemeral(true).queue();
        } catch (Exception e) {
            interaction.reply("Unknown Error: " + e).setEphemeral(true).queue();
        }
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        String category;
        try {
            category = interaction.getOption("category").getAsString();
        } catch (NullPointerException _) {
            interaction.reply("Invalid category name!").setEphemeral(true).queue();
            return;
        }

        if (!interaction.isFromGuild())
        {
            interaction.reply("This command can only be used on a guild!").setEphemeral(true).queue();
            return;
        }

        BiConsumer<ModalInteraction, User> preHandlerAddItem = (modalInteraction, user) -> new Runnable() {
            @Override
            public void run()
            {
                System.out.println("hood moddin");
                handlerAddItem(modalInteraction, user, category);
            }
        }.run();

        interaction.replyModal(AddItemModal.create(
                "creating_item_" + interaction.getId(), category, preHandlerAddItem::accept
        )).queue();
    }
}
