package com.f22labs.food.Network;

import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.Utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCallInterface {


    @GET(AppConstants.GET_FOOD_ITEMS)
    Call<List<FoodItemModel>> getFoodItemList();


}