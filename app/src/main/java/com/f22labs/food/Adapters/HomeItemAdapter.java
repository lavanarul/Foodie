package com.f22labs.food.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.f22labs.food.Activities.DescActivity;
import com.f22labs.food.Listeners.LoadTotal;
import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.R;
import com.f22labs.food.Utils.AppConstants;
import com.f22labs.food.Utils.DatabaseHandler;

import java.util.List;

public class HomeItemAdapter extends Adapter<HomeItemAdapter.ViewHolder> {
    private List<FoodItemModel> foodItemList;
    private Context context;
    private DatabaseHandler databaseHandler;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemCount;
        RatingBar itemRating;
        ImageButton addButton, removeButton;
        ImageView itemImage;


        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemCount = itemView.findViewById(R.id.no_item);
            itemRating = itemView.findViewById(R.id.item_rating);
            addButton = itemView.findViewById(R.id.add_item);
            removeButton = itemView.findViewById(R.id.remove_item);
            itemImage = itemView.findViewById(R.id.item_image);

        }
    }


    public HomeItemAdapter(Context context, List<FoodItemModel> categoriesModels) {
        this.context = context;
        this.foodItemList = categoriesModels;
        databaseHandler = new DatabaseHandler(context);
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FoodItemModel foodItemModel = foodItemList.get(position);
        holder.itemName.setText(foodItemModel.getItemName());
        holder.itemPrice.setText(AppConstants.RS + String.valueOf(foodItemModel.getItemPrice()));
        holder.itemRating.setRating(foodItemModel.getAverageRating().floatValue());
        holder.itemCount.setText(String.valueOf(foodItemModel.getQuantity()));
        if (foodItemModel.getQuantity() != 0)
            holder.itemCount.setVisibility(View.VISIBLE);
        else
            holder.itemCount.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DescActivity.class);
                intent.putExtra(context.getString(R.string.food_item), (Parcelable) foodItemModel);
                context.startActivity(intent);
            }
        });


        Glide.with(context).load(foodItemModel.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.itemImage);


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textCount = Integer.parseInt(holder.itemCount.getText().toString());
                if (textCount >= 0) {
                    textCount++;
                    holder.itemCount.setText(String.valueOf(textCount));
                    foodItemModel.setQuantity(textCount);
                    foodItemList.set(position, foodItemModel);
                    databaseHandler.addFoodItem(foodItemModel);
                    notifyDataSetChanged();
                    if (onLoadTotal != null)
                        onLoadTotal.Loading(foodItemList);
                    if (textCount > 0) {
                        holder.itemCount.setVisibility(View.VISIBLE);

                    } else
                        holder.itemCount.setVisibility(View.GONE);
                } else
                    holder.itemCount.setVisibility(View.GONE);

            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int textCount = Integer.parseInt(holder.itemCount.getText().toString());
                if (textCount > 0) {
                    textCount--;
                    holder.itemCount.setText(String.valueOf(textCount));
                    foodItemModel.setQuantity(textCount);
                    foodItemList.set(position, foodItemModel);
                    notifyDataSetChanged();
                    if (textCount == 0)
                        databaseHandler.deleteFoodItem(foodItemModel);
                    else
                        databaseHandler.addFoodItem(foodItemModel);
                    if (onLoadTotal != null)
                        onLoadTotal.Loading(foodItemList);
                    if (textCount > 0) {
                        holder.itemCount.setVisibility(View.VISIBLE);

                    } else
                        holder.itemCount.setVisibility(View.GONE);
                } else
                    holder.itemCount.setVisibility(View.GONE);
            }
        });


    }


    public int getItemCount() {
        return foodItemList.size();
    }

    private LoadTotal onLoadTotal;

    public void setOnLoadTotal(LoadTotal onLoadTotal) {
        this.onLoadTotal = onLoadTotal;
    }


}
