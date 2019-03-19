package com.f22labs.food.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.R;
import com.f22labs.food.Utils.AppConstants;
import com.f22labs.food.Utils.DatabaseHandler;

public class DescActivity extends AppCompatActivity {


    private TextView itemName, itemPrice, itemCount;
    private RatingBar itemRating;
    private ImageView itemImage;
    private TextView orderNow;
    private FoodItemModel foodItemModel;
    private ImageButton addButton, removeButton;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        databaseHandler=new DatabaseHandler(this);
        foodItemModel = getIntent().getParcelableExtra(getString(R.string.food_item));
        initializeViews();
        showFoodItemViews();


    }

    private void showFoodItemViews() {
        itemName.setText(foodItemModel.getItemName());
        itemPrice.setText(AppConstants.RS + String.valueOf(foodItemModel.getItemPrice()));
        itemRating.setRating(foodItemModel.getAverageRating().floatValue());
        itemCount.setText(String.valueOf(foodItemModel.getQuantity()));
        if (foodItemModel.getQuantity() != 0)
            itemCount.setVisibility(View.VISIBLE);
        else
            itemCount.setVisibility(View.GONE);

        Glide.with(this).load(foodItemModel.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemImage);


        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textCount = Integer.parseInt(itemCount.getText().toString());
                if (textCount >= 0) {
                    textCount++;
                    itemCount.setText(String.valueOf(textCount));
                    foodItemModel.setQuantity(textCount);
                    databaseHandler.addFoodItem(foodItemModel);
                    if (textCount > 0)
                        itemCount.setVisibility(View.VISIBLE);
                    else
                        itemCount.setVisibility(View.GONE);
                } else
                    itemCount.setVisibility(View.GONE);

            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textCount = Integer.parseInt(itemCount.getText().toString());
                if (textCount > 0) {
                    textCount--;
                    itemCount.setText(String.valueOf(textCount));
                    foodItemModel.setQuantity(textCount);
                    if (textCount == 0)
                        databaseHandler.deleteFoodItem(foodItemModel);
                    else
                        databaseHandler.addFoodItem(foodItemModel);
                    if (textCount > 0)
                        itemCount.setVisibility(View.VISIBLE);
                    else
                        itemCount.setVisibility(View.GONE);
                } else
                    itemCount.setVisibility(View.GONE);
            }
        });


    }

    private void initializeViews() {
        itemName = findViewById(R.id.item_name);
        itemPrice = findViewById(R.id.item_price);
        itemCount = findViewById(R.id.no_item);
        itemRating = findViewById(R.id.item_rating);
        itemImage = findViewById(R.id.item_image);
        orderNow = findViewById(R.id.orderNow);
        addButton = findViewById(R.id.add_item);
        removeButton = findViewById(R.id.remove_item);

    }

}
