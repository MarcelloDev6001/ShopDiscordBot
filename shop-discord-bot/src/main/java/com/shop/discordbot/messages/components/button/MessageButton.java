package com.shop.discordbot.messages.components.button;

import com.shop.discordbot.messages.components.ComponentsManager;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageButton
{
    public final ButtonStyle style;
    public final String uniqueId;
    public final String label;
    @Nullable public final MessageButtonOnClickEvent onClicked;
    public @Nullable String link;

    public Button toComponentButton() {
        switch (style) {
            case ButtonStyle.SECONDARY -> {
                return Button.secondary(uniqueId, label);
            }
            case ButtonStyle.DANGER -> {
                return Button.danger(uniqueId, label);
            }
            case ButtonStyle.LINK -> {
                return Button.link((link != null) ? link : "", label);
            }
            case ButtonStyle.SUCCESS -> {
                return Button.success(uniqueId, label);
            }
            default -> { // Primary, Unknown or Premium (Premium will be added later)
                return Button.primary(uniqueId, label);
            }
        }
    }

    public void uncacheWithDelay()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            ComponentsManager.cachedMessageButtons.remove(this);
        }, 60, TimeUnit.MINUTES);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }

    public MessageButton(ButtonStyle style, String uniqueId, String label, MessageButtonOnClickEvent onClicked, @Nullable String link) {
        this.style = style;
        this.uniqueId = uniqueId;
        this.label = label;
        this.onClicked = onClicked;
        this.link = link;

        ComponentsManager.cachedMessageButtons.add(this);
        uncacheWithDelay();
    }
}
