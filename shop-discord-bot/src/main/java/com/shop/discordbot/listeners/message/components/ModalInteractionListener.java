package com.shop.discordbot.listeners.message.components;

import com.shop.discordbot.messages.components.ComponentsManager;
import com.shop.discordbot.messages.components.button.MessageButton;
import com.shop.discordbot.messages.components.modal.ModalObject;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalInteractionListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        System.out.println(event.getModalId());
        for (ModalObject modalObject : ComponentsManager.cachedModals)
        {
            if (modalObject.uniqueId.equals(event.getModalId()) && modalObject.onCompleteInteraction != null)
            {
                modalObject.onCompleteInteraction.run(event.getInteraction(), event.getUser());
                return;
            }
        }

        event.reply("This button had lost his Interaction data!\nCreate a new Interaction to use this button again").setEphemeral(true).queue();
        super.onModalInteraction(event);
    }
}
