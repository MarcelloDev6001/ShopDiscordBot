package com.shop.discordbot.commands.catalogue;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.consumer.TriConsumer;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.purchase.PurchaseItem;
import com.shop.discordbot.database.entities.purchase.exceptions.ItemNotFound;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import com.shop.discordbot.messages.components.button.MessageButton;
import com.shop.discordbot.messages.components.dropdown.DropdownMenuOption;
import com.shop.discordbot.messages.components.dropdown.StringSelectMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
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

    public void addItemToUserCart(User user, net.dv8tion.jda.api.entities.Guild guild, String categoryName, String itemName) throws ItemNotFound
    {
        Guild dbGuild = FirebaseManager.getOrCreateGuild(guild.getIdLong());
        ShopCategory category = dbGuild.getCategory(categoryName);
        PurchaseItem item = category.getItem(itemName);

        if (item == null)
        {
            throw new ItemNotFound(itemName, "");
        }

        com.shop.discordbot.database.entities.user.User dbUser = FirebaseManager.getOrCreateUser(user.getIdLong());
        dbUser.addItemToCart(item.getId(), item.getName(), guild.getIdLong());
    }

    public void categorySelectHandler(StringSelectInteraction interaction, User user, List<String> choices)
    {
        interaction.deferReply(true).queue();
        Guild dbGuild = FirebaseManager.getOrCreateGuild(interaction.getGuild().getIdLong());
        System.out.println(choices.getFirst());
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

        TriConsumer<StringSelectInteraction, User, List<String>> itemSelectHandler = getItemSelectTriConsumer(interaction, category);

        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();

        StringSelectMenu selectMenu = getCategoryStringSelectMenu(interaction, category, itemSelectHandler);
        interaction.getHook().editOriginalComponents(ActionRow.of(selectMenu.toSelectMenu())).queue();
    }

    @NotNull
    private static StringSelectMenu getCategoryStringSelectMenu(StringSelectInteraction interaction, ShopCategory category, TriConsumer<StringSelectInteraction, User, List<String>> itemSelectHandler) {
        List<DropdownMenuOption> stringOptions = new ArrayList<>();
        for (PurchaseItem item : category.getItems())
        {
            stringOptions.add(new DropdownMenuOption(
                    item.getName(),
                    item.getName(),
                    item.getDetails(),
                    itemSelectHandler::accept
            ));
        }

        return new StringSelectMenu(
                "item_select_" + interaction.getId(),
                "Select an Item",
                stringOptions,
                1,
                1
        );
    }

    @NotNull
    private TriConsumer<StringSelectInteraction, User, List<String>> getItemSelectTriConsumer(StringSelectInteraction interaction, ShopCategory category) {
        final PurchaseItem[] currentItem = {null};

        BiConsumer<ButtonInteraction, User> addToCartHandler = (buttonInteraction, userBiConsumer) -> new Runnable(){
            @Override
            public void run() {
                try {
                    addItemToUserCart(userBiConsumer, interaction.getGuild(), category.getName(), currentItem[0].getName());
                    buttonInteraction.reply("Item added successfully!").setEphemeral(true).queue();
                } catch (ItemNotFound _) {
                    buttonInteraction.reply("Error: item not found!").setEphemeral(true).queue();
                }
            }
        }.run();

        TriConsumer<StringSelectInteraction, User, List<String>> itemSelectHandler = (interaction1, userListTriConsumer, choicesTriConsumer) -> {
            interaction1.deferReply(true).queue();

            PurchaseItem item = category.getItem(choicesTriConsumer.getFirst());
            if (item == null)
            {
                interaction1.getHook().editOriginal("Item not found!").queue();
                return;
            }
            currentItem[0] = item;

            EmbedBuilder itemEmbed = new EmbedBuilder()
                    .setTitle(item.getName())
                    .setDescription(item.getDetails());

            interaction1.getHook().editOriginalEmbeds(itemEmbed.build()).queue();

            MessageButton addToCartButton = new MessageButton(
                    ButtonStyle.PRIMARY,
                    "add_cart_" + item.getName() + "_" + interaction1.getId(),
                    "Add to cart",
                    addToCartHandler::accept,
                    ""
            );
            interaction1.getHook().editOriginalComponents(ActionRow.of(addToCartButton.toComponentButton())).queue();
        };
        return itemSelectHandler;
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
