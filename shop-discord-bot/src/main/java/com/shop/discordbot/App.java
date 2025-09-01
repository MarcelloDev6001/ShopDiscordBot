package com.shop.discordbot;

import com.shop.discordbot.listeners.MessageListener;
import com.shop.discordbot.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class App {
    public static void main(String[] args) {
        try
        {
            JDA jda = JDABuilder.createLight(DiscordProperties.token, DiscordProperties.intents)
                    .addEventListeners(new MessageListener())
                    .addEventListeners(new SlashCommandListener())
                    .setActivity(Activity.watching("your messages"))
                    .build();

            jda.getRestPing().queue(ping ->
                    System.out.println("Logged in with ping: " + ping)
            );

            CommandListUpdateAction commands = jda.updateCommands();

            commands.addCommands(
                    Commands.slash("say", "Makes the bot say what you tell it to")
                            .addOption(STRING, "content", "What the bot should say", true), // Accepting a user input
                    Commands.slash("leave", "Makes the bot leave the server")
                            .setContexts(InteractionContextType.GUILD) // this doesn't make sense in DMs
                            .setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
            );

            commands.queue();

            jda.awaitReady();

            System.out.println("Guilds: " + jda.getGuildCache().size());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}