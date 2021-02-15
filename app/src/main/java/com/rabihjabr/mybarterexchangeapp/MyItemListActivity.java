package com.rabihjabr.mybarterexchangeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MyItemListActivity extends AppCompatActivity implements MyItemRecyclerViewAdapter.ItemClickListener {

    MyItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_list);

        ItemListActivity.populateItemsArray(this);

        ArrayList<Item> items = User.my_items;
        Log.i("MyItemListActivity", "Array length " + items.size());
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvMyItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyItemRecyclerViewAdapter(this, items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, int position) {
        this.finish();
        startActivity(getIntent());
    }


}