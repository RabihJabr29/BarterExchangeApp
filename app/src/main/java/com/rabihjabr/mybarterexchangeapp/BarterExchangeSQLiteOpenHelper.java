package com.rabihjabr.mybarterexchangeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BarterExchangeSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myBarterExchangeAppDatabase";
    private static final int DB_VERSION = 1;

    public static User[] test_users = {
            new User("Sonia Mike", "SoniaMike", "soniaMike@email.com", "12345"),
            new User("John Micheal", "jMicheal", "jMicheal@email.com", "12345"),
            new User("Jonny Junior", "jJunior", "jJunior@email.com", "12345")
    };


    public BarterExchangeSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE USER (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "USERNAME TEXT," +
                "EMAIL TEXT," +
                "PASSWORD TEXT)");

        for (int i = 0; i < test_users.length; i++) {
            insertUser(db, test_users[i].getName(), test_users[i].getUserName(), test_users[i].getEmail(), test_users[i].getPassword());
        }
        insertUser(db, "Root", "root", "root@email.com", "root");

        db.execSQL("CREATE TABLE ITEM (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT," +
                "IMAGE_URL INTEGER," +
                "CATEGORY_TYPE TEXT," +
                "INTERESTS TEXT," +
                "USER_ID)");

        Item[] test_items = {
                new Item("Nice Yellow Dress", R.drawable.clothes, "clothes", "Interested in books, toys and makeup", test_users[0]),
                new Item("Honda 2012 (Family car)", R.drawable.car1, "vehicles", "Interested in sport cars.", test_users[1]),
                new Item("New car and Motorcycle", R.drawable.vehicles, "vehicles", "Interested in other vehicles and motorcycles' stuff.", test_users[1]),
                new Item("Many toys", R.drawable.toys, "toys", "Interested in boys' toys only", test_users[2])
        };

        Cursor usersCursor = db.query("USER", new String[]{"_id", "USERNAME"}, null, null, null, null, null);
        for (int i = 0; i < test_items.length; i++) {
            usersCursor.moveToPosition(i);
            long userId = usersCursor.getLong(0);
            insertItem(db, test_items[i].getTitle(), test_items[i].getImgUrl(), test_items[i].getCategoryType(), test_items[i].getInterests(), userId);
        }

        db.execSQL("CREATE TABLE USER_ITEM_LIKE (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_ID LONG," +
                "ITEM_ID LONG)"
        );
        Log.i("From Database", "Good");
    }

    private void insertUserItemLike(SQLiteDatabase db, long userID, long itemID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_ID", userID);
        contentValues.put("ITEM_ID", itemID);
        db.insert("USER_ITEM_LIKE", null, contentValues);

    }

    private void insertItem(SQLiteDatabase db, String title, int imgUrl, String categoryType, String interests, long id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("IMAGE_URL", imgUrl);
        contentValues.put("CATEGORY_TYPE", categoryType);
        contentValues.put("INTERESTS", interests);
        contentValues.put("USER_ID", id);
        db.insert("ITEM", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertUser(SQLiteDatabase db, String name, String userName, String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("USERNAME", userName);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        db.insert("USER", null, contentValues);
    }


}
