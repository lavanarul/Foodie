package com.f22labs.food.viewmodel;


import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f22labs.food.Listeners.LoadTotal;
import com.f22labs.food.R;
import com.f22labs.food.model.Food;
import com.f22labs.food.room.FoodDatabase;
import com.f22labs.food.view.DescActivity;

public class FoodViewModel extends BaseObservable {

    private Food food;
    private Context context;
    private FoodDatabase foodDatabase;

    public FoodViewModel(Context context, Food food) {
        this.food = food;
        this.context = context;
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

    @Bindable
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
        Intent intent = new Intent(context, DescActivity.class);
        intent.putExtra(context.getString(R.string.food_item), (Parcelable) food);
        context.startActivity(intent);
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

    private LoadTotal onLoadTotal;

    public void setOnLoadTotal(LoadTotal onLoadTotal) {
        this.onLoadTotal = onLoadTotal;
    }

}
