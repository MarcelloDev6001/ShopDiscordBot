package com.shop.discordbot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Objects;
import java.util.Set;

public class ListenersLoader {
    public static void initListeners(JDA jda)
    {
        for (ListenerAdapter listener : loadAllListeners())
        {
            jda.addEventListener(listener);
            //System.out.println("Listener loaded: " + listener.getClass().getName());
        }
    }

    public static ListenerAdapter[] loadAllListeners() {
        String basePackage = "com.shop.discordbot.listeners";

        Reflections reflections = new Reflections(basePackage, Scanners.SubTypes);

        Set<Class<? extends ListenerAdapter>> classes = reflections.getSubTypesOf(ListenerAdapter.class);

        return classes.stream().map(cls -> {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toArray(ListenerAdapter[]::new);
    }
}
