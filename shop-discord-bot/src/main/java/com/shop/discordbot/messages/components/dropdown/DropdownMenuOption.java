package com.shop.discordbot.messages.components.dropdown;

public record DropdownMenuOption(String label, String value, String description, DropdownMenuOptionOnClickEvent onClicked) {
    public DropdownMenuOption {}
}
