package com.shop.discordbot.database.entities.user;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.purchase.exceptions.ItemNotFound;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id = 0L;
    private List<CartItem> cartItems = new ArrayList<>();

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

    public Purchase finishCart(Guild guild, boolean needsConfirmation) throws ItemNotFound
    {
        Purchase purchase = new Purchase(getId(), guild, getCartItemsIDs(), needsConfirmation);
        clearCart();
        return purchase;
    }

    public UpdateUserStatus updateToFirestore()
    {
        try {
            FirebaseManager.updateUser(getId(), this);
            return UpdateUserStatus.SUCCESS;
        } catch (Exception e) {
            return UpdateUserStatus.FAIL;
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public List<String> getCartItemsIDs() {
        List<String> cartItemsIDs = new ArrayList<>();
        for (CartItem item : getCartItems())
        {
            cartItemsIDs.add(item.getId());
        }
        return cartItemsIDs;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addItemToCart(String itemID, String itemName, long guildId)
    {
        CartItem newCartItem = new CartItem();
        newCartItem.setId(itemID);
        newCartItem.setName(itemName);
        newCartItem.setGuildId(guildId);
        cartItems.add(newCartItem);
        updateToFirestore();
    }

    public void removeItemOfCart(String itemID)
    {
        for (CartItem item : getCartItems())
        {
            if (item.getId().equals(itemID))
            {
                cartItems.remove(item);
                break;
            }
        }
        updateToFirestore();
    }

    public void clearCart()
    {
        cartItems.clear();
        updateToFirestore();
    }
}