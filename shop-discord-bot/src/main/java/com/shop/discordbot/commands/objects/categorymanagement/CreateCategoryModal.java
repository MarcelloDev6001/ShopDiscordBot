package com.shop.discordbot.commands.objects.categorymanagement;

import com.shop.discordbot.messages.components.modal.ModalObject;
import com.shop.discordbot.messages.components.modal.ModalObjectOnCompleteInteraction;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public abstract class CreateCategoryModal implements Modal {
    public static Modal create(String id, ModalObjectOnCompleteInteraction onCompleteInteraction)
    {
        ModalObject modalObject = new ModalObject(id, "Creating Category:", onCompleteInteraction);
        modalObject.addTextInput("name_input", "Name", "My new category", TextInputStyle.SHORT, true);
        modalObject.addTextInput("description_input", "Description", "A category of X stuffs", TextInputStyle.PARAGRAPH, true);
        return modalObject.build();
    }
}
