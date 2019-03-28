package com.f22labs.food.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.f22labs.food.model.Food;
import com.f22labs.food.utils.AppConstants;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food " + AppConstants.TABLE_NAME)
    List<Food> getAll();

    @Query("SELECT * from food WHERE itemName= :itemName")
    List<Food> getItemById(String itemName);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food foodDB);

    @Query("UPDATE food SET quantity = :quantity WHERE itemName = :foodDB")
    void update(String foodDB,int quantity);

    @Delete
    void delete(Food foodDB);

    @Delete
    void delete(Food... foodDB);


}