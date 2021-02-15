package com.rabihjabr.mybarterexchangeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onClickNavigateToRegisterFragment(null);
    }

    public void onClickLogin(View view) {
        EditText usernameEditText, passwordEditText;
        usernameEditText = findViewById(R.id.loginLayoutEditTextUsername);
        passwordEditText = findViewById(R.id.loginLayoutEditTextPassword);
        String username = usernameEditText.getText().toString().toLowerCase();
        String password = passwordEditText.getText().toString();

        try {
            SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(this);
            db = sqLiteOpenHelper.getReadableDatabase();

            cursor = db.query("USER", new String[]{"USERNAME", "PASSWORD", "NAME"},
                    null, null, null, null, null);


            while (cursor.moveToNext()) {
                Log.i("login", cursor.getString(0)+" "+cursor.getString(1));
                if (cursor.getString(0).equalsIgnoreCase(username) && cursor.getString(1).equals(password)) {
                    navigateToHomePage(cursor.getString(0), cursor.getString(2));
                    return;
                }
            }

            Toast.makeText(this, "Invalid username or password!\nPlease Try again.",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Database is unavailable",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToHomePage(String username, String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("Name", name);
        MainActivity.loggedIn = true;
        startActivity(intent);
        this.finish();
    }

    public void onClickRegister(View view) {
        EditText nameEditText, usernameEditText, emailEditText, passwordEditText;
        nameEditText = findViewById(R.id.registerLayoutEditTextName);
        usernameEditText = findViewById(R.id.registerLayoutEditTextUsername);
        emailEditText = findViewById(R.id.registerLayoutEditTextEmail);
        passwordEditText = findViewById(R.id.registerLayoutEditTextPassword);
        boolean nameValidated = validateName(nameEditText),
                usernameValidated = validateUserName(usernameEditText),
                emailValidated = validateEmail(emailEditText),
                passwordValidated = validatePassword(passwordEditText);
        if (!nameValidated || !usernameValidated || !emailValidated || !passwordValidated) {
            return;
        }
        String name = nameEditText.getText().toString();
        String userName = usernameEditText.getText().toString().toLowerCase();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User newUser = new User(name, userName, email, password);
        newUser.addUserToDatabase(this);
        navigateToHomePage(userName, name);
    }

    private boolean validateUserName(EditText usernameEditText) {
        String username = usernameEditText.getText().toString().toLowerCase();

        if (username.equals("")) {
            usernameEditText.setError("Field cannot be empty");
            return false;
        } else if (username.contains(" ")) {
            usernameEditText.setError("Field cannot contain white spaces");
            return false;
        } else {
            try {
                SQLiteOpenHelper sqLiteOpenHelper = new BarterExchangeSQLiteOpenHelper(this);
                db = sqLiteOpenHelper.getReadableDatabase();

                cursor = db.query("USER", new String[]{"USERNAME"},
                        null, null, null, null, null);

                cursor.moveToFirst();
                if (cursor.getString(0).equals(username)) {
                    usernameEditText.setError("Username not available");
                    return false;
                }
                while (cursor.moveToNext()) {
                    if (cursor.getString(0).equals(username)) {
                        usernameEditText.setError("Username not available");
                        return false;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Database is unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private boolean validateName(EditText nameEditText) {
        if (nameEditText.getText().toString().equals("")) {
            nameEditText.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail(EditText emailEditText) {
        String val = emailEditText.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailEditText.setError("Invalid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }

    private boolean validatePassword(EditText passwordEditText) {
        String val = passwordEditText.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            passwordEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            passwordEditText.setError("Password is too weak");
            Toast.makeText(this, "Use at least 4 characters including a special character [@#$%^&+=]", Toast.LENGTH_LONG).show();
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }


    public void onClickNavigateToLoginFragment(View view) {
        View fragmentContainer = findViewById(R.id.loginActivityFrameLayout);
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginActivityFrameLayout, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
//        this.finish();
    }

    public void onClickNavigateToRegisterFragment(View view) {
        View fragmentContainer = findViewById(R.id.loginActivityFrameLayout);
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.loginActivityFrameLayout, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
//        this.finish();
    }
}