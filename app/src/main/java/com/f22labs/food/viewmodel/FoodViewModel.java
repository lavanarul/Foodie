package com.f22labs.food.viewmodel;


import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f22labs.food.R;
import com.f22labs.food.model.Food;
import com.f22labs.food.room.FoodDatabase;
import com.f22labs.food.view.DescActivity;

public class FoodViewModel extends BaseObservable {

    private Food food;
    private Context context;
    private FoodDatabase foodDatabase;
    private MainViewModel mainViewModel;
    private int position;

    public FoodViewModel(Context context, Food food, MainViewModel mainViewModel, int position) {
        this.food = food;
        this.context = context;
        this.mainViewModel = mainViewModel;
        this.position = position;
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
            mainViewModel.setFoodList(food, position);

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
            if (food.getQuantity() > 0)
                foodDatabase.upSert(food);
            else {
                Log.e("--->","Delete");
                foodDatabase.getFoodDao().delete(food.getItemName());
            }
        }
        else
            foodDatabase.getFoodDao().delete(food.getItemName());


        notifyChange();
        mainViewModel.setFoodList(food, position);
    }


    public void setFood(Food food) {
        this.food = food;
        notifyChange();
    }


}
