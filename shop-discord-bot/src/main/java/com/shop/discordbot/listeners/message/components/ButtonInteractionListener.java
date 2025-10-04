package com.shop.discordbot.listeners.message.components;

import com.shop.discordbot.messages.components.ComponentsManager;
import com.shop.discordbot.messages.components.button.MessageButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonInteractionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        for (MessageButton messageButton : ComponentsManager.cachedMessageButtons)
        {
            if (messageButton.uniqueId.equals(event.getComponentId()) && messageButton.onClicked != null)
            {
                messageButton.onClicked.run(event.getInteraction(), event.getUser());
                return;
            }
        }

        event.reply("This button had lost his Interaction data!\nCreate a new Interaction to use this button again").setEphemeral(true).queue();
        super.onButtonInteraction(event);
    }
}
