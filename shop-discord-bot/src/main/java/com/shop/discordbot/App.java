package com.shop.discordbot;

import com.shop.discordbot.commands.Command;
import com.shop.discordbot.commands.CommandInitializer;
import com.shop.discordbot.commands.CommandOption;
import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.listeners.message.MessageReceivedListener;
import com.shop.discordbot.listeners.interaction.SlashCommandInteractionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.io.IOException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class App {
    public static void main(String[] args) {
        try
        {
            JDA jda = JDABuilder.createLight(DiscordProperties.token, DiscordProperties.intents)
                    .addEventListeners(new MessageReceivedListener())
                    .addEventListeners(new SlashCommandInteractionListener())
                    .setActivity(Activity.watching("your messages"))
                    .build();

            jda.getRestPing().queue(ping ->
                    System.out.println("Logged in with ping: " + ping)
            );

            CommandListUpdateAction commands = jda.updateCommands();

            CommandInitializer.initializeCommands();
            FirebaseManager.initialize();

            // this is from Carol (my other project)
            // repository here: https://github.com/MarcelloDev6001/CarolBot
            for (Command command : Command.allCommands)
            {
                SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());
                // slashCommand.setGuildOnly(command.getGuildOnly());

                if (command.getOptions() != null)
                {
                    for (CommandOption optionOnCommand : command.getOptions())
                    {
                        SlashCommandData newOption = slashCommand.addOption(
                                optionOnCommand.getOptionType(),
                                optionOnCommand.getName(),
                                optionOnCommand.getDescription(),
                                optionOnCommand.getRequired(),
                                optionOnCommand.getAutoComplete() != null && !optionOnCommand.getAutoComplete().isEmpty()
                        );
                    }
                }

                commands.addCommands(slashCommand);
                System.out.println("Command Loaded to Discord: " + slashCommand.getName());
            }

            commands.queue();

            jda.awaitReady();

            System.out.println("Guilds: " + jda.getGuildCache().size());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}