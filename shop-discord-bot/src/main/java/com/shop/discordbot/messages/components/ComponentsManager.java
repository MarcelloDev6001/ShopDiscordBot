package com.shop.discordbot.messages.components;

import com.shop.discordbot.messages.components.button.MessageButton;
import com.shop.discordbot.messages.components.dropdown.StringSelectMenu;
import com.shop.discordbot.messages.components.modal.ModalObject;

import java.util.ArrayList;
import java.util.List;

public class ComponentsManager {
    public static List<MessageButton> cachedMessageButtons = new ArrayList<>();
    public static List<StringSelectMenu> cachedStringMenus = new ArrayList<>();
    public static List<ModalObject> cachedModals = new ArrayList<>();
}
