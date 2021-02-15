package com.rabihjabr.mybarterexchangeapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class User {
    private String name, userName, email, password;
    private long id;
    static ArrayList<Item> my_items;
    static ArrayList<Long> liked_items_ids;

    public User(String name, String userName, String email, String password) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        liked_items_ids = new ArrayList<>();
        my_items = new ArrayList<>();
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    static Cursor usersCursor;

    public static ArrayList<Long> getLiked_items_ids(long userID, Context c) {
        liked_items_ids.clear();
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();
            usersCursor = db.query("USER_ITEM_LIKE", new String[]{"USER_ID", "ITEM_ID"}, null, null, null, null, null);
            while (usersCursor.moveToNext()) {
                if (usersCursor.getLong(0) == userID) {
                    liked_items_ids.add(usersCursor.getLong(1));
                }
            }
            return liked_items_ids;

        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. getLikeditems ids method. msg " + e.getMessage());
            return null;

        }

    }


    public ArrayList<Item> getMy_items_ids(long userID) {
        return my_items;
    }

    public void setMy_items_ids(ArrayList<Item> my_items) {
        this.my_items = my_items;
    }

    public void setLiked_items_ids(ArrayList<Long> liked_items_ids) {
        this.liked_items_ids = liked_items_ids;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    static SQLiteDatabase db;
    static Cursor cursor;


    public void addUserToDatabase(Context c) {
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME", name);
            contentValues.put("USERNAME", userName);
            contentValues.put("EMAIL", email);
            contentValues.put("PASSWORD", password);
            db.insert("USER", null, contentValues);
            db.close();
        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. addUser method.");
        }
    }

    public static void addItem(Item newItem, long userID, Context c) {

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TITLE", newItem.getTitle());
            contentValues.put("IMAGE_URL", newItem.getImgUrl());
            contentValues.put("CATEGORY_TYPE", newItem.getCategoryType());
            contentValues.put("INTERESTS", newItem.getInterests());
            contentValues.put("USER_ID", newItem.getOwner().getId());
            db.insert("ITEM", null, contentValues);

        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. add liked method.");
        }


    }

    public static void removeItem(long selectedItemID,Context c) {
        for (int i = 0; i < my_items.size(); i++) {
            if (my_items.get(i).getItemID() == selectedItemID) {
                my_items.remove(i);
                break;
            }
        }

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();

            db.delete("ITEM", "_id = ?", new String[]{selectedItemID + ""});

        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. add liked method.");
        }
    }

    public static void addLikedItemID(long newLikedItemID, long userID, Context c) {
        liked_items_ids.add(newLikedItemID);
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("USER_ID", userID);
            contentValues.put("ITEM_ID", newLikedItemID);

            db.insert("USER_ITEM_LIKE", null, contentValues);

        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. add liked method. Exception " + e.getMessage());
        }
    }


    public static void removeLikedItemID(long selectedLikedItemID, long userID, Context c) {
        for (int i = 0; i < liked_items_ids.size(); i++) {
            if (liked_items_ids.get(i) == selectedLikedItemID) {
                liked_items_ids.remove(i);
                break;
            }
        }

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();

            db.delete("USER_ITEM_LIKE", "USER_ID = ? AND ITEM_ID = ?", new String[]{userID + "", selectedLikedItemID + ""});

        } catch (Exception e) {
            Log.i("Database error", "Database unavailable. add liked method.");
        }
    }
}