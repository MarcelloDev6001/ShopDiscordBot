package com.shop.discordbot.messages.components.button;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

@FunctionalInterface
public interface MessageButtonOnClickEvent { // it's a long name, isn't?
    void run(ButtonInteraction interaction, User user);
}
