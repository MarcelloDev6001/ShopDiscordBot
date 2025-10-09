package com.shop.discordbot.listeners.interaction;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.shop.ShopCategory;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandAutoCompleteInteractionListener extends ListenerAdapter {
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        switch (event.getFocusedOption().getName().toLowerCase()) {
            case "category": {
                if (event.getChannelType() == ChannelType.PRIVATE) {
                    event.replyChoices(new Command.Choice("This command needs to be used on a guild!", "null")).queue();
                    break;
                }

                Guild dbGuild = FirebaseManager.getOrCreateGuild(event.getGuild().getIdLong());

                String input = event.getFocusedOption().getValue();
                List<String> suggestions = new ArrayList<>();

                for (ShopCategory category : dbGuild.getCategories())
                {
                    System.out.println(category.getName());
                    if (!category.getName().isEmpty()) {
                        suggestions.add(category.getName());
                    }
                }

                List<Command.Choice> choices = suggestions.stream()
                        .filter(s -> s.startsWith(input.toLowerCase()))
                        .map(s -> new Command.Choice(s, s))
                        .collect(Collectors.toList());

                event.replyChoices(choices).queue();
                break;
            }
            default: {
                event.replyChoices(new Command.Choice("Unknown Option!", "null")).queue();
            }
        }
    }
}
