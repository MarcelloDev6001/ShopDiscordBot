package com.shop.discordbot.commands;

import org.reflections.Reflections;
import java.util.Set;

public class CommandInitializer {
    public static void initializeCommands() {
        Reflections reflections = new Reflections("com.shop.discordbot.commands");

        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

        for (Class<? extends Command> clazz : classes) {
            try {
                clazz.getDeclaredConstructor().newInstance();
                System.out.println("Command initialized: " + clazz.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
