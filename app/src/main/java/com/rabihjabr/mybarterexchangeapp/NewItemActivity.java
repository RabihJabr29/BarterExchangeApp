package com.rabihjabr.mybarterexchangeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class NewItemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView editTextFilledExposedDropdown;
    EditText titleEditText, interestsEditText;
    ImageView imageView;

    final ArrayList<String> categories = new ArrayList<String>(Arrays.asList("clothes", "devices", "appliances", "vehicles", "toys", "others"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_category,
                        categories);

        titleEditText = findViewById(R.id.newItemEditTextTitle);
        interestsEditText = findViewById(R.id.newItemEditTextInterests);
        imageView = findViewById(R.id.newItemImageViewUpload);


        editTextFilledExposedDropdown =
                findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setOnItemClickListener(this);
    }

    String selectedCategory = "";

    public void onClickAddNewItem(View view) {
        boolean titleValidated = validateTextField(titleEditText);
        boolean interestsValidated = validateTextField(interestsEditText);
        boolean categoryValidated = true;

        if (!categories.contains(selectedCategory)) {
            categoryValidated = false;
            editTextFilledExposedDropdown.setError("Please Choose a category");
        }
        if (!titleValidated || !interestsValidated || !categoryValidated) {
            return;
        }
        Item newItem = new Item(titleEditText.getText().toString(), R.drawable.no_image, selectedCategory, interestsEditText.getText().toString(), new User(MainActivity.userLoggedInID, MainActivity.userLoggedInName));
        User.addItem(newItem, MainActivity.userLoggedInID, this);
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    private boolean validateTextField(EditText nameEditText) {
        if (nameEditText.getText().toString().equals("")) {
            nameEditText.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    public void onClickCancel(View view) {
        this.finish();
    }

    public void onSelectImage(View view) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}