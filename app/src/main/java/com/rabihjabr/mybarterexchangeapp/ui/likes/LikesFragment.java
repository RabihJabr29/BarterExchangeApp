package com.rabihjabr.mybarterexchangeapp.ui.likes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rabihjabr.mybarterexchangeapp.BarterExchangeSQLiteOpenHelper;
import com.rabihjabr.mybarterexchangeapp.Item;
import com.rabihjabr.mybarterexchangeapp.MainActivity;
import com.rabihjabr.mybarterexchangeapp.MyRecyclerViewAdapter;
import com.rabihjabr.mybarterexchangeapp.R;
import com.rabihjabr.mybarterexchangeapp.User;

import java.util.ArrayList;


public class LikesFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private SQLiteDatabase db;
    private Cursor itemsCursor, userCursor;
    MyRecyclerViewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_likes, container, false);
        ArrayList<Long> itemsIDs = User.getLiked_items_ids(MainActivity.userLoggedInID, (Context) getActivity());
        ArrayList<Item> likedItems = new ArrayList<Item>();

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(getActivity());
            db = sqLiteOpenHelper.getReadableDatabase();

            userCursor = db.query("USER", new String[]{"_id", "NAME"},
                    null, null, null, null, null);

            itemsCursor = db.query("ITEM", new String[]{"USER_ID", "TITLE", "IMAGE_URL", "CATEGORY_TYPE", "INTERESTS", "_id"},
                    null, null, null, null, null);

            while (itemsCursor.moveToNext()) {
                long userID = itemsCursor.getLong(0);

                if (itemsIDs.contains(itemsCursor.getLong(5))) {
                    User theOwner = new User(MainActivity.userLoggedInID, MainActivity.userLoggedInName);
                    while (userCursor.moveToNext()) {
                        Log.i("User", "User id " + userCursor.getLong(0) + " " + userID);
                        if (userCursor.getLong(0) == userID) {

                            theOwner = new User(userCursor.getLong(0), userCursor.getString(1));
                            break;
                        }
                    }
                    Item newItem = new Item(itemsCursor.getString(1), itemsCursor.getInt(2), itemsCursor.getString(3), itemsCursor.getString(4), theOwner);
                    newItem.setItemID(itemsCursor.getLong(5));
                    likedItems.add(newItem);
                }
            }
        } catch (Exception e) {
            Log.i("Database", "Database is unavailable, Likes Fragment");
        }
        Log.i("Likes Fragment", "Length " + likedItems.size());

        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.rvItemsLikesFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new MyRecyclerViewAdapter(root.getContext(), likedItems);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }


}