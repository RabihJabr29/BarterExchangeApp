package com.rabihjabr.mybarterexchangeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context c;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        c = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position), mInflater);

    } // total number of rows

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cardImageView;
        public TextView titleTextView;
        public TextView userNameTextView;
        public ImageButton imageButton;
        public TextView interestsTextView, itemIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.card_title);
            userNameTextView = itemView.findViewById(R.id.card_username);
            imageButton = itemView.findViewById(R.id.likeImageButton);
            interestsTextView = itemView.findViewById(R.id.card_interests);
            itemIdTextView = itemView.findViewById(R.id.itemIdTextView);

        }

        public void bindData(Item item, LayoutInflater context) {

            cardImageView.setImageResource(item.getImgUrl());
            imageButton.setImageResource(R.drawable.ic_likes_black_24dp);
            imageButton.setTag(R.drawable.ic_likes_black_24dp);
            titleTextView.setText(item.getTitle());
            userNameTextView.setText("By: " + item.getOwner().getName());
            interestsTextView.setText(item.getInterests());
            itemIdTextView.setText(Long.toString(item.getItemID()));
            ArrayList<Long> likedItemsIDs = User.getLiked_items_ids(MainActivity.userLoggedInID, c);

            if (likedItemsIDs.contains(item.getItemID())) {
                imageButton.setImageResource(R.drawable.icon_love);
                imageButton.setTag(R.drawable.icon_love);
            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer resource = (Integer) imageButton.getTag();
                    if (resource == R.drawable.icon_love) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        imageButton.setImageResource(R.drawable.ic_likes_black_24dp);
                                        imageButton.setTag(R.drawable.ic_likes_black_24dp);
                                        // delete it from likes
                                        User.removeLikedItemID(Long.parseLong(itemIdTextView.getText().toString()), MainActivity.userLoggedInID, c);
                                        mClickListener.onItemClick(view, 0);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setMessage("Are you sure you want to delete this item from your liked items?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                    } else {
                        imageButton.setImageResource(R.drawable.icon_love);
                        imageButton.setTag(R.drawable.icon_love);
                        //add it to likes
                        long userID = MainActivity.userLoggedInID, newLikedItemID = Long.parseLong(itemIdTextView.getText().toString());
                        Log.i("Recycler", "User id " + userID + " item id " + newLikedItemID);
                        User.addLikedItemID(newLikedItemID, userID, c);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Item getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
