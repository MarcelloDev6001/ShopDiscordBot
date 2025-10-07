package com.shop.discordbot.commands.items;

import com.shop.discordbot.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class AddItemCommand extends Command {
    public AddItemCommand() {
        super("add-item", "Adds an item to a category");
        setGuildOnly(true);
        setPermissionsRequired(List.of(Permission.ADMINISTRATOR));

        addOption(OptionType.STRING, "category", "Category you won't to add the item", true, List.of("categories-names"));

        addOption(OptionType.STRING, "item-name", "Name of your item", true, null);
        addOption(OptionType.STRING, "item-description", "Description of your item", true, null);
        addOption(OptionType.STRING, "item-secrets", "Secrets of you item (these will appear to the buyer once he pay the purchase)", true, null);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        interaction.reply("testing!").setEphemeral(true).queue();
    }
}
