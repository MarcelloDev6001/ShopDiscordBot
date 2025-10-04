package com.shop.discordbot.database.entities.user;

import com.shop.discordbot.database.FirebaseManager;
import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.purchase.exceptions.ItemNotFound;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id = 0L;
    private List<Long> cartItemsIDs = new ArrayList<>();

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

    public List<Long> getCartItemsIDs() {
        return cartItemsIDs;
    }

    public void setCartItemsIDs(List<Long> cartItemsIDs) {
        this.cartItemsIDs = cartItemsIDs;
        updateToFirestore();
    }

    public void addItemToCart(Long itemID)
    {
        cartItemsIDs.add(itemID);
        updateToFirestore();
    }

    public void removeItemOfCart(Long itemID)
    {
        if (cartItemsIDs.contains(itemID))
        {
            cartItemsIDs.remove(itemID);
            updateToFirestore();
        }
    }

    public Purchase finishCart(Guild guild, boolean needsConfirmation) throws ItemNotFound
    {
        Purchase purchase = new Purchase(getId(), guild, getCartItemsIDs(), needsConfirmation);
        clearCart();
        return purchase;
    }

    public void clearCart()
    {
        cartItemsIDs.clear();
        updateToFirestore();
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
}