package com.shop.discordbot.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// btw this is the same code of CarolCommand.java from my other project
// the only difference is that there's no locale map system
// repository of Carol: https://github.com/MarcelloDev6001/CarolBot
public abstract class Command {
    public String name;
    public String description;
    public @Nullable List<CommandOption> options;
    public boolean guildOnly;

    public static final List<Command> allCommands = new ArrayList<>();
    private static SlashCommandInteraction interaction;

    public Command(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
        options = new ArrayList<>();

        allCommands.add(this);
    }

    public static void dispatchInteraction(SlashCommandInteraction interaction) {
        // System.out.println(allCommands.size());
        for (Command cmd : allCommands) {
            if (cmd.getName().equals(interaction.getName())) {
                cmd.setInteraction(interaction);
                cmd.onCommandExecuted(interaction);
                break;
            }
        }
    }

    public void setInteraction(SlashCommandInteraction newInteraction)
    {
        interaction = newInteraction;
    }

    public void onCommandExecuted(SlashCommandInteraction interaction) {
        // System.out.println("Command executed: " + interaction.getName());
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    @Nullable public List<CommandOption> getOptions() { return options; }
    public boolean getGuildOnly() { return guildOnly; }

    public void setGuildOnly(boolean guildOnly) { this.guildOnly = guildOnly; }

    public void addOption(OptionType optionType, String name, String description, boolean required, List<String> autoComplete)
    {
        assert options != null;
        options.add(new CommandOption(name, description, optionType, required, autoComplete));
    }

    // for OptionType.STRING
    public void addOption(OptionType optionType, String name, String description, boolean required, List<String> autoComplete, int minLength, int maxLength)
    {
        assert options != null;
        options.add(new CommandOption(name, description, optionType, required, autoComplete));
    }

    public void addOption(CommandOption option)
    {
        assert options != null;
        options.add(option);
    }
}
