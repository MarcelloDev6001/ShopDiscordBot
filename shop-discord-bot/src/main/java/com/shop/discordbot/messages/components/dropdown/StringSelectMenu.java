package com.shop.discordbot.messages.components.dropdown;

import com.shop.discordbot.messages.components.ComponentsManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StringSelectMenu {
    private final net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.Builder builder;
    private final List<DropdownMenuOption> options;

    public StringSelectMenu(String id, String placeholder, @NotNull List<DropdownMenuOption> options, int minValue, int maxValue)
    {
        builder = net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create(id)
                .setPlaceholder(placeholder)
                .setMinValues(minValue)
                .setMaxValues(maxValue);

        for (DropdownMenuOption option : options)
        {
            builder.addOption(option.label(), option.value(), option.description());
        }
        this.options = options;

        ComponentsManager.cachedStringMenus.add(this);
        uncacheWithDelay();
    }

    public net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu toSelectMenu()
    {
        return builder.build();
    }

    public List<DropdownMenuOption> getOptions()
    {
        return options;
    }

    public void uncacheWithDelay()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            ComponentsManager.cachedStringMenus.remove(this);
        }, 60, TimeUnit.MINUTES);

        scheduler.schedule(scheduler::shutdown, 0, TimeUnit.SECONDS);
    }
}
