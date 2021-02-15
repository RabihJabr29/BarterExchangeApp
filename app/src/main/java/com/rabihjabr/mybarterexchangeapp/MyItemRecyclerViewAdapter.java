package com.rabihjabr.mybarterexchangeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context c;

    public MyItemRecyclerViewAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        c = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_my_item, parent, false);
        return new MyItemRecyclerViewAdapter.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
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
        public ImageButton editImageButton, removeImageButton;
        public TextView interestsTextView, itemIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.imageViewMyItem);
            titleTextView = itemView.findViewById(R.id.card_titleMyItem);
            userNameTextView = itemView.findViewById(R.id.card_usernameMyItem);
            editImageButton = itemView.findViewById(R.id.editImageButtonMyItem);
            removeImageButton = itemView.findViewById(R.id.removeImageButtonMyItem);
            interestsTextView = itemView.findViewById(R.id.card_interestsMyItem);
            itemIdTextView = itemView.findViewById(R.id.itemIdTextViewMyItem);

        }

        public void bindData(Item item, LayoutInflater context) {

            cardImageView.setImageResource(item.getImgUrl());
            titleTextView.setText(item.getTitle());
            userNameTextView.setText("By me: " + item.getOwner().getName());
            interestsTextView.setText(item.getInterests());
            itemIdTextView.setText(Long.toString(item.getItemID()));


            editImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            removeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    User.removeItem(Long.parseLong(itemIdTextView.getText().toString()), c);
                                    mClickListener.onItemClick(view, 0);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setMessage("Are you sure you want to delete this item?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
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
    public void setClickListener(MyItemRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
