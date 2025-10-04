package com.shop.discordbot.commands.purchases;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.Purchase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetPurchasesCommand extends Command {
    public GetPurchasesCommand() {
        super("your-purchases", "See your purchases");
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        List<Purchase> purchases = FirebaseManager.getPurchasesWhere("buyerID", interaction.getIdLong());

        EmbedBuilder purchasesEmbed = new EmbedBuilder();
        purchasesEmbed.setTitle("Your purchases:");

        for (int i = 0; i < purchases.size(); i++) {
            purchasesEmbed.getDescriptionBuilder().append(Integer.toString(i + 1)).append(" - ").append(purchases.get(i).getTitle()).append("\n");
        }

        if (purchases.isEmpty())
        {
            purchasesEmbed.setDescription("You didn't bought anything yet!");
        }

        interaction.replyEmbeds(purchasesEmbed.build()).setEphemeral(true).queue();
    }
}
