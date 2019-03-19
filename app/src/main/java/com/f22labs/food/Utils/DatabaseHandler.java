package com.f22labs.food.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f22labs.food.Models.FoodItemModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Food";
    private static final String TABLE_CONTACTS = "foodItems";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_RATING = "rating";
    private static final String KEY_URL = "url";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QNTY = "qnty";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + KEY_NAME + " TEXT" +
                "," + KEY_RATING + " TEXT" +
                "," + KEY_URL + " TEXT" +
                "," + KEY_PRICE + " TEXT" +
                "," + KEY_QNTY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addFoodItem(FoodItemModel foodItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, foodItemModel.getItemName());
        values.put(KEY_RATING, foodItemModel.getAverageRating());
        values.put(KEY_URL, foodItemModel.getImageUrl());
        values.put(KEY_PRICE, foodItemModel.getItemPrice());
        values.put(KEY_QNTY, foodItemModel.getQuantity());
        if (isServerIdExist(TABLE_CONTACTS, foodItemModel.getItemName())) {

            updateContact(foodItemModel);
        } else {
            db.insertWithOnConflict(TABLE_CONTACTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }

    }

    public boolean isServerIdExist(String table_name, String server_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long line = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + table_name + " WHERE name=?",
                new String[]{server_id});
        return line > 0;
    }


    public List<FoodItemModel> getAllFoodItems() {
        List<FoodItemModel> contactList = new ArrayList<FoodItemModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FoodItemModel contact = new FoodItemModel();
                contact.setItemName(cursor.getString(1));
                contact.setAverageRating(Double.valueOf(cursor.getString(2)));
                contact.setImageUrl(cursor.getString(3));
                contact.setItemPrice(Float.valueOf(cursor.getString(4)));
                contact.setQuantity(Integer.parseInt(cursor.getString(5)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int updateContact(FoodItemModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QNTY, contact.getQuantity());
        return db.update(TABLE_CONTACTS, values, KEY_NAME + " = ?",
                new String[]{String.valueOf(contact.getItemName())});
    }

    public void deleteFoodItem(FoodItemModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                new String[]{String.valueOf(contact.getItemName())});
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

}  