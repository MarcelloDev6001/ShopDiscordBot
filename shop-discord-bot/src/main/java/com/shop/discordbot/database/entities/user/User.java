package com.shop.discordbot.database.entities.user;

public class User {
    private long id = 0L;

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
}