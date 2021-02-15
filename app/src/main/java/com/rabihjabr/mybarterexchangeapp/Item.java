package com.rabihjabr.mybarterexchangeapp;

import java.util.ArrayList;

public class Item {
    private long itemID;
    private String title;
    private int imgUrl;
    private String categoryType;
    private String interests;
    private String location; // to be implemented in the next version
    private User owner;

    public Item(String title, int imgUrl, String categoryType, String interests, User owner) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.categoryType = categoryType;
        this.interests = interests;
        this.owner = owner;
    }

    static ArrayList<Item> items_array;


    public static ArrayList<Item> getItemsArrayByCategory(String categoryType) {
        ArrayList<Item> categoryItems = new ArrayList<>();
        for (int i = 0; i < items_array.size(); i++) {
            if (items_array.get(i).getCategoryType().equalsIgnoreCase(categoryType)) {
                categoryItems.add(items_array.get(i));
            }
        }
        return categoryItems;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return title;
    }
}
