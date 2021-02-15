package com.rabihjabr.mybarterexchangeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Intent intent = getIntent();
        String category = intent.getStringExtra("Category");
        // data to populate the RecyclerView with
        ArrayList<Item> items = new ArrayList<>();
        populateItemsArray(this);
        this.setTitle(category.toUpperCase());
        items = Item.getItemsArrayByCategory(category);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    static SQLiteDatabase db;
    static Cursor itemsCursor, userCursor;

    public static void populateItemsArray(Context c) {
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(c);
            db = sqLiteOpenHelper.getReadableDatabase();

            itemsCursor = db.query("ITEM", new String[]{"USER_ID", "TITLE", "IMAGE_URL", "CATEGORY_TYPE", "INTERESTS", "_id"},
                    null, null, null, null, null);

            ArrayList<Item> items = new ArrayList<Item>();
            ArrayList<Item> my_items = new ArrayList<Item>();

            while (itemsCursor.moveToNext()) {

                long userID = itemsCursor.getLong(0);
                User theOwner = new User(0, "unkown");
                userCursor = db.query("USER", new String[]{"_id", "NAME"},
                        null, null, null, null, null);
                while (userCursor.moveToNext()) {
                    if (userCursor.getLong(0) == userID) {

                        theOwner = new User(userCursor.getLong(0), userCursor.getString(1));
                        break;
                    }
                }
                Item newItem = new Item(itemsCursor.getString(1), itemsCursor.getInt(2), itemsCursor.getString(3), itemsCursor.getString(4), theOwner);
                newItem.setItemID(itemsCursor.getLong(5));
                if (userID == MainActivity.userLoggedInID) {
                    my_items.add(newItem);
                    Log.i("MyItems", "aa");
                } else {
                    items.add(newItem);
                }
            }
            User.my_items = my_items;
            Item.items_array = items;
        } catch (Exception e) {
            Log.i("Database", "Database is unavailable");
        }

    }


}