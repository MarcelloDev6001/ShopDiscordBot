package com.shop.discordbot.listeners.message.components;

import com.shop.discordbot.messages.components.ComponentsManager;
import com.shop.discordbot.messages.components.dropdown.DropdownMenuOption;
import com.shop.discordbot.messages.components.dropdown.StringSelectMenu;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StringSelectInteractionListener extends ListenerAdapter {
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        for (StringSelectMenu selectMenu : ComponentsManager.cachedStringMenus)
        {
            for (DropdownMenuOption option : selectMenu.getOptions())
            {
                if (option.onClicked() != null)
                {
                    option.onClicked().run(event.getInteraction(), event.getUser(), event.getValues());
                    return;
                }
            }
        }

        event.reply("This button had lost his Interaction data!\nCreate a new Interaction to use this button again").setEphemeral(true).queue();
    }
}
