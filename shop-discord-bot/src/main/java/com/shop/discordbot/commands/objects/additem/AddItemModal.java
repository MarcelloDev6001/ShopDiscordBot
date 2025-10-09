package com.shop.discordbot.commands.objects.additem;

import com.shop.discordbot.messages.components.modal.ModalObject;
import com.shop.discordbot.messages.components.modal.ModalObjectOnCompleteInteraction;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public abstract class AddItemModal implements Modal {
    public static Modal create(String id, String categoryName, ModalObjectOnCompleteInteraction onCompleteInteraction)
    {
        ModalObject modalObject = new ModalObject(id, "Adding an item to " + categoryName + ":", onCompleteInteraction);
        modalObject.addTextInput("name_input", "Name", "My new category", TextInputStyle.SHORT, true, 1, 50);
        modalObject.addTextInput("description_input", "Description", "A category of X stuffs", TextInputStyle.PARAGRAPH, true, 1, 512);
        modalObject.addTextInput("secrets_input", "Secrets", "Username: XXXX.\\nPassword: XXXX.", TextInputStyle.PARAGRAPH, true, 1, 512);
        return modalObject.build();
    }
}
