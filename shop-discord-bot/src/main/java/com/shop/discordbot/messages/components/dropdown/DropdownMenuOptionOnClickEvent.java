package com.shop.discordbot.messages.components.dropdown;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction;

import java.util.List;

@FunctionalInterface
public interface DropdownMenuOptionOnClickEvent { // it's a long name, isn't?
    void run(StringSelectInteraction interaction, User user, List<String> choice);
}
