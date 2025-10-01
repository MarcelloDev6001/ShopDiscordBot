package com.shop.discordbot.database.entities.guild;

public class Guild {
    private long id = 0L;

    public Guild() {}

    public static Guild getDefault(long id)
    {
        Guild carolDatabaseGuild = new Guild();
        carolDatabaseGuild.setId(id);
        return carolDatabaseGuild;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}