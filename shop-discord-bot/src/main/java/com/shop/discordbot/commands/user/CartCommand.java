package com.shop.discordbot.commands.user;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.user.CartItem;
import com.shop.discordbot.database.entities.user.User;
import com.shop.discordbot.messages.components.button.MessageButton;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class CartCommand extends Command {
    public CartCommand() {
        super("cart", "See your cart!");
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        interaction.deferReply(true).queue();
        net.dv8tion.jda.api.entities.User user = interaction.getUser();
        User dbUser = FirebaseManager.getOrCreateUser(user.getIdLong());

        if (dbUser.getCartItems().isEmpty())
        {
            interaction.reply("You don't have any items on your cart!").queue();
            return;
        }

        BiConsumer<ButtonInteraction, net.dv8tion.jda.api.entities.User> handlerFinishCartButton = (buttonInteraction, userWhoInteracted) -> new Runnable(){
            @Override
            public void run() {
                if (dbUser.getCartItems().getFirst().getGuildId() != interaction.getGuild().getIdLong())
                {
                    interaction.getHook().sendMessage("To finish your cart, you need to use this command on the guild of the items!").setEphemeral(true).queue();
                    return;
                }
                dbUser.finishCart(interaction.getGuild(), false);
                interaction.getHook().sendMessage("Cart finished successfully!").setEphemeral(true).queue();
            }
        }.run();

        BiConsumer<ButtonInteraction, net.dv8tion.jda.api.entities.User> handlerClearCartButton = (buttonInteraction, userWhoInteracted) -> new Runnable(){
            @Override
            public void run() {
                if (dbUser.getCartItems().isEmpty())
                {
                    interaction.getHook().sendMessage("Your cart is empty.").setEphemeral(true).queue();
                    return;
                }

                dbUser.clearCart();
                interaction.getHook().sendMessage("Cart cleared successfully!").setEphemeral(true).queue();
            }
        }.run();

        EmbedBuilder cartEmbed = new EmbedBuilder();
        cartEmbed.setTitle("Cart of " + user.getEffectiveName());
        cartEmbed.setColor(user.retrieveProfile().complete().getAccentColor());

        for (int i = 0; i < dbUser.getCartItems().size(); i++)
        {
            CartItem item = dbUser.getCartItems().get(i);
            cartEmbed.getDescriptionBuilder().append("- ").append(Integer.toString(i)).append(": `").append(item.getName()).append("`.\n");
        }

        MessageButton finishCartButton = new MessageButton(
                ButtonStyle.PRIMARY,
                "finish_cart_" + interaction.getId(),
                "Finish Cart",
                handlerFinishCartButton::accept,
                ""
        );

        MessageButton clearCartButton = new MessageButton(
                ButtonStyle.DANGER,
                "clear_cart_" + interaction.getId(),
                "Clear Cart",
                handlerClearCartButton::accept,
                ""
        );

        interaction.getHook().editOriginal("").setEmbeds(cartEmbed.build()).setActionRow(finishCartButton.toComponentButton(), clearCartButton.toComponentButton());
    }
}
