package com.shop.discordbot.commands.test;

import com.shop.discordbot.commands.Command;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "A simple test command");
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        interaction.reply("hello!").queue();
    }
}
