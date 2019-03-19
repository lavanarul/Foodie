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
import com.f22labs.food.Models.FoodItemModel;
import com.f22labs.food.R;
import com.f22labs.food.Utils.AppConstants;

import java.util.List;

public class CartItemAdapter extends Adapter<CartItemAdapter.ViewHolder> {
    private List<FoodItemModel> foodItemList;
    private Context context;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView itemName,itemPrice,itemQuantity,itemTotal;
        ImageView itemImage;


        ViewHolder(View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
            itemPrice=itemView.findViewById(R.id.item_price);
            itemImage=itemView.findViewById(R.id.item_image);
            itemQuantity=itemView.findViewById(R.id.item_quantity);
            itemTotal=itemView.findViewById(R.id.item_total);

        }
    }


    public CartItemAdapter(Context context, List<FoodItemModel> categoriesModels) {
        this.context = context;
        this.foodItemList = categoriesModels;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_item, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemName.setText(foodItemList.get(position).getItemName());
        holder.itemPrice.setText(AppConstants.RS+String.valueOf(foodItemList.get(position).getItemPrice()));
        holder.itemQuantity.setText(String.valueOf(foodItemList.get(position).getQuantity()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DescActivity.class);
                intent.putExtra(context.getString(R.string.food_item), (Parcelable) foodItemList.get(position));
                context.startActivity(intent);
            }
        });
        holder.itemTotal.setText(String.valueOf(foodItemList.get(position).getQuantity()*
                foodItemList.get(position).getItemPrice()
                ));
        Glide.with(context).load(foodItemList.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.itemImage);

    }


    public int getItemCount() {
        return foodItemList.size();
    }



}
