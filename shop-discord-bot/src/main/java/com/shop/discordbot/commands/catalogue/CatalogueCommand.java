package com.shop.discordbot.commands.catalogue;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.purchase.PurchaseItem;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.messages.components.dropdown.DropdownMenuOption;
import com.shop.discordbot.messages.components.dropdown.StringSelectMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CatalogueCommand extends Command {
    public CatalogueCommand() {
        super("catalogue", "See the catalogue of the guild!");
        setGuildOnly(true);
    }

    public void categorySelectHandler(StringSelectInteraction interaction, User user, List<String> choices)
    {
        interaction.deferReply(true).queue();
        Guild dbGuild = FirebaseManager.getOrCreateGuild(interaction.getGuild().getIdLong());
        ShopCategory category = dbGuild.getCategory(choices.getFirst());

        if (category == null)
        {
            interaction.getHook().editOriginal("Category not found!").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Catalogue of " + category.getName())
                .setDescription("");

        for (int i = 0; i < category.getItems().size(); i++)
        {
            PurchaseItem item = category.getItems().get(i);
            embedBuilder.getDescriptionBuilder().append(i).append(". ").append(item.getName()).append("\n");
        }

        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        if (interaction.getGuild() == null)
        {
            interaction.reply("This command can only be used on a guild!").setEphemeral(true).queue();
            return;
        }

        Guild dbGuild = FirebaseManager.getOrCreateGuild(interaction.getGuild().getIdLong());
        List<ShopCategory> guildCategories = dbGuild.getCategories();
        if (guildCategories.isEmpty())
        {
            interaction.reply("This guild doesn't have categories").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder catalogueBuilder = new EmbedBuilder()
                .setTitle("Catalogue of " + interaction.getGuild().getName())
                .setDescription("Here you can see all categories and items of this guild");

        StringSelectMenu categoriesMenu = getStringSelectMenu(interaction, guildCategories);
        interaction.reply("").setEmbeds(catalogueBuilder.build()).setActionRow(categoriesMenu.toSelectMenu()).queue();
    }

    @NotNull
    private StringSelectMenu getStringSelectMenu(SlashCommandInteraction interaction, List<ShopCategory> guildCategories) {
        List<DropdownMenuOption> stringOptions = new ArrayList<>();
        for (ShopCategory category : guildCategories)
        {
            stringOptions.add(new DropdownMenuOption(
                    category.getName(),
                    category.getName(),
                    "",
                    this::categorySelectHandler
            ));
        }

        return new StringSelectMenu("categories_catalogue_" + interaction.getId(), "Select a Category", stringOptions, 1, 1);
    }
}
