package com.rabihjabr.mybarterexchangeapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static boolean loggedIn = false;
    public static long userLoggedInID = -1;
    public static String userLoggedInName;
    private SQLiteDatabase db;
    private Cursor itemsCursor, userCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            loggedIn = savedInstanceState.getBoolean("LoggedIn");
        }
        if (!loggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        Intent intent = getIntent();
        setUserLoggedInID(intent.getStringExtra("Username"));
        userLoggedInName = intent.getStringExtra("Name");


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_likes, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_new_item:
                Intent newItemIntent = new Intent(this, NewItemActivity.class);
                startActivity(newItemIntent);
                return true;

            case R.id.action_my_profile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.action_my_items:
                Intent myItemListIntent = new Intent(this, MyItemListActivity.class);
                startActivity(myItemListIntent);
                return true;

            case R.id.action_logout:
                loggedIn = false;
                userLoggedInID = -1;
                userLoggedInName = "";
                Intent logInIntent = new Intent(this, LoginActivity.class);
                startActivity(logInIntent);
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }

    }


    private void setUserLoggedInID(String username) {
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(this);
            db = sqLiteOpenHelper.getReadableDatabase();

            userCursor = db.query("USER", new String[]{"_id", "USERNAME"},
                    null, null, null, null, null);

            while (userCursor.moveToNext()) {
                if (userCursor.getString(1).equalsIgnoreCase(username)) {
                    userLoggedInID = userCursor.getLong(0);
                }
            }

        } catch (Exception e) {
            Log.i("Database", "Database is unavailable");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("LoggedIn", loggedIn);
    }

    public void onClickClothesCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "clothes");
        startActivity(intent);
    }

    public void onClickDevicesCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "devices");
        startActivity(intent);
    }

    public void onClickVehiclesCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "vehicles");
        startActivity(intent);
    }

    public void onClickAppliancesCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "appliances");
        startActivity(intent);
    }

    public void onClickToysCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "toys");
        startActivity(intent);
    }

    public void onClickOtherCategory(View view) {
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra("Category", "others");
        startActivity(intent);
    }


}