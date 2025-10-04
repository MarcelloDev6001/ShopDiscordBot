package com.shop.discordbot.messages.components.modal;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;

@FunctionalInterface
public interface ModalObjectOnCompleteInteraction {
    void run(ModalInteraction interaction, User user);
}
