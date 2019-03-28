package com.f22labs.food.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Food implements Parcelable {

    public Food(Double averageRating, String imageUrl, String itemName, Float itemPrice, int quantity) {
        this.averageRating = averageRating;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    @SerializedName("average_rating")
    @Expose
    public Double averageRating;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("item_name")
    @Expose
    public String itemName;
    @SerializedName("item_price")
    @Expose
    public Float itemPrice;
    @PrimaryKey(autoGenerate = true)
    private int foodId;

    protected Food(Parcel in) {
        if (in.readByte() == 0) {
            averageRating = null;
        } else {
            averageRating = in.readDouble();
        }
        imageUrl = in.readString();
        itemName = in.readString();
        if (in.readByte() == 0) {
            itemPrice = null;
        } else {
            itemPrice = in.readFloat();
        }
        quantity = in.readInt();
    }

    public Food() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (averageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(averageRating);
        }
        dest.writeString(imageUrl);
        dest.writeString(itemName);
        if (itemPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(itemPrice);
        }
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int quantity;


    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}