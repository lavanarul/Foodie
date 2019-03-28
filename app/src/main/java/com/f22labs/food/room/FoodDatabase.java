package com.f22labs.food.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.f22labs.food.model.Food;
import com.f22labs.food.utils.AppConstants;


@Database(entities = {Food.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {

    public abstract FoodDao getFoodDao();

    private static FoodDatabase foodDb;



    public static FoodDatabase getInstance(Context context) {
        if (null == foodDb) {
            foodDb = buildDatabaseInstance(context);
        }
        return foodDb;
    }

    private static FoodDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                FoodDatabase.class,
                AppConstants.DB_NAME)
                .allowMainThreadQueries().build();
    }

    public void cleanUp() {
        foodDb = null;
    }


    public void upSert(Food entity) {
        if( getFoodDao().getItemById(entity.getItemName()).size()==0)
        {
            getFoodDao().insert(entity);
        } else
        {
            getFoodDao().update(entity.getItemName(),entity.getQuantity());
        }
    }
}