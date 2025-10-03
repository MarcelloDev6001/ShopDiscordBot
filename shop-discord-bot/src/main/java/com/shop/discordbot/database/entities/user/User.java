package com.shop.discordbot.database.entities.user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id = 0L;
    private List<String> cartItemsIDs = new ArrayList<>();

    public User() {}

    public static User getDefault(long id)
    {
        User user = new User();
        user.setId(id);
        return user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getCartItemsIDs() {
        return cartItemsIDs;
    }

    public void setCartItemsIDs(List<String> cartItemsIDs) {
        this.cartItemsIDs = cartItemsIDs;
    }

    public void addItemToCart(String itemID)
    {
        cartItemsIDs.add(itemID);
    }
}