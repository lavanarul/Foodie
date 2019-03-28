package com.f22labs.food.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.f22labs.food.BR;
import com.f22labs.food.R;
import com.f22labs.food.model.Food;
import com.f22labs.food.remote.FoodFactory;
import com.f22labs.food.remote.FoodService;
import com.f22labs.food.room.FoodDatabase;
import com.f22labs.food.view.CartActivity;
import com.f22labs.food.view.HomeItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseObservable {
    private List<Food> foodList;
    private Context context;
    private int totalCount;
    private ProgressDialog progressDialog;

    @Bindable
    public List<Food> getFoodList() {
        return foodList;
    }


    public void onCartClick(View view) {
        Intent intent = new Intent(context, CartActivity.class);
        context.startActivity(intent);
    }

    public MainViewModel(Context context) {
        this.context = context;
        foodList = new ArrayList<>();
        cartItemAdapter = new HomeItemAdapter(this);
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Bindable
    public HomeItemAdapter getHomeAdapter() {
        return cartItemAdapter;
    }


    private HomeItemAdapter cartItemAdapter;

    public List<Food> getFoodDetails() {
        if (foodList.isEmpty()) {
            loadFoodItems();
        } else
            dbSetFoodItems();
        return foodList;
    }


    private void loadFoodItems() {
        progressDialog.show();
        foodList = new ArrayList<>();
        FoodService apiInterface = FoodFactory.getClient().create(FoodService.class);
        Call<List<Food>> callLogin = apiInterface.getFoodItemList();
        callLogin.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    foodList.addAll(response.body());
                    dbSetFoodItems();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                progressDialog.dismiss();
                call.cancel();

            }
        });
    }


    @Bindable
    public int getBottomVisibility() {
        return totalCount > 0 ? View.VISIBLE : View.GONE;
    }


    private void dbSetFoodItems() {
        FoodDatabase foodDatabase = FoodDatabase.getInstance(context);
        List<Food> dbFoodItemList = foodDatabase.getFoodDao().getAll();
        for (int i = 0; i < foodList.size(); i++) {
            Food foodItemModel = foodList.get(i);
            foodItemModel.setQuantity(0);
            int itemTotal = 0;
            for (int j = 0; j < dbFoodItemList.size(); j++) {
                if (foodList.get(i).getItemName().equalsIgnoreCase(dbFoodItemList.get(j).getItemName())) {
                    foodItemModel.setQuantity(dbFoodItemList.get(j).getQuantity());
                }
                itemTotal += dbFoodItemList.get(j).getQuantity();
            }
            foodList.set(i, foodItemModel);
        }
        notifyChange();
    }


    public void getPriceSort() {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food p1, Food p2) {
                if (p1.getItemPrice() > p2.getItemPrice()) return 1;
                if (p1.getItemPrice() < p2.getItemPrice()) return -1;
                return 0;
            }
        });
        notifyChange();
    }

    public void getRatingSort() {
        Collections.sort(foodList, new Comparator<Food>() {
            @Override
            public int compare(Food p1, Food p2) {
                if (p1.getAverageRating() < p2.getAverageRating()) return 1;
                if (p1.getAverageRating() > p2.getAverageRating()) return -1;
                return 0;
            }
        });
        notifyChange();
    }

    @Bindable
    public String getTotalCount() {
        totalCount = 0;
        for (Food food : foodList) {
            totalCount += food.quantity;
        }
        return String.valueOf(totalCount);
    }

    void setFoodList(Food food, int position) {
        foodList.set(position, food);
        notifyPropertyChanged(BR.totalCount);
        notifyPropertyChanged(BR.bottomVisibility);
    }
}