package com.shop.discordbot.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.shop.discordbot.database.entities.guild.Guild;
import com.shop.discordbot.database.entities.purchase.Purchase;
import com.shop.discordbot.database.entities.user.User;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.concurrent.ExecutionException;

// i admit, this was made by me but i used ChatGPT to optimize this a LOT
// Originally this project would use Supabase as main database, but this database is a little confuse for me ;-;
public class FirebaseManager {
    private static Firestore db;

    public static InputStream getDatabaseKeyThing()
    {
        try { // try to found the file
            return new FileInputStream("src/main/resources/firebase-key.json");
        } catch (FileNotFoundException e) { // if not found, then use an environment variable
            throw new RuntimeException("FIREBASE_KEY not found!");
        }
    }

    public static void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(getDatabaseKeyThing()))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    // ---------- Generic Methods ----------
    private static <T> @Nullable T getEntity(String collection, String id, Class<T> clazz) {
        try {
            DocumentReference docRef = db.collection(collection).document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(clazz);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void addOrUpdateEntity(String collection, String id, T entity) {
        try {
            DocumentReference docRef = db.collection(collection).document(id);
            ApiFuture<WriteResult> future = docRef.set(entity);
            //System.out.println("Updated at: " + future.get().getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> T getOrCreateEntity(String collection, String id, Class<T> clazz, T defaultEntity) {
        T entity = getEntity(collection, id, clazz);
        if (entity == null) {
            addOrUpdateEntity(collection, id, defaultEntity);
            return defaultEntity;
        }
        return entity;
    }

    // ---------- User wrappers ----------
    public static User getUserFromDatabase(long id) {
        return getEntity(Tables.USERS_TABLE, String.valueOf(id), User.class);
    }

    public static void addUserToDatabase(User user) {
        addOrUpdateEntity(Tables.USERS_TABLE, String.valueOf(user.getId()), user);
    }

    public static User getOrCreateUser(long id) {
        return getOrCreateEntity(Tables.USERS_TABLE, String.valueOf(id), User.class, User.getDefault(id));
    }

    public static void updateUser(long id, User user) {
        addOrUpdateEntity(Tables.USERS_TABLE, String.valueOf(id), user);
    }

    // ---------- Guild wrappers ----------
    public static Guild getGuildFromDatabase(long id) {
        return getEntity(Tables.GUILDS_TABLE, String.valueOf(id), Guild.class);
    }

    public static void addGuildToDatabase(Guild guild) {
        addOrUpdateEntity(Tables.GUILDS_TABLE, String.valueOf(guild.getId()), guild);
    }

    public static Guild getOrCreateGuild(long id) {
        return getOrCreateEntity(Tables.GUILDS_TABLE, String.valueOf(id), Guild.class, Guild.getDefault(id));
    }

    public static void updateGuild(long id, Guild guild) {
        addOrUpdateEntity(Tables.GUILDS_TABLE, String.valueOf(id), guild);
    }

    // ---------- Purchases wrappers ----------
    public static Purchase getPurchaseFromDatabase(long id) {
        return getEntity(Tables.PURCHASES_TABLE, String.valueOf(id), Purchase.class);
    }

    public static void addPurchaseToDatabase(Purchase purchase) {
        addOrUpdateEntity(Tables.PURCHASES_TABLE, String.valueOf(purchase.getId()), purchase);
    }

    public static Purchase getOrCreatePurchase(long id, long buyerId) {
        return getOrCreateEntity(Tables.GUILDS_TABLE, String.valueOf(id), Purchase.class, Purchase.getDefault(id, buyerId));
    }

    public static void updatePurchase(long id, Purchase purchase) {
        addOrUpdateEntity(Tables.PURCHASES_TABLE, String.valueOf(id), purchase);
    }
}
