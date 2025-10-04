package com.shop.discordbot.messages.components.modal;

import com.shop.discordbot.messages.components.ComponentsManager;
import com.shop.discordbot.messages.components.dropdown.DropdownMenuOption;
import com.shop.discordbot.messages.components.dropdown.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModalObject {
    private final Modal.Builder builder;
    public final ModalObjectOnCompleteInteraction onCompleteInteraction;
    public final String uniqueId;

    public ModalObject(String uniqueId, String title, ModalObjectOnCompleteInteraction onCompleteInteraction) {
        builder = Modal.create(uniqueId, title);
        this.uniqueId = uniqueId;
        this.onCompleteInteraction = onCompleteInteraction;

        ComponentsManager.cachedModals.add(this);
        uncacheWithDelay();
    }

    public Modal build()
    {
        return builder.build();
    }

    public void addTextInput(String uniqueId, String name, String placeholder, TextInputStyle style, boolean required)
    {
        TextInput textInput = TextInput.create(uniqueId, name, style)
                .setPlaceholder(placeholder)
                .setRequired(required)
                .build();

        builder.addActionRow(textInput);
    }

    public void addTextInput(String uniqueId, String name, String placeholder, TextInputStyle style, boolean required, int minLength, int maxLength)
    {
        TextInput textInput = TextInput.create(uniqueId, name, style)
                .setPlaceholder(placeholder)
                .setRequired(required)
                .setMinLength(minLength)
                .setMaxLength(maxLength)
                .build();

        builder.addActionRow(textInput);
    }

    public void uncacheWithDelay()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            ComponentsManager.cachedModals.remove(this);
        }, 60, TimeUnit.MINUTES);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }
}
