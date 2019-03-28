package com.f22labs.food.remote;

import com.f22labs.food.model.Food;
import com.f22labs.food.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodService {


    @GET(AppConstants.GET_FOOD_ITEMS)
    Call<List<Food>> getFoodItemList();


}