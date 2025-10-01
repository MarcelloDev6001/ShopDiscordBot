package com.shop.discordbot.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CommandOption(
        @NotNull String _name,
        String _description,
        @NotNull OptionType _type,
        boolean _required,
        List<String> _autocomplete
) {
    public String getName() { return _name; }
    public String getDescription() { return _description; }
    public OptionType getOptionType() { return _type; }
    public boolean getRequired() { return _required; }
    public List<String> getAutoComplete() { return _autocomplete; }
}
