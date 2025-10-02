package com.shop.discordbot.listeners.message;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.Purchase;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        MessageChannelUnion channel = event.getChannel();
        Message message = event.getMessage();

        //System.out.println("message from " + author.getName() + ": " + message.getContentRaw());
        super.onMessageReceived(event);
    }
}
