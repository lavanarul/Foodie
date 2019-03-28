package com.f22labs.food.viewmodel;


import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f22labs.food.Listeners.LoadTotal;
import com.f22labs.food.model.Food;
import com.f22labs.food.room.FoodDatabase;

public class DescViewModel extends BaseObservable {

    private Food food;
    private Activity context;
    private FoodDatabase foodDatabase;

    public DescViewModel(Activity context, Food food) {
        this.context = context;
        this.food = food;
        foodDatabase = FoodDatabase.getInstance(context);
    }

    public String getImageUrl() {
        return food.imageUrl;
    }

    public String getItemName() {
        return food.itemName;
    }

    public Float getItemPrice() {
        return food.itemPrice;
    }

    public double getAvgRating() {
        return food.averageRating;
    }


    public int getQuantity() {
        return food.quantity;
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void onItemClick(View view) {
        context.finish();
    }

    public void onAddItemClick(View view) {
        int textCount = food.getQuantity();
        if (textCount >= 0) {
            textCount++;
            food.setQuantity(textCount);
            foodDatabase.upSert(food);
            notifyChange();

        }
    }

    @Bindable
    public int getItemVisibility() {
        return food.quantity > 0 ? View.VISIBLE : View.GONE;
    }


    public void onRemoveItemClick(View view) {
        int textCount = food.quantity;
        if (textCount > 0) {
            textCount--;
            food.setQuantity(textCount);
            foodDatabase.upSert(food);
            notifyChange();
        }
    }


    public void setFood(Food food) {
        this.food = food;
        notifyChange();
    }


}
